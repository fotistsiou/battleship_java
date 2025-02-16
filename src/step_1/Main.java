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
 *    placed. Therefore output this information to a player. Here is an example of possible formatting: Length: 3 and
 *    Parts: F2 F3 F4
 * 4. If an error occurs in the input coordinates (coordinates are not on the same line or out of bounds), your program
 *    should report it. The message should contain the word Error.
 */

public class Main {
}
