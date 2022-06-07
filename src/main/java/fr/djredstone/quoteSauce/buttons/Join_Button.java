package fr.djredstone.quoteSauce.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Join_Button extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("join-game")) {
            event.reply("WIP").setEphemeral(true).queue();
        }
    }

}
