package org.onassignment.compassinterview.controller;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onassignment.compassinterview.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author: qzhanghp
 * @date: 5/24/18
 * @description:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class CrawlerControllerTest {
    @Autowired
    CrawlerService crawlerService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void testGetParseUrl() throws Exception {
        mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.totalUrl")
                        .value(Matchers.greaterThan(0)));

    }

    @Test
    public void testGetWrongUrl() throws Exception {
        mockMvc.perform(get("/nosuch")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/")
                .param("jsonurl", "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostParseUrl() throws Exception {
        mockMvc.perform(post("/")
                .param("jsonurl", "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.totalUrl").value(Matchers.greaterThan(0)));
    }

    @Test
    public void testWrongPost() throws Exception {
        mockMvc.perform(post("/")
                .param("hello", "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/")
                .param("jsonurl", "https://raw.githubusercontent.com/OnAssignment/master.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.retCode").value(-100));

        mockMvc.perform(post("/nourl")
                .param("jsonurl", "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());


        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
