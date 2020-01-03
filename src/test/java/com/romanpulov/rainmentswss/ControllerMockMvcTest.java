package com.romanpulov.rainmentswss;


import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ControllerMockMvcTest {

    protected MockMvc mvc;

    protected final WebApplicationContext context;

    public ControllerMockMvcTest(WebApplicationContext context) {
        this.context = context;
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
