/**
 *	GameBoard.java tries to consolidate Board and Bank into one JComponent
 *		to allow for drag and drop
 * 
 * ideas
 *  - animation when line cleared (turn white then disappear?)
 *   - winning score has multiple choices like
 *   		short game: 3000 pts
 *   		regular game: 5000 pts
 *   		long game: 7000 pts
 *    - count # turns
 *     - log games, store scores, store dates, players, etc..
 *     - draw null first, then blocks
 *     
 *     
 *     TODO
 *      - don't do Turns, but track COMBOS !!!!!
 *       - rethink how exactly combo / streaks work
 * 
 */


package blockblast.gui.gamepanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import blockblast.core.Block;
import blockblast.core.BlockGrid;

public class GameBoard extends JComponent implements MouseInputListener, ActionListener
{
	// serial version uid
	private static final long serialVersionUID = 5421876096690844012L;
	
	public static final int GRID_SIZE = 8;
	public static final double UNIT_WIDTH_PROP = 1.0 / 16.0;
	public static final double GRID_UNIT_WIDTH_PROP = 1.0 / GRID_SIZE;
	
	public static final Color DARK_BLUE = new Color(46, 20, 148);
	public static final Color LESS_DARK_BLUE = new Color(71, 57, 197, 200);
	public static final Color DEACTIVATED_COLOR = new Color(50, 50, 50, 100);
	public static final Color TURN_COUNTER_COLOR = Color.WHITE;
	public static final Color END_COLOR = new Color(0, 0, 0, 150);
	public static final Color WIN_COLOR = Block.BLOCK_COLORS[2];
	public static final Color LOSE_COLOR = Block.BLOCK_COLORS[0];
	
	public static final Color[] HIGHLIGHTS = {
			new Color(255, 255, 255, 20),
			new Color(255, 255, 255, 45), new Color(255, 255, 255, 100),
			new Color(255, 255, 255, 150), new Color(255, 255, 255, 255)};
	public static final int[] HIGHLIGHT_STROKES = {18, 13, 9, 6, 4};
	
	public static final int BORDER_THICKNESS = 4;
	private static final float BOARD_UNIT_THICKNESS = 3f;
	
	public static final int DEFAULT_WIN_SCORE = 5000;
	
	public static final String WIN_MSG = "YOU WIN!";
	public static final String LOSE_MSG = "YOU LOSE :(";
	public static final String NO_SPACE_MSG = "There's no space left on the board";
	public static final String SCORE_WIN_MSG = "You reached 5000 points first!";
	
	private static final int BLOCK_1 = 0;
	private static final int BLOCK_2 = 1;
	private static final int BLOCK_3 = 2;
	private static final int NO_BLOCK = -1;
	
	// associated header
	private Header header;
	private GameBoard partnerBoard;
	
	// game core variables
	private BlockGrid grid;
	private boolean[] blockExists;
	private int winScore;
	
	// game state variables
	private int selectedBlock;
	private int mouseX;
	private int mouseY;
	private boolean mouseDragged; // used for future drag & drop
	private double blockX;
	private double blockY;
	
	private boolean tempAdded;
	private int tempBlock;
	private int tempRow;
	private int tempCol;
	private Color[][] tempGrid;
	boolean[][] tempCleared;
	int[] tempNumCleared;
	
	private int turnNum;
	
	private boolean boardActivated;
	private boolean winActivated;
	private boolean winFromScore;
	private boolean loseActivated;
	private boolean lossFromNoSpace;
	private int comboLength;
	
	// buffer
	private Dimension buffer;
	
	public GameBoard(Dimension bufferSize, Header header)
	{
		grid = new BlockGrid();
		blockExists = new boolean[grid.getBlockBank().length];
		Arrays.fill(blockExists, true);
		buffer = bufferSize;
		
		winScore = DEFAULT_WIN_SCORE;
		
		this.header = header;
		
		// set state vars
		selectedBlock = NO_BLOCK;
		boardActivated = true;
		turnNum = 1;
		comboLength = 0;
		
		// set transparent
		setOpaque(false);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public GameBoard(Dimension bufferSize, Header header, int winScore)
	{
		this(bufferSize, header);
		this.winScore = winScore;
	}
	
	/**
	 * 	MUST CALL THIS METHOD FOR PAIR PLAY
	 * @param partner
	 */
	public void setPartner(GameBoard partner)
	{
		partnerBoard = partner;
	}
	
	public void setActive(boolean active)
	{
		if (boardActivated != active)
			turnNum = active ? turnNum + 1 : turnNum - 1;
		
		boardActivated = active;
		header.setActive(active);
		comboLength = active ? comboLength : 0;
		
		repaint();
	}
	
	private void setWin(boolean win, boolean scoreWin)
	{
		winActivated = win;
		winFromScore = scoreWin;
		repaint();
	}
	
	private void setLose(boolean lose, boolean noSpace)
	{
		loseActivated = lose;
		lossFromNoSpace = noSpace;
		repaint();
	}
	
	public BlockGrid getGrid()
	{
		return grid;
	}
	
	public int getScore()
	{
		return grid.getScore();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		paintBoard(g2);
		paintBank(g2);
		paintBorder(g2, DARK_BLUE, BORDER_THICKNESS);
		paintGameActions(g2);
		paintComboNum(g2);
		paintBoardDeactivated(g2);
		paintWinOrLoseMessage(g2);
	}
	
	private void paintBoard(Graphics2D g2)
	{
		Color[][] colorGrid = grid.getGrid();
		
		double width = getWidth() - (double) BOARD_UNIT_THICKNESS;
		double unitHeight = width / GRID_SIZE;
		
		// fill squares - first null, then others
		for (int row = 0; row < GRID_SIZE; row++)
			for (int col = 0; col < GRID_SIZE; col++)			
				if (colorGrid[row][col] == null)
				{
					g2.setColor(LESS_DARK_BLUE);
					g2.fill(new Rectangle2D.Double(unitHeight * col, unitHeight * row,
							unitHeight, unitHeight));
					
					g2.setColor(DARK_BLUE);
					g2.setStroke(new BasicStroke(BOARD_UNIT_THICKNESS));
					g2.draw(new Rectangle2D.Double(1.5 + unitHeight * col, 1.5 + unitHeight * row,
							unitHeight, unitHeight));
				}
		
		// fill squares - non full
		for (int row = 0; row < GRID_SIZE; row++)
		{
			for (int col = 0; col < GRID_SIZE; col++)
			{				
				if (colorGrid[row][col] != null && (tempGrid == null ? true
						: !tempCleared[0][row] || !tempCleared[1][col]))
				{
					g2.setColor(tempGrid == null ? colorGrid[row][col] : tempGrid[row][col]);
					g2.fill(new Rectangle2D.Double(unitHeight * col, unitHeight * row,
							unitHeight, unitHeight));
					
					g2.setColor(tempGrid == null ? Block.getSecondaryColor(colorGrid[row][col])
							: Block.getSecondaryColor(tempGrid[row][col]));
					g2.setStroke(new BasicStroke(3));
					g2.draw(new Rectangle2D.Double(unitHeight * col, unitHeight * row,
							unitHeight, unitHeight));
				}
			}
		}
		
		// before filling painted squares, outline cleared lines
		// TODO - constants for ROWS & COLS - 0 & 1; constant for line thickness - 3
		if (tempGrid != null)
		{
			//1: draw outline for cleared rows
			if (tempNumCleared[0] > 0)
			{
				for (int row = 0; row < tempCleared[0].length; row++)
					if (tempCleared[0][row]) // row is full
						for (int highlight = 0; highlight < HIGHLIGHTS.length; highlight++)
						{
							g2.setColor(HIGHLIGHTS[highlight]);
							g2.setStroke(new BasicStroke(HIGHLIGHT_STROKES[highlight]));
							g2.draw(new Line2D.Double(0, row * getWidth() * GRID_UNIT_WIDTH_PROP,
									getWidth() - ((BasicStroke) g2.getStroke()).getLineWidth() / 2.0, 
									row * getWidth() * GRID_UNIT_WIDTH_PROP));
							g2.draw(new Line2D.Double(0, (row + 1) * getWidth() * GRID_UNIT_WIDTH_PROP,
									getWidth() - ((BasicStroke) g2.getStroke()).getLineWidth() / 2.0, 
									(row + 1) * getWidth() * GRID_UNIT_WIDTH_PROP));
						}
			}
			
			//1: draw outline for cleared columns
			if (tempNumCleared[1] > 0)
			{
				for (int col = 0; col < tempCleared[1].length; col++)
					if (tempCleared[1][col]) // row is full
						for (int highlight = 0; highlight < HIGHLIGHTS.length; highlight++)
						{
							g2.setColor(HIGHLIGHTS[highlight]);
							g2.setStroke(new BasicStroke(HIGHLIGHT_STROKES[highlight]));
							
							g2.draw(new Line2D.Double(col * getWidth() * GRID_UNIT_WIDTH_PROP, 0,
									col * getWidth() * GRID_UNIT_WIDTH_PROP,
									getWidth() - ((BasicStroke) g2.getStroke()).getLineWidth() / 2.0));
							g2.draw(new Line2D.Double((col + 1) * getWidth() * GRID_UNIT_WIDTH_PROP, 0,
									(col + 1) * getWidth() * GRID_UNIT_WIDTH_PROP,
									getWidth() - ((BasicStroke) g2.getStroke()).getLineWidth() / 2.0));
						}
			}
		}
		
		// fill squares - other squares
		for (int row = 0; row < GRID_SIZE; row++)
		{
			for (int col = 0; col < GRID_SIZE; col++)
			{				
				if (colorGrid[row][col] != null && (tempGrid == null ? true
						: tempCleared[0][row] || tempCleared[1][col]))
				{
					g2.setColor(tempGrid == null ? colorGrid[row][col] : tempGrid[row][col]);
					g2.fill(new Rectangle2D.Double(unitHeight * col, unitHeight * row,
							unitHeight, unitHeight));
					
					g2.setColor(tempGrid == null ? Block.getSecondaryColor(colorGrid[row][col])
							: Block.getSecondaryColor(tempGrid[row][col]));
					g2.setStroke(new BasicStroke(3));
					g2.draw(new Rectangle2D.Double(unitHeight * col, unitHeight * row,
							unitHeight, unitHeight));
				}
			}
		}
		

	}
	
	private void paintBank(Graphics2D g2)
	{
		// calc height of grid & buffer
		double spaceAbove = getWidth() + buffer.getHeight();
		
		// draw background
		g2.setColor(LESS_DARK_BLUE);
		g2.fill(new Rectangle2D.Double(0, spaceAbove, getWidth(), getHeight()));
		
		// assume blockExists is set appropriately - CHANGE
		
		// retrieve blocks
		Block[] blocks  = grid.getBlockBank();
		
		// calculations
		int widthUnits = 0;
		for (Block block : blocks)
			widthUnits += block.getBlock()[0].length;
		
		double width = getWidth();
		double height = getHeight() - spaceAbove;
		
		double blocksWidth = width * UNIT_WIDTH_PROP * widthUnits;
		int spaces = blocks.length + 1;
		double spaceBetween = (width - blocksWidth) / spaces;
		
		double widthElapsed = 0;
		double unitSize = width * UNIT_WIDTH_PROP;
		
		// draw each block
		for (int b = 0; b < blocks.length; b++)
		{
			widthElapsed += spaceBetween;
			Block block = blocks[b];
			boolean[][] boolBlock = block.getBlock();
			
			// calculate starting height
			double startHeight = spaceAbove + (height - (boolBlock.length * unitSize)) / 2.0;
			
			// if block exists & it's not alr selected, paint it
			if (blockExists[b] && selectedBlock != b)
				paintBlock(g2, block, 2, unitSize, widthElapsed, startHeight);
			
			// increase widthElapsed
			widthElapsed += boolBlock[0].length * unitSize;
		}
	}
	
	public void paintBorder(Graphics2D g2, Color borderColor, int lineThickness)
	{
		g2.setColor(borderColor);
		g2.setStroke(new BasicStroke(lineThickness));
		
		// draw outline of board
		g2.draw(new Rectangle2D.Double(lineThickness / 2.0, lineThickness / 2.0, 
				getWidth() - lineThickness, getWidth() - lineThickness / 2.0));
		
		// draw outline of bank
		double bankHeight = getHeight() - getWidth() - buffer.getHeight();
		g2.draw(new Rectangle2D.Double(lineThickness / 2.0, 
				lineThickness / 2.0 + getWidth() + buffer.getHeight(), 
				getWidth() - lineThickness, bankHeight - lineThickness));
	}

	private void paintGameActions(Graphics2D g2)
	{
		if (selectedBlock != NO_BLOCK)
		{
			double unitSize = getWidth() * GRID_UNIT_WIDTH_PROP;
//			boolean[][] block = grid.getBlockBank()[selectedBlock].getBlock();
			
//			double blockHeight = block.length * unitSize;
//			double blockWidth = block[0].length * unitSize;
			
			paintBlock(g2, grid.getBlockBank()[selectedBlock], 3, unitSize, blockX, blockY);
		}
	}
	
	/**
	 * Not used right now -- could be used to track turns
	 * @param g2
	 */
	private void paintTurnNum(Graphics2D g2)
	{
		String turnString = "Turn: " + turnNum;
		g2.setColor(TURN_COUNTER_COLOR);
		
		Font turnFont = new Font("Juice ITC", Font.PLAIN, getWidth() / 16);
		g2.setFont(turnFont);
		
		g2.drawString(turnString, 5, g2.getFont().getSize() + 5);
		
	}
	
	private void paintComboNum(Graphics2D g2)
	{
		if (comboLength > 0)
		{
			// TODO - like 50 magic numbers
			String comboString = "Streak: " + comboLength;
			g2.setColor(TURN_COUNTER_COLOR); // TODO
			
			Font comboFont = new Font("Rockwell", Font.PLAIN, getWidth() / 16);
			g2.setFont(comboFont);
			
			g2.drawString(comboString, 10, g2.getFont().getSize() + 5);
		}
	}
	
	private void paintBoardDeactivated(Graphics2D g2)
	{
		// paint board deactivated if necessary
		g2.setColor(winActivated || loseActivated ? END_COLOR : DEACTIVATED_COLOR);
		
		if (!boardActivated)
		{
			g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getWidth() + BORDER_THICKNESS / 2.0));
			g2.fill(new Rectangle2D.Double(0, getWidth() + buffer.getHeight(), 
					getWidth(), getHeight() - getWidth() - buffer.getHeight()));
			
		}
	}
	
	private void paintWinOrLoseMessage(Graphics2D g2)
	{
		// TODO paint win message - put in method
		if (winActivated || loseActivated)
		{
			g2.setColor(winActivated ? WIN_COLOR : LOSE_COLOR);
			
			// smooth text drawing
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			// set font
			Font font = new Font("Juice ITC", Font.BOLD, (int) (getWidth() / 4.0));
			g2.setFont(font);
			
			int stringWidth = g2.getFontMetrics().stringWidth(winActivated ? WIN_MSG : LOSE_MSG);
			int stringHeight = g2.getFont().getSize();
			int xPos = (getWidth() - stringWidth) / 2;
			int yPos = (getWidth() - stringHeight) / 2 + stringHeight - 1;
			
			g2.drawString(winActivated ? WIN_MSG : LOSE_MSG, xPos, yPos);
			
			// if you lost from no space, put message; or if you win from score, put message
			if ((loseActivated && lossFromNoSpace) || (winActivated && winFromScore))
			{
				Font font2 = new Font("Juice ITC", Font.PLAIN, (int) (getWidth() / 11.0));
				g2.setFont(font2);
				
				stringWidth = g2.getFontMetrics().stringWidth(loseActivated ? 
						NO_SPACE_MSG : SCORE_WIN_MSG);
				stringHeight = g2.getFont().getSize();
				
				xPos = (getWidth() - stringWidth) / 2;
				yPos += stringHeight + 20;
				
				g2.drawString(loseActivated ? NO_SPACE_MSG : SCORE_WIN_MSG, xPos, yPos);
				
			}
		}
	}
	
	private static void paintBlock(Graphics2D g2, Block block, int lineThickness, 
			double unitSize, double x, double y)
	{
		g2.setStroke(new BasicStroke(lineThickness));
		boolean[][] boolBlock = block.getBlock();
		for (boolean[] row : boolBlock)
		{
			double xChange = 0;
			for (boolean unit : row)
			{
				if (unit)
				{
					g2.setColor(block.getColor());
					g2.fill(new Rectangle2D.Double(x + xChange, y, unitSize, unitSize));
					g2.setColor(block.getSecondaryColor());
					g2.draw(new Rectangle2D.Double(x + xChange, y, unitSize, unitSize));
				}
				xChange += unitSize;
			}
			y += unitSize;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (boardActivated)
		{
			mouseX = e.getX();
			mouseY = e.getY();
			
			// ADD TO BOARD ********
			if (tempAdded)
			{
				completeTurn();
			}
			
			// block bank click
			else if (e.getSource() == this)		
				if (mouseY > getWidth() + buffer.getHeight())
				{
					if (mouseX < getWidth() * 1.0 / 3)
					{
						if (blockExists[BLOCK_1])
							selectedBlock = selectedBlock == BLOCK_1? NO_BLOCK : BLOCK_1;
						else
							selectedBlock = NO_BLOCK;
					} 
					else if (mouseX < getWidth() * 2.0 / 3)
					{
						if (blockExists[BLOCK_2])
							selectedBlock = selectedBlock == BLOCK_2 ? NO_BLOCK : BLOCK_2;
						else
							selectedBlock = NO_BLOCK;
					}
					else
					{
						if (blockExists[BLOCK_3])
							selectedBlock = selectedBlock == BLOCK_3 ? NO_BLOCK : BLOCK_3;
						else
							selectedBlock = NO_BLOCK;
					}
					setBlockCoordinates();
				}
			
			// repaint scene
			repaint();
		}
	}
	
	private void completeTurn()
	{
		// update variables & grid
		grid.remove(selectedBlock, tempRow, tempCol);
		grid.addToGridFromBank(selectedBlock, tempRow, tempCol, false, comboLength); // PERM ADD
		int linesCleared = grid.clearLines();
		tempAdded = false;
		tempGrid = null;
		blockExists[selectedBlock] = false;
		selectedBlock = NO_BLOCK;
		turnNum++;
		comboLength++;
		
		// refill block bank if necessary
		if (!blockExists[BLOCK_1] && !blockExists[BLOCK_2] && !blockExists[BLOCK_3])
		{
			grid.regenerateBlockBank();
			Arrays.fill(blockExists, true);
		}
		
		// signify score update *** CHANGE
		header.updateScore(grid.getScore());
		
		// before moving on to next turn, check if turn ended
		// aka 1. you win OR 2. you run out of space
		if (grid.getScore() >= winScore)
		{
			// win protocol
			setActive(false);
			setWin(true, true);
			partnerBoard.setLose(true, false);
			
			// TODO - account for if p1 wins and p2 hasn't had equal # turns
			// ORRRR.. randomize first player !!!! *******
			
		}
		else // not at win score yet -- continue game
		{
			boolean noSpace = true;
			for (int b = BLOCK_1; b <= BLOCK_3; b++)
				if (blockExists[b])
					noSpace &=  !grid.isSpaceInGrid(grid.getBlockBank()[b]);
			
			if (noSpace)
			{
				// lose protocol
				setActive(false);
				setLose(true, true);
				partnerBoard.setWin(true, false);
				
				// TODO - account for if p1 wins and p2 hasn't had equal # turns
			}
			// game not over--deactivate self, activate partner IF no combo
			else if (partnerBoard != null)
			{
				if (linesCleared == 0)
				{
					setActive(false);
					partnerBoard.setActive(true);
				}
				else // linesCleared > 0 -- go again AFTER animation
				{
					
				}
			}
		}
	}
	
	private void setBlockCoordinates()
	{
		if (selectedBlock != NO_BLOCK)
		{
			double blockWidth = grid.getBlockBank()[selectedBlock].getBlock()[0].length
					* getWidth() * GRID_UNIT_WIDTH_PROP;
			double blockHeight = grid.getBlockBank()[selectedBlock].getBlock().length
					* getWidth() * GRID_UNIT_WIDTH_PROP;
			
			blockX = mouseX - blockWidth / 2;
			blockY = mouseY == 0 ? mouseY - blockHeight - 2
					: mouseY - blockHeight - ((getHeight() - mouseY) * .2); // CHAGNE
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (boardActivated)
		{
			mouseX = e.getX();
			mouseY = e.getY();
			setBlockCoordinates();
			
			mouseDragged = true;
			
			updateBlockPreview();
			
			// repaint scene
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (boardActivated)
		{
			mouseX = e.getX();
			mouseY = e.getY();
			setBlockCoordinates();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	//  mousemotion

	@Override
	public void mouseMoved(MouseEvent e) {
		if (boardActivated)
		{
			mouseX = e.getX();
			mouseY = e.getY();
			setBlockCoordinates();
			
			updateBlockPreview();
			
			// repaint scene
			repaint();
		}
	}
	
	private void updateBlockPreview()
	{
		if (tempAdded)
		{
			grid.remove(grid.getBlockBank()[tempBlock], tempRow, tempCol);
			tempAdded = false;
			tempGrid = null;
		}
		
		if (blockY < getWidth() && selectedBlock != NO_BLOCK)
		{
			// calc which square the click is in
			int row = -1;
			int col = -1;
			for (int c = 0; c <= GRID_SIZE; c++)
				if (blockX <= getWidth() * GRID_UNIT_WIDTH_PROP * c)
				{
					col = c;
					break;
				}
			
			for (int r = 0; r <= GRID_SIZE; r++)
				if (blockY <= getWidth() * GRID_UNIT_WIDTH_PROP * r)
				{
					row = r;
					break;
				}
			
			// calc which square is temp
			final double right = ((col - 1) + .5) * getWidth() * GRID_UNIT_WIDTH_PROP;
			final double down = ((row - 1) + .5) * getWidth() * GRID_UNIT_WIDTH_PROP;
			boolean goRight = blockX > right;
			boolean goDown = blockY > down;
			
			// calc which square to add block to
			col = goRight ? col : col - 1;
			row = goDown ? row : row - 1;
			
			// temporarily add to grid
			tempAdded = grid.addToGridFromBank(selectedBlock, row, col, true);
			if (tempAdded)
			{
				tempBlock = selectedBlock;
				tempRow = row;
				tempCol = col;
			}
			
			
			// if the temp add resulted in possible cleared lines, highlight them
			tempCleared = grid.getPossibleLinesCleared();
			tempNumCleared = grid.getPossibleNumLinesCleared();
			
			if (tempNumCleared[0] > 0 || tempNumCleared[1] > 0)
			{
				tempGrid = new Color[grid.getGrid().length][grid.getGrid()[0].length];
				for (int r = 0; r < grid.getGrid().length; r++)
					for (int c = 0; c < grid.getGrid()[r].length; c++)
						tempGrid[r][c] = grid.getGrid()[r][c];
			}
			
			// fill tempGrid's highlights
			if (tempNumCleared[0] > 0)
				for (int r = 0; r < tempGrid.length; r++)
				{
					if (tempCleared[0][r])
					{
						for (int c = 0; c < tempGrid[r].length; c++)
							tempGrid[r][c] = grid.getBlockBank()[tempBlock].getColor();
					}
				}
				
			if (tempNumCleared[1] > 0)
				for (int c = 0; c < tempGrid[0].length; c++)
				{
					if (tempCleared[1][c])
					{
						for (int r = 0; r < tempGrid.length; r++)
							tempGrid[r][c] = grid.getBlockBank()[tempBlock].getColor();
					}
				}
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}