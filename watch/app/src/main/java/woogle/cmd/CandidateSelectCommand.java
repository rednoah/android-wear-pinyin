package woogle.cmd;

import woogle.chart.CKYDecoder;
import woogle.chart.ChartCell;
import woogle.spi.WoogleLookupCandidate;
import woogle.spi.WooglePinyinHandler;
import woogle.spi.WoogleState;
import woogle.util.PinyinSyllable;

public class CandidateSelectCommand implements Command {

    WooglePinyinHandler handler;

    WoogleState state;

    PinyinSyllable pinyinSyllable;

    CKYDecoder decoder;

    WoogleLookupCandidate candidate;

    public CandidateSelectCommand(WooglePinyinHandler handler, WoogleLookupCandidate candidate) {
        this.handler = handler;
        state = handler.state;
        pinyinSyllable = handler.pinyinSyllable;
        decoder = handler.decoder;
        this.candidate = candidate;
    }

    @Override
    public void execute() {
        if (state.isChinese() && !state.isInputStringEmpty()) {
            candidate.c.selectedIndex = candidate.pathIndex;
            decoder.setChartCellSelected(handler.chart, candidate.c);
            if (state.isFirstPage() && candidate.c.row == 0
                    && candidate.c.column == handler.chart.num - 1) {
                state.result.clearResult();
            }
            state.result.pushChartCell(candidate.c);
            if (state.result.isFinished()) {
                handler.sendText(state.result.toSendText());
                handler.clear();
            } else {
                decoder.CKYParse(handler.chart, candidate.c.column);
                handler.setCand();
            }
        }
    }


    @Override
    public void undo() {
        if (state.isChinese() && !state.isInputStringEmpty()) {
            ChartCell c = state.result.popChartCell();
            c.selectedIndex = ChartCell.NOT_SEL;
            decoder.setChartCellSelected(handler.chart, c);
            decoder.CKYParse(handler.chart, c.column);
            handler.setCand();
        }
    }
}
