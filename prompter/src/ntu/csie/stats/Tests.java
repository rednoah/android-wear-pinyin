package ntu.csie.stats;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import ntu.csie.prompter.Record;

public class Tests {

	@Test
	public void metrics() {
		Sample s = new Sample("過的開心麼", getTestRecord());

		assertEquals(24037, s.getDuration().toMillis(), 0.0);
		assertEquals(12.48, s.getHanziPerMinute(), 0.01);

		assertEquals(12, s.getPinyinInputCount(), 0.0);
		assertEquals(3, s.getHanziSelectionCount(), 0.0);
		assertEquals(3, s.getDeleteCount(), 0.0);
		
		// 3 out of 18 key strokes
		assertEquals(0.166, s.getCharacterErrorStatistics().getTotalErrorRate(), 0.001);
		assertEquals(0.285, s.getCharacterErrorStatistics().getWastedBandwidth(), 0.001);
		assertEquals(0.714, s.getCharacterErrorStatistics().getUtilisedBandwidth(), 0.001);
	}

	
	

	public static List<Record> getTestRecord() {
		return Stream.of("1499255634691	62	15D1297BBD1	LG Watch Style	PinyinSyllables	3	g	g",
				"1499255638379	63	15D1297BBD1	LG Watch Style	PinyinSyllables	3	uo'	guo'",
				"1499255641287	64	15D1297BBD1	LG Watch Style	PinyinSyllables	3	sh	guo'sh",
				"1499255642161	65	15D1297BBD1	LG Watch Style	PinyinSyllables	3	⌫	guo'",
				"1499255642992	66	15D1297BBD1	LG Watch Style	PinyinSyllables	3	d	guo'd",
				"1499255643862	67	15D1297BBD1	LG Watch Style	PinyinSyllables	3	e'	guo'de'",
				"1499255646690	68	15D1297BBD1	LG Watch Style	PinyinSyllables	3	過的	過的",
				"1499255647572	69	15D1297BBD1	LG Watch Style	PinyinSyllables	3	k	過的k",
				"1499255648666	70	15D1297BBD1	LG Watch Style	PinyinSyllables	3	a'	過的ka'",
				"1499255649543	71	15D1297BBD1	LG Watch Style	PinyinSyllables	3	⌫	過的k",
				"1499255650075	72	15D1297BBD1	LG Watch Style	PinyinSyllables	3	ai'	過的kai'",
				"1499255651010	73	15D1297BBD1	LG Watch Style	PinyinSyllables	3	⌫	過的k",
				"1499255653234	74	15D1297BBD1	LG Watch Style	PinyinSyllables	3	ai'	過的kai'",
				"1499255654773	75	15D1297BBD1	LG Watch Style	PinyinSyllables	3	x	過的kai'x",
				"1499255655879	76	15D1297BBD1	LG Watch Style	PinyinSyllables	3	開心	過的開心",
				"1499255656803	77	15D1297BBD1	LG Watch Style	PinyinSyllables	3	m	過的開心m",
				"1499255657937	78	15D1297BBD1	LG Watch Style	PinyinSyllables	3	e'	過的開心me'",
				"1499255658728	79	15D1297BBD1	LG Watch Style	PinyinSyllables	3	麼	過的開心麼",
				"1499255659955	80	15D1297BBD1	LG Watch Style	PinyinSyllables	3	⏎	過的開心麼")
				.map(Record::parse)
				.collect(toList());
	}

}
