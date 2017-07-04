package com.osfans.trime.model;


public enum Punctuation {

    APOSTROPHE("'"),
    COMMA("，"),
    DOT("。"),
    QUESTION_MARK("？");


    private String value;


    Punctuation(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }


}
