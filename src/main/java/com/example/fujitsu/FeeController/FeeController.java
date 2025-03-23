package com.example.fujitsu.FeeController;

import com.example.fujitsu.Calculator.DeliveryFeeCalculator;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for calculating delivery fees.
 */
@RestController
@RequestMapping("/api/delivery")
@Validated
public class FeeController {


    @Autowired
    private DeliveryFeeCalculator deliveryFeeCalculator;

    /**
     * Calculates the delivery fee for a given city and vehicle type.
     *
     * @param city The name of the city.
     * @param vehicleType The type of vehicle.
     * @return A ResponseEntity containing the calculated fee in the response body.
     */
    @GetMapping(value = "/fee")
    public ResponseEntity<Map<String, Double>> calculateFee(
            @RequestParam @NotBlank String city,
            @RequestParam @NotBlank String vehicleType) {
        double fee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);

        Map<String, Double> response = new HashMap<>();
        response.put("fee", fee);

        return ResponseEntity.ok(response);
    }
}
