package edu.iastate.cs472.proj2;

public class Main {
    public static void main(String[] args) {
        CheckersData game = new CheckersData();
        game.setUpGame(); // Initialize the board


        System.out.println(game.toString());

        int player = CheckersData.BLACK;
        int row = 2;
        int col = 0;

        String color = "";
        if(player == 1)
        {
            color = "RED";
        }
        else if(player == 3)
        {
            color = "BLACK";
        }

        CheckersMove[] jumps = game.getLegalMoves(player);

        if (jumps != null) {
            System.out.printf("\nLegal Jumps for %s from (%d,%d):\n", color, row, col);
            for (CheckersMove move : jumps) {
                System.out.print("Move Sequence: ");
                for (int i = 0; i < move.rows.size(); i++) {
                    System.out.print("(" + (8 - move.rows.get(i)) + "," + (char)('a' + move.cols.get(i)) + ") ");
                }
                System.out.println();
            }
        } else {
            System.out.printf("\nNo legal Jumps for %s from (%d,%d) available:\n", color, row, col);
        }
    }
}
