package step_2;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Place all ships
 * ---------------
 * Description
 * Now that we've figured out the rules of the game let's continue with positioning the ships and drawing them on our
 * game field! The goal of this stage is to place all the ships on the game field according to the rules.
 * ---------------
 * Objectives
 * You have 5 ships:
 * - Aircraft Carrier is 5 cells
 * - Battleship is 4 cells
 * - Submarine is 3 cells
 * - Cruiser is also 3 cells
 * - Destroyer is 2 cells.
 * In this stage, you should arrange them all on the game field.
 * 1. Start placing your ships with the largest one.
 * 2. For each ship read two coordinates: the beginning and the end of the ship. Again, the order of the coordinates
 *    does not matter.
 * 3. Add new ships to a game field and output it the same way as in the previous stage.
 * 4. If the user has entered coordinates in such a way that the length of the created ship does not match the expected
 *    length, this should be considered an incorrect input. Also, the game rules state that ships cannot be adjacent to
 *    each other. For both of these cases report it with a message containing Error word.
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
        Main.scanner.close();
    }

    public static void initBoard() {
        for (String[] row : board) {
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

        // 1st Check: Coordinate Bounds
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
}
