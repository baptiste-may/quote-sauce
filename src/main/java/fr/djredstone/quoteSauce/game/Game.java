package fr.djredstone.quoteSauce.game;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

public class Game {

    public static final int minmumPlayers = 2;

    public static final HashMap<String, Quintet<Map<String, Object>, HashMap<String, Integer>, HashSet<Integer>, Quartet<Boolean, Integer, String, TimerTask>, Integer>> games = new HashMap<>();
    // ChannelID : [ File | PlayersAndPoints : [ PlayerID | Points ] | Questions : [ QuestionNumber ] | ActualQuote: [ CanAswer | QuoteNumber | QuoteMessageID | TimeoutTask ] | MaxQuestionNumber]

    @SuppressWarnings("unchecked")
    public static void startGame(TextChannel channel) throws FileNotFoundException {
        String[] messageID = new String[1];
        EmbedBuilder embed = Utils.getDefaultEmbed()
                .setTitle("Près ?");
        channel.sendMessageEmbeds(embed.build()).queue(message -> messageID[0] = message.getId());

        Quintet<Map<String, Object>, HashMap<String, Integer>, HashSet<Integer>, Quartet<Boolean, Integer, String, TimerTask>, Integer> game = games.get(channel.getId());

        int nbQuote;
        nbQuote = new Random().nextInt(game.getValue4()) + 1;
        games.get(channel.getId()).getValue2().add(nbQuote);

        HashMap<String, Object> quote = (HashMap<String, Object>) getQuestions(channel.getId()).get(nbQuote-1);

        int finalNbQuote = nbQuote;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                EmbedBuilder embed = Utils.getDefaultEmbed()
                        .setTitle((String) quote.get("quote"))
                        .setDescription("???");
                channel.editMessageEmbedsById(messageID[0], embed.build()).queue();
                TimerTask timeout = new Timeout(channel);
                new Timer().schedule(timeout, 60 * 1000);
                Quartet<Boolean, Integer, String, TimerTask> actualGame = new Quartet<>(true, finalNbQuote, messageID[0], timeout);
                games.put(channel.getId(), game.setAt3(actualGame));
            }
        }, 3 * 1000);
    }

    @SuppressWarnings("unchecked")
    public static void playerFindQuote(TextChannel channel, @Nullable User user) throws FileNotFoundException {
        Quintet<Map<String, Object>, HashMap<String, Integer>, HashSet<Integer>, Quartet<Boolean, Integer, String, TimerTask>, Integer> game = Game.games.get(channel.getId());
        HashMap<String, Object> quote = (HashMap<String, Object>) getQuestions(channel.getId()).get(game.getValue3().getValue1()-1);
        ArrayList<Object> aswers = (ArrayList<Object>) quote.get("aswer");
        Quartet<Boolean, Integer, String, TimerTask> actualGame = new Quartet<>(false, game.getValue3().getValue1(), game.getValue3().getValue2(), null);
        games.put(channel.getId(), game.setAt3(actualGame));
        EmbedBuilder embed = Utils.getDefaultEmbed()
                .setTitle((String) quote.get("quote"))
                .setDescription((CharSequence) aswers.get(0));
        if (user == null) {
            channel.editMessageEmbedsById(game.getValue3().getValue2(), embed.build()).queue();
            channel.sendMessage("Personne n'a trouvé ! Il s'agissait de **" + aswers.get(0) + "** !").queue();
        } else {
            HashMap<String, Integer> actualPoints = game.getValue1();
            actualPoints.put(user.getId(), game.getValue1().get(user.getId()) + 1);
            games.put(channel.getId(), game.setAt1(actualPoints));
            channel.editMessageEmbedsById(game.getValue3().getValue2(), embed.build()).queue();
            channel.sendMessage("**" + user.getAsTag() + "** a trouvé ! Il s'agissait de **" + aswers.get(0) + "** !").queue();
        }

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
        Quintet<Map<String, Object>, HashMap<String, Integer>, HashSet<Integer>, Quartet<Boolean, Integer, String, TimerTask>, Integer> game = games.get(channel.getId());

        int nbQuote;
        if (game.getValue2().size() >= game.getValue4()) {
            winGame(channel);
            return;
        } else {
            channel.sendMessageEmbeds(Utils.getDefaultEmbed()
                    .setTitle("Prochaine citation...").build())
                    .queue(message -> messageID[0] = message.getId());
            do {
                nbQuote = new Random().nextInt(game.getValue4()) + 1;
            } while (game.getValue2().contains(nbQuote));
        }
        games.get(channel.getId()).getValue2().add(nbQuote);

        HashMap<String, Object> quote = (HashMap<String, Object>) getQuestions(channel.getId()).get(nbQuote-1);

        int finalNbQuote = nbQuote;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                EmbedBuilder embed = Utils.getDefaultEmbed()
                        .setTitle((String) quote.get("quote"))
                        .setDescription("???");
                channel.editMessageEmbedsById(messageID[0], embed.build()).queue();
                TimerTask timeout = new Timeout(channel);
                new Timer().schedule(timeout, 60 * 1000);
                Quartet<Boolean, Integer, String, TimerTask> actualGame = new Quartet<>(true, finalNbQuote, messageID[0], timeout);
                games.put(channel.getId(), game.setAt3(actualGame));
            }
        }, 3 * 1000);
    }

    private static void winGame(TextChannel channel) {
        Map<String, Object> map;
        map = games.get(channel.getId()).getValue0();
        String themeName = (String) map.get("name");
        EmbedBuilder embed = Utils.getDefaultEmbed()
                .setTitle("Partie terminée ! \uD83C\uDFC6 (" + themeName + ")")
                .setDescription("Voici les résultats :");
        Map<String, Integer> sortedResults = sortByValue(games.get(channel.getId()).getValue1());
        int i = 1;
        for (String userID : sortedResults.keySet()) {
            String more = "";
            if (i == 1) more = "\uD83E\uDD47";
            if (i == 2) more = "\uD83E\uDD48";
            if (i == 3) more = "\uD83E\uDD49";
            embed.addField(i + " - " + Main.jda.retrieveUserById(userID).complete().getAsTag() + " " + more,
                    String.valueOf(sortedResults.get(userID)), false);
            i ++;
        }
        channel.sendMessageEmbeds(embed.build()).queue();
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
    public static ArrayList<Object> getQuestions(String channelID) throws FileNotFoundException {
        Map<String, Object> theme = games.get(channelID).getValue0();
        return (ArrayList<Object>) theme.get("questions");
    }

}
