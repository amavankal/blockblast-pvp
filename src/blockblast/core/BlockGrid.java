/** 
 * The BlockGrid class represents the game grid for a player
 * 
 * 	 - TODO fix generate blocks method -- don't give impossible
 * 			results
 * 			(the point of the game is for it to continue)
 *  - TODO: consider undo functionality??
 * 
 * @author Asha Mavankal
 * 
 */
package blockblast.core;

import java.awt.Color;

public class BlockGrid {
    
	// constants
    public static final int MAX_ROWS_AND_COLS = 8; //grid dimensions
    public static final int BLOCK_BANK_SIZE = 3;
    
    public static final int BLOCK_ADD_PTS = 50;
    public static final int LINE_CLEAR_PTS = 400;
    public static final int CROSS_LINE_CLEAR_BONUS = 200;
    public static final int COMBO_BONUS = 50;
    public static final int BOARD_CLEAR_PTS = 200;
    
    // instance variables
    private Color[][] grid; // state of grid
    private int score; //game score
    private Block[] blockBank; //current blocks availabe to use

    /**
     * Creates an empty BlockGrid
     */
    public BlockGrid()
    {
        grid = new Color[MAX_ROWS_AND_COLS][MAX_ROWS_AND_COLS];
        score = 0;
        blockBank = new Block[BLOCK_BANK_SIZE];
        regenerateBlockBank();

    }

    /**	Adds a block to the grid
     *	@param b the block to add
     *	@param r desired row to add block
     *	@param c desired column to add block
     *	@return true if the block was added
     */
    private boolean addToGrid(Block b, int r, int c, boolean temp)
    {
    	if (b == null || r < 0 || r >= grid.length || c < 0 || c >= grid[0].length)
    		return false;
    	
    	boolean[][] block = b.getBlock();
    	
        // check if necessary blocks are empty
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[0].length; col++)
                if ((row > grid.length - 1 || col > grid[r].length - 1) 
                    || (block[row - r][col - c] && (grid[row][col] != null)))
                    return false;
        
        // choose color
        Color color = temp ? b.getTempColor() : b.getColor();   
        
        // fill spaces
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[row - r].length; col++)
                if (block[row - r][col - c])
                    grid[row][col] = color;
        
        // increase score if not temp
        if (!temp)
        	score += BLOCK_ADD_PTS;
        
        return true;
    }
    
    /**	
     *	@param b the block to add
     *	@param r desired row to add block
     *	@param c desired column to add block
     *	@return true if the block was added
     */
    public boolean addToGridFromBank(int blockI, int r, int c, boolean temp)
    {
        if (blockI < 0 || blockI >= blockBank.length)
        	return false;
    	
    	return addToGrid(blockBank[blockI], r, c, temp);
    }
    
    /**	
     *	@param b the block to add
     *	@param r desired row to add block
     *	@param c desired column to add block
     *	@return true if the block was added
     */
    public boolean addToGridFromBank(int blockI, int r, int c, boolean temp, int combo)
    {
        if (blockI < 0 || blockI >= blockBank.length)
        	return false;
    	
    	boolean added = addToGrid(blockBank[blockI], r, c, temp);
    	
    	if (added & !temp)
    		score += combo * COMBO_BONUS;
    	
    	return added;
    }
    
    /**	Regenerate the block bank
     *	@return
     */
    public Block[] regenerateBlockBank()
    {  	
    	//repeat until 3 eligible blocks chosen
        //pick randomly from easy or hard list
            //pick one block randomly from list
            //not eligible if no space available
            //add to hashset
    	int i = 0;
	
    	while (i  < 3)
    	{
    		// 1: generate a random block
    		Block b = new Block(BlockType.generateBlockType().getBlock());
    		if (isSpaceInGrid(b))
    		{
    			blockBank[i] = b;
    			i++;
    		}
    	}
	
    	return blockBank;
    }
    
    /**	Retrieve the current block bank as a Block[]
     *	@return
     */
    public Block[] getBlockBank()
    {	
    	return blockBank;
    }
    
    /**	Returns the Color grid
     */
    public Color[][] getGrid()
    {
    	return grid;
    }
    
    public boolean isEmpty()
    {
    	for (Color[] row : grid)
    		for (Color c : row)
    			if (c != null)
    				return false;
    	
    	return true;
    }
    
    /**
     * Clears filled lines on the grid and returns
     * 		how many were cleared.
     * @return the number of lines cleared
     */
    public int clearLines()
    {
        // 1: identify lines to clear
        boolean[] horizClear = new boolean[8];
        boolean[] vertClear = new boolean[8];
        int numLinesCleared[] = new int[2]; // 0 = horiz, 1 = vert
        
        // find which horizontal lines are full
        for (int r = 0; r < grid.length; r++)
        {
            horizClear[r] = true;
            for (int c = 0; c < grid[r].length; c++)
                horizClear[r] &= (grid[r][c] != null);
            numLinesCleared[0] = horizClear[r] ? numLinesCleared[0] + 1
                : numLinesCleared[0];
        }
        
        // find which vertical lines are full
        for (int c = 0; c < grid.length; c++)
        {
            vertClear[c] = true;
            for (int r = 0; r < grid.length; r++)
                vertClear[c] &= (grid[r][c] != null);
            numLinesCleared[1] = vertClear[c] ? numLinesCleared[1] + 1
                : numLinesCleared[1];
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
        
        // increase score - lines cleared & cross bonus
        score += (numLinesCleared[0] + numLinesCleared[1]) * LINE_CLEAR_PTS;
        score += numLinesCleared[0] > 0 && numLinesCleared[1] > 0 ? 
        		Math.max(numLinesCleared[0], numLinesCleared[1])
        		* CROSS_LINE_CLEAR_BONUS: 0;
        score += isEmpty() ? BOARD_CLEAR_PTS : 0;
        
        return numLinesCleared[0] + numLinesCleared[1];
    }

    /**	Returns which lines WOULD be cleared if clearLines
     * 		was called at this moment
     * @return boolean array of which lines would be cleared
     * 		array 0 - which rows are full
     * 		array 1 - which columns are full
     */
    public boolean[][] getPossibleLinesCleared()
    {
    	boolean[][] cleared = new boolean[2][MAX_ROWS_AND_COLS];
 
    	// find which horizontal lines are full
        for (int r = 0; r < grid.length; r++)
        {
            cleared[0][r] = true;
            for (int c = 0; c < grid[r].length; c++)
                cleared[0][r] = cleared[0][r] && (grid[r][c] != null);
        }
        
        // find which vertical lines are full
        for (int c = 0; c < grid.length; c++)
        {
            cleared[1][c] = true;
            for (int r = 0; r < grid.length; r++)
            	cleared[1][c] &= (grid[r][c] != null);
        }

    	return cleared;
    }
    
    /**
     * 
     * @return
     * 	0 - num clear rows
     * 	1 - num clear cols
     */
    public int[] getPossibleNumLinesCleared()
    {
    	int[] numCleared = new int[2];
    	
    	// find which horizontal lines are full
        for (int r = 0; r < grid.length; r++)
        {
            boolean fullRow = true;
            for (int c = 0; c < grid[r].length; c++)
                fullRow &= (grid[r][c] != null);
            
            numCleared[0] += fullRow ? 1 : 0;
        }
        
        // find which vertical lines are full
        for (int c = 0; c < grid.length; c++)
        {
            boolean fullCol = true;
            for (int r = 0; r < grid.length; r++)
            	fullCol &= (grid[r][c] != null);
            
            numCleared[1] += fullCol ? 1 : 0;
        }
        
        return numCleared;
    }
    
    public int getScore()
    {
    	return score;
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
        for (int row = r; row < r + block.length; row++)
            for (int col = c; col < c + block[row - r].length; col++)
                if (block[row - r][col - c])
                    grid[row][col] = null;
                
        return true;
    }
    
    /**
     * Removes a block from the grid at a specific location
     * @param b the block shape to remove
     * @param r the desired row
     * @param c the desired col
     * @return true if the block was removed
     */
    public boolean remove(int i, int r, int c)
    {
        return remove(blockBank[i], r, c);
    }

    /**
    * Returns the BlockGrid as a string - for testing
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
     * Returns the BlockGrid as a String involving colors - for testing
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