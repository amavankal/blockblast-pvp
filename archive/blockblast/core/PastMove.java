/**
 * PastMove.java holds the previous states of a player's BlockGrid
 * 
 * @author Amya Prophet, Asha Mavankal
 */
package blockblast.core;

import java.awt.Color;

public class PastMove
{
    //previous grid state
    private Color[][] pastGrid;
    
    //grid's previous score
    private int pastScore;
    
    /**
     * Creates a PastMove from a given grid and score
     * @param grid a previous grid state
     * @param score a previous score
     */
    public PastMove(Color[][] grid, int score)
    {
        pastGrid = grid;
        pastScore = score;
    }

    /**
     * Retrieves the grid
     * @return previous grid state
     */
    public Color[][] getGrid() 
    {    
        return pastGrid;    
    }

    /**
     * Retrieves the score
     * @return previous score
     */
    public int getScore() 
    { 
        return pastScore;   
    }
}