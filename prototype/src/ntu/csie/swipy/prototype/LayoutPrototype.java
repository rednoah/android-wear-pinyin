package ntu.csie.swipy.prototype;

import java.util.stream.Stream;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ntu.csie.swipy.model.Pinyin;
import ntu.csie.swipy.model.Pinyin.Final;
import ntu.csie.swipy.model.Pinyin.Initial;

public class LayoutPrototype extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Layout");

		MigPane layout = new MigPane("fill");

		System.out.format("%d Initials%n", Pinyin.Initial.values().length);
		System.out.format("%d Pinyin%n", Stream.of(Pinyin.Initial.values()).mapToInt(i -> i.getFinals().size()).sum());

		// main keyboard
		FlowPane main = new FlowPane(Orientation.HORIZONTAL);
		for (Pinyin.Initial f : Pinyin.Initial.values()) {
			Button b = new Button(f.toString());
			main.getChildren().add(b);
		}
		layout.add(main, "grow, wrap paragraph");

		// final keyboards
		for (Pinyin.Initial i : Pinyin.Initial.values()) {
			FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
			for (Pinyin.Final f : i.getFinals()) {
				Button b = new Button(new Pinyin(i, f).toString());
				flow.getChildren().add(b);
			}

			Separator s = new Separator();
			s.setOrientation(Orientation.HORIZONTAL);
			layout.add(s, "grow, wrap paragraph");
			layout.add(flow, "grow, wrap paragraph");

			System.out.format("%s (%d)%n", i, i.getFinals().size());
		}

		// final keyboards
		for (Pinyin.Initial i : Pinyin.Initial.values()) {
			FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
			flow.setVgap(20);
			flow.setHgap(20);

			Final[] finals = i.getFinals().toArray(new Final[0]);
			int batchSize = 4;

			for (int b = 0; b < finals.length; b += batchSize) {
				GridPane grid = new GridPane();
				grid.setPrefSize(120, 80);

				for (int rowIndex = 0; rowIndex < 2; rowIndex++) {
					RowConstraints rc = new RowConstraints();
					rc.setVgrow(Priority.ALWAYS); // allow row to grow
					rc.setFillHeight(true); // ask nodes to fill height for row
					// other settings as needed...
					grid.getRowConstraints().add(rc);
				}
				for (int colIndex = 0; colIndex < 2; colIndex++) {
					ColumnConstraints cc = new ColumnConstraints();
					cc.setHgrow(Priority.ALWAYS); // allow column to grow
					cc.setFillWidth(true); // ask nodes to fill space for column
					// other settings as needed...
					grid.getColumnConstraints().add(cc);
				}

				grid.add(createButton(i, finals[b]), 0, 0);
				if (b + 1 < finals.length) {
					grid.add(createButton(i, finals[b + 1]), 0, 1);
				}
				if (b + 2 < finals.length) {
					grid.add(createButton(i, finals[b + 2]), 1, 0);
				}
				if (b + 3 < finals.length) {
					grid.add(createButton(i, finals[b + 3]), 1, 1);
				}

				flow.getChildren().add(grid);
			}

			Separator s = new Separator();
			s.setOrientation(Orientation.HORIZONTAL);
			layout.add(s, "grow, wrap paragraph");
			layout.add(flow, "grow, wrap paragraph");

			System.out.format("%s (%d)%n", i, i.getFinals().size());
		}

		ScrollPane sp = new ScrollPane();
		sp.setFitToHeight(false);
		sp.setFitToWidth(true);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setContent(layout);

		Scene scene = new Scene(sp, 1200, 800, Color.BLACK);
		stage.setScene(scene);
		stage.show();
	}

	public static Button createButton(Initial i, Final f) {
		Button b = new Button(new Pinyin(i, f).toString());
		b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		return b;
	}

	public static void main(String[] argv) {
		launch(argv);
	}

}
