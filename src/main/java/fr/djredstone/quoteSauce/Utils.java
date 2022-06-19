package fr.djredstone.quoteSauce;

import java.awt.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

import org.jetbrains.annotations.Nullable;

public class Utils {

    public static EmbedBuilder getDefaultEmbed() {
        return new EmbedBuilder().setColor(Color.decode("#FB9318"));
    }

    public static void replyEmbed(MessageReceivedEvent event, String message, @Nullable String subMessage, ItemComponent... components) {

        EmbedBuilder embed = getDefaultEmbed()
                .setFooter("Commandé par " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl())
                .setTitle(message)
                .setDescription(subMessage);

        if (components.length == 0) event.getChannel().sendMessageEmbeds(embed.build()).queue();
        else event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(components).queue();
        event.getMessage().delete().queue();

    }

}
