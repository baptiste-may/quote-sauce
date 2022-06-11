package fr.djredstone.quoteSauce.game;

import java.io.FileNotFoundException;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.TextChannel;

public class Timeout extends TimerTask {

    TextChannel channel;
    Timeout(TextChannel channel) {
        this.channel = channel;
    }
    @Override
    public void run() {
        try {
            Game.playerFindQuote(channel, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
