package com.romanpulov.rainmentswss;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ControllerPaymentObjectTest extends ControllerMockMvcTest {

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    public ControllerPaymentObjectTest(WebApplicationContext context) {
        super(context);
    }

    @Test
    void mainTest() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/paymentobjects")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
        ;


        PaymentObjectDTO paymentObjectDTO = new PaymentObjectDTO(null, "New Payment Object");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(paymentObjectDTO);
        this.mvc.perform(MockMvcRequestBuilders.post("/paymentobjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;


    }

}
