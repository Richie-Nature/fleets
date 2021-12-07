package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PricingService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PricingController.class)
public class PricingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PricingService pricingService;

    private static final Long vehicleId = 1L;

    @Test
    public void getPrice() throws Exception {
        this.mockMvc.perform(get("/services/price")
                        .param("vehicleId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").isNumber());
    }

}
