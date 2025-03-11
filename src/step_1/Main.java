package step_1;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Create the field
 * ----------------
 * Battleship (also called Battleships or Sea Battle) is a two-player strategy game whose history traces back to the
 * First World War. It started off as a pencil and paper game, until Milton Bradley coined the rules and published the
 * game. Fun fact: it was one of the first games to be produced as a computer game in 1979! In this project, we will
 * recreate this timeless classic.
 * First off, brush up on the rules of the game. There are different variations of the Battleship game, but we will
 * stick to the original rules written by Milton Bradley. You have a 10×10 game field and five ships to arrange on that
 * field. The ships can be placed horizontally or vertically but not diagonally across the grid spaces; the ships should
 * not cross or touch each other. The goal is to sink all the ships of the opponent before your opponent does this to
 * you.
 * Understanding how the ships will be fielded is exactly where we are going to start!
 * Before you start, let's discuss the conventions of the game:
 * 1. On a 10x10 field, the first row should contain numbers from 1 to 10 indicating the column, and the first column
 *    should contain letters from A to J indicating the row.
 * 2. The symbol ~ denotes the fog of war: the unknown area on the opponent's field and the yet untouched area on your
 *    field.
 * 3. The symbol O denotes a cell with your ship, X denotes that the ship was hit, and M signifies a miss.
 * ----------------
 * Objectives
 * At this stage, you will put your first ship on the game field.
 * 1. Print an empty game field, according to game conventions.
 * 2. To place a ship, enter two coordinates, the beginning and the end of the ship. The order of the coordinates (start
 *    to end or end to start) does not really matter.
 * 3. In later stages we will need to know the length of the ship and the positions on the field on which it's parts are
 *    placed. Therefore, output this information to a player. Here is an example of possible formatting: Length: 3 and
 *    Parts: F2 F3 F4
 * 4. If an error occurs in the input coordinates (coordinates are not on the same line or out of bounds), your program
 *    should report it. The message should contain the word Error.
 */

public class Main {
    public static final int SIZE = 10;
    public static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    public static final String[] COLS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static String[][] board = new String[SIZE][SIZE];

    public static void main(String[] args) {
        Main.fillBoard();
        Main.printBoard();
        String[] coords = Main.getCoordinates();
        if (coords == null) return;
        Main.printShipDetails(coords);
    }

    public static void printShipDetails(String[] coords) {
        if (coords[0].equals(coords[2])) {
            int length = Math.abs(Integer.parseInt(coords[1]) - Integer.parseInt(coords[3])) + 1;
            int startIndex = Arrays.asList(Main.COLS).indexOf(coords[1]);
            System.out.println("Length: " + length);
            System.out.print("Parts: ");
            if (Integer.parseInt(coords[1]) < Integer.parseInt(coords[3])) {
                int upperBound = length + startIndex;
                while (startIndex < upperBound) {
                    System.out.print(coords[0] + Main.COLS[startIndex] + " ");
                    startIndex++;
                }
            } else {
                int counter = 0;
                while (counter < length) {
                    System.out.print(coords[0] + Main.COLS[startIndex] + " ");
                    counter++;
                    startIndex--;
                }
            }
        }

        if (coords[1].equals(coords[3])) {
            int length = Math.abs(coords[0].charAt(0) - coords[2].charAt(0)) + 1;
            int startIndex = Arrays.asList(Main.ROWS).indexOf(coords[0]);
            System.out.println("Length: " + length);
            System.out.print("Parts: ");
            if (coords[0].charAt(0) < coords[2].charAt(0)) {
                int upperBound = length + startIndex;
                while (startIndex < upperBound) {
                    System.out.print(Main.ROWS[startIndex] + coords[1] + " ");
                    startIndex++;
                }
            } else {
                int counter = 0;
                while (counter < length) {
                    System.out.print(Main.ROWS[startIndex] + coords[1] + " ");
                    counter++;
                    startIndex--;
                }
            }
        }
    }

    public static String[] getCoordinates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the ship:");
        String[] coordinates = Arrays
            .stream(scanner.nextLine().split(" "))
            .filter(s -> s.matches("^[A-J](10|[1-9])$"))
            .toArray(String[]::new);

        if (coordinates.length != 2) {
            System.out.println("Error! Coordinates out of bounds!");
            scanner.close();
            return null;
        }

        String startRow = coordinates[0].substring(0, 1);
        String startCol = coordinates[0].substring(1);
        String endRow = coordinates[1].substring(0, 1);
        String endCol = coordinates[1].substring(1);

        if (!startRow.equals(endRow) && !startCol.equals(endCol)) {
            System.out.println("Error! Coordinates are not on the same line!");
            scanner.close();
            return null;
        }

        scanner.close();
        return new String[]{startRow, startCol, endRow, endCol};
    }

    public static void fillBoard() {
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
}
