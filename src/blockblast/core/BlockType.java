/**
 * BlockType.java is an enum that represents the finite
 * 		types and iterations of certain blocks in our block
 * 		blast game.
 * 
 * @author Asha Mavankal
 * 
 */
package blockblast.core;

import java.util.Arrays;

public enum BlockType
{
	TWO_BLOCK_I1(1, 2, new boolean[][]{
                            {true, true}
    }),
    TWO_BLOCK_I2(2, 1, new boolean[][]{
        {true},
        {true}
    }),
    THREE_BLOCK_1_I1(2, 2, new boolean[][]{
    	{false, true},
        {true, true}
    }),
    THREE_BLOCK_1_I2(2, 2, new boolean[][]{
        {true, false},
        {true, true}
    }),
    THREE_BLOCK_1_I3(2, 2, new boolean[][]{
        {true, true},
        {true, false}
    }),
    THREE_BLOCK_1_I4(2, 2, new boolean[][]{
        {true, true},
        {false, true}
    }),
    THREE_BLOCK_2_I1(1, 3, new boolean[][]{
    	{true, true, true}
    }),
    THREE_BLOCK_2_I2(3, 1, new boolean[][]{
        {true},
        {true},
        {true}
    }),
    THREE_BLOCK_3_I1(3, 3, new boolean[][]{
    	{true, false, false},
    	{false, true, false},
    	{false, false, true}
    }),
    THREE_BLOCK_3_I2(3, 3, new boolean[][]{
        {false, false, true},
        {false, true, false},
        {true, false, false}
    }),
    FOUR_BLOCK_1_I1(3, 2, new boolean[][]{
                            {true,  false}, 
                            {true,  true},
                            {false, true}
    }),
    FOUR_BLOCK_1_I2(2, 3, new boolean[][]{
        {false,  true, true}, 
        {true,  true, false},
    }),
    FOUR_BLOCK_1_I3(2, 3, new boolean[][]{
        {true,  true, false}, 
        {false,  true, true},
    }),
    FOUR_BLOCK_1_I4(3, 2, new boolean[][]{
        {false,  true}, 
        {true,  true},
        {true, false}
    }),
    FOUR_BLOCK_2_I1(3, 2, new boolean[][]{
    	{false, true},
    	{false, true},
    	{true,  true}
    }),
    FOUR_BLOCK_2_I2(2, 3, new boolean[][]{
    	{true, false, false},
        {true, true, true}
    }),
    FOUR_BLOCK_2_I3(3, 2, new boolean[][]{
        {true, true},
        {true, false},
        {true,  false}
    }),
    FOUR_BLOCK_2_I4(2, 3, new boolean[][]{
        {true, true, true},
        {false, false, true}
    }),
    FOUR_BLOCK_2_I5(3, 2, new boolean[][]{
   	 {true, true},
   	 {false, true},
   	 {false,  true}
    }),
    FOUR_BLOCK_2_I6(2, 3, new boolean[][]{
        {false, false, true},
        {true, true, true}
    }),
    FOUR_BLOCK_2_I7(3, 2, new boolean[][]{
        {true, false},
        {true, false},
        {true,  true}
    }),
    FOUR_BLOCK_2_I8(2, 3, new boolean[][]{
        {true, true, true},
        {true, false, false}
    }),
    FOUR_BLOCK_3_I1(3, 2, new boolean[][]{
    	{true, false},
    	{true, true},
    	{true, false}
    }),
    FOUR_BLOCK_3_I2(2, 3, new boolean[][]{
    	{true, true, true},
    	{false, true, false}
    }),
    FOUR_BLOCK_3_I3(3, 2, new boolean[][]{
    	{false, true},
    	{true, true},
    	{false, true}
    }),
    FOUR_BLOCK_3_I4(2, 3, new boolean[][]{
        {false, true, false},
        {true, true, true}
    }),
    FOUR_BLOCK_4_I1(1, 4, new boolean[][]{
   	 {true, true, true, true}
    }),
    FOUR_BLOCK_4_I2(4, 1, new boolean[][]{
        {true},
        {true},
        {true},
        {true}
    }),
    FOUR_BLOCK_5(2, 2, new boolean[][]{
        {true, true},
        {true, true}
    }),
    FIVE_BLOCK_1_I1(3, 3, new boolean[][]{
    	{true,  true,  true},
   	 	{false, false, true},
   	 	{false, false, true}
    }),
    FIVE_BLOCK_1_I2(3, 3, new boolean[][]{
    	{false, false,  true},
    	{false, false,  true},
    	{true,  true,   true}
    }),
    FIVE_BLOCK_1_I3(3, 3, new boolean[][]{
    	{true, false,  false},
    	{true, false, false},
    	{true, true,  true}
    }),
    FIVE_BLOCK_1_I4(3, 3, new boolean[][]{
    	{true,  true,  true},
    	{true, false, false},
    	{true, false, false}
    }),
    FIVE_BLOCK_2_I1(1, 5, new boolean[][]{
    	{true, true, true, true, true}
    }),
    FIVE_BLOCK_2_I2(5, 1, new boolean[][]{
    	{true},
    	{true},
    	{true},
    	{true},
    	{true}
    }),
    SIX_BLOCK_I1(3, 2, new boolean[][]{
    	{true, true},
    	{true, true},
    	{true, true}
    }),
    SIX_BLOCK_I2(2, 3, new boolean[][]{
    	{true, true, true},
    	{true, true, true}
    }),
    NINE_BLOCK_1(3, 3, new boolean[][]{
    	{true, true, true},
    	{true, true, true},
    	{true, true, true}
    });
    
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
	
	private int boundingRows; // # rows
	private int boundingCols; // # cols
	private boolean[][] block; // the block, represented by 2d boolean array

    /** constructor
    */
    private BlockType(int r, int c, boolean[][] filled)
    {
    	boundingRows = r;
    	boundingCols = c;
    	block = filled;
    }
     
    /**	Retrieve the width (num columns) of this block's
     * 		bounding rectangle
     * @return the width of the block
     */
    public int getWidth() {	return boundingCols;   }
    
    /**	Retrieve the height (num rows) of this block's
     * 		bounding rectangle
     * @return the height of the block
     */
    public int getHeight() {	return boundingRows; }
    
    /**	Retrieves the boolean 2d array representing
     * 		this block
     *	@return the 2d boolean array representing this block
     */
    public boolean[][] getBlock() {	return block; }
    
    /**	Generates a random block type, but weighted based on constants
     * 
     * @return
     */
    public static BlockType generateBlockType()
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
    				return choice;
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
    				return choice;
    			}
    		}
    		return null;
    	}
    }
    
    /**	Tests whether two block types, represented by their
     * 		2d boolean arrays, are equal
     * @param x the first block, as a 2d boolean array
     * @param y the second block, as a 2d boolean array
     * @return whether or not the two block types are the same
     */
    public static boolean equals(boolean[][] x, boolean[][] y)
    {
        if (x.length == y.length)
        {
            for (int r = 0; r < x.length; r++)
            {
                if (Arrays.equals(x[r], y[r]))
                    continue;
                else
                    return false;
            }
            return true;
        }
        return false;
    }
    
    /**	ToString method to enable testing
     *	@return this block as a string
     */
    public String toString()
    {
        StringBuilder results = new StringBuilder();
        for (boolean[] r : block)
        {
            for (boolean b : r)
                results.append(b ? "[]" : "  ");
            results.append("\n");
        }
        return results.toString();
    }
}