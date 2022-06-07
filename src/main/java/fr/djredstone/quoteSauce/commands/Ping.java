package fr.djredstone.quoteSauce.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;

public class Ping extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + "ping")) return;
        Utils.reply("Pong ! \uD83C\uDFD3 (`" + Main.jda.getGatewayPing() + " ms`)", event);

    }

}
