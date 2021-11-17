package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static game.Constants.Game.COLS;
import static game.Constants.Game.ROWS;

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
    public static Map<String, Double[]> readHistory() {
        Map<String, Double[]> history = new HashMap<>();
        try {
            Scanner fileRead = new Scanner(qualitiesFile);

            // Read file line by line - insert into movesMap
            while (fileRead.hasNextLine()) {
                String readLine = fileRead.nextLine();
                String[] splitString = readLine.split(":");
                String moveString = splitString[0];
                Double[] totalCount = {Double.parseDouble(splitString[1]), Double.parseDouble(splitString[2])};

                history.put(moveString, totalCount);
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
    public static void writeHistory(Map<String, Double[]> history) {
        try {
            PrintWriter fileWrite = new PrintWriter(qualitiesFile);
            TreeMap<String, Double[]> sortedHistory = new TreeMap<>(history);
            for (Map.Entry<String, Double[]> entry : sortedHistory.entrySet()) {
                Double[] totalCount = entry.getValue();
                fileWrite.println(entry.getKey() + ":" + totalCount[0] + ":" + totalCount[1]);
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

    /**
     * Converts a board state into string format used by database
     *
     * @param board the board state
     * @return the string format
     */
    public static String boardToDatabaseString(int[][] board) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                str.append(board[i][j] + 1);
            }
        }
        return str.toString();
    }
}
