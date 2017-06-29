package ntu.csie.prompter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TTS {

	private static final Executor runner = Executors.newSingleThreadScheduledExecutor();

	public static void say(String s) {
		execute("say", "-v", "Ting-Ting", s);
	}

	private static void execute(String... command) {
		runner.execute(() -> {
			try {
				new ProcessBuilder(command).inheritIO().start().waitFor();
			} catch (Exception e) {
				Logger.getLogger(TTS.class.getName()).log(Level.WARNING, e, e::toString);
			}
		});
	}

}
