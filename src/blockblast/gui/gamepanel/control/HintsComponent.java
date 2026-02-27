/**
 *	HintsComponent.java
 *
 */


package blockblast.gui.gamepanel.control;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.border.LineBorder;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import blockblast.gui.BigGUI;
import blockblast.gui.GamePanel;
import blockblast.gui.gamepanel.ControlPanel;

import javax.swing.JPanel;

public class HintsComponent extends JPanel implements ActionListener
{
	// serial version uid
	private static final long serialVersionUID = 7793618607893387568L;
	
	// message constants
	public static final String HINT_STR = "HINT: \n";
	public static final String[] HINTS = {
			"Clearing a line allows you to start a streak!",
			"The longer your streak, the more bonus points you get each turn!",
			"Each line cleared is worth 400 points!",
			"Clearing horizontal and vertical lines at the same time gets you a bonus!",
			"Be aware of all the blocks in your block bank so you don't run out of space",
			"If you clear the board, you get 200 bonus points!"
	};

	public static final Color TEXT_COLOR = Color.WHITE;
	public static final Font FONT = BigGUI.PLAIN_FONT;
	
	public static final float FONT_HEIGHT_PROP = .14f;
	private static final float PADDING_PROP = .15f;
	private static final float HINT_START_PROP = .12f;
	
	private static final int WAIT = 3000;
	
	// instance variable - track which message is displayed
	private int currMsg;
	private Timer timer;
	
	public HintsComponent(ControlPanel control)
	{
		// set background & border
		setOpaque(false);
		setBorder(new LineBorder(GamePanel.BORDER, 4)); // TODO magic number
		
		// set current message index
		currMsg = (int) (Math.random() * HINTS.length);
		repaint();

		// start timer for slideshow
		timer = new Timer(WAIT, this);
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// every time the timer hits, 
		// increase currMsg, and update message
		currMsg = currMsg >= HINTS.length - 1 ? 0 : currMsg + 1;
		repaint();
	}
	
	/** Renders the background and hint text on the component
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// draw background
		g2.setColor(GamePanel.BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));

		// set up g2 to draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(TEXT_COLOR);
		g2.setFont(FONT.deriveFont((float) (getHeight() * FONT_HEIGHT_PROP)));
		
		// store text and delimit it by newlines
		String text = HINT_STR + HINTS[currMsg];
		String[] paragraphs = text.split("\n");

		// generate the TextLayouts for the given text
		ArrayList<TextLayout> layouts = new ArrayList<TextLayout>(10);
		float totalHeight = generateTextLayouts(layouts, paragraphs, g2);

		// paint the layouts, centering them and keeping the position of HINTS consistent
		paintLayouts(layouts, totalHeight, g2);
	}

	/**	Paints the text in alyouts given by layouts, painting the first layout at a fixed
	 * 		position at the top, and the rest of the layouts centered vertically in the 
	 * 		remaining space. All text is rendered in the center of the screen.
	 * 
	 * @param layouts array of layouts to render
	 * @param totalHeight the total height of all the layouts, stacked one on top of each other
	 * @param g2 the graphics2D object to draw with
	 */
	private void paintLayouts(ArrayList<TextLayout> layouts, float totalHeight, Graphics2D g2) {
		// draw hint @ top
		TextLayout hintLayout = layouts.getFirst();

		float marginTop = getHeight() * HINT_START_PROP;
		float hintX = (getWidth() - hintLayout.getAdvance()) / 2;
		float hintY = marginTop + hintLayout.getAscent(); // go to baseline

		hintLayout.draw(g2, hintX, hintY);

		hintY += hintLayout.getDescent() + hintLayout.getLeading(); // hintY is now at bottom of text

		// subtract hint height from totalHeight & remove from layouts
		layouts.removeFirst();

		// draw rest in remaining space
		float startY = (getHeight() - hintY - totalHeight) / 2 + hintY;
		for (TextLayout layout : layouts) {
			float startX = (getWidth() - layout.getAdvance()) / 2; // center X
			startY += layout.getAscent(); // go to baseline
			layout.draw(g2, startX, startY);

			// increment startY to next line
			startY += layout.getDescent() + layout.getLeading();
		}
	}

	/**	Helper method for paintComponent. Fills layouts with the layouts for the given paragraphs
	 * 		using the screen's width and padding given by PADDING_PROP. Returns the height of all
	 * 		the lines represented by the layouts.
	 * 
	 *	@param layouts array of layouts to add to
	 *	@param paragraphs array of strings to render. Each paragraph is guaranteed to be in a separate
	 *		layout
	 *	@param g2 the graphics2D object that has the font
	 *	@return the height of all lines represented in the layouts
	 */
	private float generateTextLayouts(ArrayList<TextLayout> layouts, String[] paragraphs, Graphics2D g2) {
		// store total height of all texts
		float totalHeight = 0;
		int width = getWidth();
		
		for (String paragraph : paragraphs) {
			// set up LineBreakMeasurer that will calculate the layouts
			AttributedString str = new AttributedString(paragraph);
			str.addAttribute(TextAttribute.FONT, g2.getFont());
			AttributedCharacterIterator iter = str.getIterator();

			LineBreakMeasurer measurer = new LineBreakMeasurer(iter, g2.getFontRenderContext());
			float maxWidth = width - (PADDING_PROP * width);
			
			// calculate layouts w/ measurer
			while (measurer.getPosition() < iter.getEndIndex()) {
				TextLayout layout = measurer.nextLayout(maxWidth);
				layouts.add(layout);

				totalHeight += layout.getAscent() + layout.getDescent() + layout.getLeading();
			}
		}

		return totalHeight;
	}
}
