package ntu.csie.swipy.model;


public enum Final {


    a("ㄚ"),
    o("ㄛ"),
    e("ㄜ"),
    r("ㄦ"),
    ai("ㄞ"),
    ei("ㄟ"),
    ao("ㄠ"),
    ou("ㄡ"),
    n("ㄋ"),        // en  ... ㄣ, an  ... ㄢ
    ng("ㄫ"),       // eng ... ㄣ, ang ... ㄤ
    an("ㄢ"),
    en("ㄣ"),
    ang("ㄤ"),
    eng("ㄥ"),
    ong("ㄨㄥ"),
    i("ㄧ"),
    ia("ㄧㄚ"),
    iao("ㄧㄠ"),
    ie("ㄧㄝ"),
    iu("ㄧㄡ"),
    ian("ㄧㄢ"),
    in("ㄧㄣ"),
    iang("ㄧㄤ"),
    ing("ㄧㄥ"),
    iong("ㄩㄥ"),
    u("ㄨ"),
    ua("ㄨㄚ"),
    uo("ㄨㄛ"),
    ue("ㄩㄝ"),
    uai("ㄨㄞ"),
    ui("ㄨㄟ"),
    un("ㄨㄣ"),      // yun  ... ㄩㄣ
    uan("ㄨㄢ"),     // yuan ... ㄩㄢ
    uang("ㄨㄤ"),
    ü("ㄩ"),
    üe("ㄩㄝ");


    private String zhuyin;


    Final(String zhuyin) {
        this.zhuyin = zhuyin;
    }


    public String getZhuyin() {
        return zhuyin;
    }


    @Override
    public String toString() {
        return getZhuyin();
    }


}
