package com.osfans.trime.ime;


import android.content.Context;

import com.osfans.trime.AbstractPredictiveKeyboardLayout;
import com.osfans.trime.PinyinSyllablesQwerty;


public class PinyinSyllablesIME extends AbstractWearIME {


    @Override
    protected AbstractPredictiveKeyboardLayout createKeyboardLayout(Context context) {
        return new PinyinSyllablesQwerty(context);
    }


}
