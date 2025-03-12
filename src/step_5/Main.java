package step_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Define rules to end the game
 * ----------------------------
 * Description
 * It looks like everything is ready for full-scale battlefield maneuvers! This time, don't cease fire until all the
 * ships are sunk. At the end of the game, your program should print a congratulatory message to the winner: You sank
 * the last ship. You won. Congratulations!
 * ----------------------------
 * Objectives
 * To complete this step, you should add a check that all the ships were successfully sunk. The game is supposed to go
 * on until all ships go down. The program should print an extra message You sank a ship! when all the cells of a
 * particular ship have been hit. Take a look at the examples below!
 * For the sake of simplicity; the project does not consider shots to coordinates that are already shot at to be any
 * different. Regardless of whether the coordinate was previously a hit or a miss, you should display You hit a ship!
 * and You missed! again respectively.
 */

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int SIZE = 10;
    private static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private static final String[] COLS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final String[][] board = new String[SIZE][SIZE];
    private static final String[][] fogBoard = new String[SIZE][SIZE];
    private static final String[][] shipAllCoordinates = new String[5][2];
    public static int counterShots = 0;

    public static void main(String[] args) {
        Main.initBoards();
        Main.printBoard(Main.board);
        Main.placeShips("Aircraft Carrier", 5, 0);
        Main.printBoard(Main.board);
        Main.placeShips("Battleship", 4, 1);
        Main.printBoard(Main.board);
        Main.placeShips("Submarine", 3, 2);
        Main.printBoard(Main.board);
        Main.placeShips("Cruiser", 3, 3);
        Main.printBoard(Main.board);
        Main.placeShips("Destroyer", 2, 4);
        Main.printBoard(Main.board);

        System.out.println("The game starts!");
        Main.printBoard(Main.fogBoard);
        System.out.println("Take a shot!");
        while (Main.counterShots < 17) {
            Main.takeAshot();
        }
        System.out.println("You sank the last ship. You won. Congratulations!");

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

    public static void placeShips(String type, int shipLength, int shipNumber) {
        System.out.printf(
            "Enter the coordinates of the %s (%d cells):%n",
            type,
            shipLength
        );

        while (true) {
            String[] coords;
            String input;
            do {
                input = scanner.nextLine();
                coords = Main.getCoordinates(input, shipLength);
            } while(coords == null);

            boolean isUpdateBoard = Main.updateBoard(coords);
            if (isUpdateBoard) {
                Main.shipAllCoordinates[shipNumber] = Arrays
                    .stream(input.split(" "))
                    .toArray(String[]::new);
                return;
            }
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
            System.out.println("You missed! Try again:");
        } else if (boardValue.equals("O")) {
            Main.board[row][col] = "X";
            Main.fogBoard[row][col] = "X";
            Main.counterShots++;
            Main.printBoard(Main.fogBoard);
            boolean shipSunk = Main.checkIfShotHitShip(row, col);
            String message = shipSunk ? "You sank a ship! Specify a new target:" : "You hit a ship! Try again:";
            System.out.println(message);
        } else if (boardValue.equals("M")) {
            Main.printBoard(Main.fogBoard);
            System.out.println("You missed!");
        } else {
            Main.printBoard(Main.fogBoard);
            System.out.println("You hit a ship!");
        }
    }

    public static boolean checkIfShotHitShip(int row, int col) {
        String hitCoordinate = Main.ROWS[row] + Main.COLS[col];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (Main.shipAllCoordinates[i][j].equals(hitCoordinate)) {
                    return Main.checkIfShipIsSunken(i);
                }
            }
        }
        return false;
    }

    public static boolean checkIfShipIsSunken(int shipIndex) {
        String startRow = Main.shipAllCoordinates[shipIndex][0].substring(0, 1);
        String startCol = Main.shipAllCoordinates[shipIndex][0].substring(1);
        String endRow = Main.shipAllCoordinates[shipIndex][1].substring(0, 1);
        String endCol = Main.shipAllCoordinates[shipIndex][1].substring(1);

        if (startRow.equals(endRow)) { // If the ship is placed horizontally
            int length = Integer.parseInt(endCol) - Integer.parseInt(startCol) + 1;
            int startIndex = Arrays.asList(Main.COLS).indexOf(startCol);
            for (int i = startIndex; i < length + startIndex; i++) {
                if (!Main.board[Arrays.asList(Main.ROWS).indexOf(startRow)][i].equals("X")) {
                    return false;
                }
            }
        }

        if (startCol.equals(endCol)) { // If ship placed vertically
            int length = endRow.charAt(0) - startRow.charAt(0) + 1;
            int startIndex = Arrays.asList(Main.ROWS).indexOf(startRow);
            for (int i = startIndex; i < length + startIndex; i++) {
                if (!Main.board[i][Arrays.asList(Main.COLS).indexOf(startCol)].equals("X")) {
                    return false;
                }
            }
        }

        return true;
    }
}
