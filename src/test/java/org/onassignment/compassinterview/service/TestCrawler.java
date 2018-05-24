package org.onassignment.compassinterview.service;


import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.pojo.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.onassignment.compassinterview.utils.CrawlerUtils.*;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestCrawler {
    @Autowired
    CrawlerService crawlerService;

    Method parseJson;


    @Test
    public void testParseJson() throws Exception {
        String url = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";

        JsonData jsonData = null;
        jsonData = parseJson(url);
        assertTrue(jsonData.getLinks().length > 0);
    }

    @Test
    public void testParseJsonbyWrong() throws IOException {
        String url = "http://nosuchhost.io/data.json";
        JsonData jsonData = null;
        assertThrows(IOException.class, () -> parseJson("http://nosuchhost.io/data.json"));

        assertThrows(IllegalArgumentException.class, () -> parseJson(""));
        assertThrows(Exception.class, () -> parseJson("ftp://jsonplaceholder.typicode.com/posts/1"));

        assertThrows(JsonParseException.class, () -> parseJson("https://www.google.com"));
        assertThrows(com.fasterxml.jackson.databind.exc.MismatchedInputException.class, () -> parseJson("https://www.w3schools.com/js/myTutorials.txt"));
        assertThrows(Exception.class, () -> parseJson("https://jsonplaceholder.typicode.com/posts/1"));

    }

    @Test
    public void testParseLink() throws Exception {
        String url = "https://httpbin.org/links/7/0";
        Queue<String> links = new LinkedList<>();
        assertTrue(parseUrl(url, links) != null);
        links.forEach(link -> System.out.println(link));
        assertThat(links.size() > 0);
        links.clear();

        //test Queue
        assertTrue(parseUrl("https://httpbin.org/links/1/0", links) != null);
        assertTrue(links.size() == 0);

        links.clear();
        assertTrue(parseUrl("https://httpbin.org/links/5/0", links) != null);
        assertTrue(links.size() == 4);

        links.clear();
        assertTrue(parseUrl("https://httpbin.org/links/98", links) != null);
        assertTrue(links.size() == 97);

        links.clear();
        assertTrue(parseUrl("https://www.google.com", links) != null);

        assertTrue(links.size() > 0);
        links.clear();
        assertTrue(parseUrl("https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json", links) != null);
        assertTrue(parseUrl("https://jsonplaceholder.typicode.com/albums", links).length() > 0);
        assertTrue(links.size() == 0);
    }

    @Test
    public void testParseWrongLink() throws Exception {
        Queue<String> links = new LinkedList<>();

        assertTrue(parseUrl(null, links) == null);
        assertTrue(parseUrl("https://httpbin.org/links/7/0", null) == null);
        assertTrue(parseUrl("https://httpbin.org/status/404", links) == null);
        assertTrue(parseUrl("https://httpbin.org/status/notfound", links) == null);
        assertTrue(parseUrl("https://httpbin.org/status/500", links) == null);
        assertTrue(parseUrl("", links) == null);
        assertTrue(parseUrl("www.google.com", links) == null);
        assertTrue(parseUrl("ftp://www.google.com", links) == null);
        assertTrue(parseUrl("ssh://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json", links) == null);
    }

    @Test
    public void testBfsLinks() throws Exception {
        //String url = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";

        String[] urls = {"https://httpbin.org/status/404", "http://www.yahoo.com/notfound"};
        CrawlerResult result = bfsLinks(urls);
        assertTrue(result.getTotalUrl() == 2 && result.getFailedUrl() == 2);

        urls = new String[]{"http://www.not-google.com", "", "http", "http://192.168.11.300", "file:///tmp", "192.168.11.254"};
        result = bfsLinks(urls);
        assertTrue(result.getFailedUrl() == 5);

        urls = new String[]{"https://httpbin.org/links/10", "https://httpbin.org/links/2"};
        result = bfsLinks(urls);
        assertTrue(result.getTotalUrl() == 12 && result.getFailedUrl() == 0 );
    }

    @Test
    public void testUri() throws Exception {
        URI uri = new URI("http://www.google.com/voice");
        String path = uri.getHost();
        System.out.println(path);
    }

}
