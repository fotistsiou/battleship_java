package step_3;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Add shooting functionality
 * --------------------------
 * Description
 * The goal of this game is to sink all the ships of your opponent. Our fleet is not ready for a big battle yet, so
 * let's practice shooting on our field. Place all your units on the battlefield and take a shot!
 * In this step, you need to develop a system of shooting with accompanying messages about hits and misses.
 * --------------------------
 * Objectives
 * Take a shot at a prepared game field. You need to indicate the coordinates of the target, and the program should then
 * display a message about a hit or a miss. If the shell misses the target and falls in the water, this cell should be
 * marked with an M, and a successful strike is marked by an X. After this shot, the game should be stopped.
 * If the player managed to hit a ship, the game should display a message You hit a ship!; otherwise, the message is You
 * missed!
 */

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static final int SIZE = 10;
    public static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    public static final String[] COLS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static String[][] board = new String[SIZE][SIZE];

    public static void main(String[] args) {
        Main.initBoard();
        Main.printBoard();
        Main.placeShips("Aircraft Carrier", 5);
        Main.printBoard();
        Main.placeShips("Battleship", 4);
        Main.printBoard();
        Main.placeShips("Submarine", 3);
        Main.printBoard();
        Main.placeShips("Cruiser", 3);
        Main.printBoard();
        Main.placeShips("Destroyer", 2);
        Main.printBoard();

        System.out.println("The game starts!");
        Main.printBoard();
        Main.takeAshot();

        Main.scanner.close();
    }

    public static void initBoard() {
        for (String[] row : Main.board) {
            Arrays.fill(row, "~");
        }
    }

    public static void printBoard() {
        System.out.println("  " + String.join(" ", Main.COLS));
        for (int i = 0; i < Main.SIZE; i++) {
            System.out.println(Main.ROWS[i] + " " + String.join(" ", board[i]));
        }
    }

    public static void placeShips(String type, int shipLength) {
        System.out.printf(
                "Enter the coordinates of the %s (%d cells):%n",
                type,
                shipLength
        );

        while (true) {
            String[] coords;
            do {
                String input = scanner.nextLine();
                coords = Main.getCoordinates(input, shipLength);
            } while(coords == null);

            boolean isUpdateBoard = Main.updateBoard(coords);
            if (isUpdateBoard) return;
        }
    }

    public static String[] getCoordinates(String input, int shipLength) {
        String[] coordinates = Arrays
                .stream(input.split(" "))
                .filter(s -> s.matches("^[A-J](10|[1-9])$"))
                .toArray(String[]::new);

        // 1st Check: Coordinates Bounds
        if (coordinates.length != 2) {
            System.out.println("Error! Wrong coordinate bounds! Try again:");
            return null;
        }

        String startRow = coordinates[0].substring(0, 1);
        String startCol = coordinates[0].substring(1);
        String endRow = coordinates[1].substring(0, 1);
        String endCol = coordinates[1].substring(1);

        // 2nd Check: Ship Location
        if (!startRow.equals(endRow) && !startCol.equals(endCol)) {
            System.out.println("Error! Wrong ship location! Try again:");
            return null;
        }

        // 3rd Check: Ship Length
        int length;
        if (startRow.equals(endRow)) {
            length = Math.abs(Integer.parseInt(startCol) - Integer.parseInt(endCol)) + 1;
        } else {
            length = Math.abs(startRow.charAt(0) - endRow.charAt(0)) + 1;
        }
        if (length != shipLength) {
            System.out.println("Error! Wrong ship length! Try again:");
            return null;
        }

        return new String[]{startRow, startCol, endRow, endCol};
    }

    public static boolean updateBoard(String[] coords) {
        if (coords[0].equals(coords[2])) { // If the ship is placed horizontally
            int length = Math.abs(Integer.parseInt(coords[1]) - Integer.parseInt(coords[3])) + 1;
            int startIndex = Math.min(Arrays.asList(Main.COLS).indexOf(coords[1]) , Arrays.asList(Main.COLS).indexOf(coords[3]));
            for (int i = startIndex; i < length + startIndex; i++) {
                // 4th Check: Ship Neighbors
                boolean existWrong = false;
                if (Arrays.asList(Main.ROWS).indexOf(coords[0]) == 0) { // If coords[0] = A
                    if (
                            Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == (length + startIndex - 1) && Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i + 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (Arrays.asList(Main.ROWS).indexOf(coords[0]) == 9) { // If coords[0] = J
                    if (
                            Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || (i == startIndex && Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i - 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (startIndex == 0) { // If coords[1] = 1 || coords[3] = 1
                    if (
                            Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == (length + startIndex - 1) && Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i + 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (length + startIndex - 1 == 9) { // If coords[1] = 10 || coords[3] = 10
                    if (
                            Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == startIndex && Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i - 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else {
                    if (
                            Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == startIndex && Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i - 1].equals("O"))
                                    || (i == (length + startIndex - 1) && Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i + 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                }
                if (existWrong) {
                    System.out.println("Error! Wrong ship placement! Try again:");
                    return false;
                }

                Main.board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i] = "O";
            }
        }

        if (coords[1].equals(coords[3])) { // If ship placed vertically
            int length = Math.abs(coords[0].charAt(0) - coords[2].charAt(0)) + 1;
            int startIndex = Math.min(Arrays.asList(Main.ROWS).indexOf(coords[0]) , Arrays.asList(Main.ROWS).indexOf(coords[2]));
            for (int i = startIndex; i < length + startIndex; i++) {
                // 4th Check: Ship Neighbors
                boolean existWrong = false;
                if (Arrays.asList(Main.COLS).indexOf(coords[1]) == 0) { // If coords[1] = 1
                    if (
                            Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == (length + startIndex - 1) && Main.board[i + 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (Arrays.asList(Main.COLS).indexOf(coords[1]) == 9) { // If coords[1] = 10
                    if (
                            Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || (i == startIndex && Main.board[i - 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (startIndex == 0) { // If coords[0] = A || coords[2] = A
                    if (
                            Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == (length + startIndex - 1) && Main.board[i + 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (length + startIndex - 1 == 9) { // If coords[0] = J || coords[2] = J
                    if (
                            Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == startIndex && Main.board[i - 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else {
                    if (
                            Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == startIndex && Main.board[i - 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                                    || (i == (length + startIndex - 1) && Main.board[i + 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                }
                if (existWrong) {
                    System.out.println("Error! Wrong ship placement! Try again:");
                    return false;
                }

                Main.board[i][Arrays.asList(Main.COLS).indexOf(coords[1])] = "O";
            }
        }

        return true;
    }

    public static void takeAshot() {
        System.out.println("Take a shot!");
        String[] coordinate;
        while (true) {
            String shot = Main.scanner.nextLine();
            coordinate = Arrays
                    .stream(shot.split(" "))
                    .filter(s -> s.matches("^[A-J](10|[1-9])$"))
                    .toArray(String[]::new);

            // Check: Coordinate Bounds
            if (coordinate.length != 1) {
                System.out.println("Error! Wrong coordinate bounds! Try again:");
                continue;
            }

            break;
        }

        int row = Arrays.asList(Main.ROWS).indexOf(coordinate[0].substring(0,1));
        int col = Arrays.asList(Main.COLS).indexOf(coordinate[0].substring(1));
        String boardValue = Main.board[row][col];
        if (boardValue.equals("~")) {
            Main.board[row][col] = "M";
            Main.printBoard();
            System.out.println("You missed!");
        } else {
            Main.board[row][col] = "X";
            Main.printBoard();
            System.out.println("You hit a ship!");
        }
    }
}
