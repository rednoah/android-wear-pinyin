package woogle.spi;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class WoogleLookupSearchButton extends JButton{
	
	WoogleInputMethod w;
//	public static Dimension SIZE = new Dimension(16, 16);
	
	WoogleLookupSearchButton(
			String name,
			WoogleInputMethod w, 
			String path, 
			String description,
			Dimension size) {
		super();
		java.net.URL imgURL = getClass().getResource(path);
		ImageIcon img = new ImageIcon(imgURL, description);
		if (img != null) {
			super.setIcon(img);
		}
		else {
			super.setText(description);
		}
		super.setToolTipText(description);
		super.setPreferredSize(size);
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
		super.setBackground(Color.WHITE);
		super.setForeground(Color.WHITE);
		super.setName(name);
		this.w = w;
	}
}
