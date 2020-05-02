package com.romanpulov.rainmentswss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.romanpulov.rainmentswss.dto.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerPaymentTest extends ControllerMockMvcTest {


    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    public ControllerPaymentTest(WebApplicationContext context) {
        super(context);
    }

    @Test
    void mainTest() throws Exception {
        try {

            PaymentObjectDTO paymentObjectDTO = new PaymentObjectDTO(null, "New Payment Object");
            json = mapper.writeValueAsString(paymentObjectDTO);
            MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/payment-objects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            addResult(mvcResult);

            Number paymentObjectId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
            //assertEquals(1, paymentObjectId);
            paymentObjectDTO.setId(paymentObjectId.longValue());

            PaymentGroupDTO paymentGroupDTO = new PaymentGroupDTO(null, "New Group", null);
            json = mapper.writeValueAsString(paymentGroupDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/payment-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
            ;

            addResult(mvcResult);

            Number paymentGroupId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
            paymentGroupDTO.setId(paymentGroupId.longValue());

            ProductDTO productDTO = new ProductDTO(null, "New Product", null);
            json = mapper.writeValueAsString(productDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
            ;

            addResult(mvcResult);

            Number productId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
            productDTO.setId(productId.longValue());

            ProductDTO product2DTO = new ProductDTO(null, "New Product 2", "kg");
            json = mapper.writeValueAsString(product2DTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
            ;

            addResult(mvcResult);

            Number product2Id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
            product2DTO.setId(product2Id.longValue());

            LocalDate periodDate = LocalDate.now().minusMonths(1L).withDayOfMonth(1);

            PaymentDTO paymentDTO = new PaymentDTO(
                    null,
                    LocalDate.now(),
                    periodDate,
                    paymentObjectDTO,
                    paymentGroupDTO,
                    productDTO,
                    BigDecimal.valueOf(54.2),
                    BigDecimal.valueOf(53.22),
                    BigDecimal.valueOf(0.44)
            );
            json = mapper.writeValueAsString(paymentDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
            ;

            addResult(mvcResult);

            Number paymentId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
            paymentDTO.setId(paymentId.longValue());

            mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/payments:refs")
                    .param("paymentObjectId", String.valueOf(paymentObjectId))
                    .param("paymentPeriodDate", periodDate.atStartOfDay().format(DateTimeFormatter.ISO_DATE_TIME))
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.paymentList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.paymentList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.paymentObject.id").value(paymentObjectDTO.getId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.paymentGroupList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.paymentGroupList", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.productList").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.productList", Matchers.hasSize(2)))
                    .andReturn()
            ;

            addResult(mvcResult);

            PatchRequestDTO patchRequestDTO = new PatchRequestDTO("stuff", "/productCounter", "34");
            json = mapper.writeValueAsString(patchRequestDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.patch("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad patch request: operation: stuff"))
                    .andReturn()
            ;

            addResult(mvcResult);

            patchRequestDTO = new PatchRequestDTO("replace", "/ddd", "34");
            json = mapper.writeValueAsString(patchRequestDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.patch("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad patch request: path: /ddd"))
                    .andReturn()
            ;

            addResult(mvcResult);

            patchRequestDTO = new PatchRequestDTO("replace", "/productCounter", "y54");
            json = mapper.writeValueAsString(patchRequestDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.patch("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad patch request: value: y54"))
                    .andReturn()
            ;

            addResult(mvcResult);

            patchRequestDTO = new PatchRequestDTO("replace", "/productCounter", "34");
            json = mapper.writeValueAsString(patchRequestDTO);
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.patch("/payments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.rowsAffected").value("1"))
                    .andReturn()
            ;

            addResult(mvcResult);

        } finally {
            Path f = Paths.get("logs/ControllerPaymentTest.log");
            Files.write(f, logResult, StandardCharsets.UTF_8);
        }
    }

}
