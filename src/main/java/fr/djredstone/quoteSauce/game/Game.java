package fr.djredstone.quoteSauce.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import org.javatuples.Quintet;
import org.javatuples.Triplet;
import org.yaml.snakeyaml.Yaml;

public class Game {

    public static final int minmumPlayers = 2;

    public static final HashMap<String, Quintet<String, HashMap<String, Integer>, HashSet<Integer>, Triplet<Boolean, Integer, String>, Integer>> games = new HashMap<>();
    // ChannelID : [ themeID | PlayersAndPoints : [ PlayerID | Points ] | Questions : [ QuestionNumber ] | ActualQuote: [ CanAswer | QuoteNumber | QuoteMessageID ] | MaxQuestionNumber]

    @SuppressWarnings("unchecked")
    public static void startGame(TextChannel channel) throws FileNotFoundException {
        String[] messageID = new String[1];
        channel.sendMessage("Près ?").queue(message -> messageID[0] = message.getId());

        Quintet<String, HashMap<String, Integer>, HashSet<Integer>, Triplet<Boolean, Integer, String>, Integer> game = games.get(channel.getId());

        int nbQuote;
        nbQuote = new Random().nextInt(game.getValue4()) + 1;
        games.get(channel.getId()).getValue2().add(nbQuote);

        HashMap<String, Object> quote = (HashMap<String, Object>) getQuestions(game.getValue0()).get(nbQuote-1);

        int finalNbQuote = nbQuote;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                channel.editMessageById(messageID[0], "> " + quote.get("quote") + "\n???").queue();
                Triplet<Boolean, Integer, String> actualGame = new Triplet<>(true, finalNbQuote, messageID[0]);
                games.put(channel.getId(), game.setAt3(actualGame));
            }
        }, 3 * 1000);
    }

    @SuppressWarnings("unchecked")
    public static void playerFindQuote(TextChannel channel, User user) throws FileNotFoundException {
        Quintet<String, HashMap<String, Integer>, HashSet<Integer>, Triplet<Boolean, Integer, String>, Integer> game = Game.games.get(channel.getId());
        HashMap<String, Object> quote = (HashMap<String, Object>) Game.getQuestions(game.getValue0()).get(game.getValue3().getValue1()-1);
        ArrayList<Object> aswers = (ArrayList<Object>) quote.get("aswer");
        HashMap<String, Integer> actualPoints = game.getValue1();
        actualPoints.put(user.getId(), game.getValue1().get(user.getId()) + 1);
        Triplet<Boolean, Integer, String> actualGame = new Triplet<>(false, game.getValue3().getValue1(), game.getValue3().getValue2());
        games.put(channel.getId(), game.setAt3(actualGame));
        games.put(channel.getId(), game.setAt1(actualPoints));
        channel.editMessageById(game.getValue3().getValue2(), "> " + quote.get("quote") + "\n" + aswers.get(0)).queue();
        channel.sendMessage("**" + user.getAsTag() + "** a trouvé ! Il s'agissait de **" + aswers.get(0) + "** !").queue();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    nextQuote(channel);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 5 * 1000);
    }

    @SuppressWarnings("unchecked")
    private static void nextQuote(TextChannel channel) throws FileNotFoundException {
        String[] messageID = new String[1];
        Quintet<String, HashMap<String, Integer>, HashSet<Integer>, Triplet<Boolean, Integer, String>, Integer> game = games.get(channel.getId());

        int nbQuote;
        if (game.getValue2().size() >= game.getValue4()) {
            winGame(channel);
            return;
        } else {
            channel.sendMessage("*Prochaine citation...*").queue(message -> messageID[0] = message.getId());
            do {
                nbQuote = new Random().nextInt(game.getValue4()) + 1;
            } while (game.getValue2().contains(nbQuote));
        }
        games.get(channel.getId()).getValue2().add(nbQuote);

        HashMap<String, Object> quote = (HashMap<String, Object>) getQuestions(game.getValue0()).get(nbQuote-1);

        int finalNbQuote = nbQuote;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                channel.editMessageById(messageID[0], "> " + quote.get("quote") + "\n???").queue();
                Triplet<Boolean, Integer, String> actualGame = new Triplet<>(true, finalNbQuote, messageID[0]);
                games.put(channel.getId(), game.setAt3(actualGame));
            }
        }, 3 * 1000);
    }

    private static void winGame(TextChannel channel) {
        Map<String, Object> map;
        try {
            map = new Yaml().load(new FileInputStream("./themes/" + games.get(channel.getId()).getValue0() + ".yaml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String themeName = (String) map.get("name");
        StringBuilder results = new StringBuilder("**Partie terminée !** \uD83C\uDFC6 (" + themeName + ")\n" +
                "Voici les résultats :\n\n");
        Map<String, Integer> sortedResults = sortByValue(games.get(channel.getId()).getValue1());
        int i = 1;
        for (String userID : sortedResults.keySet()) {
            results.append(i).append(" - ").append("<@").append(userID).append("> ");
            if (i == 1) results.append("\uD83E\uDD47");
            if (i == 2) results.append("\uD83E\uDD48");
            if (i == 3) results.append("\uD83E\uDD49");
            results.append(" (`").append(sortedResults.get(userID)).append("`)\n");
            i ++;
        }
        channel.sendMessage(results).queue();
        games.remove(channel.getId());
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
        ? o2.getKey().compareTo(o1.getKey())
        : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Object> getQuestions(String themeID) throws FileNotFoundException {
        Map<String, Object> theme = new Yaml().load(new FileInputStream("./themes/" + themeID + ".yaml"));
        return (ArrayList<Object>) theme.get("questions");
    }

}
