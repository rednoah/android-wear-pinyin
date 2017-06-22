package woogle.spi;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class WoogleCompLabel extends JLabel{

	// RGB = 58, 184, 251
	// 3AB8FB
	
	public WoogleCompLabel() {
		super.setFont(WoogleLookupPanel.SERIF);
		super.setBorder(BorderFactory.createEtchedBorder(1));
	}
	
	@Override
	public void setText(String text) {
		text = "<html><font face=\"Verdana\"><b>" 
			+ text 
			+ "</b><font color=#3AB8FB>|</font></font></html>";
		super.setText(text);
	}
}
