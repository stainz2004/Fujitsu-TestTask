package com.example.fujitsu.WeatherDataTests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.fujitsu.WeatherData.WeatherData;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;

public class WeatherDataTests {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void weatherDataValidationPassesForValidData() {
        WeatherData weatherData = new WeatherData();
        weatherData.setStationName("Tallinn-Harku");
        weatherData.setAirTemperature(5.0);
        weatherData.setWindSpeed(10.0);
        weatherData.setWeatherPhenomenon("Clear");
        weatherData.setTimestamp(LocalDateTime.now());

        assertTrue(validator.validate(weatherData).isEmpty());
    }

    @Test
    void weatherDataValidationFailsForMissingStationName() {
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(5.0);
        weatherData.setWindSpeed(10.0);
        weatherData.setWeatherPhenomenon("Clear");
        weatherData.setTimestamp(LocalDateTime.now());

        assertFalse(validator.validate(weatherData).isEmpty());
    }
}