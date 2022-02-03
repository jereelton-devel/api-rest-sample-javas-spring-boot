package com.apirestsample.app;

import com.apirestsample.app.utils.Helpers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiRestSampleApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {

    protected final Properties props = Helpers.extractProps();
    protected final String authRequest = props.getProperty("application.basic-authorization");
    protected final String invalidAuthRequest = props.getProperty("application.test.basic-authorization-invalid");
    protected final String uriBaseTest = props.getProperty("application.test.uri-base-test");
    protected final String uriWrongTest = props.getProperty("application.test.uri-base-test-wrong");
    protected final String uriWrongTest2 = props.getProperty("application.test.uri-base-test-wrong-2");
    protected final String userIdFoundTest = props.getProperty("application.test.user-found");
    protected final String userIdNotFoundTest = props.getProperty("application.test.user-not-found");

    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void createCustomerBeforeTest(String customer_data) throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(uriBaseTest)
                                .content(customer_data)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", authRequest)
                ).andReturn();
    }

    protected void rollbackTest(String md5Id) throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(uriBaseTest+"/"+md5Id)
                                .header("Authorization", authRequest)
                ).andReturn();
    }

}