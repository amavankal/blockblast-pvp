/**
 * Block.java represents a block in the game
 * 
 * @author Asha Mavankal, Amya Prophet
 */
package blockblast.core;

import blockblast.gui.*;
import java.awt.Color; 

public class Block
{    
	// instance variables
	private boolean[][] block; //shape of the block
	private Color color;   //color of the block

    /**
     * Creates a block with a given shape
     * @param block an array representing the block's shape 
     */
	public Block(boolean[][] block)
    {
        this.block = block;
        color = GamePanel.BLOCK_COLORS[(int) (Math.random() 
       		 * GamePanel.BLOCK_COLORS.length)];
    }
     
    /**
     * Creates a block with a given shape and color
     * @param block an array representing the block's shape
     * @param color the block's color
     */
    public Block(boolean[][] block, Color color)
    {
        this.block = block;
        this.color = color;
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
       return color;
    }
    
    /**	Generates hash code function for when this block is stored
     * 		in a hash set.
     */
    @Override
    public int hashCode()
    {
    	return (block.hashCode() * color.hashCode()) + color.hashCode();
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
    			&& ((Block) o).color.equals(this.color);
    	else
    		return false;
    }
 }