package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.List;

/**
 * Node type for the Monte Carlo search tree.
 */
public class MCNode<CheckersData> {
    private CheckersData state;
    private CheckersMove move; // The move that led to this state
    private MCNode<CheckersData> parent; // Reference to the parent node
    private List<MCNode<CheckersData>> children; // List of child nodes

    private int playouts; // Number of times this node has been visited
    private int wins; // Number of wins observed through this node

    // Constructor
    public MCNode(CheckersData state, CheckersMove move, MCNode<CheckersData> parent) {
        this.state = state;
        this.move = move;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.playouts = 0;
        this.wins = 0;
    }

    // Getters
    public CheckersData getState() {
        return state;
    }

    public CheckersMove getMove() {
        return move;
    }

    public MCNode<CheckersData> getParent() {
        return parent;
    }

    public List<MCNode<CheckersData>> getChildren() {
        return children;
    }

    public int getNumOfPlayouts() {
        return playouts;
    }

    public int getNumOfWins() {
        return wins;
    }

    // Increment visit count
    public void incrementVisitCount() {
        playouts++;
    }

    // Increment win count
    public void incrementWinCount() {
        wins++;
    }

    // Calculate UCT (Upper Confidence Bound for Trees) score for this node
    public double getUCTScore() {
        if (playouts == 0) {
            return Double.POSITIVE_INFINITY; // Prioritize unvisited nodes
        }
        double exploitationValue = (double) wins / playouts;
        double explorationValue = Math.sqrt(Math.log(parent.playouts) / playouts);
        return exploitationValue + Math.sqrt(2) * explorationValue;
    }

    // Add a child node
    public MCNode<CheckersData> addChild(CheckersData childState, CheckersMove childMove) {
        MCNode<CheckersData> childNode = new MCNode<>(childState, childMove, this);
        children.add(childNode);
        return childNode;
    }

    // Check if the node is fully expanded
    public boolean isFullyExpanded(List<CheckersMove> possibleMoves) {
        if (children.size() >= possibleMoves.size()) {
            return true;
        }
        for (CheckersMove move : possibleMoves) {
            boolean found = false;
            for (MCNode<CheckersData> child : children) {
                if (child.getMove().equals(move)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    // Select child with max UCT score
    public MCNode<CheckersData> getChildWithMaxUCT() {
        MCNode<CheckersData> bestChild = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (MCNode<CheckersData> child : children) {
            double uctScore = child.getUCTScore();
            if (uctScore > bestScore) {
                bestScore = uctScore;
                bestChild = child;
            }
        }
        return bestChild;
    }
}
