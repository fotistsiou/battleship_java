package step_1;

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

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static final int SIZE = 10;
    public static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    public static final String[] COLS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static String[][] board = new String[SIZE][SIZE];

    public static void main(String[] args) {
        Main.fillBoard();
        Main.printBoard();

        String[] coords = Main.getCoordinates();

        if (coords == null) {
            return;
        }

        int lenght = 1;
        if (coords[0].equals(coords[2])) {
            lenght += Math.abs(Integer.parseInt(coords[1]) - Integer.parseInt(coords[3]));
            int index = Arrays.asList(COLS).indexOf(coords[1]);
            System.out.println("Length: " + lenght);
            System.out.print("Parts: ");
            if (Integer.parseInt(coords[1]) < Integer.parseInt(coords[3])) {
                for (int i = index; i < lenght + index; i++) {
                    System.out.print(coords[0] + COLS[i] + " ");
                }
            } else {
                // TODO: Wrong
                if (Arrays.asList(ROWS).indexOf(coords[3]) == 0) {
                    for (int i = index; i >= lenght - index - 1; i--) {
                        System.out.print(coords[0] + COLS[i] + " ");
                    }
                } else {
                    for (int i = index; i >= lenght - index + 1; i--) {
                        System.out.print(coords[0] + COLS[i] + " ");
                    }
                }
            }
        } else {
            lenght += Math.abs(coords[0].charAt(0) - coords[2].charAt(0));
            int index = Arrays.asList(ROWS).indexOf(coords[0]);
            System.out.println("Length: " + lenght);
            System.out.print("Parts: ");
            if (coords[0].charAt(0) < coords[2].charAt(0)) {
                for (int i = index; i < lenght + index; i++) {
                    System.out.print(ROWS[i] + coords[1] + " ");
                }
            } else {
                // TODO: Wrong
                if (Arrays.asList(ROWS).indexOf(coords[2]) == 0) {
                    for (int i = index; i >= lenght - index - 1; i--) {
                        System.out.print(ROWS[i] + coords[1] + " ");
                    }
                } else {
                    for (int i = index; i >= lenght - index + 1; i--) {
                        System.out.print(ROWS[i] + coords[1] + " ");
                    }
                }
            }
        }
    }

    public static String[] getCoordinates() {
        System.out.println("Enter the coordinates of the ship:");
        String[] coordinates = Arrays
                .stream(Main.scanner.nextLine().split(" "))
                .filter(s -> s.matches("^[A-J](10|[1-9])$"))
                .toArray(String[]::new);

        if (coordinates.length != 2) {
            System.out.println("Error! Coordinates out of bounds!");
            return null;
        }

        char startRow = coordinates[0].charAt(0);
        String startCol = coordinates[1].substring(1);
        char endRow = coordinates[1].charAt(0);
        String endCol = coordinates[1].substring(1);

        if (startRow != endRow && !startCol.equals(endCol)) {
            System.out.println("Error! Coordinates are not on the same line!");
            return null;
        }

        return new String[]{
                String.valueOf(coordinates[0].charAt(0)),
                coordinates[0].substring(1),
                String.valueOf(coordinates[1].charAt(0)),
                coordinates[1].substring(1)
        };
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
