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
    private WebApplicationContext webApplicationContext;

    @Autowired
    CrawlerService crawlerService;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void testGetParseUrl() throws  Exception {
        System.out.println("---" + mockMvc + " ---" + webApplicationContext);

        mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.totalUrl")
                        .value(Matchers.greaterThan(0)));

    }

    @Test
    public void testPostParseUrl() throws  Exception {
        System.out.println("---" + mockMvc + " ---" + webApplicationContext);
        mockMvc.perform(post("/")
                .param("jsonurl","https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.totalUrl").value(Matchers.greaterThan(0)));

    }

}
