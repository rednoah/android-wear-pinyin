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
        WoogleLog.log(this, "WoogleInputMethodDescriptor");
    }

    @Override
    public InputMethod createInputMethod()
            throws Exception {
        WoogleLog.log(this, "createInputMethod");
        return new WoogleInputMethod();
    }

    @Override
    public Locale[] getAvailableLocales()
            throws AWTException {
        Locale[] locales = WoogleInputMethod.SUPPORTED_LOCALES;
        StringBuilder sb = new StringBuilder();
        for (Locale l : locales) {
            sb.append(l.getDisplayLanguage() + ", ");
        }
        WoogleLog.log(this, "getAvailableLocales:" + sb);
        return locales;
    }

    @Override
    public String getInputMethodDisplayName(Locale inputLocale,
            Locale displayLanguage) {
        WoogleLog.log(this, "getInputMethodDisplayName: Woogle拼音输入法");
        return "Woogle拼音输入法";
    }

    @Override
    public Image getInputMethodIcon(Locale inputLocale) {
        java.net.URL imgURL = getClass().getResource(
                WoogleInputMethod.STATUS_WINDOW_ICON);
        WoogleLog.log(this, "createButton:" + imgURL.getPath());
        ImageIcon img = new ImageIcon(imgURL, "(S)");
        WoogleLog.log(this, "getInputMethodIcon:" + img);
        return img.getImage();
    }

    @Override
    public boolean hasDynamicLocaleList() {
        WoogleLog.log(this, "hasDynamicLocaleList");
        return false;
    }
}
