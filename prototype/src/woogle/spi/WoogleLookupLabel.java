package woogle.spi;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class WoogleLookupLabel extends JLabel implements MouseListener{
	
	public static final Color GOOGLE_BLUE = new Color(224, 236, 255);
	
	WoogleInputMethod w;
	int index;
	
	WoogleLookupLabel(int index, WoogleInputMethod w) {
		super();
		super.setBorder(BorderFactory.createEtchedBorder(1));
		super.setFont(WoogleLookupPanel.SONGTI);
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
		super.setBackground(Color.WHITE);
		super.addMouseListener(this);
		super.setOpaque(true);
		this.w = w;
		this.index = index;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// 在WooglePinyinHandler中处理
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		super.setBackground(GOOGLE_BLUE);
	}

	@Override
	public void mouseExited(MouseEvent event) {
		super.setBackground(Color.WHITE);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		super.setForeground(Color.RED);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		super.setForeground(Color.BLACK);
	}
}
