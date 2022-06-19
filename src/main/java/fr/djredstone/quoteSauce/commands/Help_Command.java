package fr.djredstone.quoteSauce.commands;

import java.util.HashMap;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;

public class Help_Command extends ListenerAdapter {

    public static final HashMap<String, String> cmds = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + "help")) return;

        EmbedBuilder embed = Utils.getDefaultEmbed()
                .setFooter("Commandé par " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl())
                .setTitle("Voici les commandes disponible:");

        for (String key : cmds.keySet()) {
            embed.addField(Main.prefix + key, cmds.get(key), false);
        }

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
        event.getMessage().delete().queue();

    }

}
