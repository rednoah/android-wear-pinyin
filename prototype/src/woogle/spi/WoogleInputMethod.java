package woogle.spi;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.io.File;
import java.io.IOException;
import java.lang.Character.Subset;
import java.text.AttributedString;
import java.util.HashSet;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import woogle.chart.ChartCell;
import woogle.util.WoogleDatabase;
import woogle.util.WoogleUtils;

public class WoogleInputMethod implements InputMethod {

	static final Locale[] SUPPORTED_LOCALES = { Locale.US, Locale.SIMPLIFIED_CHINESE };

	// windows - shared by all instances of this input method
	static JFrame StatusWindow;

	// current or last statusWindow owner instance
	static WoogleInputMethod StatusWindowOwner;

	// input method window titles
	static final String STATUS_WINDOW_TITLE = "Woogle输入法状态栏";

	static final String LOOKUP_WINDOW_TITLE = "Woogle输入法编辑栏";

	static final String STATUS_WINDOW_ICON = "shrc_logo_16_4.GIF";

	static final int SPACING = 2;

	// true if Solaris style; false if PC style
	static boolean isAttachedStatusWindow = true;

	// status window location in PC style
	static Point GlobalStatusWindowLocation;

	// keep live input method instances (synchronized using statusWindow)
	static HashSet<WoogleInputMethod> WoogleInputMethodInstances = new HashSet<WoogleInputMethod>(5);

	// status label
	static JLabel StatusWindowLabel = null;

	// --------------------------------------------------------
	// per-instance state
	InputMethodContext context;

	// remember the statusWindow location per instance
	Rectangle statusWindowLocation;

	// boolean isDisposed;

	JFrame lookupWindow;

	WoogleLookupPanel lookupPanel;

	WooglePinyinHandler pinyinHandler;

	WoogleState state;

	public WoogleInputMethod() {
		WoogleLog.init();
		if (!WoogleDatabase.isInit()) {
			File file1 = new File("data/syllable.txt");
			File file2 = new File("data/syllable_chars.txt");
			File file3 = new File("data/syllable_words.txt");
			File file4 = new File("data/2000-2007_wordsegment.rmrb.small.arpa");
			File file5 = new File("data/SogouR.mini.txt");
			WoogleDatabase.load(file1, file2, file3, file4, file5);
		}
		WoogleLog.log(this, "WoogleInputMethod");
		this.state = new WoogleState();
		this.pinyinHandler = new WooglePinyinHandler(this);
	}

	@Override
	public void activate() {
		WoogleLog.log(this, "activate");
		state.isActive = true;
		synchronized (StatusWindow) {
			StatusWindowOwner = this;
			this.updateStatusWindow(state.locale);
			if (!StatusWindow.isVisible()) {
				StatusWindow.setVisible(true);
				WoogleLog.log(this, "StatusWindow.getLocation:" + StatusWindow.getLocation());
			}
			this.setStatusWindowForeground(Color.black);
		}
		if (state.isSimplefiedChinese()) {
			openLookupWindow();
			updateCompAndCand();
		}
	}

	@Override
	public void deactivate(boolean arg0) {
		WoogleLog.log(this, "deactivate");
		closeLookupWindow();
		setStatusWindowForeground(Color.lightGray);
		state.isActive = false;
	}

	@Override
	public void dispatchEvent(AWTEvent event) {
		int id = event.getID();
		switch (id) {
		case KeyEvent.KEY_TYPED:
		case KeyEvent.KEY_PRESSED:
		case KeyEvent.KEY_RELEASED:
		case MouseEvent.MOUSE_CLICKED:
			this.pinyinHandler.dispatchEvent(event);
			if (state.isActive) {
				updateCompAndCand();
			}
			break;
		}
	}

	public void dispatchEvent(char event) {
		this.pinyinHandler.dispatchEvent(event);
		updateCompAndCand();
	}

	@Override
	public void dispose() {
		WoogleLog.log(this, "dispose");
		synchronized (StatusWindow) {
			WoogleInputMethodInstances.remove(this);
			if (WoogleInputMethodInstances.isEmpty()) {
				StatusWindow.dispose();
			}
		}
		// this.isDisposed = true;
	}

	@Override
	public void endComposition() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getControlObject() {
		WoogleLog.log(this, "getControlObject");
		return null;
	}

	@Override
	public Locale getLocale() {
		WoogleLog.log(this, "getLocale");
		return state.locale;
	}

	@Override
	public void hideWindows() {
		WoogleLog.log(this, "hideWindows");
		synchronized (StatusWindow) {
			if (StatusWindowOwner == this) {
				StatusWindow.setVisible(false);
			}
		}
		closeLookupWindow();
	}

	@Override
	public boolean isCompositionEnabled() {
		// always enabled
		WoogleLog.log(this, "isCompositionEnabled");
		return true;
	}

	@Override
	public void notifyClientWindowChange(Rectangle location) {
		WoogleLog.log(this, "notifyClientWindowChange:" + location);
		this.statusWindowLocation = location;
		// synchronized (StatusWindow) {
		// if (AttachedStatusWindow && StatusWindowOwner == this) {
		// if (location == null) {
		// StatusWindow.setVisible(false);
		// }
		// else {
		// StatusWindow.setLocation(
		// location.x,
		// location.y + location.height);
		// if (!StatusWindow.isVisible()) {
		// if (this.active) {
		// setStatusWindowForeground(Color.black);
		// } else {
		// setStatusWindowForeground(Color.lightGray);
		// }
		// StatusWindow.setVisible(true);
		// }
		// }
		// }
		// }
		this.updateLookupWindowLocation();
	}

	@Override
	public void reconvert() {
		// not supported yet
		WoogleLog.log(this, "reconvert");
		// throw new UnsupportedOperationException();
	}

	@Override
	public void removeNotify() {
		// not supported yet
		WoogleLog.log(this, "removeNotify");
		// throw new UnsupportedOperationException();
	}

	@Override
	public void setCharacterSubsets(Subset[] subsets) {
		// igore
		WoogleLog.log(this, "setCharacterSubsets:" + subsets);
	}

	@Override
	public void setCompositionEnabled(boolean enable) {
		// not supported yet
		WoogleLog.log(this, "setCompositionEnabled:" + enable);
		// throw new UnsupportedOperationException();
	}

	@Override
	public void setInputMethodContext(InputMethodContext context) {
		WoogleLog.log(this, "setInputMethodContext:" + context);
		this.context = context;

		// create status window, shared by all instance
		if (StatusWindow == null) {
			synchronized (this.getClass()) {
				if (StatusWindow == null) {
					StatusWindow = context.createInputMethodJFrame(STATUS_WINDOW_TITLE, false);
					StatusWindow.setResizable(false);
					// StatusWindow.setUndecorated(true);
					StatusWindow.addComponentListener(new WoogleStatusWindowComponentAdapter(this));
					StatusWindow.setLayout(new FlowLayout());
					String localeName = state.getLocaleName();
					StatusWindowLabel = new JLabel("当前语言: " + localeName);
					StatusWindowLabel.setOpaque(true);
					StatusWindowLabel.setForeground(Color.black);
					StatusWindowLabel.setBackground(Color.white);
					StatusWindowLabel.addMouseListener(new WoogleStatusWindowMouseAdapter(this));
					StatusWindowLabel.setBorder(BorderFactory.createEtchedBorder());
					StatusWindow.add(StatusWindowLabel);
					StatusWindowOwner = this;
					updateStatusWindow(state.locale);

					// add icon
					JLabel label = this.createLabel(STATUS_WINDOW_ICON, "SHRC");
					StatusWindow.add(label, 0);

					StatusWindow.pack();
					StatusWindow.setVisible(true);
					WoogleLog.log(this, "CreateStatusWindow");
					WoogleLog.log(this, "StatusWindow.getLocation:" + StatusWindow.getLocation());
				}
			}
		}
		context.enableClientWindowNotification(this, isAttachedStatusWindow);
		synchronized (StatusWindow) {
			WoogleInputMethodInstances.add(this);
		}
	}

	@Override
	public boolean setLocale(Locale locale) {
		for (Locale l : SUPPORTED_LOCALES) {
			if (locale.equals(l)) {
				WoogleLog.log(this, "setLocale: " + locale);
				if (StatusWindow != null) {
					updateStatusWindow(locale);
				}
				state.locale = locale;
				return true;
			}
		}
		return false;
	}

	private void updateStatusWindow(Locale locale) {
		WoogleLog.log(this, "updateStatusWindow:" + locale);
		synchronized (StatusWindow) {
			String localeName = state.getLocaleName();
			String text = "当前语言: " + localeName;
			if (!StatusWindowLabel.getText().equals(text)) {
				StatusWindowLabel.setText(text);
				StatusWindow.pack();
			}
			setPCStyleStatusWindow();
		}
	}

	// 是否应静态方法
	public void setPCStyleStatusWindow() {
		WoogleLog.log(this, "setPCStyleStatusWindow");
		synchronized (StatusWindow) {
			if (GlobalStatusWindowLocation == null) {
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				GlobalStatusWindowLocation = new Point(d.width - StatusWindow.getPreferredSize().width - 100, d.height - StatusWindow.getPreferredSize().height - 100);
			}
			StatusWindow.setLocation(GlobalStatusWindowLocation.x, GlobalStatusWindowLocation.y);
			WoogleLog.log(this, "StatusWindow.getLocation" + StatusWindow.getLocation());
		}
	}

	// 是否应静态方法
	private void setStatusWindowForeground(Color fg) {
		WoogleLog.log(this, "setStatusWindowForeground:" + fg);
		synchronized (StatusWindow) {
			if (StatusWindowOwner != this) {
				return;
			}
			StatusWindowLabel.setForeground(fg);
		}
	}

	private void closeLookupWindow() {
		if (lookupWindow != null) {
			lookupWindow.setVisible(false);
			lookupWindow = null;
		}
	}

	private void openLookupWindow() {
		if (lookupWindow == null) {
			lookupWindow = this.context.createInputMethodJFrame(LOOKUP_WINDOW_TITLE, true);
			lookupWindow.setResizable(false);
			// lookupWindow.setUndecorated(true);
			lookupPanel = new WoogleLookupPanel(this);
			lookupWindow.add(lookupPanel);
			lookupWindow.pack();
			this.updateLookupWindowLocation();
			WoogleLog.log(this, "openLookupWindow");
		}
		// 实现getSelectedSegmentOffset后重新考虑
		Point p = lookupWindow.getLocation();
		if (p.x != 0 && p.y != 0)
			lookupWindow.setVisible(true);
		else
			closeLookupWindow();
	}

	private ImageIcon createIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		WoogleLog.log(this, "createButton:" + imgURL.getPath());
		ImageIcon img = new ImageIcon(imgURL, description);
		return img;
	}

	private JLabel createLabel(String path, String description) {
		ImageIcon img = this.createIcon(path, description);
		if (img != null) {
			JLabel l = new JLabel(img);
			l.setToolTipText(description);
			return l;
		} else {
			return new JLabel(description);
		}
	}

	private void updateLookupWindowLocation() {
		WoogleLog.log(this, "updateLookupWindowLocation");
		if (this.context == null || this.lookupWindow == null)
			return;
		int textOffset = this.getSelectedSegmentOffset();
		Rectangle caretRect = this.context.getTextLocation(TextHitInfo.leading(textOffset));
		WoogleLog.log(this, "caretRect:" + caretRect);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = lookupWindow.getPreferredSize();
		WoogleLog.log(this, "windowSize:" + windowSize);

		Point windowLocation = new Point();
		if (caretRect.x + windowSize.width > screenSize.width) {
			windowLocation.x = screenSize.width - windowSize.width;
		} else {
			windowLocation.x = caretRect.x;
		}

		if (caretRect.y + caretRect.height + SPACING + windowSize.height > screenSize.height) {
			windowLocation.y = caretRect.y - SPACING - windowSize.height;
		} else {
			windowLocation.y = caretRect.y + caretRect.height + SPACING;
		}

		WoogleLog.log(this, "lookupWindow.getLocation:" + lookupWindow.getLocation());
		this.lookupWindow.setLocation(windowLocation);
		this.lookupWindow.pack();
	}

	int getSelectedSegmentOffset() {
		return 0;
	}

	// void update() {
	// String s = pinyinHandler.display.toDisplayString();
	// sendText(s);
	// }

	public void sendText(String s) {
		WoogleLog.log(this, "send:" + s);
		TextHitInfo caret = null;
		int count = s.length();
		context.dispatchInputMethodEvent(InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, new AttributedString(s).getIterator(), count, caret, null);
	}

	public void updateCompAndCand() {
		String comp = state.result.toCompString();
		System.out.println("Pinyin Page " + state.currentCandPage + ": " + comp);

		String[] cands = state.getCandString(5);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sb.append(cands[i] + ", ");
		}

		System.out.println(sb);
	}
}
