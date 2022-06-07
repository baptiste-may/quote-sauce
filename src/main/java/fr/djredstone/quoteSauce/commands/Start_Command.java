package fr.djredstone.quoteSauce.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.Setup;
import fr.djredstone.quoteSauce.Utils;

public class Start_Command extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        final String content = event.getMessage().getContentRaw().toLowerCase();
        final String[] args = content.split("\\s+");
        if (!content.startsWith(Main.prefix + "start")) return;

        if (args.length <= 1) {
            Utils.reply(event, "Veuillez entrer l'ID d'un thème");
            return;
        }
        if (!Setup.themeList.contains(args[1])) {
            Utils.reply(event, "L'ID est inconnu");
            return;
        }

        Utils.reply(event, "La partie va bientôt commencer ! Cliquez sur le bouton ci-dessous pour rejoindre la partie", Button.success("join-game", "Rejoindre la partie \uD83C\uDF99"));

    }

}
