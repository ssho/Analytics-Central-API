package com.wasoftware.bigdata.rest.controller;

import com.wasoftware.bigdata.rest.service.KerberosHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Logger;

@RestController
public class SolrController {

    static Logger log = Logger.getLogger(SolrController.class.getName());

    @Autowired
    private Environment env;

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/solr/**")
    @ResponseBody
    public ResponseEntity<String> SolrRedirect(HttpServletRequest request, @RequestBody(required = false) String json)
            throws SQLException, IOException, ClassNotFoundException,
            ParseException, SolrServerException, URISyntaxException {

        log.info("Request for " + request.getRequestURI());
        String result = "";

        String url = env.getProperty("solr_host") + request.getRequestURI() + "?";
        if ("POST".equalsIgnoreCase(request.getMethod()))
            url += json;
        else if ("GET".equalsIgnoreCase(request.getMethod()))
            url += request.getQueryString();
        else
            return new ResponseEntity<>("Invalid HTTP method", HttpStatus.BAD_REQUEST);

        if(Boolean.parseBoolean(env.getProperty("solrKerberoEnable"))) {

            KerberosHttpClient restTest = new KerberosHttpClient(env.getProperty("kerbero_solr_client"),
                    env.getProperty("kerbero_keytab_path"), false);
            HttpResponse response = restTest.callRestUrl(url, env.getProperty("kerbero_solr_client"));
            result = new String(IOUtils.toByteArray(response.getEntity().getContent()), "UTF-8");

        }
        else {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet httpRequest = new HttpGet(url);
            HttpResponse response = client.execute(httpRequest);

            result = new String(IOUtils.toByteArray(response.getEntity().getContent()), "UTF-8");
        }


        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}