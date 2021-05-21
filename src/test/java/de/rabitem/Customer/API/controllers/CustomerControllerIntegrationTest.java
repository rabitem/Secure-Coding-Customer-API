package de.rabitem.Customer.API.controllers;

import de.rabitem.Customer.API.repositories.CustomerRepository;
import de.rabitem.Customer.API.services.CustomerService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    CustomerService customerService;

    @Autowired
    CustomerController customerController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenPostRequestToCustomerAndValidCustomerBody_thenCorrectResponse() throws Exception {
        MediaType textJson = new MediaType(MediaType.APPLICATION_JSON);
        String user = "{\n" +
                "    \"firstName\": \"Felix\",\n" +
                "    \"lastName\": \"Huisinga\",\n" +
                "    \"email\": \"MaxMustermann@gmail.com\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addCustomer")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(textJson));
    }

    @Test
    void whenPostRequestToCustomerAndInValidCustomer_thenCorrectResponse() throws Exception {
        String user = "{\"firstName\": \"\", \"lastName\": \"Huisinga\", \"email\" : \"mustermann@domain.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addCustomer")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Is.is("First Name cannot be blank")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }
}
