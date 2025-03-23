package com.example.fujitsu.Repository;

import com.example.fujitsu.WeatherData.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing WeatherData entities.
 * Provides method to get the latest information about a station.
 */
@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

    WeatherData findFirstByStationNameOrderByTimestampDesc(String stationName);
}
