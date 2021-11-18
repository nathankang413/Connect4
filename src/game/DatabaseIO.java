package game;

import game.util.Move;
import game.util.MoveHistory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

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
    public static Map<Move, MoveHistory> readHistory() {
        Map<Move, MoveHistory> history = new HashMap<>();
        try {
            Scanner fileRead = new Scanner(qualitiesFile);

            // Read file line by line - insert into movesMap
            while (fileRead.hasNextLine()) {
                String readLine = fileRead.nextLine();
                String[] split = readLine.split(":");
                Move move = Move.fromString(split[0]);
                MoveHistory moveHistory = new MoveHistory(Integer.parseInt(split[1]), Integer.parseInt(split[2]));

                history.put(move, moveHistory);
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
    public static void writeHistory(Map<Move, MoveHistory> history) {
        try {
            PrintWriter fileWrite = new PrintWriter(qualitiesFile);
            Map<Move, MoveHistory> sortedHistory = new TreeMap<>(history);
            for (Map.Entry<Move, MoveHistory> entry : sortedHistory.entrySet()) {
                Move move = entry.getKey();
                MoveHistory moveHistory = entry.getValue();
                fileWrite.println(move.toString() + ":" + moveHistory.getScore() + ":" + moveHistory.getCount());
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
