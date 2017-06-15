package ntu.csie.swipy.model;

import static java.util.stream.Collectors.*;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Pinyin {

	public enum Initial {

		a("a i o n ng"),

		o("o u"),

		e("e r n ng"),

		y("i a ao e ou an in ang ing ong u ue uan"),

		w("u a o ai ei an en ang eng"),

		b("a o ai ei ao an en ang eng i iao ie ian in ing u"),

		p("a o ai ei ao ou an en ang eng i iao ie ian in ing u"),

		m("a o e ai ei ao ou an en ang eng i iao ie iu ian in ing u"),

		f("a o ei ou an en ang eng u"),

		d("a e ai ei ao ou an en ang eng ong i iao ie iu ian ing u uo ui uan un"),

		t("a e ai ei ao ou an ang eng ong i iao ie ian ing u uo ui uan un"),

		n("a e ai ei ao ou an en ang eng ong i iao ie iu ian in iang ing u uo uan"),

		l("a e ai ei ao ou an ang eng ong i ia iao ie iu ian in iang ing u uo uan un ü üe"),

		g("a e ai ei ao ou an en ang eng ong u ua uo uai ui uan un uang"),

		k("a e ai ei ao ou an en ang eng ong u ua uo uai ui uan un uang"),

		h("a e ai ei ao ou an en ang eng ong u ua uo uai ui uan un uang"),

		z("a e i ai ei ao ou an en ang eng ong u uo ui uan un"),

		c("a e i ai ei ao ou an en ang eng ong u uo ui uan un"),

		s("a e i ai ei ao ou an en ang eng ong u uo ui uan un"),

		zh("a e i ai ei ao ou an en ang eng ong u ua uo uai ui uan un uang"),

		ch("a e i ai ao ou an en ang eng ong u ua uo uai ui uan un uang"),

		sh("a e i ai ei ao ou an en ang eng u ua uo uai ui uan un uang"),

		r("e i ao ou an en ang eng ong u ua uo ui uan un"),

		j("i ia iao ie iu ian in iang ing iong u ue uan un"),

		q("i ia iao ie iu ian in iang ing iong u ue uan un"),

		x("i ia iao ie iu ian in iang ing iong u ue uan un");

		private final Set<Final> finals;

		private Initial(String s) {
			finals = parse(s);
		}

		public Set<Final> getFinals() {
			return finals;
		}

		private static Set<Final> parse(String s) {
			return Pattern.compile(" ").splitAsStream(s).map(Final::valueOf).collect(toCollection(() -> EnumSet.noneOf(Final.class)));
		}

	}

	public enum Final {
		a, o, e, r, ai, ei, ao, ou, n, ng, an, en, ang, eng, ong, i, ia, iao, ie, iu, ian, in, iang, ing, iong, u, ua, uo, ue, uai, ui, uan, un, uang, ueng, ü, üe, üan;
	}

	private final Initial head;
	private final Final tail;

	public Pinyin(Initial head, Final tail) {
		this.head = head;
		this.tail = tail;
	}

	public boolean hasFinal() {
		return !head.name().equals(tail.name());
	}

	public Initial getInitial() {
		return head;
	}

	public Final getFinal() {
		return tail;
	}

	@Override
	public String toString() {
		return hasFinal() ? head.toString() + tail.toString() : head.toString();
	}

}
