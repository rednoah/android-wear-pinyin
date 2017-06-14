package woogle.spi;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WoogleStatusWindowComponentAdapter extends ComponentAdapter {
	
	WoogleInputMethod w;
	
	WoogleStatusWindowComponentAdapter(WoogleInputMethod w) {
		this.w = w;
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		synchronized (WoogleInputMethod.StatusWindow) {
			if (!WoogleInputMethod.isAttachedStatusWindow) {
				Component comp = e.getComponent();
				if (comp.isVisible()) {
					WoogleInputMethod.GlobalStatusWindowLocation = comp.getLocation();
				}
			}
		}
	}
}
