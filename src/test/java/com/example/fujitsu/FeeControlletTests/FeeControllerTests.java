package com.example.fujitsu.FeeControlletTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.fujitsu.Calculator.DeliveryFeeCalculator;
import com.example.fujitsu.FeeController.FeeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
public class FeeControllerTests {

    @Mock
    private DeliveryFeeCalculator deliveryFeeCalculator;

    @InjectMocks
    private FeeController feeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(feeController).build();
    }

    @Test
    void calculateFeeReturnsCorrectFee() throws Exception {
        when(deliveryFeeCalculator.calculateDeliveryFee("Tallinn", "Car")).thenReturn(4.0);

        mockMvc.perform(get("/api/delivery/fee")
                        .param("city", "Tallinn")
                        .param("vehicleType", "Car")
                        .header("Accept", "application/json")) // Force JSON response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fee").value(4.0));
    }

    @Test
    void calculateFeeReturnsBadRequestForMissingParameters() throws Exception {
        mockMvc.perform(get("/api/delivery/fee")
                        .header("Accept", "application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateFeeWithMinimumLoanAmount() throws Exception {
        when(deliveryFeeCalculator.calculateDeliveryFee("Pärnu", "Car")).thenReturn(2.0);

        mockMvc.perform(get("/api/delivery/fee")
                        .param("city", "Pärnu")
                        .param("vehicleType", "Car")
                        .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fee").value(2.0));
    }
}