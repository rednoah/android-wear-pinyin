
package ntu.csie.prompter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ArgumentBean {

	@Option(name = "-Participant")
	public int participant = 1;

	@Option(name = "-PhraseCount")
	public int phraseCount = 25;

	@Option(name = "-Device")
	public String device = "unknown"; // emulator
	// public String device = "701KPTM0011921"; // LG Watch Style
	// public String device = "702KPKN003413"; // LG Watch Sport

	@Option(name = "-Node")
	public String node = "ws://oasis1.csie.ntu.edu.tw:22148/listen";
	// public String node = "ws://10.0.1.2:22148/listen";

	@Option(name = "-StudyPlan")
	public Path studyPlan = Paths.get("pilot-study-plan.tsv");

	@Option(name = "-PhraseSet")
	public Path phraseSet = Paths.get("pilot-study-phrase-set.txt");

	@Option(name = "-Record")
	public Path record = Paths.get("pilot-study-record.tsv");

	public KeyboardLayout[] getKeyboardOrder() throws IOException {
		return Files.lines(studyPlan).filter(line -> {
			return participant == new Scanner(line).nextInt();
		}).map(line -> {
			return Record.TAB.splitAsStream(line).skip(1).map(KeyboardLayout::valueOf).toArray(KeyboardLayout[]::new);
		}).findFirst().get();
	}

	public String[] getPhraseSet() throws IOException {
		return lines(phraseSet);
	}

	private String[] lines(Path p) throws IOException {
		return Files.lines(p).filter(s -> s.length() > 0).toArray(String[]::new);
	}

	public ArgumentBean(String... args) throws CmdLineException {
		CmdLineParser parser = new CmdLineParser(this);
		parser.parseArgument(args);
	}

}
