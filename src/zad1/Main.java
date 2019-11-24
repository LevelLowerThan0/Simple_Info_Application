package zad1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{

	static GUI gui;

	public static void main(String[] args) {

		Service s = new Service("Ukraine");
	    String weatherJson = s.getWeather("Warsaw");
	    System.out.println(weatherJson);
	    Double rate1 = s.getRateFor("USD");
	    System.out.println(rate1);
	    @SuppressWarnings("unused")
		Double rate2 = s.getNBPRate();

	    gui = new GUI(s);

	    launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 1200, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setTitle("TPO02 - Pogoda, Kurs NBP, Kurs walut, Wikipedia");
			gui.addLayouts();
			gui.addNavigation();
			gui.addLabels();
			gui.addWebView();

			root.getChildren().add(gui.getLayout());

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
