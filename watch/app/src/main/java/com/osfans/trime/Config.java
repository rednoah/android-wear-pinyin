/*
 * Copyright 2015 osfans
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.osfans.trime;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.util.Log;
import android.graphics.Typeface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

/**
 * 解析YAML配置文件
 */
public class Config {
    public static String SDCARD = "/sdcard/";

    private static String defaultFile = "trime.yaml";
    private String themeName = "trime";

    private String schema_id;

    private static String RIME = "rime";
    private static String USER_DATA_DIR = SDCARD + RIME;
    public static String OPENCC_DATA_DIR = USER_DATA_DIR + "/opencc/";

    private static int BLK_SIZE = 1024;

    private static Config self;


    public Config(Context context) {
        init();
    }

    public String getTheme() {
        return themeName;
    }

    public static void prepareRime(Context context) {
        boolean isExist = new File(USER_DATA_DIR).exists();
        boolean isOverwrite = false;
        if (isOverwrite) {
            copyFileOrDir(context, RIME, true);
        } else if (isExist) {
            copyFileOrDir(context, RIME + "/" + defaultFile, false);
        } else {
            copyFileOrDir(context, RIME, false);
        }
        while (!new File(USER_DATA_DIR + "/" + defaultFile).exists()) {
            SystemClock.sleep(3000);
            copyFileOrDir(context, RIME, isOverwrite);
        }
        Rime.get(!isExist); //覆蓋時不強制部署
    }

    public static String[] getThemeKeys() {
        File d = new File(USER_DATA_DIR);
        FilenameFilter trimeFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith("trime.yaml");
            }
        };
        return d.list(trimeFilter);
    }

    public static String[] getThemeNames(String[] keys) {
        if (keys == null) return null;
        int n = keys.length;
        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            String k = keys[i].replace(".trime.yaml", "").replace(".yaml", "");
            names[i] = k;
        }
        return names;
    }

    public static boolean deployOpencc() {
        File d = new File(OPENCC_DATA_DIR);
        if (d.exists()) {
            FilenameFilter txtFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".txt");
                }
            };
            for (String txtName : d.list(txtFilter)) {
                txtName = OPENCC_DATA_DIR + txtName;
                String ocdName = txtName.replace(".txt", ".ocd");
                Rime.opencc_convert_dictionary(txtName, ocdName, "text", "ocd");
            }
        }
        return true;
    }

    public static String[] list(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
        } catch (IOException ex) {
            Log.e("Config", "I/O Exception", ex);
        }
        return assets;
    }

    public static boolean copyFileOrDir(Context context, String path, boolean overwrite) {
        AssetManager assetManager = context.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(context, path, overwrite);
            } else {
                String fullPath = SDCARD + path;
                File dir = new File(fullPath);
                if (!dir.exists()) dir.mkdir();
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(context, path + "/" + assets[i], overwrite);
                }
            }
        } catch (IOException ex) {
            Log.e("Config", "I/O Exception", ex);
            return false;
        }
        return true;
    }

    public static boolean copyFile(Context context, String filename, boolean overwrite) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = SDCARD + filename;
            if (new File(newFileName).exists() && !overwrite) return true;
            out = new FileOutputStream(newFileName);
            byte[] buffer = new byte[BLK_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("Config", e.getMessage());
            return false;
        }
        return true;
    }

    private void deployConfig() {
        Rime.deploy_config_file(themeName + ".yaml", "config_version");
    }


    public void init() {
        deployConfig();
        reset();
    }

    public void reset() {
        schema_id = Rime.getSchemaId();
    }


    public static Config get() {
        return self;
    }

    public static Config get(Context context) {
        if (self == null) {
            prepareRime(context);
            self = new Config(context);
        }
        return self;
    }


}
