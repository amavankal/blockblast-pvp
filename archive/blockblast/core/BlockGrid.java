/** 
 * The BlockGrid class represents the game grid for a player
 * 
 * @author Amya Prophet, Asha Mavankal
 */
package blockblast.core;

import java.util.Deque;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.awt.Color;

public class BlockGrid {
    
	// constants
    public static final int MAX_ROWS_AND_COLS = 8; //grid dimensions
    
    // block generation constants
    private static final double LIKELIHOOD_EASY = .60;
    private static final BlockType[][] EASY_BLOCKS = {
    		{BlockType.TWO_BLOCK_I1, BlockType.TWO_BLOCK_I2},			// 0
    		{BlockType.THREE_BLOCK_1_I1, BlockType.THREE_BLOCK_1_I2, 	// 1
    			BlockType.THREE_BLOCK_1_I3, BlockType.THREE_BLOCK_1_I4},
    		{BlockType.THREE_BLOCK_2_I1, BlockType.THREE_BLOCK_2_I2},	// 2
    		{BlockType.FOUR_BLOCK_1_I1, BlockType.FOUR_BLOCK_1_I2, 		// 3
    			BlockType.FOUR_BLOCK_1_I3, BlockType.FOUR_BLOCK_1_I4}, 
    		{BlockType.FOUR_BLOCK_2_I1, BlockType.FOUR_BLOCK_2_I2,		// 4
    				BlockType.FOUR_BLOCK_2_I3, BlockType.FOUR_BLOCK_2_I4,
    				BlockType.FOUR_BLOCK_2_I5, BlockType.FOUR_BLOCK_2_I6,
    				BlockType.FOUR_BLOCK_2_I7, BlockType.FOUR_BLOCK_2_I8},
    		{BlockType.FOUR_BLOCK_4_I1, BlockType.FOUR_BLOCK_4_I2},		// 5
    		{BlockType.FOUR_BLOCK_5}, 									// 6
    		{BlockType.FIVE_BLOCK_2_I1, BlockType.FIVE_BLOCK_2_I2},		// 7
    		{BlockType.SIX_BLOCK_I1, BlockType.SIX_BLOCK_I2},			// 8
    		{BlockType.NINE_BLOCK_1}
    };
    private static final double[] EASY_RATINGS = {9, 7, 8, 6, 5, 6, 10, 8, 10, 5, 9};
    private static final int EASY_RATING_TOTAL = 74;
    
    private static final BlockType[][] HARD_BLOCKS = {
    		{BlockType.THREE_BLOCK_3_I1, BlockType.THREE_BLOCK_3_I2},	// 0
    		{BlockType.FOUR_BLOCK_1_I1, BlockType.FOUR_BLOCK_1_I2, 		// 1
    			BlockType.FOUR_BLOCK_1_I3, BlockType.FOUR_BLOCK_1_I4},
    		{BlockType.FOUR_BLOCK_2_I1, BlockType.FOUR_BLOCK_2_I2,		// 2
    				BlockType.FOUR_BLOCK_2_I3, BlockType.FOUR_BLOCK_2_I4,
    				BlockType.FOUR_BLOCK_2_I5, BlockType.FOUR_BLOCK_2_I6,
    				BlockType.FOUR_BLOCK_2_I7, BlockType.FOUR_BLOCK_2_I8},
    		{BlockType.FOUR_BLOCK_3_I1, BlockType.FOUR_BLOCK_3_I2,		// 3
    				BlockType.FOUR_BLOCK_3_I3, BlockType.FOUR_BLOCK_3_I4},
    		{BlockType.FIVE_BLOCK_1_I1, BlockType.FIVE_BLOCK_1_I2,		// 4
    				BlockType.FIVE_BLOCK_1_I3, BlockType.FIVE_BLOCK_1_I4},
    		{BlockType.FIVE_BLOCK_2_I1, BlockType.FIVE_BLOCK_2_I2},		// 5
    		{BlockType.NINE_BLOCK_1}									// 6
    };
    private static final double[] HARD_RATINGS = {2, 5, 6, 7, 4, 4, 3};
    private static final int HARD_RATING_TOTAL = 31;
    
    // instance variables
    private Color[][] grid; // state of grid
    private Deque<PastMove> prevMoves; //holds previous moves
    private int score; //game score
    private HashSet<Block> blockBank; //current blocks availabe to use

    /**
     * Creates an empty BlockGrid
     */
    public BlockGrid()
    {
        grid = new Color[MAX_ROWS_AND_COLS][MAX_ROWS_AND_COLS];
        prevMoves = new LinkedList<>();
        score = 0;
        blockBank = generateBlocks();

    }

    /**	Adds a block to the grid
     *	@param b the block to add
     *	@param r desired row to add block
     *	@param c desired column to add block
     *	@return true if the block was added
     */
    public boolean addToGrid(Block b, int r, int c)
    {
        boolean[][] block = b.getBlock();
        
        // check if necessary blocks are empty
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[0].length; col++)
                if ((row > grid.length - 1 || col > grid[r].length - 1) 
                    || (block[row - r][col - c] && (grid[row][col] != null)))
                    return false;
        
        // fill spaces
        Color color = b.getColor();   
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[row - r].length; col++)
                if (block[row - r][col - c])
                    grid[row][col] = color;
                
        return true;
    }
    
    /**	Regenerate the block bank
     *	@return
     */
    public Block[] regenerateBlockBank()
    {
    	blockBank = generateBlocks();
    	return getBlockBank();
    }
    
    /**	Retrieve a static version of the block bank to display
     *	@return
     */
    public Block[] getBlockBank()
    {
    	Block[] staticBank = new Block[3];
    	int i = 0;
    	
    	Iterator iter = blockBank.iterator();
    	while (iter.hasNext())
    	{
    		staticBank[i] = (Block) iter.next();
    		i++;
    	}
    	
    	return staticBank;
    }
    
    /**	Returns the Color grid
     */
    public Color[][] getGrid()
    {
    	return grid;
    }
    
    /**
     * Clears filled lines on the grid and returns
     * 		how many were cleared.
     * @return the number of lines cleared
     */
    public int clearLines()
    {
        // add grid state to previous moves
        prevMoves.push(new PastMove(grid, score));

        // 1: identify lines to clear
        boolean[] horizClear = new boolean[8];
        boolean[] vertClear = new boolean[8];
        int numLinesCleared = 0;
        
        // find which horizontal lines are full
        for (int r = 0; r < grid.length; r++)
        {
            horizClear[r] = true;
            for (int c = 0; c < grid[r].length; c++)
                horizClear[r] &= (grid[r][c] != null);
            numLinesCleared = horizClear[r] ? numLinesCleared + 1
                : numLinesCleared;
        }
        
        // find which vertical lines are full
        for (int c = 0; c < grid.length; c++)
        {
            vertClear[c] = true;
            for (int r = 0; r < grid.length; r++)
                vertClear[c] &= (grid[r][c] != null);
            numLinesCleared = vertClear[c] ? numLinesCleared + 1
                : numLinesCleared;
        }
        
        // 2: clear the given lines
        for (int i = 0; i < horizClear.length; i++)
        {
            // clear horizontals
            if (horizClear[i])
                grid[i] = new Color[8];
            
            // clear verticals
            if (vertClear[i])
                for (int r = 0; r < grid.length; r++)
                    grid[r][i] = null;
        }
        
        return numLinesCleared;
    }

    //powerup methods

    /**	Reverts this grid state by x moves
     *	@param x the number of states in the past to revert
     *	@return true if the grid was reverted
     */
    public boolean undo(int x)
    {
        // retrieve past state
    	PastMove pastState = null;
    	for (int i = 0; i < x; i++)
        	pastState = prevMoves.poll();
    	
    	// update (or de-update) grid to past state
    	grid = pastState.getGrid();
    	score = pastState.getScore();
    	
    	return pastState != null;
    }

    /**	Retrieve how many previous grid states are stored on this grid
     *	@return how many previous grid states are stored
     */
    public int getPrevMovesSize()
    {
    	return prevMoves.size();
    }
    
    /** Clears a 3x3 section on this grid, starting from r, c
     *	@return true if the section was destroyed
     */
    public boolean destroy(int r, int c)
    {
        Block toRemove = new Block(BlockType.NINE_BLOCK_1.getBlock());
    	remove(toRemove, r, c);
    	
    	return true;
    }
    
    /**	TENTATIVE method--meh imlementation, just intended to generate
     * 		anything at all
     * 
     *	@return
     */
    private HashSet<Block> generateBlocks()
    {
        //repeat until 3 eligible blocks chosen
            //pick randomly from easy or hard list
                //pick one block randomly from list
                //not eligible if no space available
                //add to hashset
    	HashSet<Block> newBlocks = new HashSet<Block>();
    	
    	while (newBlocks.size() < 3)
    	{
	    	// 1: generate a random block
	    	Block b = randomGenBlock();
	    	if (isSpaceInGrid(b))
	    		newBlocks.add(b);
    	}
    	
        return newBlocks;
    }
    
    /**	Helper method: generate a random block
     *	@return randomly generated block
     */
    private static Block randomGenBlock()
    {
    	if (Math.random() < LIKELIHOOD_EASY) // easy list
    	{
    		double rand = Math.random();
    		double summedPossibs = 0;
    		for (int i = 0; i < EASY_RATINGS.length; i++)
    		{
    			summedPossibs += EASY_RATINGS[i] / EASY_RATING_TOTAL;
    			if (rand < summedPossibs)
    			{
    				BlockType[] iterations = EASY_BLOCKS[i];
    				BlockType choice = iterations[(int) (Math.random() * iterations.length)];
    				return new Block(choice.getBlock());
    			}
    		}
    		return null;
    	}
    	else // hard list
    	{
    		double rand = Math.random();
    		double summedPossibs = 0;
    		for (int i = 0; i < HARD_RATINGS.length; i++)
    		{
    			summedPossibs += HARD_RATINGS[i] / HARD_RATING_TOTAL;
    			if (rand < summedPossibs)
    			{
    				BlockType[] iterations = HARD_BLOCKS[i];
    				BlockType choice = iterations[(int) (Math.random() * iterations.length)];
    				return new Block(choice.getBlock());
    			}
    		}
    		return null;
    	}
    }

    /**
     * Checks the grid for space to place the given block
     * @param b the block to check space for
     * @return if space is available
     */
    public boolean isSpaceInGrid(Block b)
    {
        //checks if adding is ever possible in the grid
        for(int r = 0; r < grid.length; r++)
            for(int c = 0; c < grid[r].length; c++)
                if(canAdd(b, r, c))
                    return true;
        return false;
    }
    
    /**	Returns whether a block can  be added to a space
     *	@param b the block to add
     *	@param r desired row to add block
     *	@param c desired column to add block
     *	@return true if the block was added
     */
    public boolean canAdd(Block b, int r, int c)
    {
        boolean[][] block = b.getBlock();
        
        // check if necessary blocks are empty
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[0].length; col++)
                if ((row > grid.length - 1 || col > grid[r].length - 1) 
                    || (block[row - r][col - c] && (grid[row][col] != null)))
                    return false;
       
        return true;
    }
    
    /**
     * Removes a block from the grid at a specific location
     * @param b the block shape to remove
     * @param r the desired row
     * @param c the desired col
     * @return true if the block was removed
     */
    public boolean remove(Block b, int r, int c)
    {
        boolean[][] block = b.getBlock();
        
        // check if necessary blocks are full
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[0].length; col++)
                if ((row > grid.length - 1 || col > grid.length - 1) 
                    || block[row - r][col - c] && grid[row][col] == null)
                    return false;
        
        // empty spaces
        //Color color = b.getColor();   
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[row - r].length; col++)
                if (block[row - r][col - c])
                    grid[row][col] = null;
                
        return true;
    }

    /**
    * Returns the BlockGrid as a string
    * @return the grid as a string
    */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("+----------------+\n");
        for (Color[] row : grid)
        {
            result.append("|");
            for (Color c : row)
                if (c == null)
                    result.append("..");
                else
                    result.append("[]");
            result.append("|\n");
        }
        result.append("+----------------+\n");
        
        return result.toString();
    }

    /**
     * Returns the BlockGrid as a String involving colors
     * @return the grid as a string with letters representing colors
     */
    public String toStringColor()
    {
        StringBuilder result = new StringBuilder();
        result.append("+-----------------+\n");
        for (Color[] row : grid)
        {
            result.append("| ");
            for (Color c : row)
                if (c == null)
                    result.append("~ ");
                else
                    result.append(getLetter(c));
            result.append("|\n");
        }
        result.append("+-----------------+\n");
        
        return result.toString();
    }

    /**
     * Returns a color as a string with its first initial
     * @param c the color to convert to string
     * @return a string of the color's first initial
     */
    private String getLetter(Color c)
    {
        if (c == Color.BLUE)
            return "B ";
        else if (c == Color.GREEN)
            return "G ";
        else if (c == Color.MAGENTA)
            return "M ";
        else if (c == Color.RED)
            return "R ";
        else if (c == Color.YELLOW)
            return "Y ";
        else
            return "X ";
    }

}