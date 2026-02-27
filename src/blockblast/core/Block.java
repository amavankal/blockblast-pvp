/**
 * Block.java represents a block in the game
 * 
 * @author Asha Mavankal
 * 
 */
package blockblast.core;

import java.awt.Color; 

public class Block
{    
	public static final Color[] BLOCK_COLORS = {new Color(255, 99, 88),
			new Color(255, 209, 70), new Color(120, 210, 55), // 95, 227, 66
			new Color(60, 152, 243), new Color(170, 70, 254) // 45, 115, 245
	};
	
	public static final Color[] SECONDARY_COLORS = {new Color(170, 33, 29),
			new Color(191, 148, 1), new Color(41, 155, 18),
			new Color(29, 75, 121), new Color(101, 24, 170)}; // 14, 37, 167
	
	public static final Color[] TEMP_COLORS = {new Color(255, 177, 171),
			new Color(255, 232, 130), new Color(189, 233, 155),
			new Color(148, 184, 250), new Color(215, 164, 255)};
	
	// instance variables
	private boolean[][] block; //shape of the block
	private int color;   //color of the block

    /**
     * Creates a block with a given shape
     * @param block an array representing the block's shape 
     */
	public Block(boolean[][] block)
    {
        this.block = block;
        color = (int) (Math.random() * BLOCK_COLORS.length);
    }
    
    // accessor methods
    
    /**
     * Retrieves the block's shape
     * @return an array representing the block's shape
     */
    public boolean[][] getBlock() 
    { 
       return block;
    }
     
    /**
     * Retrieves the block's color
     * @return the block's color
     */
    public Color getColor() 
    {
       return BLOCK_COLORS[color];
    }
    
    public Color getSecondaryColor()
    {
    	return SECONDARY_COLORS[color];
    }
    
    public Color getTempColor()
    {
    	return TEMP_COLORS[color];
    }
    
    public static Color getSecondaryColor(Color c)
    {
    	for (int i = 0; i < BLOCK_COLORS.length; i++)
    	{
    		if (BLOCK_COLORS[i].equals(c) || TEMP_COLORS[i].equals(c))
    			return SECONDARY_COLORS[i];
    	}
    	
    	return null;
    }
    
    /**	Generates hash code function for when this block is stored
     * 		in a hash set.
     */
    @Override
    public int hashCode()
    {
    	return (block.hashCode() * BLOCK_COLORS[color].hashCode())
    			+ BLOCK_COLORS[color].hashCode();
    }
    
    /**	Returns whether this Block is equal to another
     * 		(they're both Blocks & have the same shape / color)
     *	@return whether or not this is equal to given object
     */
    @Override
    public boolean equals(Object o)
    {
    	if (o instanceof Block)
    		return BlockType.equals(((Block) o).block, this.block)
    			&& ((Block) o).color == this.color;
    	else
    		return false;
    }
 }