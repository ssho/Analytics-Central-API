package rest;

/**
 * Created by wlo on 3/24/16.
 */


import com.wasoftware.bigdata.rest.Application;
import com.wasoftware.bigdata.rest.controller.GreetingController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class SolrControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @InjectMocks
    GreetingController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }



    @Test
    public void SolrUnauthorized() throws Exception {
        // @formatter:off
        mvc.perform(get("/solr/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
        // @formatter:on
    }

    private String getAccessToken(String username, String password) throws Exception {
        String authorization = "Basic "
                + new String(Base64Utils.encode("clientapp:123456".getBytes()));
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        // @formatter:off
        String content = mvc
                .perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
                                .contentType(
                                        MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", username)
                                .param("password", password)
                                .param("grant_type", "password")
                                .param("scope", "read write")
                                .param("client_id", "clientapp")
                                .param("client_secret", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
                .andExpect(jsonPath("$.scope", is(equalTo("read write"))))
                .andReturn().getResponse().getContentAsString();

        // @formatter:on

        return content.substring(17, 53);
    }

    @Test
    public void solrAuthorized() throws Exception {
        String accessToken = getAccessToken("wlo", "Password123");


        // @formatter:off
        // Test case 1
        // Keyword: system
        // facet: title, _text_

        String body = "q=_text_%3Acuba&fq=datacreated_dt:[2016-05-31T19:00:00.000Z%20TO%202016-05-31T20:00:00.000Z]&wt=json&facet.limit=10&indent=true&facet=true&facet.field=_text_";

        body = "q=_text_%3Acuba&wt=json&rows=0&fq=datacreated_dt%3A%5B2016-05-31T19%3A00%3A00.000Z+TO+2016-05-31T20%3A00%3A00.000Z%5D&facet=true&facet.range=datacreated_dt&facet.range.start=2016-05-31T19%3A00%3A00.000Z&facet.range.end=2016-05-31T20%3A00%3A00.000Z&facet.range.gap=%2B30SECOND";

        body = "q=_text_%3Acuba&wt=json&rows=0&fq=datacreated_dt%3A%5B2016-05-31T19%3A00%3A00.000Z+TO+2016-05-31T20%3A00%3A00.000Z%5D&facet=true&facet.range=datacreated_dt&facet.range.start=2016-05-31T19%3A00%3A00.000Z&facet.range.end=2016-05-31T20%3A00%3A00.000Z&facet.range.gap=%2B30SECOND&indent=on";
        MvcResult result = mvc.perform(post("/solr/wlo_collection/select")
                .header("Authorization", "Bearer " + accessToken)
                .content(body))
                .andExpect(status().isOk()).andReturn();
               // .andExpect(jsonPath("$.id", is(1)))
               // .andExpect(jsonPath("$.content", is("Hello, Roy!")));
        // @formatter:on



        System.out.println("SolrAuthorized: " + result.getResponse().getContentAsString());

    }



}
