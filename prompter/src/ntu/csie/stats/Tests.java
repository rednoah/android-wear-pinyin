package ntu.csie.stats;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import org.junit.Test;

import ntu.csie.prompter.Record;

public class Tests {

	@Test
	public void metrics() {
		Record r1 = Record.parse("100000	1	15BD9D15C50	LG Watch Sport	KeyDialQwertyZigZag	0	H	H");
		Record r2 = Record.parse("160000	2	15BD9D15C50	LG Watch Sport	KeyDialQwertyZigZag	0	Hello	Hello");

		Sample s = new Sample(Sample.CASE_INSENSITIVE, "Hallo", asList(r1, r2));

		// one 5 char word in 1 minute is 1 WPM but since the first letter doesn't count,
		// so it's actually a 4 char word in 1 minute, so the result should be 0.8 WPM
		assertEquals(0.8, s.getWPM(), 0.0);

		// 1 out of 5 letter has not been corrected
		assertEquals(0.2, s.getCharacterErrorStatistics().getTotalErrorRate(), 0.0);
	}

	@Test
	public void cer() {
		Record r1 = Record.parse("0	1	15BD9D15C50	LG Watch Sport	KeyDialQwertyZigZag	0	th quix	th quix");
		Record r2 = Record.parse("1	2	15BD9D15C50	LG Watch Sport	KeyDialQwertyZigZag	0	⌫	th qui");
		Record r3 = Record.parse("2	3	15BD9D15C50	LG Watch Sport	KeyDialQwertyZigZag	0	ck brpown	th quick brpown");

		CharacterErrorStatistics s = new Sample(Sample.CASE_INSENSITIVE, "the quick brown", asList(r1, r2, r3)).getCharacterErrorStatistics();
		assertEquals("[MSD: 13.33, KSPC: 1.13, Cerr: 5.88, NCerr: 11.76, Terr: 17.65, Ceff: 1.00, Waste: 22.22", s.toString());
	}

}
