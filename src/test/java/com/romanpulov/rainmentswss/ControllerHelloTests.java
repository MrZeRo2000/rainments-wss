package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(HelloController.class)
public class ControllerHelloTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void testHello() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/hello")
                .accept(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello from HelloController"))
        ;
    }
}
