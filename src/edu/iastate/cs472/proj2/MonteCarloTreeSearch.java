package edu.iastate.cs472.proj2;

/**
 * 
 * @author 
 *
 */

import java.util.ArrayList;
import java.util.Random;


/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch
{
    private static final int NUM_ITERATIONS = 1000;
    Random rand = new Random();
    /**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * Each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is an 2D array of the following integers defined below:
    	// 
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        System.out.println(board);
        System.out.println();
        if (legalMoves == null || legalMoves.length == 0) {
            return null; // No legal moves available
        }
        MCNode<CheckersData> root = new MCNode<>(board.clone(), null, null);
        for (int i = 0; i < 5; i++) {
            // Step 1: Selection - Start from root and select a promising node
            MCNode<CheckersData> leaf = select(root);

            // Step 2: Expansion - Add a child node for an untried move
            MCNode<CheckersData> child = expand(leaf);

            // Step 3: Simulation - Perform a random playout from the expanded node
            int simulationResult = simulate(child);

            // Step 4: Backpropagation - Update statistics along the path
            backpropagate(child, simulationResult);
        }

        // After all iterations, return the best move based on visit count or win rate
        return bestMove(root);
    }

    private MCNode<CheckersData> select(MCNode<CheckersData> node)
    {
        if(node.isLeaf())
        {
            return node;
        }
        double bestUCT = 0.0;
        MCNode<CheckersData> bestChild = null;
        for(MCNode<CheckersData> child : node.getChildren())
        {
            double childUTC = child.getUCTScore();
            if(childUTC > bestUCT)
            {
                bestUCT = child.getUCTScore();
                bestChild = child;
            }
        }
        return select(bestChild);
    }

    private MCNode<CheckersData> expand(MCNode<CheckersData> leaf)
    {
        // Get all possible legal moves from this node’s state
        setCheckersData(leaf.getState());
        CheckersMove[] possibleMoves = legalMoves();
        // If no legal moves, this is a terminal state
        if (possibleMoves == null || possibleMoves.length == 0)
        {
            return leaf;
        }
        for(CheckersMove move : possibleMoves)
        {
            CheckersData childBoard = board.clone();
            childBoard.makeMove(move);
            MCNode<CheckersData> child = new MCNode<>(childBoard, move, leaf);
            leaf.addChild(child);
        }
        ArrayList<MCNode<CheckersData>> childrenList = leaf.getChildren();
        return childrenList.get(rand.nextInt(childrenList.size()));
    }

    // playout from child, returns winner's int player value
    private int simulate(MCNode<CheckersData> child)
    {
        int currentPlayer = CheckersData.BLACK;
        CheckersData childBoard = board.clone();
        while(true)
        {
            CheckersMove[] possibleMoves = childBoard.getLegalMoves(currentPlayer);
            if (possibleMoves == null || possibleMoves.length == 0)
            {
                if (currentPlayer == CheckersData.BLACK)
                {
                    return CheckersData.RED;
                }
                else
                {
                    return CheckersData.BLACK;
                }
            }
            CheckersMove randomMove = possibleMoves[rand.nextInt(possibleMoves.length)];
            childBoard.makeMove(randomMove);
            if (currentPlayer == CheckersData.BLACK)
            {
                currentPlayer = CheckersData.RED;
            }
            else
            {
                currentPlayer = CheckersData.BLACK;
            }
        }
    }

    private void backpropagate(MCNode<CheckersData> child, int winner)
    {
        MCNode<CheckersData> node = child;
        while (node != null)
        {
            node.incrementPlayoutCount();
            if(winner == CheckersData.BLACK)
            {
                node.incrementWinCount();;
            }
            node = node.getParent();
        }
    }

    private CheckersMove bestMove(MCNode<CheckersData> root)
    {
        MCNode<CheckersData> bestChild = null;
        double mostPlayouts = 0.0;

        for(MCNode<CheckersData> child : root.getChildren())
        {
            double childPlayouts = child.getNumOfPlayouts();
            if(childPlayouts > mostPlayouts)
            {
                mostPlayouts = childPlayouts;
                bestChild = child;
            }
        }
        return bestChild.getMove();
    }




    // TODO
    // 
    // Implement your helper methods here. They include at least the methods for selection,  
    // expansion, simulation, and back-propagation. 
    // 
    // For representation of the search tree, you are suggested (but limited) to use a 
    // child-sibling tree already implemented in the two classes CSTree and CSNode (which  
    // you may feel free to modify).  If you decide not to use the child-sibling tree, simply 
    // remove these two classes. 
    // 

}
