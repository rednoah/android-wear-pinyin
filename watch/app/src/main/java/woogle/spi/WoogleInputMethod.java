package woogle.spi;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import woogle.cky.DependencyModel;
import woogle.cky.Dictory;
import woogle.cky.LanguageModel;

public class WoogleInputMethod {

    static final Locale[] SUPPORTED_LOCALES = {Locale.US, Locale.SIMPLIFIED_CHINESE};


    Dictory Dic = new Dictory();

    LanguageModel Lm;

    DependencyModel Dm;


    private File prepareAssets(Context context, String... assets) {
        try {
            File assetsFolder = new File(context.getFilesDir(), "assets");
            if (!assetsFolder.exists() && !assetsFolder.mkdirs()) {
                throw new IllegalStateException("Failed to create folder: " + assetsFolder);
            }

            for (String a : assets) {
                Log.d("WoogleInputMethod", "Prepare asset: " + a);
                FileUtils.copyInputStreamToFile(context.getAssets().open(a, AssetManager.ACCESS_STREAMING), new File(assetsFolder, a));
            }

            return assetsFolder;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    // --------------------------------------------------------
    // per-instance state
    Locale locale;


    WooglePinyinHandler pinyinHandler;


    public WoogleInputMethod(Context context) {
        File assetsFolder = prepareAssets(context, "model.db", "model.db.p", "model.db.t");

        File dbFile = new File(assetsFolder, "model.db");
        DB db = DBMaker.newFileDB(dbFile).readOnly().make();

        Dic.readHanziDic(db);
        Dic.readWordDic(db);
        Dic.readSyllable(db);

        Lm = new LanguageModel(db);
        Dm = new DependencyModel(db);


        // INIT
        this.log("WoogleInputMethod");
        this.pinyinHandler = new WooglePinyinHandler(this);
    }

    public void log(String s) {
        Log.d("WoogleInputMethod", s);
    }


    public Locale getLocale() {
        this.log("getLocale");
        return this.locale;
    }

    public boolean isCompositionEnabled() {
        // always enabled
        this.log("isCompositionEnabled");
        return true;
    }


    public boolean setLocale(Locale locale) {
        for (int i = 0; i < SUPPORTED_LOCALES.length; i++) {
            if (locale.equals(SUPPORTED_LOCALES[i])) {
                this.locale = locale;
                this.log("setLocale: " + locale);
                return true;
            }
        }
        return false;
    }


    public void sendText(String s) {
    }


}
