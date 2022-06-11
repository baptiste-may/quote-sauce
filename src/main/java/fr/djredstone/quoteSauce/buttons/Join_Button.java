package fr.djredstone.quoteSauce.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.game.Game;

public class Join_Button extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals((Main.devMode ? "TEST_" : "") + "join-game")) {
            if (Game.games.get(event.getChannel().getId()).getValue1().containsKey(event.getUser().getId())) {
                event.reply("Vous êtes déjà dans le jeu !").setEphemeral(true).queue();
            } else {
                Game.games.get(event.getChannel().getId()).getValue1().put(event.getUser().getId(), 0);
                event.reply("Vous avez rejoind le jeu !").setEphemeral(true).queue();
            }
        }
    }

}
