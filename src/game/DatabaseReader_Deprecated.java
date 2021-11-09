package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import static game.Constants.Game.*;
import static game.Constants.QLearn.*;

public class DatabaseReader_Deprecated {
    private static TreeMap<String, Double[]> movesMap;
    private static int[][] board;
    private static ArrayList<String> path;
    private static Scanner user;

    public static void main(String[] args) throws IOException {
        // read the file
        movesMap = new TreeMap<>();
        readFile();

        // initialize blank board
        board = new int[ROWS][COLS];
        int player = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = -1;
            }
        }

        // initialize scanner for user input
        user = new Scanner(System.in);

        // initialize current Branch to remember pathing
        path = new ArrayList<>();

        while (true) {
            // show the board
            printBoard();

            // get all options at that moment
            System.out.println("Potential moves: (win% - count)");
            for (int i = 0; i < COLS; i++) {

                System.out.print((i + 1) + ": ");

                String key = boardToString(board) + "-" + i;
                if (movesMap.containsKey(key)) {

                    double totalQ = movesMap.get(key)[0];
                    double count = movesMap.get(key)[1];

                    double winRate = (totalQ + count) / 2 / count * 100;

                    System.out.printf("%.2f - %.0f \n", winRate, count);
                } else {
                    System.out.println("unplayed");
                }
            }

            // get user input
            int move = getUserInput();
            if (move == -1) {
                board = stringToBoard(path.remove(path.size() - 1));
                player = 1 - player;
            } else if (move == -2) {
                break;
            } else {
                path.add(boardToString(board));
                dropPiece(move, player);
                player = 1 - player;
            }
        }

    }

    private static void readFile() throws IOException {
        Scanner fileRead = new Scanner(new File(QUALITIES_FILE));

        // Read file line by line - insert into tree map
        while (fileRead.hasNextLine()) {
            String readLine = fileRead.nextLine();
            String[] splitString = readLine.split(":");
            String moveString = splitString[0];
            Double[] totalCount = {Double.parseDouble(splitString[1]), Double.parseDouble(splitString[2])};

            movesMap.put(moveString, totalCount);
        }
    }

    private static String boardToString(int[][] board) {

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                str.append(board[i][j] + 1);
            }
        }

        return str.toString();

    }

    private static int[][] stringToBoard(String str) {

        int[][] board = new int[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int value = Integer.parseInt(str.substring(i * COLS + j, i * COLS + j + 1)) - 1;
                board[i][j] = value;
            }
        }
        return board;

    }

    /**
     * Displays the text version of the board
     */
    private static void printBoard() {

        System.out.println();

        // iterate through rows
        for (int i = 0; i < ROWS; i++) {

            System.out.print("|");

            // iterate through cols
            for (int j = 0; j < COLS; j++) {

                // show the correct piece
                switch (board[i][j]) {
                    case -1 -> System.out.print(" ");
                    case 0 -> System.out.print("X");
                    case 1 -> System.out.print("O");
                }
                System.out.print("|");

            }
            System.out.println();
        }

        // column labels
        for (int i = 0; i < COLS; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");
    }

    private static int getUserInput() {
        int col = -1;
        boolean invalid = true;
        while (invalid) {
            try {
                System.out.println("Choose a column to drop your piece: ");

                String input = user.nextLine();
                if (input.equals("q")) return -2;
                if (input.equals("b")) return -1;
                col = Integer.parseInt(input);

                if (col > 0 && col <= COLS) {
                    invalid = false;
                }
            } catch (Exception e) {
                user.next();
            }
            if (invalid) System.out.println("Invalid input!");
        }
        return col - 1;
    }

    private static void dropPiece(int col, int player) {
        if (board[0][col] != -1) {
            throw new IllegalArgumentException("Column " + (col + 1) + " is full.");
        }
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == -1) {
                board[i][col] = player;
                break;
            }
        }
    }
}
