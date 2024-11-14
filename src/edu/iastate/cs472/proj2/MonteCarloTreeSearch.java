package edu.iastate.cs472.proj2;

/**
 * 
 * @author 
 *
 */

/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch
{
    private static final int NUM_ITERATIONS = 1000;
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
        MCNode<CheckersData> rootNode = new MCNode<>(board.clone(), null, null);
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            // Step 1: Selection - Start from root and select a promising node
            MCNode<CheckersData> selectedNode = select(rootNode);

            // Step 2: Expansion - Add a child node for an untried move
            MCNode<CheckersData> expandedNode = expand(selectedNode);

            // Step 3: Simulation - Perform a random playout from the expanded node
            boolean simulationResult = simulate(expandedNode);

            // Step 4: Backpropagation - Update statistics along the path
            backpropagate(expandedNode, simulationResult);
        }

        // After all iterations, return the best move based on visit count or win rate
        return bestMove(rootNode);
    }

    private MCNode<CheckersData> select(MCNode<CheckersData> node)
    {
        while (!node.getChildren().isEmpty() && node.isFullyExpanded(legalMoves(node.getState())))
        {
            node = node.getChildWithMaxUCT();
        }
        return node; // Return a node that is either a leaf or not fully expanded
    }

    private MCNode<CheckersData> expand(MCNode<CheckersData> node) {
        // Get all possible legal moves from this node’s state
        CheckersMove[] possibleMoves = legalMoves(node.getState());

        for (CheckersMove move : possibleMoves) {
            // Check if the move has already been expanded
            boolean found = false;
            for (MCNode<CheckersData> child : node.getChildren()) {
                if (child.getMove().equals(move)) {
                    found = true;
                    break;
                }
            }

            // Expand a new child if the move hasn't been tried
            if (!found) {
                CheckersData newState = node.getState().clone();
                newState.makeMove(move); // Apply the move to get the new state
                return node.addChild(newState, move);
            }
        }

        return node; // If fully expanded, return the node itself
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
