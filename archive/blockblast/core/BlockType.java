/**
 * BlockType.java is an enum that represents the finite
 * 		types and iterations of certain blocks in our block
 * 		blast game.
 * 
 * @author Asha Mavankal
 * Collaborator: Amya Prophet
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
}