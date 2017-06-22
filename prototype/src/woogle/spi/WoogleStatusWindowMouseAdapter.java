package woogle.spi;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class WoogleStatusWindowMouseAdapter extends MouseAdapter {

	WoogleInputMethod w;
	
	WoogleStatusWindowMouseAdapter(WoogleInputMethod w) {
		this.w = w;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int count = e.getClickCount();
		if (count >= 2) {
			this.toggleStatusWindowStyle();
		}
	}
	
	// 双击后让status window与应用程序底部相接……
	private void toggleStatusWindowStyle() {
		WoogleLog.log("toggleStatusWindowStyle");
		synchronized (WoogleInputMethod.StatusWindow) {
			if (WoogleInputMethod.isAttachedStatusWindow) {
				WoogleInputMethod.isAttachedStatusWindow = false;
				w.setPCStyleStatusWindow();
			} else {
				WoogleInputMethod.isAttachedStatusWindow = true;
			}
			Iterator<WoogleInputMethod> itr 
				= WoogleInputMethod.WoogleInputMethodInstances.iterator();
			while (itr.hasNext()) {
				WoogleInputMethod im = itr.next();
				im.context.enableClientWindowNotification(
					im, WoogleInputMethod.isAttachedStatusWindow);
			}
		}
	}
}
