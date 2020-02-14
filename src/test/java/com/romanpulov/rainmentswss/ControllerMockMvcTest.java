package com.romanpulov.rainmentswss;


import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


public class ControllerMockMvcTest {

    @Autowired
    protected Environment env;

    protected MockMvc mvc;

    protected final WebApplicationContext context;

    public ControllerMockMvcTest(WebApplicationContext context) {
        this.context = context;
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
