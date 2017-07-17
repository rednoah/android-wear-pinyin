package com.osfans.trime.ime;


import android.content.Context;

import com.osfans.trime.AbstractPredictiveKeyboardLayout;
import com.osfans.trime.GrowingFinalsQwerty;
import com.osfans.trime.PinyinSyllablesQwerty;


public class GrowingFinalsIME extends AbstractWearIME {


    @Override
    protected AbstractPredictiveKeyboardLayout createKeyboardLayout(Context context) {
        return new GrowingFinalsQwerty(context);
    }


}
