/**
 *	DialogueTextPane.java
 * 
 * 
 */


package blockblast.gui.gamepanel;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import blockblast.gui.GamePanel;
import blockblast.gui.gamepanel.control.*;

public class ControlPanel extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = -75774742473409027L;
	
	public static final int WIN_MESSAGE_COMPONENT = 0;
	public static final int HOME_COMPONENT = 1;
	public static final int INFO_COMPONENT = 2;
	public static final int HINTS_COMPONENT = 3;
	
	// gui instance variables
	private BoxLayout layout;
	
	private WinMessageComponent winInstructions;
	private HomeComponent homeButton;
	private InfoComponent informationButton;
	private HintsComponent hints;
	
	private Dimension winDim;
	private Dimension homeDim;
	private Dimension infoDim;
	private Dimension hintsDim;
	
	// associated GamePanle
	private GamePanel associatedPanel;
	
	public ControlPanel(GamePanel associate)
	{
		// set layout
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		// set opacity
		setOpaque(false);
		
		// set associatedPanel
		associatedPanel = associate;
		
		// calc dimensions
		calcDimensions(associatedPanel.getControlPanelSize());
		
		// initialize the components
		winInstructions = new WinMessageComponent(associatedPanel, this);
		homeButton = new HomeComponent();
		informationButton = new InfoComponent();
		hints = new HintsComponent(this);
		
		// set dimensions
		winInstructions.setPreferredSize(winDim);
		homeButton.setPreferredSize(homeDim);
		informationButton.setPreferredSize(infoDim);
		hints.setPreferredSize(hintsDim);
		
		// add components to panel
		add(winInstructions);
		add(Box.createRigidArea(associatedPanel.getBuffer()));
		add(homeButton);
		add(Box.createRigidArea(associatedPanel.getBuffer()));
		add(informationButton);
		add(Box.createRigidArea(associatedPanel.getBuffer()));
		add(hints);
	}
	
	private void calcDimensions(Dimension panelSize)
	{
		int height = (int) ((panelSize.getHeight()
				- associatedPanel.getBuffer().getHeight() * 3) / 4);
		int width = (int) panelSize.getWidth();
		
		winDim = new Dimension(width, height);
		homeDim = new Dimension(width, height);
		infoDim = new Dimension(width, height);
		hintsDim = new Dimension(width, height);
	}
	
	public Dimension getChildSize(int child)
	{
		Dimension result = null;
		
		switch (child)
		{
			case WIN_MESSAGE_COMPONENT: result = winDim;
			break;
			case HOME_COMPONENT: result = homeDim;
			break;
			case INFO_COMPONENT: result = infoDim;
			break;
			case HINTS_COMPONENT: result = hintsDim;
			break;
		}
		
		return result;
	}
}