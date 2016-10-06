package com.vendavo.poc.boot.rest

import com.vendavo.poc.boot.DemoApplication
import com.vendavo.poc.boot.model.Invoice
import com.vendavo.poc.boot.repository.InvoiceRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.http.MockHttpOutputMessage
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import java.nio.charset.Charset

import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertNotNull
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
public class InvoiceTest {

    private MediaType contentTypeJSON = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"))

    private MediaType contentTypeHAL_JSON = new MediaType('application', 'hal+json', Charset.forName("utf8"))

    private MockMvc mockMvc

    private HttpMessageConverter mappingJackson2HttpMessageConverter

    @Autowired
    private InvoiceRepository invoiceRepository

    @Autowired
    private WebApplicationContext webApplicationContext

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = converters.find {
            hmc -> hmc instanceof MappingJackson2HttpMessageConverter
        }

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter)
    }

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build()

        invoiceRepository.deleteAll()
        invoiceRepository.save(new Invoice(company: 'JOpenspace 2016', address: 'Pelhrimov', ico: '42'))

    }

    @Test
    public void listInvocies() throws Exception {
        mockMvc.perform(get("/invoices/").contentType(contentTypeJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentTypeHAL_JSON))
                .andExpect(jsonPath('$._embedded.invoices[0].company', is('JOpenspace 2016')))
                .andExpect(jsonPath('$._embedded.invoices[0].address', is('Pelhrimov')))
                .andExpect(jsonPath('$._embedded.invoices[0].ico', is('42')))

    }

    @Test
    public void createInvoice() throws Exception {
        mockMvc.perform(post("/invoices/")
                .content(this.json(new Invoice(company: 'Foo', address: 'Bar', ico: '1234')))
                .contentType(contentTypeJSON))
                .andExpect(status().isCreated())

        mockMvc.perform(get("/invoices/").contentType(contentTypeJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentTypeHAL_JSON))
                .andExpect(jsonPath('$._embedded.invoices[1].company', is('Foo')))
                .andExpect(jsonPath('$._embedded.invoices[1].address', is('Bar')))
                .andExpect(jsonPath('$._embedded.invoices[1].ico', is('1234')))
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage()
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage)
        return mockHttpOutputMessage.getBodyAsString()
    }
}