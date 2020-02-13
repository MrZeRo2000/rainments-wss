package com.romanpulov.rainmentswss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.romanpulov.rainmentswss.dto.PaymentDTO;
import com.romanpulov.rainmentswss.dto.PaymentGroupDTO;
import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.dto.ProductDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerPaymentTest extends ControllerMockMvcTest {

    ObjectMapper mapper = new ObjectMapper();
    String json;

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    public ControllerPaymentTest(WebApplicationContext context) {
        super(context);
    }

    @Test
    void mainTest() throws Exception {

        PaymentObjectDTO paymentObjectDTO = new PaymentObjectDTO(null, "New Payment Object");
        json = mapper.writeValueAsString(paymentObjectDTO);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/payment-objects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
        ;

        Number paymentObjectId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        //assertEquals(1, paymentObjectId);
        paymentObjectDTO.setId(paymentObjectId.longValue());

        PaymentGroupDTO paymentGroupDTO = new PaymentGroupDTO(null, "New Group", null);
        json = mapper.writeValueAsString(paymentGroupDTO);
        mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/payment-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
        ;

        Number paymentGroupId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        paymentGroupDTO.setId(paymentGroupId.longValue());

        ProductDTO productDTO = new ProductDTO(null, "New Product", null);
        json = mapper.writeValueAsString(productDTO);
        mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
        ;

        Number productId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        productDTO.setId(productId.longValue());

        LocalDate periodDate = LocalDate.now().minusMonths(1L).withDayOfMonth(1);

        PaymentDTO paymentDTO = new PaymentDTO(
                null,
                LocalDate.now(),
                periodDate,
                paymentObjectDTO,
                paymentGroupDTO,
                productDTO,
                300L,
                BigDecimal.valueOf(53.22),
                BigDecimal.valueOf(0.44)
                );
        json = mapper.writeValueAsString(paymentDTO);
        mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
        ;

        Number paymentId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        paymentDTO.setId(paymentId.longValue());

        this.mvc.perform(MockMvcRequestBuilders.get("/payments/refs")
                .param("paymentObjectId", String.valueOf(paymentObjectId))
                .param("paymentPeriodDate", periodDate.atStartOfDay().format(DateTimeFormatter.ISO_DATE_TIME))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentList").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentObjectList").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentObjectList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentGroupList").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentGroupList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productList").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productList", Matchers.hasSize(1)))
        ;
    }

}