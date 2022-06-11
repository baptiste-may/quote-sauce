package fr.djredstone.quoteSauce.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Setup;
import fr.djredstone.quoteSauce.Utils;
import fr.djredstone.quoteSauce.game.Game;
import org.javatuples.Pair;

public class Start_Command extends ListenerAdapter {

    private static final String cmd = "start";
    public Start_Command() { Help_Command.cmds.put(cmd, "Permet de commencer une partie"); }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        final String content = event.getMessage().getContentRaw().toLowerCase();
        final String[] args = content.split("\\s+");
        if (!content.startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd)) return;

        if (args.length <= 1) {
            Utils.reply(event, "Veuillez entrer l'ID d'un thème");
            return;
        }
        if (!Setup.themeList.contains(args[1])) {
            Utils.reply(event, "L'ID est inconnu");
            return;
        }

        if (Game.games.containsKey(event.getChannel().getId())) {
            Utils.reply(event, "Une partie est déjà en cours !");
            return;
        }

        String[] messageID = new String[1];
        event.getChannel()
                .sendMessage("La partie commence dans 30s ! Cliquez sur le bouton ci-dessous pour rejoindre la partie")
                .setActionRow(Button.success((Main.devMode ? "TEST_" : "") + "join-game", "Rejoindre la partie \uD83C\uDF99"))
                .queue(message -> messageID[0] = message.getId());
        event.getMessage().delete().queue();
        HashMap<String, Integer> PlayersAndPoints = new HashMap<>();
        PlayersAndPoints.put(event.getAuthor().getId(), 0);
        Game.games.put(event.getChannel().getId(), new Pair<>(PlayersAndPoints, new HashSet<>()));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Game.games.get(event.getChannel().getId()).getValue0().size() < 2) {
                    Game.games.remove(event.getChannel().getId());
                    event.getChannel()
                            .editMessageById(messageID[0], "Partie annulée ! Il faut au moins " + Game.minmumPlayers + " joueurs pour commencer une partie.")
                            .setActionRows(new ArrayList<>()).queue();
                } else {
                    System.out.println("Game started at channel " + event.getChannel().getId());
                    StringBuilder message = new StringBuilder("La partie commence ! Les joueurs suivants participents :\n");
                    for (String userID : Game.games.get(event.getChannel().getId()).getValue0().keySet()) {
                        message.append("- ").append("<@").append(userID).append(">").append("\n");
                    }
                    event.getChannel().editMessageById(messageID[0], message).setActionRows(new ArrayList<>()).queue();
                }
            }
        }, 30 * 1000);

    }

}
