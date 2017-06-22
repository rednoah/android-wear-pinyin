package woogle.spi;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Locale;
import java.util.Stack;

import woogle.chart.CKYDecoder;
import woogle.chart.Chart;
import woogle.chart.ChartCell;
import woogle.chart.Path;
import woogle.cmd.Command;
import woogle.cmd.DigitLetterCommand;
import woogle.cmd.LowerLetterCommand;
import woogle.cmd.UpperLetterCommand;
import woogle.ds.PathNode;
import woogle.util.PinyinSyllable;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class WooglePinyinHandler {

	public boolean isShiftDn;

	public boolean consumeKeyTypedAndPressed;

	public WoogleInputMethod woogleInputMethod;

	public Chart chart;

	public Score score;

	public PinyinSyllable pinyinSyllable;

	public WoogleState state;

	public CKYDecoder decoder;

	public Stack<Command> cmdManager;

	WooglePinyinHandler(WoogleInputMethod w) {
		this.woogleInputMethod = w;
		this.score = new Score(WoogleDatabase.nGramLanguageModel, WoogleDatabase.dependencyLanguageModel);
		this.pinyinSyllable = new PinyinSyllable();
		this.state = w.state;
		this.cmdManager = new Stack<Command>();
		this.isShiftDn = false;
		this.consumeKeyTypedAndPressed = false;
		this.chart = new Chart();
		this.decoder = new CKYDecoder(score);
	}

	public void dispatchEvent(char c) {
		Command cmd = new LowerLetterCommand(this, c);
		cmd.execute();
		cmdManager.push(cmd);
		this.consumeKeyTypedAndPressed = true;
	}

	public void dispatchEvent(AWTEvent event) {
		int eventType = event.getID();
		KeyEvent ke = null;
		char c;

		switch (eventType) {
		case MouseEvent.MOUSE_CLICKED:
			MouseEvent me = (MouseEvent) event;
			Component component = me.getComponent();
			if (component instanceof WoogleLookupLabel) {
				int i = ((WoogleLookupLabel) component).index;
				Command cmd = new DigitLetterCommand(this, i);
				cmd.execute();
				cmdManager.push(cmd);
			} else if (component instanceof WoogleLookupSearchButton) {
				String name = component.getName();
				if (name.equals("Up")) {
					this.upAction(me);
				} else if (name.equals("Down")) {
					this.downAction(me);
				} else if (name.equals("Search")) {
					this.searchAction(me);
				}
			}
			break;
		case KeyEvent.KEY_PRESSED:
			ke = (KeyEvent) event;
			c = ke.getKeyChar();

			System.out.println("------------------------");
			System.out.println("KEY_PRESSED: " + c);
			System.out.println("------------------------");

			if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
				this.isShiftDn = true;
			} else if ('0' <= c && c <= '9') {
				Command cmd = new DigitLetterCommand(this, c - '0');
				cmd.execute();
				cmdManager.push(cmd);
				ke.consume();
				this.consumeKeyTypedAndPressed = true;
			} else if ('a' <= c && c <= 'z' || c == '\'') {
				Command cmd = new LowerLetterCommand(this, c);
				cmd.execute();
				cmdManager.push(cmd);
				ke.consume();
				this.consumeKeyTypedAndPressed = true;
			} else if ('A' <= c && c <= 'Z') {
				Command cmd = new UpperLetterCommand(this, c);
				cmd.execute();
				cmdManager.push(cmd);
				ke.consume();
				this.isShiftDn = false;
				this.consumeKeyTypedAndPressed = true;
			} else if (c == '=' || c == '\t') {
				this.downAction(ke);
			} else if (c == '-') {
				this.upAction(ke);
			} else if (c == ' ' || c == '\n') {
				this.enterAction(ke);
			} else if (c == 27) { // esc
				this.escapeAtcion(ke);
			} else if (c == '\b') {
				this.backspaceAction(ke);
			}
			break;
		case KeyEvent.KEY_TYPED:
			ke = (KeyEvent) event;
			if (this.consumeKeyTypedAndPressed && woogleInputMethod.state.isSimplefiedChinese()) {
				ke.consume();
			}
			break;
		case KeyEvent.KEY_RELEASED:
			ke = (KeyEvent) event;
			if (ke.getKeyCode() == KeyEvent.VK_SHIFT && this.isShiftDn) {
				this.shiftAction(ke);
				ke.consume();
				this.isShiftDn = false;
			} else if (this.consumeKeyTypedAndPressed) {
				ke.consume();
				this.consumeKeyTypedAndPressed = false;
			}
			break;
		}
	}

	public void searchAction(MouseEvent me) {
		String url = "http://www.baidu.com/";
		String[] driver = new String[] { "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "X", "Y", "Z" };
		for (String d : driver) {
			try {
				String cmd = d + ":/Program Files/Internet Explorer/IEXPLORE.EXE " + url;
				WoogleLog.log(this, "searchAction: " + cmd);
				Runtime.getRuntime().exec(cmd);
				break;
			} catch (Exception e) {
				WoogleLog.log(e.getMessage());
			}
		}
		me.consume();
	}

	public void downAction(InputEvent e) {
		if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
			if (!state.isLastPage())
				state.pageDown();
			this.consumeKeyTypedAndPressed = true;
			e.consume();
		}
	}

	public void downAction() {
		if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
			if (!state.isLastPage())
				state.pageDown();
			this.consumeKeyTypedAndPressed = true;
		}
	}

	public void upAction(InputEvent e) {
		if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
			if (!state.isFirstPage())
				state.pageUp();
			this.consumeKeyTypedAndPressed = true;
			e.consume();
		}
	}

	public void upAction() {
		if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
			if (!state.isFirstPage())
				state.pageUp();
			this.consumeKeyTypedAndPressed = true;
		}
	}

	public void shiftAction(KeyEvent e) {
		if (state.isSimplefiedChinese()) {
			if (!state.isInputStringEmpty()) {
				woogleInputMethod.sendText(state.inputString.toString());
				this.clear();
			}
			woogleInputMethod.setLocale(Locale.US);
			woogleInputMethod.deactivate(true);
		} else if (state.isUS()) {
			woogleInputMethod.setLocale(Locale.SIMPLIFIED_CHINESE);
			woogleInputMethod.activate();
		}
	}

	public void enterAction(KeyEvent e) {
		if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
			WoogleLookupCandidate c = state.getCand(0);
			c.c.selectedIndex = c.pathIndex;
			// 如果是第一页首选
			if (state.isFirstPage())
				state.result.clearResult();
			state.result.pushChartCell(c.c);
			woogleInputMethod.sendText(state.result.toSendText());
			this.clear();
			e.consume();
			this.consumeKeyTypedAndPressed = true;
		}
	}

	public void backspaceAction(KeyEvent e) {
		if (state.isSimplefiedChinese() && !state.isInputStringEmpty()) {
			Command cmd = cmdManager.pop();
			cmd.undo();
			e.consume();
			this.consumeKeyTypedAndPressed = true;
		}
	}

	public void escapeAtcion(KeyEvent e) {
		if (state.isSimplefiedChinese()) {
			this.clear();
			e.consume();
			this.consumeKeyTypedAndPressed = true;
		}
	}

	public void clear() {
		this.isShiftDn = false;
		this.consumeKeyTypedAndPressed = false;
		state.inputString = new StringBuilder();
		state.cands.clear();
		state.currentCandPage = 0;
		state.result.clearAll();
	}

	public void setCand() {
		state.cands.clear();
		state.currentCandPage = 0;
		HashSet<String> list = new HashSet<String>();
		ChartCell cell = decoder.getOneBestCell(chart);
		// System.out.println("--------------------");
		// System.out.println(cell);
		// System.out.println("--------------------");
		if (cell != null) {
			for (int i = 0; list.size() <= 3 && i < cell.sentences.size(); i++) {
				PathNode p = cell.getPath(i);
				String s = Path.getSentenceString(p);
				if (!list.contains(s)) {
					state.cands.add(new WoogleLookupCandidate(i, cell));
					list.add(s);
				}
			}
		}
		int row = state.result.selectedCell.size();
		for (int j = row; j < chart.num; j++) {
			cell = chart.getCell(row, j);
			if (cell == null)
				continue;
			for (int k = 0; k < cell.sentences.size(); k++) {
				PathNode p = cell.getPath(k);
				String s = Path.getSentenceString(p);
				if (s.length() == 1 && !list.contains(s)) {
					state.cands.add(new WoogleLookupCandidate(k, cell));
					list.add(s);
				}
			}
		}
	}

	public void sendText(String text) {
		woogleInputMethod.sendText(text);
	}

}
