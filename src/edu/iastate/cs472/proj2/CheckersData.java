package edu.iastate.cs472.proj2;

import java.util.ArrayList;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData implements Cloneable
{
  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.
    int drawcount = 40;

    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append("0");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
        // Empties board
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                board[i][j] = 0;
            }
        }
        // Places black mens
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(i % 2 == j % 2)
                {
                    board[i][j] = 3;
                }
            }
        }
        // Place red mens
        for(int i = 5; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(i % 2 == j % 2)
                {
                    board[i][j] = 1;
                }
            }
        }
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move) {
        int l = move.rows.size();
        for(int i = 0; i < l-1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i+1), move.cols.get(i+1));
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
    	// Update the board for the given move. You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captured piece
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king

        int man = board[fromRow][fromCol];

        if(fromRow - toRow == 2 || fromRow - toRow == -2) // if move is jump
        {
            int dead_man_row = Math.min(fromRow, toRow) + 1;
            int dead_man_col = Math.min(fromCol, toCol) + 1;
            board[toRow][toCol] = board[fromRow][fromCol];
            board[dead_man_row][dead_man_col] = 0;
            board[fromRow][fromCol] = 0;
        }
        else
        {
            board[toRow][toCol] = board[fromRow][fromCol];
            board[fromRow][fromCol] = 0;
        }

        // if on opponent's kings row, crown it
        if(man == RED && toRow == 0)
        {
            board[toRow][toCol] = RED_KING;
        }
        if(man == BLACK && toRow == 7)
        {
            board[toRow][toCol] = BLACK_KING;
        }
    }

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
        ArrayList<CheckersMove> jumpMoves = new ArrayList<>();
        ArrayList<CheckersMove> singleMoves = new ArrayList<>();

        // return null if invalid player value passed in
        if(player != RED && player != BLACK)
        {
            return null;
        }

        // loop through board and find corresponding man to call find legal jumps
        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 8; col++)
            {
                if(board[row][col] == player || board[row][col] == player + 1)
                {
                    CheckersMove[] jumps = getLegalJumpsFrom(board[row][col], row, col);
                    if(jumps != null)
                    {
                        for(CheckersMove jump : jumps)
                        {
                            jumpMoves.add(jump);
                        }
                    }
                }
            }
        }
        // if jump moves found, return jump moves
        if(!jumpMoves.isEmpty())
        {
            return jumpMoves.toArray(new CheckersMove[0]);
        }
        // if no jump moves, get single moves
        else
        {
            for(int row = 0; row < 8; row++)
            {
                for(int col = 0; col < 8; col++)
                {
                    if(board[row][col] == player || board[row][col] == player + 1)
                    {
                        CheckersMove[] singles = getLegalSingleMoves(board[row][col], row, col);
                        if(singles != null)
                        {
                            for(CheckersMove single : singles)
                            {
                                singleMoves.add(single);
                            }
                        }
                    }
                }
            }
        }
        // if single moves found, return single moves
        if(!singleMoves.isEmpty())
        {
            return singleMoves.toArray(new CheckersMove[0]);
        }
        // if no moves found, return null
        return null;
    }


    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Note that each CheckerMove may contain multiple jumps.
     * Each move returned in the array represents a sequence of jumps
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        ArrayList<CheckersMove> allJumps = new ArrayList<>();
        CheckersMove currentMove = new CheckersMove();
        currentMove.addMove(row, col);
        findJumps(player, row, col, currentMove, allJumps);

        ArrayList<CheckersMove> validJumps = new ArrayList<>();
        for (CheckersMove move : allJumps) {
            if (move.rows.size() > 1) { // Ensure at least one jump occurred
                CheckersMove actualMove = new CheckersMove();
                // Add all positions from the move sequence
                for (int i = 0; i < move.rows.size(); i++) {
                    actualMove.addMove(move.rows.get(i), move.cols.get(i));
                }
                validJumps.add(actualMove);
            }
        }

        if (validJumps.isEmpty()) {
            return null; // No jumps available
        }

        return validJumps.toArray(new CheckersMove[0]);
    }

    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Note that each CheckerMove may contain multiple jumps.
     * Each move returned in the array represents a sequence of jumps
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param fromRow    row index of the start square.
     * @param fromCol    col index of the start square.
     */
    CheckersMove[] getLegalSingleMoves(int player, int fromRow, int fromCol) {
        ArrayList<CheckersMove> singleMoves = new ArrayList<>();
        int[][] moveDirections;
        if(player == RED_KING || player == BLACK_KING)
        {
            moveDirections = new int[][] {{-1,-1}, {-1, 1}, {1, -1}, {1, 1}};
        }
        else if(player == RED)
        {
            moveDirections = new int[][] {{-1,-1}, {-1, 1}};
        }
        else
        {
            moveDirections = new int[][] {{1, -1}, {1, 1}};
        }

        for(int[] dir : moveDirections)
        {
            int toRow = fromRow + dir[0];
            int toCol = fromCol + dir[1];
            if(isWithinBounds(toRow, toCol) && board[toRow][toCol] == EMPTY)
            {
                CheckersMove currentMove = new CheckersMove(fromRow, fromCol, toRow, toCol);
                singleMoves.add(currentMove);
            }
        }
        if (singleMoves.isEmpty()) {
            return null; // No jumps available
        }
        return singleMoves.toArray(new CheckersMove[0]);
    }

    /**
     * Recursively finds all possible jump sequences from the current position.
     *
     * @param player     The player making the jump.
     * @param fromRow        The current row position of the piece.
     * @param fromCol        The current column position of the piece.
     * @param currentMove The current sequence of moves being built.
     * @param allJumps    The list collecting all complete jump sequences.
     */
    private void findJumps(int player, int fromRow, int fromCol, CheckersMove currentMove, ArrayList<CheckersMove> allJumps)
    {
        boolean canJump = false;
        int man = board[fromRow][fromCol];

        int[][] jumpDirections;
        if(player == RED_KING || player == BLACK_KING)
        {
            jumpDirections = new int[][] {{-1,-1}, {-1, 1}, {1, -1}, {1, 1}};
        }
        else if(player == RED)
        {
            jumpDirections = new int[][] {{-1,-1}, {-1, 1}};
        }
        else
        {
            jumpDirections = new int[][] {{1, -1}, {1, 1}};
        }

        for(int[] dir : jumpDirections)
        {
            int midRow = fromRow + dir[0];
            int midCol = fromCol + dir[1];
            int toRow = fromRow + (2 * dir[0]);
            int toCol = fromCol + (2 * dir[1]);

            if(isWithinBounds(toRow, toCol))
            {
                int midMan = board[midRow][midCol];
                if(isOppPiece(player, midMan) && board[toRow][toCol] == EMPTY)
                {
                    canJump = true;
                    makeMove(fromRow, fromCol, toRow, toCol);
                    currentMove.addMove(toRow, toCol);
                    findJumps(player, toRow, toCol, currentMove.clone(), allJumps);
                    board[toRow][toCol] = EMPTY;
                    board[fromRow][fromCol] = man;
                    board[midRow][midCol] = midMan;

                    currentMove.rows.removeLast();
                    currentMove.cols.removeLast();
                }
            }
        }

        // If no further jumps are possible and at least one jump has been made, add the move sequence
        if (!canJump && currentMove.rows.size() > 1)
        {
            allJumps.add(currentMove.clone());
        }

    }

    private boolean isOppPiece(int player, int piece)
    {
        if (player == RED || player == RED_KING)
        {
            return piece == BLACK || piece == BLACK_KING;
        }
        else if (player == BLACK || player == BLACK_KING)
        {
            return piece == RED || piece == RED_KING;
        }
        return false;
    }

    private boolean isWithinBounds(int row, int col)
    {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public CheckersData clone() {
        try {
            // Create a shallow copy of the CheckersData instance
            CheckersData cloned = (CheckersData) super.clone();
            // Deep copy the board array
            cloned.board = new int[board.length][];
            for (int i = 0; i < board.length; i++) {
                cloned.board[i] = board[i].clone(); // Clone each row
            }
            // Return the cloned instance
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed", e); // Should never happen since we implement Cloneable
        }
    }

}
