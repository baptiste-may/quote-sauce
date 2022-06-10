package fr.djredstone.quoteSauce.game;

import java.util.HashMap;
import java.util.HashSet;

import org.javatuples.Pair;

public class Game {

    public static final int minmumPlayers = 2;

    public static final HashMap<String, Pair<HashMap<String, Integer>, HashSet<Integer>>> games = new HashMap<>();
    // ChannelID : [ PlayersAndPoints : [ PlayerID | Points ] | Questions : [ QuestionNumber ] ]

}
