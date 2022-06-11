package fr.djredstone.quoteSauce;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

public class Utils {

    public static void reply(MessageReceivedEvent event, String message) {

        event.getChannel().sendMessage(message).queue();
        event.getMessage().delete().queue();

    }

    public static void reply(MessageReceivedEvent event, String message, ItemComponent... components) {

        if (components.length == 0) event.getChannel().sendMessage(message).queue();
        else event.getChannel().sendMessage(message).setActionRow(components).queue();
        event.getMessage().delete().queue();

    }

}
