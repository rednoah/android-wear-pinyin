package ntu.csie.prompter;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static ntu.csie.prompter.Symbols.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.tbee.javafx.scene.layout.MigPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class Prompter extends Application {

	enum State {
		Connecting, SelectKeyboard, PhraseInput, Complete;
	}

	private State state = State.Connecting;

	private WebSocketClient client = new WebSocketClient();
	private Session session;

	private Cursor<KeyboardLayout> keyboard;
	private Cursor<String> phrase;

	public void start() throws Exception {
		keyboard = new Cursor<>(args.getKeyboardOrder());
		phrase = new Cursor<>(args.getPhraseSet());

		client.start();
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::ping, 0, 60, TimeUnit.SECONDS);

		promptNextKeyboard();
	}

	public boolean onRecord(Record r) {
		setText(status, r.buffer);

		if (r.keyboard != keyboard.current()) {
			setText(status, "BAD KEYBOARD: " + r.keyboard);
			return false;
		}

		switch (state) {
		case SelectKeyboard:
			if (START_OF_TEXT.equals(r.key)) {
				promptNextPhrase();
				return true;
			}
			return false;
		case PhraseInput:
			if (END_OF_TEXT.equals(r.key)) {
				disconnect(StatusCode.NORMAL, "Complete");
				say("Phrase Input Complete");
				promptNextKeyboard();
				return true;
			} else if (ENTER.equalsIgnoreCase(r.key) && r.phrase <= args.phraseCount) {
				setText(progress, String.format("%d / %d", r.phrase, args.phraseCount));
				if (r.phrase < args.phraseCount) {
					promptNextPhrase();
				}
				return true;
			}

			// collect all keystrokes
			return true;
		default:
			return false;
		}
	}

	public void promptNextKeyboard() {
		setState(state);

		if (keyboard.hasNext()) {
			keyboard.next();

			setState(State.Connecting);
			setText(title, String.format("%d %s", keyboard.position() + 1, keyboard.current()));
			setText(prompt, "Connecting...");

			connect();
			return;
		}

		setState(state);
		say("All Complete!");
	}

	public void promptNextPhrase() {
		setState(State.PhraseInput);

		say(phrase.next());
		System.out.println(phrase.position());
		System.out.println(phrase.current());
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;

		// listen to device
		try {
			this.session.getRemote().sendString("CONNECT " + args.device);
		} catch (IOException e) {
			onError(e);
		}
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		setText(status, String.format("%d %s", statusCode, reason));
	}

	private Record lastRecord;
	private TreeSet<Record> buffer = new TreeSet<Record>();

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.println(msg);

		if (msg.startsWith("CONNECTED") && msg.endsWith(args.device)) {
			setText(prompt, String.format("Please select <%s>", keyboard.current()));
			setText(status, "Connected");
			setState(State.SelectKeyboard);
			return;
		}

		if (msg.startsWith("PONG")) {
			return;
		}

		try {
			Record record = Record.parse(msg);

			synchronized (buffer) {
				// reset sequence on first package
				if (record.number == 1) {
					lastRecord = null;
					buffer.clear();
				}

				// check for out-of-order messages
				if (lastRecord != null && record.number != lastRecord.number + 1) {
					System.err.println("BUFFER MESSAGE OUT OF ORDER: " + record.number);

					if (record.number > lastRecord.number) {
						buffer.add(record); // buffer for later
					} else {
						System.err.println("IGNORE DUPLICATE MESSAGE: " + record.number);
					}

					return;
				}

				Record[] records = { record };
				if (buffer.size() > 0) {
					// collect next sequence
					LinkedList<Record> batch = new LinkedList<Record>();
					batch.add(record);

					Iterator<Record> i = buffer.iterator();
					while (i.hasNext()) {
						Record r = i.next();
						if (r.number == batch.getLast().number + 1) {
							batch.addLast(r);
							i.remove();
						}
					}

					System.err.println("DRAIN BUFFER: " + batch.stream().map(r -> r.number).map(Objects::toString).collect(joining(" ")));
					records = batch.toArray(new Record[0]);
				}

				for (Record r : records) {
					if (onRecord(r)) {
						try {
							Files.write(args.record, singleton(r.toString()), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
							Thread.sleep(100);
						} catch (Exception e) {
							onError(e);
						}
					}
					lastRecord = r;
				}
			}
		} catch (IllegalStateException e) {
			onError(e);
			disconnect(StatusCode.NORMAL, "Handshake failed");

			// rewind and try again
			if (keyboard.hasPrevious()) {
				keyboard.previous();
			}

			promptNextKeyboard();
		} catch (Exception e) {
			onError(e);
		}
	}

	public void ping() {
		try {
			if (session != null && session.isOpen()) {
				session.getRemote().sendString("PING");
			}
		} catch (Exception e) {
			onError(e);
		}
	}

	private void connect() {
		try {
			client.connect(this, new URI(args.node), new ClientUpgradeRequest());
		} catch (Exception e) {
			onError(e);
		}
	}

	private void disconnect(int status, String message) {
		try {
			if (session != null && session.isOpen()) {
				session.close(status, message);
				client.destroy();
			}
		} catch (Exception e) {
			onError(e);
		}
	}

	public void setState(State state) {
		this.state = state;
		this.setText(mode, state.toString());
	}

	public void say(String s) {
		setText(prompt, s);
		TTS.say(s);
	}

	private void onError(Exception e) {
		setText(status, e.toString());
		e.printStackTrace();
	}

	private void setText(Text t, String s) {
		Platform.runLater(() -> t.setText(s));
	}

	private Text prompt = createTextField();
	private Text title = createTextField();
	private Text progress = createTextField();
	private Text status = createTextField();
	private Text mode = createTextField();

	private Text createTextField() {
		Text t = new Text();
		t.setFill(Color.WHITE);
		return t;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Prompter");

		MigPane layout = new MigPane("fill");
		prompt.setFont(Font.font(Font.getDefault().getName(), 64));

		layout.add(title, "pos 0al 0al");
		layout.add(progress, "pos 1al 0al");
		layout.add(prompt, "pos 0.5al 0.5al");
		layout.add(status, "pos 0al 1.0al");
		layout.add(mode, "pos 1.0al 1.0al");

		Scene scene = new Scene(layout, 1200, 800, Color.BLACK);
		stage.setScene(scene);
		stage.show();

		// disconnect on shutdown
		stage.setOnCloseRequest(event -> {
			disconnect(StatusCode.SHUTDOWN, "Close");
			System.exit(0);
		});

		start();
	}

	private static ArgumentBean args;

	public static void main(String[] argv) {
		try {
			args = new ArgumentBean(argv);
			launch(argv);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
