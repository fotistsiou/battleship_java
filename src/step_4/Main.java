package step_4;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Implement the "fog of war" feature
 * ----------------------------------
 * Description
 * It seems a little odd to shoot your own ships. Let's imagine they are not ours! You can ask a friend to place the
 * ships on the game field (or do it yourself, but it will be less exciting), and then the program will hide the ships
 * under the fog of war. You just have to take a blind shot.
 * ----------------------------------
 * Objectives
 * In this stage, you need to implement the "fog of war" feature in your game. First, place all the ships on the game
 * field, and then hide them with the symbol ~. Take a shot like in the previous stage, and after your attempt, the
 * program should print a message along with two versions of the field: one covered with the fog of war and the other
 * one uncovered.
 */

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static final int SIZE = 10;
    public static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    public static final String[] COLS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static String[][] board = new String[SIZE][SIZE];
    public static String[][] fogBoard = new String[SIZE][SIZE];

    public static void main(String[] args) {
        Main.initBoards();
        Main.printBoard(Main.board);
        Main.placeShips("Aircraft Carrier", 5);
        Main.printBoard(Main.board);
        Main.placeShips("Battleship", 4);
        Main.printBoard(Main.board);
        Main.placeShips("Submarine", 3);
        Main.printBoard(Main.board);
        Main.placeShips("Cruiser", 3);
        Main.printBoard(Main.board);
        Main.placeShips("Destroyer", 2);
        Main.printBoard(Main.board);

        System.out.println("The game starts!");
        Main.printBoard(Main.fogBoard);
        Main.takeAshot();

        Main.scanner.close();
    }

    public static void initBoards() {
        for (String[] row : Main.board) {
            Arrays.fill(row, "~");
        }
        for (String[] row : Main.fogBoard) {
            Arrays.fill(row, "~");
        }
    }

    public static void printBoard(String[][] board) {
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
            Main.fogBoard[row][col] = "M";
            Main.printBoard(Main.fogBoard);
            System.out.println("You missed!");
            Main.printBoard(Main.board);
        } else {
            Main.board[row][col] = "X";
            Main.fogBoard[row][col] = "X";
            Main.printBoard(Main.fogBoard);
            System.out.println("You hit a ship!");
            Main.printBoard(Main.board);
        }
    }
}

