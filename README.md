# Delivery Fee Calculator

## Description
A delivery fee calculator based on:
- **City**: Tallinn, Tartu, or Pärnu.
- **Vehicle Type**: Car, Scooter, or Bike.
- **Weather Conditions**: Temperature, wind speed, and weather phenomenon (e.g., snow, rain).

The application fetches weather data from an external API and saves it to a local database. This data is then used 
to calculate extra fees or give restrictions. The project also includes a **front end** for user interaction.

Built using **Java 17** and **Spring Boot**.

---

## Structure

### Backend
- **DeliveryfeeCalculator**: Handles the logic for calculating the delivery fee.
- **FeeController**: REST API endpoint for requesting the delivery fee.
- **WeatherRepository**: Manages database operations for weather data.
- **WeatherData**: Model class representing weather information (temperature, wind speed, phenomenon).
- **WeatherDataScheduler**: Scheduled task that fetches weather data from the external API every hour at 15 minutes.
- **CalculatorApplication**: Main application class to run the project.

### Frontend
- **index.html**: Main HTML file for the front end.
- **script.js**: Handles requests to the backend for fee calculation.
- **style.css**: Styles the website.

---

## How to run

- Clone the repository to your pc.
- run build.gradle
- Run [FujitsuApplication.java](src/main/java/com/example/fujitsu/CalculatorApplication.java)
- Go to http://localhost:8080/.