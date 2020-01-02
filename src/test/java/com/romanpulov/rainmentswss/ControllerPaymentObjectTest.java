package com.romanpulov.rainmentswss;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ControllerPaymentObjectTest extends ControllerMockMvcTest {

    public ControllerPaymentObjectTest(WebApplicationContext context) {
        super(context);
    }

    @Test
    void mainTest() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/paymentobjects")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        );
    }

}
