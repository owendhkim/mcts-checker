# Monte Carlo Tree Search Checkers AI

## Overview

This project implements a Checkers AI agent using Monte Carlo Tree Search (MCTS).
MonteCarloTreeSearch.java has makeMove() method that uses 4 helper methods,
- select() - Select leaf with best UCT score
- expand() - Expand children of leaf and pick random child
- simulate() - Perform a random playout from child
- backpropagate() - Update playouts/wins traversing up to root

Few things to note:
 1. UCT formula uses √2 as a exploration constant.
 2. This condition from the official rulebook was used to rule if the game is a draw, during playouts.
    (1.32.1	At any stage of the game, a player can demonstrate to the satisfaction of the referee that
    with their next move they would create the same position for the third time during the game.)
 3. MCTree class is not used, everything is handled with MCNode class.
