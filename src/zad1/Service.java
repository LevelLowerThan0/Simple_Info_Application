package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Service {

	String country, currencyCode;

	public Service(String kraj) {
		country = kraj;
		currencyCode = getCurrency();
	}

	public String getWeather(String miasto) {
		URL url;
		String errorMessage;
		try {
			url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + miasto
					+ "&appid=d179933ff7367f9af491b5cf1a87e7d3");

			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();

			String weather;

			while ((weather = br.readLine()) != null) {
				sb.append(weather);
			}

			br.close();

			return sb.toString();
		} catch (IOException e) {
			errorMessage = "Bledna nazwa miasta!";
		}
		return errorMessage;
	}

	public Double getRateFor(String kod_waluty) {
		URL url;

		try {
			url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();

			double rate = 0.0;
			String source;

			while ((source = br.readLine()) != null) {
				sb.append(source);

			}

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(sb.toString()));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("Cube");

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				if (element.getAttribute("currency").equals(kod_waluty)) {
					rate = Double.parseDouble(element.getAttribute("rate"));
				}
			}

			br.close();

			return rate;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return 0.0;

	}

	public Double getNBPRate() {

		URL urlA, urlB;

		double rate = 0.0;
		try {
			urlA = new URL("http://www.nbp.pl/kursy/xml/a063z190329.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(urlA.openStream()));
			StringBuilder sbA = new StringBuilder(), sbB = new StringBuilder();
			String rateA, rateB;

			while ((rateA = br.readLine()) != null) {
				sbA.append(rateA);
			}

			urlB = new URL("http://www.nbp.pl/kursy/xml/b013z190327.xml");
			br.close();
			br = new BufferedReader(new InputStreamReader(urlB.openStream()));

			while ((rateB = br.readLine()) != null) {
				sbB.append(rateB);
			}
			br.close();

			rate = findRate(sbA);
			if (rate == 0.0) {
				rate = findRate(sbB);
			}

			return rate;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	/*
	 * extra methods to operate on data obtained from methods: getWeather(),
	 * getRateFor(String kod_waluty), getNBPRate()
	 */

	/* Parse weather JSON string to read weather */
	public String[] findWeather(String s) {

		if (s.equals("Bledna nazwa miasta!")) {

			String[] details = { "Couldn't find weather :(", "Check your city and try again", ":(", ":(", ":(", ":(",
					":(", "Wrong city name!" };

			return details;
		}

		String[] details = new String[8];
		String[] detailsRegex = { ".+\"name\":\"((\\w+\\s*)+)\".+", ".+\"description\":\"((\\w+\\s*)+)\".+",
				".+\"temp\":(\\d+\\.\\d+).+", ".+\"temp_min\":(\\d+\\.\\d+).+", ".+\"temp_max\":(\\d+\\.\\d+).+",
				".+\"pressure\":(\\d+).+", ".+\"humidity\":(\\d+).+", ".+\"speed\":(\\d+\\.\\d+).+" };

		for (int i = 0; i < detailsRegex.length; i++) {
			Pattern p = Pattern.compile(detailsRegex[i]);
			Matcher m = p.matcher(s);
			if (m.matches()) {
				details[i] = m.group(1);
				if (i == 2 || i == 3 || i == 4) {
					details[i] = convertTemp(details[i]);
				}
			}
		}
		return details;
	}

	/* convert temperature from obtained data to Celsius */
	public String convertTemp(String s) {

		double degrees = (-273) + Double.parseDouble(s);
		degrees = Math.round((degrees * 100) / 100.0);
		DecimalFormat value = new DecimalFormat("#.#");

		return value.format(degrees);
	}

	public double findRate(StringBuilder sb) {
		double rate = 0.0;

		try {
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(sb.toString()));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("pozycja");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				NodeList tag = element.getElementsByTagName("kod_waluty");
				Element line = (Element) tag.item(0);

				NodeList tag1 = element.getElementsByTagName("kurs_sredni");
				Element rateLine = (Element) tag1.item(0);

				currencyCode = getCurrency();

				if (currencyCode.equals("Wrong country name!")) {
					return 0.0;
				}

				if (getCharacterDataFromElement(line).equals(currencyCode)) {
					String rateString = getCharacterDataFromElement(rateLine);

					rate = Double.parseDouble(rateString.replace(",", "."));
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rate;

	}

	/* convertion from coutry name to this country currency */
	@SuppressWarnings("static-access")
	public String getCurrency() {

		Map<String, String> countries = new TreeMap<String, String>();
		String errorMessage;
		String[] countryCodes = Locale.getISOCountries();

		for (String countryCode : countryCodes) {

			Locale locale = new Locale("", countryCode);
			locale.setDefault(Locale.ENGLISH);
			String code = locale.getCountry();
			String name = locale.getDisplayCountry();
			countries.put(name, code);
		}
		try {
			Locale loc = new Locale("EN", countries.get(country));
			Currency curr = Currency.getInstance(loc);
			return curr.getCurrencyCode();

		} catch (Exception e) {
			errorMessage = "Wrong country name!";
		}
		return errorMessage;
	}

	/* Obtaining data from xml file used in findRate() */
	public String getCharacterDataFromElement(Element e) {
		if (e != null) {
			Node child = e.getFirstChild();
			if (child instanceof CharacterData) {
				CharacterData cd = (CharacterData) child;
				return cd.getData();
			}
		}
		return "";
	}

	/* setter for String country */
	public void setCountry(String coun) {

		char[] tmp = coun.toCharArray();
		String countryName = "";

		for (int i = 0; i < tmp.length; i++) {
			if (i == 0) {
				countryName += tmp[0];
				countryName = countryName.toUpperCase();
			} else {
				countryName += tmp[i];
			}
		}

		this.country = countryName;
	}
}
