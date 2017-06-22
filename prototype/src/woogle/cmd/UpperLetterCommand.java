package woogle.cmd;

import woogle.chart.CKYDecoder;
import woogle.spi.WooglePinyinHandler;
import woogle.spi.WoogleState;
import woogle.util.PinyinSyllable;

public class UpperLetterCommand implements Command {

    WooglePinyinHandler handler;

    WoogleState         state;

    PinyinSyllable      pinyinSyllable;

    CKYDecoder          decoder;

    char                upperletter;

    public UpperLetterCommand(WooglePinyinHandler handler, char upperletter) {
        this.handler = handler;
        state = handler.state;
        pinyinSyllable = handler.pinyinSyllable;
        decoder = handler.decoder;
        this.upperletter = upperletter;
    }

    @Override
    public void execute() {
        if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
            state.inputString.append(upperletter);
            state.result.pinyin = pinyinSyllable.split(state.inputString
                    .toString());
            handler.setCand();
        }
        else if (state.isInputStringEmpty()) {
            handler.sendText(Character.toString(upperletter));
        }
    }

    @Override
    public void undo() {
        if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
            state.inputString.deleteCharAt(state.inputString.length() - 1);
            state.result.pinyin = pinyinSyllable.split(state.inputString
                    .toString());
            handler.setCand();
        }
    }

}
