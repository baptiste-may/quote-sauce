package fr.djredstone.quoteSauce;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Utils {

    public static void reply(String message, MessageReceivedEvent event) {

        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(message).queue();
        event.getMessage().delete().queue();

    }

}
