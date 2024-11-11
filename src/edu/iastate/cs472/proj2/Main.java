package edu.iastate.cs472.proj2;

public class Main {
    public static void main(String[] args) {
        CheckersData game = new CheckersData();
        game.setUpGame(); // Initialize the board


        System.out.println(game.toString());

        // Example: Get all jump moves for BLACK from position (2, 1)
        int player = CheckersData.BLACK;
        int row = 2;
        int col = 0;

        CheckersMove[] jumps = game.getLegalJumpsFrom(player, row, col);

        if (jumps != null) {
            System.out.println("\nLegal Jumps for BLACK from (2,0):");
            for (CheckersMove move : jumps) {
                System.out.print("Move Sequence: ");
                for (int i = 0; i < move.rows.size(); i++) {
                    System.out.print("(" + (8 - move.rows.get(i)) + "," + (char)('a' + move.cols.get(i)) + ") ");
                }
                System.out.println();
            }
        } else {
            System.out.println("\nNo legal jumps available for BLACK from (2,1).");
        }
    }
}
