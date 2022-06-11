package fr.djredstone.quoteSauce.commands;

import java.io.File;
import java.util.Objects;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;
import org.apache.commons.io.FilenameUtils;

public class ThemeList_Command extends ListenerAdapter {

    private static final String cmd = "themelist";
    private static final String cmd2 = "tl";
    public ThemeList_Command() { Help_Command.cmds.put(cmd + "` / `" + Main.prefix + cmd2, "Affiche tous les thèmes avec leurs ID"); }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd)
                && !event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + (Main.devMode ? "test_" : "") + cmd2)) return;
        StringBuilder builder = new StringBuilder("Thèmes disponibles : ");
        String path = "./themes";
        boolean first = true;
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            if (!first) builder.append(" | ");
            else first = false;
            builder.append("`").append(FilenameUtils.removeExtension(file.getName())).append("`");
        }
        Utils.reply(event, builder.toString());

    }

}
