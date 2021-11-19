package game.backend;

import game.util.Move;
import game.util.MoveMetrics;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static game.util.Constants.Game.*;

/**
 * A class to read, store, and write move qualities data
 */
public class DatabaseIO {
    private static File qualitiesFile = new File("qualities.txt");

    private static Map<Move, MoveMetrics> qualitiesMap;

    /**
     * Initializes the DatabaseIO if it hasn't been initialized already
     *
     * @return whether initialization was successful
     */
    public static boolean initialize() {
        if (qualitiesMap == null) {
            try {
                readFile();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, """
                        Missing qualities file\s
                        In Q-Learn Options, either Select a Qualities file or\s
                        press Save Qualities File to create a new file.""");
                qualitiesMap = null;
                return false;
            } catch(Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Qualities file invalid");
                qualitiesMap = null;
                return false;
            }
        }
        return true;
    }

    /**
     * Reads in history of all games from file into a Map
     */
    public static void readFile() throws FileNotFoundException {
        Scanner fileRead = new Scanner(qualitiesFile);
        qualitiesMap = new HashMap<>();

        // Read file line by line - insert into movesMap
        while (fileRead.hasNextLine()) {
            String readLine = fileRead.nextLine();
            String[] split = readLine.split(":");
            Move move = Move.fromString(split[0]);
            MoveMetrics moveMetrics = new MoveMetrics(Integer.parseInt(split[1]), Integer.parseInt(split[2]));

            qualitiesMap.put(move, moveMetrics);
        }
    }

    /**
     * Writes the current qualitiesMap to the file
     */
    public static void writeFile() {
        if (qualitiesMap == null || qualitiesMap.size() <= 0) return;

        try {
            PrintWriter fileWrite = new PrintWriter(qualitiesFile);
            Map<Move, MoveMetrics> sortedHistory = new TreeMap<>(qualitiesMap);
            for (Map.Entry<Move, MoveMetrics> entry : sortedHistory.entrySet()) {
                Move move = entry.getKey();
                MoveMetrics moveMetrics = entry.getValue();
                fileWrite.println(move.toString() + ":" + moveMetrics.getScore() + ":" + moveMetrics.getCount());
            }
            fileWrite.flush();
            fileWrite.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * Adds the given moves with the correct scores to the qualitiesMap
     * Assumes the last played move won
     *
     * @param gameHistory a ArrayList of moves made in one game
     * @param tie whether the game ended in a tie
     */
    public static void addToHistory(ArrayList<Move> gameHistory, boolean tie) {
        boolean winner = true;

        // iterate backwards through currHistory, alternate winning and losing qualities
        for (int i = gameHistory.size() - 1; i >= 0; i--) {
            if (tie) {
                addToHistory(gameHistory.get(i), TIE);
            } else if (winner) {
                addToHistory(gameHistory.get(i), WIN);
            } else {
                addToHistory(gameHistory.get(i), LOSS);
            }
            winner = !winner;
        }
    }

    /**
     * Adds the given move and score the qualitiesMap
     *
     * @param move the move that has been played
     * @param score the resulting score of the move
     */
    public static void addToHistory(Move move, int score) {
        if (qualitiesMap.containsKey(move)) {
            MoveMetrics moveMetrics = qualitiesMap.get(move);

            moveMetrics.addScore(score);
            moveMetrics.incrementCount();
        } else {
            qualitiesMap.put(move, new MoveMetrics(score, 1));
        }
    }

    /**
     * Reads the file if necessary and returns the qualities Map
     *
     * @return the qualities Map
     */
    public static Map<Move, MoveMetrics> getQualitiesMap() {
        if (qualitiesMap == null) {
            initialize();
        }

        return qualitiesMap;
    }

    /**
     * @param file the new qualities file
     */
    public static void setQualitiesFile(File file) {
        qualitiesFile = file;
    }
}
