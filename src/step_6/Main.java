package step_6;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Add multiplayer option
 * ----------------------
 * Description
 * Here is a good way to show off your new skills: offer a friend to play a computer game that you wrote yourself! Of
 * course, it is much more fun to play Battleship with someone else: the possibility of winning or losing adds a thrill
 * to the game!
 * Both players add the ships to their fields one by one (no peeking!), and then start shelling each other until one of
 * them succeeds. To make the game fair and prevent the players from peeping at each other's fields, after each move add
 * the message "Press Enter and pass the move to another player", which will clear the screen.
 * ----------------------
 * Objectives
 * To complete this stage and the entire project, add a PvP component to your game. Now the player will see not only the
 * opponent's screen but their own as well. Place the opponent's screen at the top and your field at the bottom.
 */

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int SIZE = 10;
    private static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private static final String[] COLS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static String currentPlayer;

    // Player 1
    private static final String[][] board1 = new String[SIZE][SIZE];
    private static final String[][] fogBoard1 = new String[SIZE][SIZE];
    private static final String[][] shipAllCoordinates1 = new String[5][2];
    public static int counterShots1 = 0;

    // Player 2
    private static final String[][] board2 = new String[SIZE][SIZE];
    private static final String[][] fogBoard2 = new String[SIZE][SIZE];
    private static final String[][] shipAllCoordinates2 = new String[5][2];
    public static int counterShots2 = 0;

    public static void main(String[] args) {
        // Init Board Player 1
        Main.initBoards(Main.board1, Main.fogBoard1);
        System.out.println("Player 1, place your ships on the game field");
        Main.printBoard(Main.board1);
        Main.placeShips(Main.board1, Main.shipAllCoordinates1, "Aircraft Carrier", 5, 0);
        Main.printBoard(Main.board1);
        Main.placeShips(Main.board1, Main.shipAllCoordinates1, "Battleship", 4, 1);
        Main.printBoard(Main.board1);
        Main.placeShips(Main.board1, Main.shipAllCoordinates1, "Submarine", 3, 2);
        Main.printBoard(Main.board1);
        Main.placeShips(Main.board1, Main.shipAllCoordinates1, "Cruiser", 3, 3);
        Main.printBoard(Main.board1);
        Main.placeShips(Main.board1, Main.shipAllCoordinates1, "Destroyer", 2, 4);
        Main.printBoard(Main.board1);

        // Change Player
        System.out.println("Press Enter and pass the move to another player");
        Main.scanner.nextLine();

        // Init Board Player 2
        Main.initBoards(Main.board2, Main.fogBoard2);
        System.out.println("Player 2, place your ships to the game field");
        Main.printBoard(Main.board2);
        Main.placeShips(Main.board2, Main.shipAllCoordinates2, "Aircraft Carrier", 5, 0);
        Main.printBoard(Main.board2);
        Main.placeShips(Main.board2, Main.shipAllCoordinates2, "Battleship", 4, 1);
        Main.printBoard(Main.board2);
        Main.placeShips(Main.board2, Main.shipAllCoordinates2, "Submarine", 3, 2);
        Main.printBoard(Main.board2);
        Main.placeShips(Main.board2, Main.shipAllCoordinates2, "Cruiser", 3, 3);
        Main.printBoard(Main.board2);
        Main.placeShips(Main.board2, Main.shipAllCoordinates2, "Destroyer", 2, 4);
        Main.printBoard(Main.board2);

        // Change Player
        System.out.println("Press Enter and pass the move to another player");
        Main.scanner.nextLine();

        // Start Game
        Main.currentPlayer = "Player 1";
        while (Main.counterShots1 < 17 && Main.counterShots2 < 17) {
            Main.takeAshot();
        }

        Main.scanner.close();
    }

    public static void initBoards(String[][] board, String[][] fogBoard) {
        for (String[] row : board) {
            Arrays.fill(row, "~");
        }
        for (String[] row : fogBoard) {
            Arrays.fill(row, "~");
        }
    }

    public static void printBoard(String[][] board) {
        System.out.println("  " + String.join(" ", Main.COLS));
        for (int i = 0; i < Main.SIZE; i++) {
            System.out.println(Main.ROWS[i] + " " + String.join(" ", board[i]));
        }
    }

    public static void placeShips(String[][] board, String[][] shipAllCoordinates, String type, int shipLength, int shipNumber) {
        System.out.printf(
                "Enter the coordinates of the %s (%d cells):%n",
                type,
                shipLength
        );

        while (true) {
            String[] coords;
            String input;

            // Get coords from user input & update board
            do {
                input = scanner.nextLine();
                coords = Main.getCoordinates(input, shipLength);
            } while(coords == null);
            boolean isUpdateBoard = Main.updateBoard(coords, board);

            if (isUpdateBoard) {
                shipAllCoordinates[shipNumber] = Arrays
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

    public static boolean updateBoard(String[] coords, String[][] board) {
        if (coords[0].equals(coords[2])) { // If the ship is placed horizontally
            int length = Math.abs(Integer.parseInt(coords[1]) - Integer.parseInt(coords[3])) + 1;
            int startIndex = Math.min(Arrays.asList(Main.COLS).indexOf(coords[1]) , Arrays.asList(Main.COLS).indexOf(coords[3]));
            for (int i = startIndex; i < length + startIndex; i++) {
                // 4th Check: Ship Neighbors
                boolean existWrong = false;
                if (Arrays.asList(Main.ROWS).indexOf(coords[0]) == 0) { // If coords[0] = A
                    if (
                            board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == (length + startIndex - 1) && board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i + 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (Arrays.asList(Main.ROWS).indexOf(coords[0]) == 9) { // If coords[0] = J
                    if (
                            board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || (i == startIndex && board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i - 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (startIndex == 0) { // If coords[1] = 1 || coords[3] = 1
                    if (
                            board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == (length + startIndex - 1) && board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i + 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (length + startIndex - 1 == 9) { // If coords[1] = 10 || coords[3] = 10
                    if (
                            board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == startIndex && board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i - 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else {
                    if (
                            board[Arrays.asList(Main.ROWS).indexOf(coords[0]) - 1][i].equals("O")
                                    || board[Arrays.asList(Main.ROWS).indexOf(coords[0]) + 1][i].equals("O")
                                    || (i == startIndex && board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i - 1].equals("O"))
                                    || (i == (length + startIndex - 1) && board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i + 1].equals("O"))
                    ) {
                        existWrong = true;
                    }
                }
                if (existWrong) {
                    System.out.println("Error! Wrong ship placement! Try again:");
                    return false;
                }

                board[Arrays.asList(Main.ROWS).indexOf(coords[0])][i] = "O";
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
                            board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == (length + startIndex - 1) && board[i + 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (Arrays.asList(Main.COLS).indexOf(coords[1]) == 9) { // If coords[1] = 10
                    if (
                            board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || (i == startIndex && board[i - 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (startIndex == 0) { // If coords[0] = A || coords[2] = A
                    if (
                            board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == (length + startIndex - 1) && board[i + 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else if (length + startIndex - 1 == 9) { // If coords[0] = J || coords[2] = J
                    if (
                            board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == startIndex && board[i - 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                } else {
                    if (
                            board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) - 1].equals("O")
                                    || board[i][Arrays.asList(Main.COLS).indexOf(coords[1]) + 1].equals("O")
                                    || (i == startIndex && board[i - 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                                    || (i == (length + startIndex - 1) && board[i + 1][Arrays.asList(Main.COLS).indexOf(coords[1])].equals("O"))
                    ) {
                        existWrong = true;
                    }
                }
                if (existWrong) {
                    System.out.println("Error! Wrong ship placement! Try again:");
                    return false;
                }

                board[i][Arrays.asList(Main.COLS).indexOf(coords[1])] = "O";
            }
        }

        return true;
    }

    public static void takeAshot() {
        if (Main.currentPlayer.equals("Player 1")) {
            Main.printBoard(Main.fogBoard1);
            System.out.println("---------------------");
            Main.printBoard(Main.board1);
            System.out.println("Player 1, it's your turn:");
        } else {
            Main.printBoard(Main.fogBoard2);
            System.out.println("---------------------");
            Main.printBoard(Main.board2);
            System.out.println("Player 2, it's your turn:");
        }

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

        if (Main.currentPlayer.equals("Player 1")) {
            String boardValue = Main.board2[row][col];
            if (boardValue.equals("~")) {
                Main.board2[row][col] = "M";
                Main.fogBoard1[row][col] = "M";
                System.out.println("You missed!");
            } else if (boardValue.equals("O")) {
                Main.board2[row][col] = "X";
                Main.fogBoard1[row][col] = "X";
                Main.counterShots1++;
                boolean shipSunk = Main.checkIfShotHitShip(Main.shipAllCoordinates2, Main.board2, row, col);
                String message = shipSunk ? "You sank a ship!" : "You hit a ship!";
                message = (Main.counterShots1 < 17) ? message : "You sank the last ship. You won. Congratulations!";
                System.out.println(message);
            } else if (boardValue.equals("M")) {
                System.out.println("You missed!");
            } else {
                System.out.println("You hit a ship!");
            }

            // Change Player
            Main.currentPlayer = "Player 2";
            System.out.println("Press Enter and pass the move to another player");
            Main.scanner.nextLine();
        } else {
            String boardValue = Main.board1[row][col];
            if (boardValue.equals("~")) {
                Main.board1[row][col] = "M";
                Main.fogBoard2[row][col] = "M";
                System.out.println("You missed!");
            } else if (boardValue.equals("O")) {
                Main.board1[row][col] = "X";
                Main.fogBoard2[row][col] = "X";
                Main.counterShots2++;
                boolean shipSunk = Main.checkIfShotHitShip(Main.shipAllCoordinates1, Main.board1, row, col);
                String message = shipSunk ? "You sank a ship!" : "You hit a ship!";
                message = (Main.counterShots2 < 17) ? message : "You sank the last ship. You won. Congratulations!";
                System.out.println(message);
            } else if (boardValue.equals("M")) {
                System.out.println("You missed!");
            } else {
                System.out.println("You hit a ship!");
            }

            // Change Player
            Main.currentPlayer = "Player 1";
            System.out.println("Press Enter and pass the move to another player");
            Main.scanner.nextLine();
        }

    }

    public static boolean checkIfShotHitShip(String[][] shipAllCoordinates, String[][] board, int row, int col) {
        String hitCoordinate = Main.ROWS[row] + Main.COLS[col];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (shipAllCoordinates[i][j].equals(hitCoordinate)) {
                    return Main.checkIfShipIsSunken(shipAllCoordinates, board, i);
                }
            }
        }
        return false;
    }

    public static boolean checkIfShipIsSunken(String[][] shipAllCoordinates, String[][] board, int shipIndex) {
        String startRow = shipAllCoordinates[shipIndex][0].substring(0, 1);
        String startCol = shipAllCoordinates[shipIndex][0].substring(1);
        String endRow = shipAllCoordinates[shipIndex][1].substring(0, 1);
        String endCol = shipAllCoordinates[shipIndex][1].substring(1);

        if (startRow.equals(endRow)) { // If the ship is placed horizontally
            int length = Integer.parseInt(endCol) - Integer.parseInt(startCol) + 1;
            int startIndex = Arrays.asList(Main.COLS).indexOf(startCol);
            for (int i = startIndex; i < length + startIndex; i++) {
                if (!board[Arrays.asList(Main.ROWS).indexOf(startRow)][i].equals("X")) {
                    return false;
                }
            }
        }

        if (startCol.equals(endCol)) { // If ship placed vertically
            int length = endRow.charAt(0) - startRow.charAt(0) + 1;
            int startIndex = Arrays.asList(Main.ROWS).indexOf(startRow);
            for (int i = startIndex; i < length + startIndex; i++) {
                if (!board[i][Arrays.asList(Main.COLS).indexOf(startCol)].equals("X")) {
                    return false;
                }
            }
        }

        return true;
    }
}

