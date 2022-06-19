package fr.djredstone.quoteSauce.commands;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;
import org.apache.commons.io.FilenameUtils;
import org.yaml.snakeyaml.Yaml;

public class ThemeList_Command extends ListenerAdapter {

    private static final String cmd = "themelist";
    private static final String cmd2 = "tl";
    public ThemeList_Command() { Help_Command.cmds.put(cmd + " / " + Main.prefix + cmd2, "Affiche tous les thèmes avec leurs ID"); }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd)
                && !event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd2)) return;

        EmbedBuilder embed = Utils.getDefaultEmbed()
                .setFooter("Commandé par " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl())
                .setTitle("Thèmes disponibles :");

        String path = "./themes";
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            try {
                String fileName = FilenameUtils.removeExtension(file.getName());
                Map<String, Object> map = new Yaml().load(new FileInputStream(file));
                String themeName = (String) map.get("name");

                embed.addField(themeName, "`" + fileName + "`", false);
            } catch (Exception ignored) {}
        }

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
        event.getMessage().delete().queue();

    }

}
