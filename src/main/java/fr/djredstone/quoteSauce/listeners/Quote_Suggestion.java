package fr.djredstone.quoteSauce.listeners;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import fr.djredstone.quoteSauce.Main;
import fr.djredstone.quoteSauce.game.Game;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

public class Quote_Suggestion extends ListenerAdapter {

    @SuppressWarnings("unchecked")
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(Main.prefix)) return;

        if (Game.games.containsKey(event.getChannel().getId())) {
            Quintet<String, HashMap<String, Integer>, HashSet<Integer>, Triplet<Boolean, Integer, String>, Integer> game = Game.games.get(event.getChannel().getId());
            if (game.getValue1().containsKey(event.getAuthor().getId()) && game.getValue3().getValue0()) {
                try {
                    HashMap<String, Object> quote = (HashMap<String, Object>) Game.getQuestions(game.getValue0()).get(game.getValue3().getValue1()-1);
                    ArrayList<Object> aswers = (ArrayList<Object>) quote.get("aswer");
                    for (Object aswer : aswers) {
                        if ((event.getMessage().getContentRaw().toLowerCase().equalsIgnoreCase((String) aswer))) {
                            Game.playerFindQuote(event.getTextChannel(), event.getAuthor());
                        }
                    }
                    event.getMessage().delete().queue();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
