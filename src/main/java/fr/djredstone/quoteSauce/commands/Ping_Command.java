package fr.djredstone.quoteSauce.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;

public class Ping_Command extends ListenerAdapter {

    private static final String cmd = "ping";
    public Ping_Command() { Help_Command.cmds.put(cmd, "Affiche la vitesse de réaction du bot"); }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd)) return;
        Utils.replyEmbed(event, "Pong ! \uD83C\uDFD3", "`" + Main.jda.getGatewayPing() + " ms`");

    }

}
