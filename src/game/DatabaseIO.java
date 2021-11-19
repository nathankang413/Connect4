package game;

import game.util.Move;
import game.util.MoveMetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * A class to read and write move qualities data
 */
public class DatabaseIO {
    private static File qualitiesFile = new File("./src/game/qualities.txt");

    /**
     * Reads in history of all games from database into a Map
     *
     * @return a map of the history
     */
    public static Map<Move, MoveMetrics> readHistory() {
        Map<Move, MoveMetrics> history = new HashMap<>();
        try {
            Scanner fileRead = new Scanner(qualitiesFile);

            // Read file line by line - insert into movesMap
            while (fileRead.hasNextLine()) {
                String readLine = fileRead.nextLine();
                String[] split = readLine.split(":");
                Move move = Move.fromString(split[0]);
                MoveMetrics moveMetrics = new MoveMetrics(Integer.parseInt(split[1]), Integer.parseInt(split[2]));

                history.put(move, moveMetrics);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Missing or invalid qualities.txt file: " + qualitiesFile);
            throw new IllegalArgumentException("Missing or invalid qualities.txt file: " + qualitiesFile);
        }
        return history;
    }

    /**
     * Writes the history of a completed game into the database
     *
     * @param history the history of the completed game
     */
    public static void writeHistory(Map<Move, MoveMetrics> history) {
        try {
            PrintWriter fileWrite = new PrintWriter(qualitiesFile);
            Map<Move, MoveMetrics> sortedHistory = new TreeMap<>(history);
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
     * @param file the new qualities file
     */
    public static void setQualitiesFile(File file) {
        qualitiesFile = file;
    }
}
