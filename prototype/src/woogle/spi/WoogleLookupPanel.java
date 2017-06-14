package woogle.spi;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * 只考虑如何显示，不考虑位置
 * @author pengyifan
 */
@SuppressWarnings("serial")
public class WoogleLookupPanel extends JPanel {

	WoogleInputMethod w;
	
//	JPanel compPanel;
//	JPanel candPanel;
	WoogleLookupLabel[] candLabel;
	WoogleLookupLabel glueLabel;
	WoogleLookupSearchButton upButton;
	WoogleLookupSearchButton dnButton;
	WoogleLookupSearchButton searchButton;
	WoogleCompLabel compLabel;
	
	public static Font SONGTI = new Font("宋体", Font.PLAIN, 16);
	public static Font SERIF = new Font("Serif", Font.PLAIN, 16);
	public static Dimension SIZE_25_25 = new Dimension(25, 25);
	public static Dimension SIZE_50_25 = new Dimension(50, 25);
	
	WoogleLookupPanel(WoogleInputMethod w) {
		super();
		this.w = w;
		
		super.setOpaque(true);
		super.setForeground(Color.black);
		super.setBackground(Color.white);

		super.enableEvents(AWTEvent.KEY_EVENT_MASK);
		super.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		
		super.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		compLabel = new WoogleCompLabel();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 6;
		c.gridheight = 1;
		super.add(compLabel, c);
		
		searchButton = new WoogleLookupSearchButton("Search",
				w, "Search16.gif", "Search", SIZE_50_25);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 6;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		super.add(searchButton, c);
			
		candLabel = new WoogleLookupLabel[5];
		for(int i=0; i<5; i++) {
			candLabel[i] = new WoogleLookupLabel((i+1) + ".  ", w);
			candLabel[i].setName(Integer.toString(i+1));
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0;
			c.gridx = i;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			super.add(candLabel[i], c);
		}
		// 吸收compLabe带来的额外空间
		glueLabel = new WoogleLookupLabel("", w);
		glueLabel.removeMouseListener(glueLabel);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		super.add(glueLabel, c);
		upButton = new WoogleLookupSearchButton("Up",
				w, "Up16.gif", "Up", SIZE_25_25);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		super.add(upButton, c);
		dnButton = new WoogleLookupSearchButton("Down",
				w, "Down16.gif", "Down", SIZE_25_25);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 7;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		super.add(dnButton, c);
	}
}
