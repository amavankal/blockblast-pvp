/**
 *	GamePanel.java - holds the 2 GameBoards, title text panes (score displayers),
 *		and control panel
 * 
 * 	@author Asha Mavankal
 */


package blockblast.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import blockblast.gui.gamepanel.*;

public class GamePanel extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = 4699179019589248253L;
	
	// color constants
	public static final Color BACKGROUND = new Color(71, 57, 197, 180);
	public static final Color BORDER = new Color(46, 20, 148);
	
	// component size constants v2
	private static final double UNIT_SQUARE = 1;
	private static final double BUFFER_SIZE = UNIT_SQUARE * .4;
	
	private static final double BOARD_SIZE = UNIT_SQUARE * 8;
	private static final double BANK_WIDTH = BOARD_SIZE;
	private static final double BANK_HEIGHT = BANK_WIDTH * .50;
	private static final double TITLE_WIDTH = BOARD_SIZE;
	private static final double TITLE_HEIGHT = UNIT_SQUARE * .9;
	
	private static final double PLAYER_AREA_HEIGHT = TITLE_HEIGHT
			+ BUFFER_SIZE
			+ BOARD_SIZE
			+ BUFFER_SIZE
			+ BANK_HEIGHT;
	private static final double FRAME_HEIGHT = BUFFER_SIZE
			+ TITLE_HEIGHT
			+ BUFFER_SIZE
			+ BOARD_SIZE
			+ BUFFER_SIZE
			+ BANK_HEIGHT
			+ BUFFER_SIZE;
	
	private static final double MIN_CONTROL_WIDTH = BOARD_SIZE * .5;
	private static final double MIN_FRAME_WIDTH = BUFFER_SIZE
			+ BOARD_SIZE
			+ BUFFER_SIZE
			+ MIN_CONTROL_WIDTH
			+ BUFFER_SIZE
			+ BOARD_SIZE
			+ BUFFER_SIZE;
	
	private double controlWidth;
	
	private static final int NUM_PLAYERS = 2;
	private static final int P1 = 0;
	private static final int P2 = 1;
	
	// container instance variables
	private JPanel[] pAreas; // 2, 4
	private JPanel controlArea; // 3
	private JPanel topBuffer; // for maximized
	private JPanel bottomBuffer; // for maximized
	
	private JPanel[] titleContainers; // 7
	private JPanel[] gameBoardContainers; // NEW
	
	private TitleTextPane[] titleTexts;
	private GameBoard[] gameBoards; // NEW
	private ControlPanel controlPanel; // 8
	
	// size multiplier
	private double mult;
	private Dimension buffer;
	private Dimension titleDim;
	private Dimension gameBoardDim;
	private Dimension controlDim;
	private Dimension topBottomBufferDim;
	
	// FIXME game instance variables
	private int winScore;
			
	public GamePanel(JFrame parent)
	{
		initializeGUI(parent);
	}
	
	private void initializeGUI(JFrame parent)
	{
		// FIXME initialize game variables
		winScore = GameBoard.DEFAULT_WIN_SCORE;
		
		// FIXME 0: set JFrame to maximized initially
		parent.setResizable(true);
		parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
		parent.setVisible(true);
		parent.setResizable(false);
		
		// 1: identify sizeMultiplier value
		mult = calcSizeMultiplier(parent);
		System.out.println(mult);
		
		// 1.5 fill dimensions
		fillComponentDimensions();
		
		// 2: initialize overall frame - horiz BoxLayout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		// 3: initialize player areas
		pAreas = new JPanel[NUM_PLAYERS];
		titleContainers = new JPanel[NUM_PLAYERS];
		gameBoardContainers = new JPanel[NUM_PLAYERS];
		titleTexts = new TitleTextPane[NUM_PLAYERS];
		gameBoards = new GameBoard[NUM_PLAYERS];
		
		initializePlayerArea(P1);
		initializePlayerArea(P2);
		
		gameBoards[P1].setPartner(gameBoards[P2]);
		gameBoards[P2].setPartner(gameBoards[P1]);
		
		// 4: initialize dialogue area
		controlArea = new JPanel();
		controlArea.setLayout(new BoxLayout(controlArea, BoxLayout.Y_AXIS));
		
		controlPanel = new ControlPanel(this);
		
		controlArea.add(Box.createRigidArea(buffer));
		controlArea.add(controlPanel);
		controlArea.add(Box.createRigidArea(buffer));
		
		controlArea.setOpaque(false);
		
		// initialize top & bottom buffer
		topBuffer = new JPanel();
		bottomBuffer = new JPanel();
		topBuffer.setOpaque(false);
		bottomBuffer.setOpaque(false);
		
		// add areas to this container
		add(topBuffer);
		add(pAreas[P1]);
		add(controlArea);
		add(pAreas[P2]);
		add(bottomBuffer);
		
		// set everyone's dimensions
		applyComponentDimensions();
		
		// make p2's gamebaord inactivate 
		// TODO - randomize who goes first 
		gameBoards[P2].setActive(false);
	}

	private void initializePlayerArea(int p)
	{
		// set the area to vertical BoxLayout
		pAreas[p] = new JPanel();
		pAreas[p].setLayout(new BoxLayout(pAreas[p], BoxLayout.Y_AXIS));
		pAreas[p].setOpaque(false); // set transparent
		
		// initialize containers that go inside the area
		titleContainers[p] = new JPanel();
		titleContainers[p].setLayout(new BoxLayout(titleContainers[p],
				BoxLayout.X_AXIS));
		titleTexts[p] = new TitleTextPane();
		
		titleContainers[p].add(Box.createRigidArea(buffer));
		titleContainers[p].add(titleTexts[p]);
		titleContainers[p].add(Box.createRigidArea(buffer));
		
		titleContainers[p].setOpaque(false); // set transparent
		
		// gameBoard
		gameBoardContainers[p] = new JPanel();
		gameBoardContainers[p].setLayout(new BoxLayout(gameBoardContainers[p],
				BoxLayout.X_AXIS));
		
		gameBoards[p] = new GameBoard(buffer, titleTexts[p]);
		
		gameBoardContainers[p].add(Box.createRigidArea(buffer));
		gameBoardContainers[p].add(gameBoards[p]);
		gameBoardContainers[p].add(Box.createRigidArea(buffer));
		
		gameBoardContainers[p].setOpaque(false); // set transparent
		
		// add to player area
		pAreas[p].add(Box.createRigidArea(buffer));
		pAreas[p].add(titleContainers[p]);
		pAreas[p].add(Box.createRigidArea(buffer));
		pAreas[p].add(gameBoardContainers[p]);
		pAreas[p].add(Box.createRigidArea(buffer));
	}
	
	public int getWinScore()
	{
		return winScore;
	}
	
	public Dimension getBuffer()
	{
		return buffer;
	}
	
	public Dimension getControlPanelSize()
	{
		return controlDim;
	}
	
	public ControlPanel getControlPanel()
	{
		return controlPanel;
	}
	
	private double calcSizeMultiplier(JFrame frame)
	{
		Dimension frameSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets frameInsets = frame.getInsets();
		Dimension screenSize = new Dimension(
				(int) (frameSize.getWidth() - frameInsets.right - frameInsets.left),
				(int) (frameSize.getHeight() - frameInsets.top - frameInsets.bottom));
		
		System.out.println("frame: " + screenSize);
		System.out.println("screen size: " + Toolkit.getDefaultToolkit().getScreenSize());
		
		double screenW = screenSize.getWidth();
		double screenH = screenSize.getHeight(); // pretend shorter screen
		
		double screenProp = screenH / screenW;
		double frameProp = FRAME_HEIGHT / MIN_FRAME_WIDTH;
		
		if (screenProp == frameProp) // perfect
		{
			controlWidth = MIN_CONTROL_WIDTH;
			return screenH / FRAME_HEIGHT;
		}
		else if (screenProp > frameProp) // tall screen
		{
			controlWidth = MIN_CONTROL_WIDTH;
			return screenW / MIN_FRAME_WIDTH;
		}
		else  // screenProp < frameProp wide screen
		{
			controlWidth = ((screenW - (MIN_FRAME_WIDTH * (screenH / FRAME_HEIGHT)))
					/ (screenH / FRAME_HEIGHT)) + MIN_CONTROL_WIDTH;
			System.out.println("control width: " + controlWidth + " min width: " + MIN_CONTROL_WIDTH);
			return screenH / FRAME_HEIGHT;
		}
	}
	
	private void fillComponentDimensions()
	{
		// initialize dimensions that don't change
		buffer = new Dimension((int) (BUFFER_SIZE * mult), (int) (BUFFER_SIZE * mult));
		titleDim = new Dimension((int) (TITLE_WIDTH * mult), (int) (TITLE_HEIGHT * mult));
		gameBoardDim = new Dimension((int) (BOARD_SIZE * mult), 
				(int) ((BOARD_SIZE + BANK_HEIGHT) * mult));
		
		// TODO
		controlDim = new Dimension((int) (controlWidth * mult),
				(int) (PLAYER_AREA_HEIGHT * mult));
		
		
		topBottomBufferDim = new Dimension(0, 0);
	}
	
	/**
	 * prerequisite: fillComponentDimensions has been called
	 */
	private void applyComponentDimensions()
	{
		// apply dimensions
		titleTexts[P1].setPreferredSize(titleDim);
		titleTexts[P2].setPreferredSize(titleDim);
		
		gameBoards[P1].setPreferredSize(gameBoardDim);
		gameBoards[P2].setPreferredSize(gameBoardDim);
		
		controlPanel.setPreferredSize(controlDim);
		
		topBuffer.setPreferredSize(topBottomBufferDim);
		bottomBuffer.setPreferredSize(topBottomBufferDim);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);
		
		BufferedImage background;
		
		// FIXME: make platform independent
		try
		{
			setOpaque(false);
			InputStream backgroundStream = getClass()
				.getResourceAsStream("/blockblast/images/background2.png");
			background = ImageIO.read(backgroundStream);

			g2.drawImage(background, 0, 0, getWidth(), getHeight(), 0, 0,
					background.getWidth(), background.getHeight(), null);
		}
		catch (IOException io)
		{
			System.out.println(io.getMessage() + " Unable to draw Game panel background");
			
			// just set BG to blue if input file couldn't be read
			setOpaque(true);
			setBackground(BigGUI.BACKGROUND);
		}
	}

}