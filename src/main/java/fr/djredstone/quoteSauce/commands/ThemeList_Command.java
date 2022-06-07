package fr.djredstone.quoteSauce.commands;

import java.io.File;
import java.util.Objects;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Utils;
import org.apache.commons.io.FilenameUtils;

public class ThemeList_Command extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + "themelist") && !event.getMessage().getContentRaw().toLowerCase().startsWith(Main.prefix + "tl")) return;
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
