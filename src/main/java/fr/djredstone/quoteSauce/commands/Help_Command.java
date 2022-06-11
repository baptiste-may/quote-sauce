package fr.djredstone.quoteSauce.commands;

import java.util.HashMap;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;

public class Help_Command extends ListenerAdapter {

    public static final HashMap<String, String> cmds = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + "help")) return;
        StringBuilder message = new StringBuilder("Voici les commandes disponible:\n\n");
        for (String key : cmds.keySet()) {
            message.append("> `").append(Main.prefix).append(key).append("`\n");
            message.append(cmds.get(key)).append("\n\n");
        }
        Utils.reply(event, message.toString());

    }

}
