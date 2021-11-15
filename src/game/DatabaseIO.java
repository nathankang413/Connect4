package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static game.Constants.Game.*;
import static game.Constants.QLearn.*;

public class DatabaseIO {
    private static TreeMap<String, Double[]> movesMap;
    private static int[][] board;
    private static ArrayList<String> path;
    private static Scanner user;

    public static Map<String, Double[]> readHistory() {
        Map<String, Double[]> history = new HashMap<>();
        try {
            Scanner fileRead = new Scanner(new File(QUALITIES_FILE));

            // Read file line by line - insert into movesMap
            while (fileRead.hasNextLine()) {
                String readLine = fileRead.nextLine();
                String[] splitString = readLine.split(":");
                String moveString = splitString[0];
                Double[] totalCount = {Double.parseDouble(splitString[1]), Double.parseDouble(splitString[2])};

                history.put(moveString, totalCount);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Missing or invalid qualities.txt file: " + e);
            throw new IllegalArgumentException("Missing or invalid qualities.txt file");
        }
        return history;
    }

    public static void writeHistory(Map<String, Double[]> history) {
        try {
            PrintWriter fileWrite = new PrintWriter(QUALITIES_FILE);
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

    public static String boardToDatabaseString(int[][] board) {
        // TODO: dependent on "color" ie if player 1 goes first vs player 2
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                str.append(board[i][j] + 1);
            }
        }
        return str.toString();
    }
}
