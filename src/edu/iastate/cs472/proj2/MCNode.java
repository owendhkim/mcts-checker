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
    private ArrayList<MCNode<CheckersData>> children; // List of child nodes

    private double playouts; // Number of times this node has been visited
    private double wins; // Number of wins observed through this node

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

    public ArrayList<MCNode<CheckersData>> getChildren() {
        return children;
    }

    public double getNumOfPlayouts() {
        return playouts;
    }

    public double getNumOfWins() {
        return wins;
    }

    // Increment visit count
    public void incrementPlayoutCount() {
        playouts++;
    }

    // Increment win count
    public void incrementWinCount(double value) {
        wins+=value;
    }

    public boolean isLeaf() {
        return children.isEmpty();
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
    public MCNode<CheckersData> addChild(MCNode<CheckersData> child) {
        children.add(child);
        return child;
    }
}
