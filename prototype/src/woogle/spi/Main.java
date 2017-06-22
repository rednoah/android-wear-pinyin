package woogle.spi;

public class Main {

	public static void main(String[] args) {
		WoogleInputMethod w = new WoogleInputMethod();

		pressKeys(w, "ni'jin'tian'hao'ma");

		w.pinyinHandler.downAction();
		w.updateCompAndCand();

		w.pinyinHandler.downAction();
		w.updateCompAndCand();

		w.pinyinHandler.downAction();
		w.updateCompAndCand();
	}

	public static void pressKeys(WoogleInputMethod w, String s) {
		s.chars().forEach(c -> w.dispatchEvent((char) c));
	}

}
