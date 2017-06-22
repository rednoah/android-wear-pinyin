package woogle.cmd;

import woogle.chart.CKYDecoder;
import woogle.chart.ChartCell;
import woogle.spi.WoogleLookupCandidate;
import woogle.spi.WooglePinyinHandler;
import woogle.spi.WoogleState;
import woogle.util.PinyinSyllable;

public class DigitLetterCommand implements Command {

    WooglePinyinHandler handler;

    WoogleState         state;

    PinyinSyllable      pinyinSyllable;

    CKYDecoder          decoder;

    int                 digit;

    public DigitLetterCommand(WooglePinyinHandler handler, int digit) {
        this.handler = handler;
        state = handler.state;
        pinyinSyllable = handler.pinyinSyllable;
        decoder = handler.decoder;
        this.digit = digit;
    }

    @Override
    public void execute() {
        if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
            if (1 <= digit && digit <= 5) {
                WoogleLookupCandidate c = state.getCand(digit - 1);
                c.c.selectedIndex = c.pathIndex;
                decoder.setChartCellSelected(handler.chart, c.c);
                if (state.isFirstPage() && c.c.row == 0
                        && c.c.column == handler.chart.num - 1) {
                    state.result.clearResult();
                }
                state.result.pushChartCell(c.c);
                if (state.result.isFinished()) {
                    handler.sendText(state.result.toSendText());
                    handler.clear();
                }
                else {
                    decoder.CKYParse(handler.chart, c.c.column);
                    handler.setCand();
                }
            }
        }
        else if (state.isInputStringEmpty()) {
            handler.sendText(Integer.toString(digit));
        }
    }

    @Override
    public void undo() {
        if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
            ChartCell c = state.result.popChartCell();
            c.selectedIndex = ChartCell.NOT_SEL;
            decoder.setChartCellSelected(handler.chart, c);
            decoder.CKYParse(handler.chart, c.column);
            handler.setCand();
        }
    }
}
