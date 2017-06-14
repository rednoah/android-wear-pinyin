package woogle.spi;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;

import javax.swing.ImageIcon;

public class WoogleInputMethodDescriptor implements InputMethodDescriptor {

	public WoogleInputMethodDescriptor() {
		WoogleLog.init();
		this.log("WoogleInputMethodDescriptor");
	}

	@Override
	public InputMethod createInputMethod() throws Exception {
		this.log("createInputMethod");
		return new WoogleInputMethod();
	}

	@Override
	public Locale[] getAvailableLocales() throws AWTException {
		Locale[] locales = WoogleInputMethod.SUPPORTED_LOCALES;
//		this.log("getAvailableLocales:" + locales);
		return locales;
	}

	@Override
	public String getInputMethodDisplayName(Locale inputLocale,
			Locale displayLanguage) {
//		this.log("getInputMethodDisplayName");
		return "Woogle∆¥“Ù ‰»Î∑®";
	}

	@Override
	public Image getInputMethodIcon(Locale inputLocale) {
		java.net.URL imgURL = getClass().getResource(WoogleInputMethod.STATUS_WINDOW_ICON);
		this.log("createButton:" + imgURL.getPath());
		ImageIcon img = new ImageIcon(imgURL, "(S)");
		this.log("getInputMethodIcon:" + img);
		return img.getImage();
	}

	@Override
	public boolean hasDynamicLocaleList() {
//		this.log("getAvailableLocales");
		return false;
	}
	
	private void log(String s) {
		WoogleLog.log(this, s);
	}
}
