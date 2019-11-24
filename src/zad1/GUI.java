package zad1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class GUI {

	private BorderPane mainLayout;
	private VBox controlLayout, infoLayout;
	private HBox extraButtonPanel;
	private FlowPane webLayout;
	private Service service;


	private Button btnWeather, btnNBP, btnRate, btnWiki, btnCountry;
	private TextField tfWeather, tfRate, tfWiki, tfCountry;
	private Label lblNBPPanelInfo, lblWeatherPanelInfo, lblRateForPanelInfo, lblWikiPanelInfo, lblCity, lblSky, lblTemp,
			lblPressure, lblHumidity, lblTempMin, lblTempMax, lblWindSpeed, lblRateValue, lblNBPRate;
	private Separator line;

	private String textWeather = "Warsaw", textRate = "USD";

	private WebView wikiPage;
	private WebEngine webEngine;

	public GUI(Service service) {
		this.service = service;

		btnWeather = new Button();
		btnNBP = new Button();
		btnRate = new Button();
		btnWiki = new Button();
		btnCountry = new Button();

		tfWeather = new TextField();
		tfRate = new TextField();
		tfWiki = new TextField();
		tfCountry = new TextField();

		lblNBPPanelInfo = new Label();
		lblWeatherPanelInfo = new Label();
		lblRateForPanelInfo = new Label();
		lblWikiPanelInfo = new Label();

		lblCity = new Label();
		lblSky = new Label();
		lblTemp = new Label();
		lblPressure = new Label();
		lblHumidity = new Label();

		lblTempMin = new Label();
		lblTempMax = new Label();
		lblWindSpeed = new Label();
		lblRateValue = new Label();
		lblNBPRate = new Label();

		line = new Separator(Orientation.HORIZONTAL);

	}

	public void addLayouts() {
		mainLayout = new BorderPane();
		controlLayout = new VBox();
		infoLayout = new VBox();
		extraButtonPanel = new HBox();
		webLayout = new FlowPane();

		infoLayout.setPadding(new Insets(10, 50, 50, 50));
		infoLayout.setSpacing(10);
	}

	public void addNavigation() {

		lblNBPPanelInfo.setText("NBP Rate:");
		lblWeatherPanelInfo.setText("Weather:");
		lblRateForPanelInfo.setText("Currency rate:");
		lblWikiPanelInfo.setText("Wiki page:");

		lblNBPPanelInfo.setId("control");
		lblWeatherPanelInfo.setId("control");
		lblRateForPanelInfo.setId("control");
		lblWikiPanelInfo.setId("control");

		tfWeather.setPromptText("Enter here city");
		tfRate.setPromptText("Enter here currency code");
		tfWiki.setPromptText("Enter here city");
		tfCountry.setPromptText("Enter here country");

		tfWeather.setTooltip(new Tooltip("Enter here city name that You want to display weather"));
		tfRate.setTooltip(new Tooltip("Enter here currency code to check it's rate"));
		tfWiki.setTooltip(new Tooltip("Enter here city to show its Wiki page"));
		tfCountry.setTooltip(new Tooltip("Enter here country name that You want to check NBP rate"));

		// tfWeather.setPrefSize(180, 10);
		// tfRate.setPrefSize(180, 10);
		// tfWiki.setPrefSize(180, 10);
		// tfCountry.setPrefSize(180, 10);

		btnWeather.setText("Show Weather");
		btnNBP.setText("Show NBP rate");
		btnRate.setText("Show rate");
		btnWiki.setText("show Wiki Page");
		btnCountry.setText("Set this country");

		setButtonEvents();
		setTextFieldEvents();

		Button[] btnArray = { btnCountry, btnWeather, btnRate, btnWiki, btnNBP };
		TextField[] tfArray = { tfCountry, tfWeather, tfRate, tfWiki };
		Label[] lblArray = { lblNBPPanelInfo, lblWeatherPanelInfo, lblRateForPanelInfo, lblWikiPanelInfo };

		Label lblTitle = new Label("Control Panel:");
		lblTitle.setPadding(new Insets(10));
		controlLayout.getChildren().add(lblTitle);
		line.setId("mainSeparator");
		controlLayout.getChildren().add(line);

		for (int i = 0; i < tfArray.length; i++) {
			controlLayout.getChildren().add(lblArray[i]);
			controlLayout.getChildren().add(tfArray[i]);
			if(i == 0){
				extraButtonPanel.getChildren().add(btnCountry);
				extraButtonPanel.getChildren().add(btnNBP);
				controlLayout.getChildren().add(extraButtonPanel);
			} else {
				controlLayout.getChildren().add(btnArray[i]);
			}
			controlLayout.getChildren().add(new Separator(Orientation.HORIZONTAL));
		}
		mainLayout.setLeft(controlLayout);
	}

	public void addLabels() {
		Label lblWeather = new Label(), lblNBP = new Label(), lblRate = new Label();

		lblWeather.setText("Weather:");
		lblNBP.setText("NBP rate:");
		lblRate.setText("Rate:");

		infoLayout.getChildren().add(lblWeather);
		initializeWeatherLables(textWeather);
		addWeatherToPane();

		infoLayout.getChildren().add(lblNBP);
		initializeNBPLabel();
		infoLayout.getChildren().add(lblNBPRate);

		infoLayout.getChildren().add(lblRate);
		initializeRateLabel(textRate);
		infoLayout.getChildren().add(lblRateValue);

		mainLayout.setRight(infoLayout);
	}

	private void initializeWeatherLables(String city) {
		String[] s = service.findWeather(service.getWeather(city));

		if (s[0].equals("Couldn't find weather :(")) {
			lblCity.setText(s[0]);
			lblSky.setText(s[1]);
			lblTemp.setText(s[2]);
			lblTempMin.setText(s[3]);
			lblTempMax.setText(s[4]);
			lblPressure.setText(s[5]);
			lblHumidity.setText(s[6]);
			lblWindSpeed.setText(s[7]);
		} else {

			lblCity.setText(s[0]);
			lblSky.setText(s[1]);
			lblTemp.setText("Temperature: " + s[2] + "\u00b0 C");
			lblTempMin.setText("Min temperature: " + s[3] + "\u00b0 C");
			lblTempMax.setText("Max temperature: " + s[4] + "\u00b0 C");
			lblPressure.setText("Pressure: " + s[5] + "hPa");
			lblHumidity.setText("Humidity: " + s[6] + "%");
			lblWindSpeed.setText("Wind: " + s[7] + "km/s");
		}
	}

	private void addWeatherToPane() {
		lblCity.setId("info_h1");
		lblSky.setId("info");
		lblTemp.setId("info");
		lblTempMin.setId("info");
		lblTempMax.setId("info");
		lblPressure.setId("info");
		lblHumidity.setId("info");
		lblWindSpeed.setId("info");

		infoLayout.getChildren().add(lblCity);
		infoLayout.getChildren().add(lblSky);
		infoLayout.getChildren().add(lblTemp);
		infoLayout.getChildren().add(lblTempMin);
		infoLayout.getChildren().add(lblTempMax);
		infoLayout.getChildren().add(lblPressure);
		infoLayout.getChildren().add(lblHumidity);
		infoLayout.getChildren().add(lblWindSpeed);

	}

	private void initializeNBPLabel() {
		String message = service.getNBPRate().toString();

		if (message.equals("0.0")) {
			message = "Wrong country name!";
		}

		lblNBPRate.setText(message);
		lblNBPRate.setId("info");

	}

	public void initializeRateLabel(String code) {
		String message = service.getRateFor(code).toString();

		if (message.equals("0.0")) {
			message = "Wrong currency code!";
		}

		lblRateValue.setText(message);
		lblRateValue.setId("info");
	}

	public void addWebView() {
		wikiPage = new WebView();
		webEngine = wikiPage.getEngine();
		webEngine.load("https://en.wikipedia.org/wiki/Warsaw");

		webLayout.getChildren().add(wikiPage);

		mainLayout.setCenter(webLayout);
	}

	public void changePage(String city) {
		webEngine.load("https://en.wikipedia.org/wiki/" + city);
	}

	public void setButtonEvents() {

		btnWeather.setOnAction((event) -> {
			String fromTextArea = tfWeather.getText();
			initializeWeatherLables(fromTextArea);
		});

		btnNBP.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				initializeNBPLabel();

			}
		});

		btnRate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String fromTextArea = tfRate.getText();
				initializeRateLabel(fromTextArea);

			}
		});

		btnWiki.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String fromTextArea = tfWiki.getText();
				changePage(fromTextArea);
			}
		});

		btnCountry.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String fromTextArea = tfCountry.getText();
				service.setCountry(fromTextArea);
			}
		});
	}

	public void setTextFieldEvents() {

		tfWeather.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String fromTextArea = tfWeather.getText();
					initializeWeatherLables(fromTextArea);
				}
			}
		});

		tfRate.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String fromTextArea = tfRate.getText();
					initializeRateLabel(fromTextArea);
				}
			}
		});

		tfWiki.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String fromTextArea = tfWiki.getText();
					changePage(fromTextArea);
				}
			}
		});

		tfCountry.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String fromTextArea = tfCountry.getText();
					service.setCountry(fromTextArea);
				}
			}
		});
	}

	public BorderPane getLayout() {

		return mainLayout;
	}
}
