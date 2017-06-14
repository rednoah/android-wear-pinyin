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
import java.lang.Character.Subset;
import java.text.AttributedString;
import java.util.HashSet;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import woogle.cky.DependencyModel;
import woogle.cky.DependencyModelReader;
import woogle.cky.Dictory;
import woogle.cky.LanguageModel;
import woogle.cky.LanguageModelReader;
import woogle.cky.Utils;

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

	static Dictory Dic = new Dictory();

	static LanguageModel Lm;

	static DependencyModel Dm;

	static {
		File file;
		try {
			file = Utils.getInstalledFile("拼音字典.txt");
			Dic.readHanziDic(file);
			file = Utils.getInstalledFile("拼音词典.txt");
			Dic.readWordDic(file);
			file = Utils.getInstalledFile("syllable.txt");
			Dic.readSyllable(file);
			file = Utils.getInstalledFile("lm.arpa");
			Lm = LanguageModelReader.readLanguageModel(file);
			file = Utils.getInstalledFile("SogouR.mini.txt");
			Dm = DependencyModelReader.readDependencyModel(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// --------------------------------------------------------
	// per-instance state
	InputMethodContext context;
	Locale locale;
	boolean isActive;

	// remember the statusWindow location per instance
	Rectangle statusWindowLocation;

	// boolean isDisposed;

	JFrame lookupWindow;

	WoogleLookupPanel lookupPanel;

	WooglePinyinHandler pinyinHandler;

	public WoogleInputMethod() {
		WoogleLog.init();
		this.log("WoogleInputMethod");
		this.pinyinHandler = new WooglePinyinHandler(this);
	}

	public void log(String s) {
		WoogleLog.log(this, s);
	}

	@Override
	public void activate() {
		this.log("activate");
		this.isActive = true;
		synchronized (StatusWindow) {
			StatusWindowOwner = this;
			this.updateStatusWindow(locale);
			if (!StatusWindow.isVisible()) {
				StatusWindow.setVisible(true);
				this.log("StatusWindow.getLocation:" + StatusWindow.getLocation());
			}
			this.setStatusWindowForeground(Color.black);
		}
		if (this.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			openLookupWindow();
			String comp = this.pinyinHandler.getCompString();
			this.log("comp: " + comp);
			this.lookupPanel.compLabel.setText(comp);
			String[] cands = this.pinyinHandler.getCandString();
			this.log("cands: " + cands);
			for (int i = 0; i < 5; i++) {
				this.lookupPanel.candLabel[i].setText(cands[i]);
			}
			this.updateLookupWindowLocation();
		}
	}

	@Override
	public void deactivate(boolean arg0) {
		this.log("deactivate");
		closeLookupWindow();
		setStatusWindowForeground(Color.lightGray);
		this.isActive = false;
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
			if (this.isActive) {
				String comp = this.pinyinHandler.getCompString();
				this.log("comp: " + comp);
				this.lookupPanel.compLabel.setText(comp);
				String[] cands = this.pinyinHandler.getCandString();
				this.log("cands: " + cands);
				for (int i = 0; i < 5; i++) {
					this.lookupPanel.candLabel[i].setText(cands[i]);
				}
				this.updateLookupWindowLocation();
			}
			break;
		}
	}

	@Override
	public void dispose() {
		this.log("dispose");
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
		this.log("getControlObject");
		return null;
	}

	@Override
	public Locale getLocale() {
		this.log("getLocale");
		return this.locale;
	}

	@Override
	public void hideWindows() {
		this.log("hideWindows");
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
		this.log("isCompositionEnabled");
		return true;
	}

	@Override
	public void notifyClientWindowChange(Rectangle location) {
		this.log("notifyClientWindowChange:" + location);
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
		this.log("reconvert");
		// throw new UnsupportedOperationException();
	}

	@Override
	public void removeNotify() {
		// not supported yet
		this.log("removeNotify");
		// throw new UnsupportedOperationException();
	}

	@Override
	public void setCharacterSubsets(Subset[] subsets) {
		// igore
		this.log("setCharacterSubsets:" + subsets);
	}

	@Override
	public void setCompositionEnabled(boolean enable) {
		// not supported yet
		this.log("setCompositionEnabled:" + enable);
		// throw new UnsupportedOperationException();
	}

	@Override
	public void setInputMethodContext(InputMethodContext context) {
		this.log("setInputMethodContext:" + context);
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
					String localeName = locale == null ? "无" : locale.getDisplayName();
					StatusWindowLabel = new JLabel("当前语言: " + localeName);
					StatusWindowLabel.setOpaque(true);
					StatusWindowLabel.setForeground(Color.black);
					StatusWindowLabel.setBackground(Color.white);
					StatusWindowLabel.addMouseListener(new WoogleStatusWindowMouseAdapter(this));
					StatusWindowLabel.setBorder(BorderFactory.createEtchedBorder());
					StatusWindow.add(StatusWindowLabel);
					StatusWindowOwner = this;
					updateStatusWindow(this.locale);

					// add icon
					JLabel label = this.createLabel(STATUS_WINDOW_ICON, "SHRC");
					StatusWindow.add(label, 0);

					StatusWindow.pack();
					StatusWindow.setVisible(true);
					this.log("CreateStatusWindow");
					this.log("StatusWindow.getLocation:" + StatusWindow.getLocation());
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
		for (int i = 0; i < SUPPORTED_LOCALES.length; i++) {
			if (locale.equals(SUPPORTED_LOCALES[i])) {
				this.locale = locale;
				this.log("setLocale: " + locale);
				if (StatusWindow != null) {
					updateStatusWindow(locale);
				}
				return true;
			}
		}
		return false;
	}

	private void updateStatusWindow(Locale locale) {
		this.log("updateStatusWindow:" + locale);
		synchronized (StatusWindow) {
			String localeName = locale == null ? "无" : locale.getDisplayName();
			String text = "当前语言: " + localeName;
			if (!StatusWindowLabel.getText().equals(text)) {
				StatusWindowLabel.setText(text);
				StatusWindow.pack();
			}
			// if (AttachedStatusWindow) {
			// if (clientWindowLocation != null) {
			// StatusWindow.setLocation(
			// clientWindowLocation.x,
			// clientWindowLocation.y + clientWindowLocation.height);
			// this.log("StatusWindow.setLocation:" + StatusWindow.getLocation());
			// }
			// } else {
			setPCStyleStatusWindow();
			// }
		}
	}

	// 是否应静态方法
	public void setPCStyleStatusWindow() {
		this.log("setPCStyleStatusWindow");
		synchronized (StatusWindow) {
			if (GlobalStatusWindowLocation == null) {
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				GlobalStatusWindowLocation = new Point(d.width - StatusWindow.getPreferredSize().width - 100, d.height - StatusWindow.getPreferredSize().height - 100);
			}
			StatusWindow.setLocation(GlobalStatusWindowLocation.x, GlobalStatusWindowLocation.y);
			this.log("StatusWindow.getLocation" + StatusWindow.getLocation());
		}
	}

	// 是否应静态方法
	private void setStatusWindowForeground(Color fg) {
		this.log("setStatusWindowForeground:" + fg);
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
			this.log("openLookupWindow");
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
		this.log("createButton:" + imgURL.getPath());
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
		this.log("updateLookupWindowLocation");
		if (this.context == null || this.lookupWindow == null)
			return;
		int textOffset = this.getSelectedSegmentOffset();
		Rectangle caretRect = this.context.getTextLocation(TextHitInfo.leading(textOffset));
		this.log("caretRect:" + caretRect);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = lookupWindow.getPreferredSize();
		this.log("windowSize:" + windowSize);

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

		this.log("lookupWindow.getLocation:" + lookupWindow.getLocation());
		this.lookupWindow.setLocation(windowLocation);
		this.lookupWindow.pack();
	}

	int getSelectedSegmentOffset() {
		return 0;
	}

	void sendText(String s) {
		// this.log("send:" + s);
		// TextHitInfo caret = null;
		// int count = s.length();
		// context.dispatchInputMethodEvent(InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, new AttributedString(s).getIterator(), count, caret, null);
	}

	void sendChar(char c) {
		String s = Character.toString(c);
		this.sendText(s);
	}
}
