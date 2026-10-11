/**
 * GamePanel.java is the game panel on which the actual game
 * 		is played on.
 * 
 * @author Asha Mavankal, Amya Prophet
 */
package blockblast.gui;

import blockblast.core.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel implements MouseListener, ActionListener
{	
	// color constants
	public static final Color[] BLOCK_COLORS = {new Color(255, 99, 88),
			new Color(255, 209, 70), new Color(120, 210, 55),
			new Color(45, 115, 245), new Color(170, 70, 254)
	};
	protected static final Color DEFAULT_COLOR = new Color(148, 147, 141);
	private static final LineBorder NORMAL_BORDER = new LineBorder(
			BigGUI.BACKGROUND, 3);
	private static final LineBorder HIGHLIGHT_BORDER = new LineBorder(
			Color.YELLOW, 3);
	private static final double GRID_SCR_HEIGHT_RATIO = 5.0 / 9;
	private static final double INSTR_HELP_H_PROP = .8;
	private static final double BLOCK_BANK_GRID_H_PROP = .5;
	private static final double POWERUP_PROB = .05;
	private static final double COMBO_POWERUP_INC = .025;
	private static final int MIN_UNDO = 1;
	private static final int MAX_UNDO = 3;
	private static final int UNDO_REDUCTION = 300;
	private static final int DESTROY_INCREASE = 500;
	
	// text constants
	private static final String P1 = "PLAYER 1:\n\n";
	private static final String P2 = "PLAYER 2:\n\n";
	private static final String P1_MOVE = "Player 1's move...\n\n";
	private static final String P2_MOVE = "Player 2's move...\n\n";
	private static final String P2_WAIT = "Waiting for Player 1 to take their turn...\n\n";
	private static final String P1_WAIT = "Waiting for Player 2 to take their turn...\n\n";
	private static final String INSTRUCTIONS = "Select a block from the block bank"
			+ " below by clicking on it, and then select a space to add it there."
			+ " Hit confirm to cement your move.\n\n";
	private static final String CONFIRM = "CONFIRM";
	private static final String POWER_UP = "POWER UP!";
	private static final String BAD_CONFIRM = "Please select a valid block and a valid "
			+ "space in the grid to add that block to before hitting confirm";
	private static final String BAD_SPOT_SELECTED = "You can't add a block to that spot";
	private static final String COMBO = "Congratulations! Since you cleared a line you may go"
			+ " again!\n\n";
	private static final String UNDO_POWERUP = "Congratulations! You received "
			+ "an undo powerup. Select POWER UP! and undo the grid state on your opponent's grid by a "
			+ "random amount between 1 and 3 states.\n\n";
	private static final String DESTROY_POWERUP = "Congratulations! You received a destroy"
			+ " power. Select the top left corner of a 3x3 section of your grid to destroy"
			+ " and click POWER UP! and you will receive 500 points!\n\n";
	private static final String SELECT_DESTROY = "Please select a square to destroy";
	private static final String NO_SPACE = "Game Over! No Space!\n\n";
	private static final String SCORE_WIN = "You reached 5,000 points!!";
	private static final String SCORE_LOSS = "Better luck next time :(";
	private static final String P1_WIN = "Player 1 Wins!!!\n\n";
	private static final String P2_WIN = "Player 2 Wins!!!\n\n";
	private static final String SCORE_STR = "Score: ";

	//score constants
	private static final int LINE_CLEAR = 500;
	//private static final int BASE_MULT = 5;
	private static final int BASE = 50;
	//private static final int COMBO = 100;
	private static final int WIN_SCORE = 5000;
	
	// instance variables - gui objects
	protected JPanel row1;
	protected JPanel row2;
	
	protected JPanel p1Grid;
	protected JPanel p1HelpPanel; //holds instructions & button
	protected JTextPane p1Instructions;
	protected JButton p1Confirm;
	protected JButton p1Powerup;
	protected JPanel p1BlockBank;
	private JPanel[] p1Blocks;
	
	protected JPanel p2Grid;
	protected JPanel p2HelpPanel;
	protected JTextPane p2Instructions;
	protected JButton p2Confirm;
	protected JButton p2Powerup;
	protected JPanel p2BlockBank;
	private JPanel[] p2Blocks;
	
	protected Dimension gridDimensions;
	protected Dimension helpDimensions;
	protected Dimension instructionDimensions;
	protected Dimension blockBankDimensions;
	
	// instance variables - game mechanics / temp tracking
	private boolean p1Turn; // keep track of turns
	private int selectedBlock;
	private int selectedRow;
	private int selectedCol;
	private int p1Score;
	private int p2Score;
	
	private int combo;
	private boolean undoActivated;
	private boolean destroyActivated;
	
	// instance variables = core
	private BlockGrid p1BlockGrid;
	private BlockGrid p2BlockGrid;
	
	private Block[] currentP1Blocks;
	private Block[] currentP2Blocks;

	/**	Constructor - initializes game panel
	 */
	public GamePanel()
	{
		super(true);
		
		// initialize the GUI elements
		initializePanels();
		
		// initialize listeners
		initializeListeners();
	}
	
	/**	Initializes the fundamental GUI elements
	 */
	private void initializePanels()
	{
		// 0: set this background to correct color
		setBackground(BigGUI.BACKGROUND);
		
		// 1: make overall boxLayouts
		initializeRowLayouts();
	
		// 2: initialize core elements
		p1Turn = true;
		p1BlockGrid = new BlockGrid();
		p2BlockGrid = new BlockGrid();
		currentP1Blocks = p1BlockGrid.getBlockBank();
		currentP2Blocks = p2BlockGrid.getBlockBank();
		selectedBlock = -1;
		selectedRow = -1;
		selectedCol = -1;
		p1Score = 0;
		p2Score = 0;
		
		undoActivated = false;
		destroyActivated = false;
		combo = 0;
		
		// 3: initialize players' grids
		initializeGrids();
		
		// 4: initialize player instructions & button
		initializeHelp();
		
		// 5: initialize block banks
		initializeBlockBanks();
		
		// fill BoxLayouts with panels
		row1.add(p1Grid);
		row1.add(p1HelpPanel);
		row1.add(p2Grid);
		row1.add(p2HelpPanel);
		row2.add(p1BlockBank);
		row2.add(p2BlockBank);
		
		// add rows to this panel
		add(row1);
		add(row2);
	}
	
	/**	Initializes listeners on interactive elements
	 */
	private void initializeListeners()
	{
		// 1: add listener for the each players' blocks in the block bank
		for (JPanel block : p1Blocks)
			block.addMouseListener(this);
		for (JPanel block : p2Blocks)
			block.addMouseListener(this);
		
		// 2: add listener for game grid
		p1Grid.addMouseListener(this);
		p2Grid.addMouseListener(this);
		
		// 3: add listener for each players' confirm buttons
		p1Confirm.addActionListener(this);
		p2Confirm.addActionListener(this);
		
		// 4: add listener for each player's powerup button
		p1Powerup.addActionListener(this);
		p2Powerup.addActionListener(this);
	}
	
	/**	Initializes the layout of the panel
	 */
	private void initializeRowLayouts()
	{
		// set the layout of this JPanel to vertical box layout
		BoxLayout vertLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(vertLayout);
		
		// set the layouts of 2 sub-JPanels to horizontal box layout
		row1 = new JPanel();
		BoxLayout row1Layout = new BoxLayout(row1, BoxLayout.X_AXIS);
		row1.setLayout(row1Layout);
		row1.setBackground(BigGUI.BACKGROUND);
		
		row2 = new JPanel();
		BoxLayout row2Layout = new BoxLayout(row2, BoxLayout.X_AXIS);
		row2.setLayout(row2Layout);
		row2.setBackground(BigGUI.BACKGROUND);
	}
	
	/**	Initializes the game grids
	 */
	private void initializeGrids()
	{
		// set layouts & backgrounds
		p1Grid = new JPanel(new GridLayout(8, 8, 5, 5));
		p2Grid = new JPanel(new GridLayout(8, 8, 5, 5));
		p1Grid.setBackground(BigGUI.BACKGROUND);
		p2Grid.setBackground(BigGUI.BACKGROUND);
		
		// fill the grids with the appropriate colors
		initializeGrid(p1Grid, p1BlockGrid.getGrid());
		initializeGrid(p2Grid, p2BlockGrid.getGrid());
		
		// set the correct grid dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		gridDimensions = new Dimension((int) (screenSize.getHeight() * GRID_SCR_HEIGHT_RATIO),
				(int) (screenSize.getHeight() * GRID_SCR_HEIGHT_RATIO));
    	p1Grid.setPreferredSize(gridDimensions);
    	p2Grid.setPreferredSize(gridDimensions);
	}
	
	/** Initializes the help panel (instructions & buttons)
	 */
	private void initializeHelp()
	{
		// set instruction panel dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int vertBuffer = (int) (screenSize.getHeight() - gridDimensions.getHeight() 
				* (1 + BLOCK_BANK_GRID_H_PROP));
		int w = (int) ((screenSize.getWidth() - vertBuffer - (gridDimensions.getWidth() * 2)) / 2);
		helpDimensions = new Dimension(w, (int) gridDimensions.getHeight());
		
		// initialize help JPanels
		p1HelpPanel = new JPanel();
		p1HelpPanel.setLayout(new BoxLayout(p1HelpPanel, BoxLayout.Y_AXIS));
		p1HelpPanel.setBackground(BigGUI.BACKGROUND);
		p1HelpPanel.setPreferredSize(helpDimensions);
		
		p2HelpPanel = new JPanel();
		p2HelpPanel.setLayout(new BoxLayout(p2HelpPanel, BoxLayout.Y_AXIS));
		p2HelpPanel.setBackground(BigGUI.BACKGROUND);
		p2HelpPanel.setPreferredSize(helpDimensions);
		
		// initialize instructions
		initializeInstructions();
		
		// initialize confirm buttons
		initializeButtons();
		
		// add them to help panels, and help panels to game panel
		p1HelpPanel.add(p1Instructions);
		p1HelpPanel.add(p1Powerup);
		p1HelpPanel.add(p1Confirm);
		
		p2HelpPanel.add(p2Instructions);
		p2HelpPanel.add(p2Powerup);
		p2HelpPanel.add(p2Confirm);
	}
	
	/**	Initializes the instruction panels
	 */
	private void initializeInstructions()
	{
		// set instruction dimensions
		instructionDimensions = new Dimension((int) helpDimensions.getWidth(),
				(int) (helpDimensions.getHeight() * INSTR_HELP_H_PROP));
		
		// configure instructions panel 1
		p1Instructions = new JTextPane();
		p1Instructions.setEditable(false);
		p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + INSTRUCTIONS);
		p1Instructions.setBackground(BigGUI.BACKGROUND);
		p1Instructions.setPreferredSize(instructionDimensions);
		
		// configure instruction panel 2
		p2Instructions = new JTextPane();
		p2Instructions.setEditable(false);
		p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_WAIT);
		p2Instructions.setBackground(BigGUI.BACKGROUND);
		p2Instructions.setPreferredSize(instructionDimensions);
	}
	
	/**	Initializes the buttons
	 */
	private void initializeButtons()
	{
		// configure player 1
		p1Confirm = new JButton(CONFIRM);
		p1Confirm.setBackground(BigGUI.BACKGROUND);
		
		p1Powerup = new JButton(POWER_UP);
		p1Powerup.setBackground(BigGUI.BACKGROUND);
		p1Powerup.setVisible(false);
		
		// configure player 2
		p2Confirm = new JButton(CONFIRM);
		p2Confirm.setBackground(BigGUI.BACKGROUND);
		
		p2Powerup = new JButton(POWER_UP);
		p2Powerup.setBackground(BigGUI.BACKGROUND);
		p2Powerup.setVisible(false);
		
		p2Confirm.setVisible(false);
	}
	
	/**	Initializes the block banks
	 */
	private void initializeBlockBanks()
	{
		// set dimensions
		blockBankDimensions = new Dimension((int) gridDimensions.getWidth() + (int) helpDimensions.getWidth(),
				(int) (gridDimensions.getHeight() * BLOCK_BANK_GRID_H_PROP));
		
		// initialize p1 block bank
		initializeP1BlockBanks();
		
		// initialize p2 block bank
		initializeP2BlockBanks();
	}

	/**	Initializes Player 1's block bank
	 */
	private void initializeP1BlockBanks()
	{
		// create p1 block bank, box layout & configure
		p1BlockBank = new JPanel();
		BoxLayout p1BlockLayout = new BoxLayout(p1BlockBank, BoxLayout.X_AXIS);
		
		p1BlockBank.setLayout(p1BlockLayout);
		p1BlockBank.setBackground(BigGUI.BACKGROUND);
		p1BlockBank.setPreferredSize(blockBankDimensions);
		
		// create 3 JPanels that'll hold blocks, GridLayout
		p1Blocks = new JPanel[currentP1Blocks.length];
		for (int i = 0; i < p1Blocks.length; i++)
		{
			p1Blocks[i] = new JPanel(new GridLayout(5, 5, 3, 3));
			p1Blocks[i].setBackground(BigGUI.BACKGROUND);
		}
		
		// fill those panels with colors
		fillBankToMatch(p1Blocks, currentP1Blocks);
		
		// add JPanels to P1BlockBank
		for (int k = 0; k < p1Blocks.length; k++)
			p1BlockBank.add(p1Blocks[k]);
	}
	
	/** Initialize Player 2's block bank
	 */
	private void initializeP2BlockBanks()
	{
		// create p2 block bank, box layout & configure
		p2BlockBank = new JPanel();
		BoxLayout p2BlockLayout = new BoxLayout(p2BlockBank, BoxLayout.X_AXIS);
		
		p2BlockBank.setLayout(p2BlockLayout);
		p2BlockBank.setBackground(BigGUI.BACKGROUND);
		p2BlockBank.setPreferredSize(blockBankDimensions);
		
		// create 3 JPanels that'll hold blocks, GridLayout
		p2Blocks = new JPanel[currentP1Blocks.length];
		for (int i = 0; i < p2Blocks.length; i++)
		{
			p2Blocks[i] = new JPanel(new GridLayout(5, 5, 3, 3));
			p2Blocks[i].setBackground(BigGUI.BACKGROUND);
		}
		
		// fill those panels with colors
		fillBankToMatch(p2Blocks, currentP2Blocks);
		
		// add JPanels to P1BlockBank
		for (int k = 0; k < p2Blocks.length; k++)
			p2BlockBank.add(p2Blocks[k]);
	}
	
	/**	Given an array of JPanels with grid layout, fills them up to
	 * 		match the given Block[], which represents a block bank.
	 * 		This is a helper method to display the block banks.
	 *	@param gridPanels the array of JPanels in gridLayout
	 *	@param blockBank the block bank to match the JPanels to
	 */
	private static void fillBankToMatch(JPanel[] gridPanels, Block[] blockBank)
	{
		for (int panelI = 0; panelI < gridPanels.length; panelI++)
		{
			// if block spot is empty just fill spaces
			boolean empty = (blockBank[panelI] == null);
			
			// if not empty prep to represent block
			boolean[][] block = null;
			Color color = null;
			int nRows = 0, nCols = 0, startRow = 0, startCol = 0;
			
			if (!empty)
			{
				block = blockBank[panelI].getBlock();
				color = blockBank[panelI].getColor();
				nRows = block.length;
				nCols = block[0].length;
				startRow = (5 - nRows) / 2;
				startCol = (5 - nCols) / 2;
			}
			
			// fill the JPanels
			for (int r = 0; r < 5; r++)
			{
				for (int c = 0; c < 5; c++)
				{
					JPanel unit = new JPanel();
					if (!empty && r >= startRow && r < startRow + block.length
							&& c >= startCol && c < startCol + block[0].length)
					{
						System.out.println("startRow = " + startRow + " r = " + r);
						System.out.println("startCol = " + startCol + " c = " + c);
						unit.setBackground(block[r - startRow][c - startCol] ?
								color : BigGUI.BACKGROUND);
						gridPanels[panelI].add(unit);
					}
					else
					{
						unit.setBackground(BigGUI.BACKGROUND);
						gridPanels[panelI].add(unit);
					}
				}
			}
		}
	}
	
	
	/**	Initializes the given JPanel with grid layout to match given Color[][].
	 * 		This is a helper method to display the game grids.
	 *	@param gridLayout The Jpanel with a gridLayout
	 *	@param grid the Color[][] to match the JPanel to
	 */
	private static void initializeGrid(JPanel gridLayout, Color[][] grid)
	{
		for (Color[] row : grid)
			for (Color col : row)
			{
				JPanel block = new JPanel();
				block.setBackground(col == null ? DEFAULT_COLOR : col);
				gridLayout.add(block);
			}
	}
	
	
	/**	Updates the given JPanel with grid layout to match given Color[][].
	 * 		This is a helper method to display the game grids.
	 *	@param gridLayout The Jpanel with a gridLayout
	 *	@param grid the Color[][] to match the JPanel to
	 */
	private static void updateGrid(JPanel gridLayout, Color[][] grid)
	{
		int i = 0;
		Component[] gridUnits = gridLayout.getComponents();
		
		for (Color[] row : grid)
			for (Color col : row)
			{
				gridUnits[i].setBackground(col == null ? DEFAULT_COLOR : col);
				i++;
			}
		// repaint?
	}

	
	/**	Given an array of JPanels with grid layout, UPDATES them up to
	 * 		match the given Block[], which represents a block bank.
	 * 		This is a helper method to display the block banks.
	 *	@param gridPanels the array of JPanels in gridLayout
	 *	@param blockBank the block bank to match the JPanels to
	 */
	private static void updateBankToMatch(JPanel[] gridPanels, Block[] blockBank)
	{
		for (int panelI = 0; panelI < gridPanels.length; panelI++)
		{
			// if block spot is empty just fill spaces
			boolean empty = (blockBank[panelI] == null);
			
			// if not empty prep to represent block
			boolean[][] block = null;
			Color color = null;
			int nRows = 0, nCols = 0, startRow = 0, startCol = 0;
			
			if (!empty)
			{
				block = blockBank[panelI].getBlock();
				color = blockBank[panelI].getColor();
				nRows = block.length;
				nCols = block[0].length;
				startRow = (5 - nRows) / 2;
				startCol = (5 - nCols) / 2;
			}
			
			// update the JPanels
			Component[] unitPanels = gridPanels[panelI].getComponents();
			int i = 0;
			
			for (int r = 0; r < 5; r++)
			{
				for (int c = 0; c < 5; c++)
				{
					System.out.println("component i = " + i); // REMOVE
					Component unit = unitPanels[i];
					if (!empty && r >= startRow && r < startRow + block.length
							&& c >= startCol && c < startCol + block[0].length)
					{
						System.out.println("startRow = " + startRow + " r = " + r);
						System.out.println("startCol = " + startCol + " c = " + c);
						unit.setBackground(block[r - startRow][c - startCol] ?
								color : BigGUI.BACKGROUND);
						gridPanels[panelI].add(unit);
					}
					else
					{
						unit.setBackground(BigGUI.BACKGROUND);
						gridPanels[panelI].add(unit);
					}
					i++;
				}
			}
		}
	}
	
	// == RESPONDING TO BUTTON CLICKS ==
	
	/**	Respond to button clicks for Confirm and Powerup buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!manageConfirmClicks(e))
			managePowerups(e);
	}
	
	/**	Manage clicks of confirm buttons
	 *	@param e ActionEvent to manage
	 *	@return whether or not this actionEvent was a Confirm click
	 */
	private boolean manageConfirmClicks(ActionEvent e)
	{
		if (e.getSource() == p1Confirm)
		{
			onP1Confirm();
			return true;
		}
		else if (e.getSource() == p2Confirm)
		{
			onP2Confirm();
			return true;
		}
		else
			return false;
	}
	
	
	/**	Helper method for manageConfirmClicks. Dictates what to do
	 * 		if Player 1's confirm button is clicked.
	 */
	private void onP1Confirm()
	{
		// ensure it's their turn (it will be but good to check)
		// ensure a VALID block was selected
		// ensure a VALID space was selected
		if (p1Turn && selectedBlock > -1 && currentP1Blocks[selectedBlock] != null
				&& selectedRow > -1 && selectedCol > -1)
		{
			int linesCleared = resetAfterP1Turn();
			
			// end turn if conditions met
			endP1Turn();
			
			// CHANGING TURNS
			if (linesCleared > 0)
				prepareForP1Turn();
			else
				prepareForP2Turn();
		}
		else
		{
			// update player message to say you did bad >:(
			p1Instructions.setText(P1 + P1_MOVE + INSTRUCTIONS + BAD_CONFIRM);
		}	
	}
	
	/**	Helper method for manageConfirmClicks. Dictates what to do
	 * 		if Player 2's confirm button is clicked.
	 */
	private void onP2Confirm()
	{
		// ensure it's their turn (it will be but good to check)
		// ensure a VALID block was selected
		// ensure a VALID space was selected
		if (!p1Turn && selectedBlock > -1 && currentP2Blocks[selectedBlock] != null
				&& selectedRow > -1 && selectedCol > -1)
		{
			int linesCleared = resetAfterP2Turn();
			
			// end turn if conditions met
			endP2Turn();
			
			// CHANGING TURNS
			if (linesCleared > 0)
				prepareForP2Turn();
			else
				prepareForP1Turn();
		}
		else
		{
			// update player message to say you did bad >:(
			p2Instructions.setText(P2 + P2_MOVE + INSTRUCTIONS + BAD_CONFIRM);
		}	
	}
	
	/**	Manages clicks in the game grid and block bank
	 *	@param e ActionEvent to manage
	 */
	private void managePowerups(ActionEvent e)
	{
		if (e.getSource() == p1Powerup)
			if (undoActivated)
				manageUndoP1(e);
			else
				manageDestroyP1(e);
		else if (e.getSource() == p2Powerup)
			if (undoActivated)
				manageUndoP2(e);
			else
				manageDestroyP2(e);
	}
	
	/**	Helper method for managePowerups. Dictates what to do if
	 * 		Player 1's undo button is clicked.
	 * @param e ActionEvent to manage
	 */
	private void manageUndoP1(ActionEvent e)
	{
		// undo other grid
		int undo = (int) (Math.random() * (MAX_UNDO - MIN_UNDO + 1)) + MIN_UNDO;
		p2BlockGrid.undo(undo);
		updateGrid(p2Grid, p2BlockGrid.getGrid());
		
		// update other score
		p2Score -= UNDO_REDUCTION;
		p2Score = p2Score < 0 ? 0 : p2Score;
		p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_WAIT);
		
		// hide powerup button, show confirm button
		p1Powerup.setVisible(false);
		p1Confirm.setVisible(true);
		
		// set all powerups to false
		undoActivated = false;
		destroyActivated = false; // just in case
		
		// update message
		if (combo > 0)
			p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + 
					COMBO + INSTRUCTIONS);
		else
			p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + INSTRUCTIONS);
	}
	
	/**	Helper method for managePowerups. Dictates what to do if
	 * 		Player 1's destroy button is clicked.
	 * @param e ActionEvent to manage
	 */
	private void manageDestroyP1(ActionEvent e)
	{
		// ensure row and column are selected
		if (selectedRow > -1 && selectedCol > -1)
		{
			p1BlockGrid.destroy(selectedRow, selectedCol);
			updateGrid(p1Grid, p1BlockGrid.getGrid());
			
			// hide powerup button, show confirm button
			p1Powerup.setVisible(false);
			p1Confirm.setVisible(true);
			
			// increase score
			p1Score += DESTROY_INCREASE;
			
			// set all powerups to false
			undoActivated = false;
			destroyActivated = false; // just in case
			
			// update message
			if (combo > 0)
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + 
						COMBO + INSTRUCTIONS);
			else
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + INSTRUCTIONS);
		}
		else
			p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + SELECT_DESTROY);
	}
	
	/**	Helper method for managePowerups. Dictates what to do if
	 * 		Player 2's undo button is clicked.
	 * @param e ActionEvent to manage
	 */
	private void manageUndoP2(ActionEvent e)
	{
		// undo other grid
		int undo = (int) (Math.random() * (MAX_UNDO - MIN_UNDO + 1)) + MIN_UNDO;
		p1BlockGrid.undo(undo);
		updateGrid(p1Grid, p1BlockGrid.getGrid());
		
		// update other score
		p1Score -= UNDO_REDUCTION;
		p1Score = p1Score < 0 ? 0 : p1Score;
		p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_WAIT);
		
		// hide powerup button, show confirm button
		p2Powerup.setVisible(false);
		p2Confirm.setVisible(true);
		
		// set all powerups to false
		undoActivated = false;
		destroyActivated = false; // just in case
		
		// update message
		if (combo > 0)
			p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + 
					COMBO + INSTRUCTIONS);
		else
			p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + INSTRUCTIONS);
	}
	
	/**	Helper method for managePowerups. Dictates what to do if
	 * 		Player 2's destroy button is clicked.
	 * @param e ActionEvent to manage
	 */
	private void manageDestroyP2(ActionEvent e)
	{
		// ensure row and column are selected
		if (selectedRow > -1 && selectedCol > -1)
		{
			p2BlockGrid.destroy(selectedRow, selectedCol);
			updateGrid(p2Grid, p2BlockGrid.getGrid());
			
			// hide powerup button, show confirm button
			p2Powerup.setVisible(false);
			p2Confirm.setVisible(true);
			
			// increase score
			p2Score += DESTROY_INCREASE;
			
			// set all powerups to false
			undoActivated = false;
			destroyActivated = false; // just in case
			
			// update message
			if (combo > 0)
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + 
						COMBO + INSTRUCTIONS);
			else
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + INSTRUCTIONS);
		}
		else
			p1Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + SELECT_DESTROY);
	}
	
	/**	Helper method for onP1Confirm. Updates the grid and score for
	 * 		Player 1, and resets any highlighting and selections
	 */
	private int resetAfterP1Turn()
	{
		// add block to space - might be redundant
		p1BlockGrid.addToGrid(currentP1Blocks[selectedBlock], selectedRow, 
				selectedCol);
		
		// clear lines & count how many
		int linesCleared = p1BlockGrid.clearLines();
		
		// Block bank 1: remove block from block bank
		currentP1Blocks[selectedBlock] = null;
		
		// Block bank 2: if block bank is empty, generate new bank
		if (currentP1Blocks[0] == null && currentP1Blocks[1] == null
				&& currentP1Blocks[2] == null)
		currentP1Blocks = p1BlockGrid.regenerateBlockBank();
		
		// Block bank 3: update block bank GUI
		updateBankToMatch(p1Blocks, currentP1Blocks);
		
		// turn off selected border on block just added
		p1Blocks[selectedBlock].setBorder(NORMAL_BORDER); // NORMAL
		
		// Now that grid is updated, update grid GUI
		updateGrid(p1Grid, p1BlockGrid.getGrid());
		
		// CALCULATE SCORE ***
		p1Score += linesCleared * LINE_CLEAR;
		p1Score += BASE;
		
		// deselect all blocks
		selectedBlock = selectedRow = selectedCol = -1;
		
		return linesCleared;
	}
	
	/**	Helper method for onP2Confirm. Updates the grid and score for
	 * 		Player 2, and resets any highlighting and selections
	 */
	private int resetAfterP2Turn()
	{
		// add block to space - might be redundant
		p2BlockGrid.addToGrid(currentP2Blocks[selectedBlock], selectedRow, 
				selectedCol);
		
		// clear lines & count how many
		int linesCleared = p2BlockGrid.clearLines();
		
		// Block bank 1: remove block from block bank
		currentP2Blocks[selectedBlock] = null;
		
		// Block bank 2: if block bank is empty, generate new bank
		if (currentP2Blocks[0] == null && currentP2Blocks[1] == null
				&& currentP2Blocks[2] == null)
		currentP2Blocks = p2BlockGrid.regenerateBlockBank();
		
		// Block bank 3: update block bank GUI
		updateBankToMatch(p2Blocks, currentP2Blocks);
		
		// turn off selected border on block just added
		p2Blocks[selectedBlock].setBorder(NORMAL_BORDER); // NORMAL
		
		// Now that grid is updated, update grid GUI
		updateGrid(p2Grid, p2BlockGrid.getGrid());
		
		// CALCULATE SCORE ***
		p2Score += linesCleared * LINE_CLEAR;
		p2Score += BASE;
		
		// deselect all blocks
		selectedBlock = selectedRow = selectedCol = -1;
		
		return linesCleared;
	}
	
	/**	Helper method for onP1Confirm and onP2Confirm. Prepares the text
	 * 		and core gameplay tracking variables in preparation for 
	 * 		Player 1's turn.
	 */
	private void prepareForP1Turn()
	{
		// set it to player 1's turn; track whether this is a repeat turn
		boolean p1Earlier = p1Turn;
		p1Turn = true;
		
		// turn ON p1 Confirm; turn OFF p2 Confirm
		p1Confirm.setVisible(true);
		p2Confirm.setVisible(false);
		
		// determine possible powerup
		if (p1Earlier)
			combo++;
		else
			combo = 0;
		
		// update other player's message
		p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_WAIT);
		
		// determine powerups
		boolean powerup = Math.random() < (POWERUP_PROB + combo * COMBO_POWERUP_INC);
		if (powerup)
		{
			undoActivated = p1BlockGrid.getPrevMovesSize() >= 3 && Math.random() < .50;
			destroyActivated = !undoActivated;
			
			if (undoActivated)
			{
				// hide confirm, show powerup, show instructions
				p1Confirm.setVisible(false);
				p1Powerup.setVisible(true);
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + 
					UNDO_POWERUP + INSTRUCTIONS);
			}
			else // destroyActivated
			{
				// hide confirm, show powerup, show instructions
				p1Confirm.setVisible(false);
				p1Powerup.setVisible(true);
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + 
					DESTROY_POWERUP + INSTRUCTIONS);
			}
		}
		else
		{
			if (p1Earlier)
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + 
						COMBO + INSTRUCTIONS);
			else
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_MOVE + INSTRUCTIONS);
		}
	}
	
	/**	Helper method for onP1Confirm and onP2Confirm. Prepares the text
	 * 		and core gameplay tracking variables in preparation for 
	 * 		Player 1's turn.
	 */
	private void prepareForP2Turn()
	{
		// set it to player 1's turn; track whether this is a repeat turn
		boolean p2Earlier = !p1Turn;
		p1Turn = false;
		
		// turn OFF p1 Confirm; turn ON p2 Confirm
		p2Confirm.setVisible(true);
		p1Confirm.setVisible(false);
		
		// determine possible powerup
		if (p2Earlier)
			combo++;
		else
			combo = 0;
		
		// update other player's message
		p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_WAIT);
		
		// determine powerups
		boolean powerup = Math.random() < (POWERUP_PROB + combo * COMBO_POWERUP_INC);
		if (powerup)
		{
			undoActivated = p2BlockGrid.getPrevMovesSize() >= 3 && Math.random() < .50;
			destroyActivated = !undoActivated;
			
			if (undoActivated)
			{
				// hide confirm, show powerup, show instructions
				p2Confirm.setVisible(false);
				p2Powerup.setVisible(true);
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + 
					UNDO_POWERUP + INSTRUCTIONS);
			}
			else // destroyActivated
			{
				// hide confirm, show powerup, show instructions
				p2Confirm.setVisible(false);
				p2Powerup.setVisible(true);
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + 
					DESTROY_POWERUP + INSTRUCTIONS);
			}
		}
		else
		{
			if (p2Earlier)
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + 
						COMBO + INSTRUCTIONS);
			else
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_MOVE + INSTRUCTIONS);
		}
	}
	
	/**	Helper method for onP1Confirm. Checks if the game
	 * 		needs to end and ends it.
	 */
	private void endP1Turn()
	{
		//	If P1's turn resulted in a score >= 5000 say that they won
		if(p1Score >= WIN_SCORE)
		{
			p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_WIN + SCORE_WIN);
			p2Instructions.setText(P2 + P1_WIN + SCORE_LOSS);
			//and disable game board/block bank
			disableBoard();
		}
		
		//  If P1's turn resulted in being unable to add more, say they lost
		if(!spaceLeft(currentP1Blocks, p1BlockGrid))
		{
			p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + NO_SPACE + P2_WIN);
			p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_WIN);
			//and disable game board/block bank
			disableBoard();
		}	
	}
	
	/**	Helper method for onP2Confirm. Checks if the game
	 * 		needs to end and ends it.
	 */
	private void endP2Turn()
	{
		//	If P1's turn resulted in a score >= 5000 say that they won
		if(p2Score >= WIN_SCORE)
		{
			p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + P2_WIN + SCORE_WIN);
			p1Instructions.setText(P1 + P2_WIN + SCORE_LOSS);
			//and disable game board/block bank
			disableBoard();
		}
		
		//  If P1's turn resulted in being unable to add more, say they lost
		if(!spaceLeft(currentP2Blocks, p2BlockGrid))
		{
			p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n" + NO_SPACE + P1_WIN);
			p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n" + P1_WIN);
			//and disable game board/block bank
			disableBoard();
		}	
	}
	
	/**	Helper method for endP1Turn and endP2Turn. Disables
	 * 		the game board when the game is over.
	 */
	private void disableBoard()
	{
		p1BlockBank.setVisible(false);
		p2BlockBank.setVisible(false);
		p1Confirm.setVisible(false);
		p2Confirm.setVisible(false);
	}
	
	// == RESPONDING TO USER INTERACTION ==
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!manageBlockBankClicks(e));
			manageGridClicks(e);
	}
	
	/**	Manages block bank clicks
	 *	@param e The MouseEvent for which to check if it came
	 *		from a block bank, and to then respond to
	 *	@return true if e's source was a block bank
	 */
	private boolean manageBlockBankClicks(MouseEvent e)
	{
		// one of the p1 block bank was selected
		for (int i = 0; i < p1Blocks.length; i++)
		{
			if (e.getSource() == p1Blocks[i])
			{
				onP1BlockBankClick(i);
				return true;
			}
		}
		
		// one of the p2 block bank was selected
		for (int i = 0; i < p2Blocks.length; i++)
		{
			if (e.getSource() == p2Blocks[i])
			{
				onP2BlockBankClick(i);
				return true;
			}
		}
		return false;
	}
	
	/**	Helper method for manageBlockBankClicks. dictates what to do
	 * 		when a click is detected in Player 1's block bank.
	 * @param i which block in the block bank was clicked
	 */
	private void onP1BlockBankClick(int i)
	{
		// ensure that it's their turn
		// change selected block to this index
		// highlight it's Jpanel in yellow border
		if (p1Turn && currentP1Blocks[i] != null)
		{
			// if another block is alr selected, remove it from grid
			if (selectedRow > -1 && selectedCol > -1)
			{
				if (selectedBlock != i)
				{
					// remove previous block from grid
					p1BlockGrid.remove(currentP1Blocks[selectedBlock], selectedRow,
							selectedCol);
					updateGrid(p1Grid, p1BlockGrid.getGrid());
					
					p1Blocks[selectedBlock].setBorder(NORMAL_BORDER); // NORMAL
					
					// update highlights
					selectedBlock = i;
					p1Blocks[selectedBlock].setBorder(HIGHLIGHT_BORDER);
					selectedRow = selectedCol = -1;
				}
			}
			else if (selectedBlock > -1  && selectedBlock != i) // another block sel, not on grid
			{
				p1Blocks[selectedBlock].setBorder(NORMAL_BORDER); // BACK TO NORMAL
				selectedBlock = i;
				p1Blocks[selectedBlock].setBorder(HIGHLIGHT_BORDER); // HIGHLIGHT
			}
			else if (selectedBlock != i)
			{
				selectedBlock = i;
				p1Blocks[i].setBorder(HIGHLIGHT_BORDER); // HIGHLIGHT
			}
		}
	}
	
	/**	Helper method for manageBlockBankClicks. dictates what to do
	 * 		when a click is detected in Player 2's block bank.
	 * @param i which block in the block bank was clicked
	 */
	private void onP2BlockBankClick(int i)
	{
		// ensure that it's their turn
		// change selected block to this index
		// highlight it's Jpanel in yellow border
		if (!p1Turn && currentP2Blocks[i] != null)
		{
			// if another block is alr selected, remove it from grid
			if (selectedRow > -1 && selectedCol > -1)
			{
				if (selectedBlock != i)
				{
					// remove previous block from grid
					p2BlockGrid.remove(currentP2Blocks[selectedBlock], selectedRow,
							selectedCol);
					updateGrid(p2Grid, p2BlockGrid.getGrid());
					
					p2Blocks[selectedBlock].setBorder(NORMAL_BORDER); // NORMAL
					
					// update highlights
					selectedBlock = i;
					p2Blocks[selectedBlock].setBorder(HIGHLIGHT_BORDER);
					selectedRow = selectedCol = -1;
				}
			}
			else if (selectedBlock > -1  && selectedBlock != i) // another block sel, not on grid
			{
				p2Blocks[selectedBlock].setBorder(NORMAL_BORDER); // BACK TO NORMAL
				selectedBlock = i;
				p2Blocks[selectedBlock].setBorder(HIGHLIGHT_BORDER); // HIGHLIGHT
			}
			else if (selectedBlock != i)
			{
				selectedBlock = i;
				p2Blocks[i].setBorder(HIGHLIGHT_BORDER); // HIGHLIGHT
			}
		}
	}
	
	/**	Manages grid clicks
	 *	@param e The MouseEvent for which to respond to
	 */
	private void manageGridClicks(MouseEvent e)
	{
		if (e.getSource() == p1Grid)
		{
			onP1GridCLick(e);
		}
		else if (e.getSource() == p2Grid)
		{
			onP2GridClick(e);
		}
	}
	
	/**	Helper method for manageGridClicks. Dictates what to do
	 * 		when a click is detected in Player 1's grid.
	 * @param e The MouseEvent to respond to
	 */
	private void onP1GridCLick(MouseEvent e)
	{
		// ensure a valid block is selected
		// ensure that it's p1 turn
		if (selectedBlock > -1 && p1Turn)
		{
			// identify the row and col
			int[] rowAndCol = getRowCol(e.getPoint(), p1Grid);
			
			// if os couldn't find where mouse click was, do nothing
			if (rowAndCol == null)
				return;
			
			// if you've placed this block in another spot temporarily, remove it
			if (selectedRow > -1 && selectedCol > -1)
				p1BlockGrid.remove(currentP1Blocks[selectedBlock], 
						selectedRow, selectedCol);
			
			// see if possible to add
			if (p1BlockGrid.canAdd(currentP1Blocks[selectedBlock], 
					rowAndCol[0], rowAndCol[1]))
			{
				// set new row & col, add to grid
				selectedRow = rowAndCol[0];
				selectedCol = rowAndCol[1];
				p1BlockGrid.addToGrid(currentP1Blocks[selectedBlock], 
						selectedRow, selectedCol);
				
				// update grid & instructions
				updateGrid(p1Grid, p1BlockGrid.getGrid());
				p1Instructions.setText(P1 + SCORE_STR + p1Score + "\n\n"
						+ P1_MOVE + INSTRUCTIONS);
			}
			else // clicked on a bad spot -- error message
				p1Instructions.setText(P1+ SCORE_STR + p1Score + "\n\n"
						+ P1_MOVE + INSTRUCTIONS + BAD_SPOT_SELECTED);
		}
	}
	
	/**	Helper method for manageGridClicks. Dictates what to do
	 * 		when a click is detected in Player 2's grid.
	 * @param e The MouseEvent to respond to
	 */
	private void onP2GridClick(MouseEvent e)
	{
		// ensure a valid block is selected
		// ensure that it's p1 turn
		if (selectedBlock > -1 && !p1Turn)
		{
			// identify the row and col
			int[] rowAndCol = getRowCol(e.getPoint(), p2Grid);
			
			// if os couldn't find where mouse click was, do nothing
			if (rowAndCol == null)
				return;
			
			// if you've placed this block in another spot temporarily, remove it
			if (selectedRow > -1 && selectedCol > -1)
				p2BlockGrid.remove(currentP2Blocks[selectedBlock], 
						selectedRow, selectedCol);
			
			// see if possible to add
			if (p2BlockGrid.canAdd(currentP2Blocks[selectedBlock], 
					rowAndCol[0], rowAndCol[1]))
			{
				// set new row & col, add to grid
				selectedRow = rowAndCol[0];
				selectedCol = rowAndCol[1];
				p2BlockGrid.addToGrid(currentP2Blocks[selectedBlock], 
						selectedRow, selectedCol);
				
				// update grid & instructions
				updateGrid(p2Grid, p2BlockGrid.getGrid());
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n"
						+ P2_MOVE + INSTRUCTIONS);
			}
			else // clicked on a bad spot -- error message
				p2Instructions.setText(P2 + SCORE_STR + p2Score + "\n\n"
						+ P2_MOVE + INSTRUCTIONS + BAD_SPOT_SELECTED);
		}
	}
	
	/**	Helper method to detect where in the grid a grid click came
	 * 		from.
	 * @param mouseClick the Point where the mouse was clicked
	 * @param gridLayout the grid that was clicked
	 * @return an int[] of length 2 with int at 0 being the row, 
	 * 		and int at 1 being the column
	 */
	private int[] getRowCol(Point mouseClick, JPanel gridLayout)
	{
		Component key = gridLayout.getComponentAt(mouseClick);
		int r = 0, c = 0;
		
		for (Component comp : gridLayout.getComponents())
		{
			if (comp.equals(key))
				return new int[]{r, c};
			else
			{
				r = c >= BlockGrid.MAX_ROWS_AND_COLS - 1 ? r + 1 : r;
				c = c >= BlockGrid.MAX_ROWS_AND_COLS - 1 ? 0 : c + 1;
			}
		}
		
		System.out.println("Component not found.");
		return null;
	}

	/**	Checks if it's possible to add blocks from a given bank onto a player's grid
	 *	@return true if a block can be placed
	 */
	private static boolean spaceLeft(Block[] currBlocks, BlockGrid playerGrid)
	{
		for(int i = 0; i < currBlocks.length; i++)
		{
			if(currBlocks[i] != null && !playerGrid.isSpaceInGrid(currBlocks[i]))
				return false;
		}
		return true;
	}

	// MouseListener methods -- don't need to implement
	
	/**	Dictates what to do on mouse press (nothing)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**	Dictates what to do on mouse release (nothing)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**	Dictates what to do on mouse enter component (nothing)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**	Dictates what to do on mouse exit component (nothing)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}