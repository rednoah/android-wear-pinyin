package woogle.cmd;

import woogle.chart.CKYDecoder;
import woogle.spi.WooglePinyinHandler;
import woogle.spi.WoogleState;
import woogle.util.PinyinSyllable;

public class LowerLetterCommand implements Command {

    WooglePinyinHandler handler;

    WoogleState         state;

    PinyinSyllable      pinyinSyllable;

    CKYDecoder          decoder;

    char                lowerletter;

    public LowerLetterCommand(WooglePinyinHandler handler, char lowerletter) {
        this.handler = handler;
        state = handler.state;
        pinyinSyllable = handler.pinyinSyllable;
        decoder = handler.decoder;
        this.lowerletter = lowerletter;
    }

    @Override
    public void execute() {
        if (state.isChinese()) {
            state.inputString.append(lowerletter);
            state.result.pinyin = pinyinSyllable.split(state.inputString
                    .toString());
            decoder.buildChart(handler.chart, state.result.getPinyin(), 0);
            decoder.CKYParse(handler.chart, 0);
            handler.setCand();
        }
    }

    @Override
    public void undo() {
        if (state.isChinese()) {
            state.inputString.deleteCharAt(state.inputString.length() - 1);
            if (state.inputString.length() != 0) {
                state.result.pinyin = pinyinSyllable.split(state.inputString
                        .toString());
                decoder.buildChart(handler.chart, state.result.getPinyin(), 0);
                decoder.CKYParse(handler.chart, 0);
            }
            else {
                state.result.clearAll();
                state.cands.clear();
                decoder.clear(handler.chart);
            }
            handler.setCand();
        }
    }
}
