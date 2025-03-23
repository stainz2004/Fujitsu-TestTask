package com.example.fujitsu.SchedulerTests;

import static org.mockito.Mockito.*;

import com.example.fujitsu.Repository.WeatherRepository;
import com.example.fujitsu.WeatherScheduler.WeatherDataScheduler;
import com.example.fujitsu.WeatherData.WeatherData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class WeatherDataSchedulerTests {

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherDataScheduler weatherDataScheduler;

    @Test
    void fetchAndSaveWeatherDataSavesDataToRepository() {
        // Mock the API response
        String xmlResponse = "<observations><station><name>Tallinn-Harku</name><wmocode>123</wmocode><airtemperature>5.0</airtemperature><windspeed>10.0</windspeed><phenomenon>Clear</phenomenon></station></observations>";

        // Call the scheduler method
        weatherDataScheduler.fetchAndSaveWeatherData();

        // Verify that the repository save method was called
        verify(weatherRepository, atLeastOnce()).save(any(WeatherData.class));
    }
}