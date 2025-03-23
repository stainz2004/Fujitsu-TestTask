package com.example.fujitsu.Calculator;

import com.example.fujitsu.WeatherData.WeatherData;
import com.example.fujitsu.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for calculating delivery fees based on weather data and vehicle type.
 */
@Service
public class DeliveryFeeCalculator {

    @Autowired
    private WeatherRepository weatherRepository;

    /**
     * Calculates the delivery fee for a given city and vehicle type.
     * @return The calculated delivery fee (Double).
     */
    public double calculateDeliveryFee(String city, String vehicleType) {
        String stationToCheck;
        if (city.equalsIgnoreCase("Tallinn")) {
            stationToCheck = "Tallinn-Harku";
        } else if (city.equalsIgnoreCase("Tartu")) {
            stationToCheck = "Tartu-Tõravere";
        } else if (city.equalsIgnoreCase("pärnu")) {
            stationToCheck = "Pärnu";
        } else {
            throw new IllegalArgumentException("Invalid city: " + city);
        }


        WeatherData weatherData = weatherRepository.findFirstByStationNameOrderByTimestampDesc(stationToCheck);

        if (weatherData == null) {
            throw new IllegalArgumentException("No weather data found for the city: " + city);
        }

        double baseFee = calculateBaseFee(city, vehicleType);

        double extraFee = calculateExtraFee(weatherData, vehicleType);

        return baseFee + extraFee;
    }

    /**
     * Calculates the base delivery fee based on the city and vehicle type.
     * @return The base delivery fee.
     */
    private double calculateBaseFee(String city, String vehicleType) {
        city = city.toLowerCase();
        vehicleType = vehicleType.toLowerCase();
        switch (city) {
            case "tallinn" -> {
                switch (vehicleType) {
                    case "car":
                        return 4.0;
                    case "scooter":
                        return 3.5;
                    case "bike":
                        return 3.0;
                }
            }
            case "tartu" -> {
                switch (vehicleType) {
                    case "car":
                        return 3.5;
                    case "scooter":
                        return 3.0;
                    case "bike":
                        return 2.5;
                }
            }
            case "pärnu" -> {
                switch (vehicleType) {
                    case "car":
                        return 3.0;
                    case "scooter":
                        return 2.5;
                    case "bike":
                        return 2.0;
                }
            }
        }
        throw new IllegalArgumentException("Invalid city or vehicle");
    }

    /**
     * Calculates the extra delivery fee based on weather conditions and vehicle type.
     * @return The extra delivery fee.
     */
    private double calculateExtraFee(WeatherData weatherData, String vehicleType) {
        double extraFee = 0.0;
        if (vehicleType.equalsIgnoreCase("Car")) {
            return 0.0;
        }

        if (weatherData.getAirTemperature() < -10) {
            extraFee += 1.0;
        } else if (weatherData.getAirTemperature() < 0) {
            extraFee += 0.5;
        }

        if (vehicleType.equalsIgnoreCase("Bike")) {
            if (weatherData.getWindSpeed() >= 10 && weatherData.getWindSpeed() <= 20) {
                extraFee += 0.5;
            } else if (weatherData.getWindSpeed() > 20) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
        }

        if (vehicleType.equalsIgnoreCase("Scooter") || vehicleType.equalsIgnoreCase("Bike")) {
            String phenomenen = weatherData.getWeatherPhenomenon().toLowerCase();
            if (phenomenen.contains("snow")
                    || phenomenen.contains("sleet")) {
                extraFee += 1;
            } else if (phenomenen.contains("rain")) {
                extraFee += 0.5;
            } else if (phenomenen.contains("glaze") || phenomenen.contains("hail") || phenomenen.contains("thunder")) {
                throw new IllegalArgumentException("Usage of selected vehicle is forbidden");
            }
        }
        return extraFee;
    }
}
