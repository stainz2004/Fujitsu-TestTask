package com.example.fujitsu.WeatherScheduler;

import com.example.fujitsu.WeatherData.WeatherData;
import com.example.fujitsu.Repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Scheduled task for fetching weather data from an external API and saving it to the database.
 * This task runs every hour at 15 minutes past the hour.
 */
@Component
public class WeatherDataScheduler {

    private static final Logger logger = LoggerFactory.getLogger(WeatherDataScheduler.class);

    @Autowired
    private WeatherRepository weatherRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches weather data from the external API, parses the XML response, and saves the data to the database.
     * This method is scheduled to run every hour at 15 minutes past the hour.
     */
    @Scheduled(cron = "0 15 * * * *")
    public void fetchAndSaveWeatherData() {
        String url = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
        try {
            String xmlData = restTemplate.getForObject(url, String.class);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new ByteArrayInputStream(xmlData.getBytes()));
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();
            String timestampStr = root.getAttribute("timestamp");
            long unixTimestamp = Long.parseLong(timestampStr);
            LocalDateTime timestamp = Instant.ofEpochSecond(unixTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            List<String> targetStations = List.of("Tartu-Tõravere", "Pärnu", "Tallinn-Harku");

            for (String stationName : targetStations) {
                NodeList stationNodes = root.getElementsByTagName("station");
                for (int i = 0; i < stationNodes.getLength(); i++) {
                    Element stationElement = (Element) stationNodes.item(i);
                    String currentStationName = stationElement.getElementsByTagName("name").item(0).getTextContent();

                    if (stationName.equals(currentStationName)) {
                        String wmoCode = stationElement.getElementsByTagName("wmocode").item(0).getTextContent();
                        double airTemperature = Double.parseDouble(stationElement.getElementsByTagName("airtemperature").item(0).getTextContent());
                        double windSpeed = Double.parseDouble(stationElement.getElementsByTagName("windspeed").item(0).getTextContent());
                        String weatherPhenomenon = stationElement.getElementsByTagName("phenomenon").item(0).getTextContent();

                        WeatherData weatherData = new WeatherData();
                        weatherData.setStationName(stationName);
                        weatherData.setWmoCode(wmoCode);
                        weatherData.setAirTemperature(airTemperature);
                        weatherData.setWindSpeed(windSpeed);
                        weatherData.setWeatherPhenomenon(weatherPhenomenon);
                        weatherData.setTimestamp(timestamp);

                        weatherRepository.save(weatherData);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to fetch or parse weather data", e);
        }
    }
}
