package fr.djredstone.quoteSauce.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Setup;
import fr.djredstone.quoteSauce.Utils;
import fr.djredstone.quoteSauce.game.Game;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.yaml.snakeyaml.Yaml;

public class Start_Command extends ListenerAdapter {

    private static final String cmd = "start";
    public Start_Command() { Help_Command.cmds.put(cmd, "Permet de commencer une partie"); }

    @SuppressWarnings("unchecked")
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        final String content = event.getMessage().getContentRaw().toLowerCase();
        final String[] args = content.split("\\s+");
        if (!content.startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd)) return;

        if (args.length <= 1) {
            Utils.reply(event, "Veuillez entrer l'ID d'un thème \uD83D\uDCCE");
            return;
        }
        if (!Setup.themeList.contains(args[1])) {
            Utils.reply(event, "L'ID est inconnu \uD83E\uDD14");
            return;
        }

        if (Game.games.containsKey(event.getChannel().getId())) {
            Utils.reply(event, "Une partie est déjà en cours ! ⛔️");
            return;
        }

        int maxQuestionNumber = 0;
        try {
            ArrayList<Object> questions = Game.getQuestions(args[1]);
            for (Object key : questions) {
                final HashMap<String, Object> quote = (HashMap<String, Object>) key;
                final int number = (int) quote.get("number");
                if (number > maxQuestionNumber)
                    maxQuestionNumber = number;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String[] messageID = new String[1];
        Map<String, Object> map;
        try {
            map = new Yaml().load(new FileInputStream("./themes/" + args[1] + ".yaml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String themeName = (String) map.get("name");
        event.getChannel()
                .sendMessage("Une partie commence dans **30s** ! \uD83D\uDCAA Elle est sur le thème : __" + themeName + "__ \uD83D\uDC40\n" +
                        "*Cliquez sur le bouton ci-dessous pour rejoindre la partie*")
                .setActionRow(Button.success((Main.devMode ? "TEST_" : "") + "join-game", "Rejoindre la partie \uD83C\uDF99"))
                .queue(message -> messageID[0] = message.getId());
        event.getMessage().delete().queue();
        HashMap<String, Integer> PlayersAndPoints = new HashMap<>();
        PlayersAndPoints.put(event.getAuthor().getId(), 0);
        Game.games.put(event.getChannel().getId(), new Quintet<>(args[1], PlayersAndPoints, new HashSet<>(), new Quartet<>(false, null, null, null), maxQuestionNumber));

        int finalMaxQuestionNumber = maxQuestionNumber;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Game.games.get(event.getChannel().getId()).getValue1().size() < 2) {
                    Game.games.remove(event.getChannel().getId());
                    event.getChannel()
                            .editMessageById(messageID[0], "**Partie annulée !** \uD83D\uDED1 Il faut au moins __" + Game.minmumPlayers + "__ joueurs pour commencer une partie.")
                            .setActionRows(new ArrayList<>()).queue();
                } else {
                    System.out.println("Game started at channel " + event.getChannel().getId() + " with " + finalMaxQuestionNumber + " questions");
                    StringBuilder message = new StringBuilder("**La partie commence !** \uD83C\uDF99 (" + themeName + ")\n\n" +
                            "Les joueurs suivants participents :\n");
                    for (String userID : Game.games.get(event.getChannel().getId()).getValue1().keySet()) {
                        message.append("- ").append("<@").append(userID).append(">").append("\n");
                    }
                    event.getChannel().editMessageById(messageID[0], message).setActionRows(new ArrayList<>()).queue();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Game.startGame((TextChannel) event.getChannel());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 5 * 1000);
                }
            }
        }, 30 * 1000);

    }

}
