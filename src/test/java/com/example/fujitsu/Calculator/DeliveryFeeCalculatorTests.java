package com.example.fujitsu.Calculator;

import com.example.fujitsu.Repository.WeatherRepository;
import com.example.fujitsu.WeatherData.WeatherData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DeliveryFeeCalculatorTests {
    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private DeliveryFeeCalculator deliveryFeeCalculator;

    @Test
    void calculateDeliveryFeeTallinnReturnsCorrectFeeNoExtraBonus() {
        String city = "Tallinn";
        String vehicleType = "Car";
        String vehicleType2 = "Scooter";
        String vehicleType3 = "Bike";
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(3);
        weatherData.setWindSpeed(2);
        weatherData.setWeatherPhenomenon("Clear");

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Tallinn-Harku"))
                .thenReturn(weatherData);

        double fee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
        double fee2 = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType2);
        double fee3 = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType3);

        assertEquals(4.0, fee);
        assertEquals(3.5, fee2);
        assertEquals(3.0, fee3);
    }

    @Test
    void calculateDeliveryFeeTartuCarReturnsCorrectFee() {
        String city = "Tartu";
        String vehicleType = "Car";
        String vehicleType2 = "Scooter";
        String vehicleType3 = "Bike";
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(3);
        weatherData.setWindSpeed(2);
        weatherData.setWeatherPhenomenon("Clear");

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Tartu-Tõravere"))
                .thenReturn(weatherData);

        double fee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
        double fee2 = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType2);
        double fee3 = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType3);

        assertEquals(3.5, fee);
        assertEquals(3.0, fee2);
        assertEquals(2.5, fee3);
    }

    @Test
    void calculateDeliveryFeePärnuCarReturnsCorrectFee() {
        String city = "Pärnu";
        String vehicleType = "Car";
        String vehicleType2 = "Scooter";
        String vehicleType3 = "Bike";
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(3);
        weatherData.setWindSpeed(2);
        weatherData.setWeatherPhenomenon("Clear");

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Pärnu"))
                .thenReturn(weatherData);

        double fee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
        double fee2 = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType2);
        double fee3 = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType3);

        assertEquals(3.0, fee);
        assertEquals(2.5, fee2);
        assertEquals(2.0, fee3);
    }

    @Test
    void calculateDeliveryFeeCarReturnsCorrectFeeWhenExtremeConditions() {
        String city = "Tartu";
        String vehicleType = "Car";
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(-11);
        weatherData.setWindSpeed(35);
        weatherData.setWeatherPhenomenon("snow");

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Tartu-Tõravere"))
                .thenReturn(weatherData);

        double fee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);

        assertEquals(3.5, fee);
    }

    @Test
    void calculateDeliveryFeeScooterReturnsErrorWhileConditionGlaze() {
        String city = "Pärnu";
        String vehicleType = "Scooter";
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(-11);
        weatherData.setWindSpeed(35);
        weatherData.setWeatherPhenomenon("glaze");

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Pärnu"))
                .thenReturn(weatherData);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
        });

        String expectedMessage = "Usage of selected vehicle is forbidden";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void calculateDeliveryFeeScooterReturnsErrorNotCorrectCityOrVehicle() {
        String city = "Pärnuuu";
        String vehicleType = "Scooter";
        String city2 = "Pärnu";
        String vehicleType2 = "Scooterrrr";
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(-11);
        weatherData.setWindSpeed(35);
        weatherData.setWeatherPhenomenon("glaze");

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Pärnu"))
                .thenReturn(weatherData);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
        });

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            deliveryFeeCalculator.calculateDeliveryFee(city2, vehicleType2);
        });

        String expectedMessage = "Invalid city: " + city;
        String expectedMessage2 = "Invalid city or vehicle";
        String actualMessage = exception.getMessage();
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage2.contains(expectedMessage2));
    }

    @Test
    void testNoWeatherData() {
        String city = "Pärnu";
        String vehicleType = "Scooter";

        when(weatherRepository.findFirstByStationNameOrderByTimestampDesc("Pärnu"))
                .thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
        });

        String expectedMessage = "No weather data found for the city: " + city;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
