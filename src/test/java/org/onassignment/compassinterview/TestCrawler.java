package org.onassignment.compassinterview;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.pojo.JsonData;
import org.onassignment.compassinterview.utils.CrawlerUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.onassignment.compassinterview.utils.CrawlerUtils.*;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestCrawler {


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
        assertTrue(parseJson("http://nosuchhost.io/data.json") == null);

        assertTrue(parseJson("") == null);
        assertTrue(parseJson("ftp://jsonplaceholder.typicode.com/posts/1") == null);

        assertTrue(parseJson("https://www.google.com") == null);
        assertTrue(parseJson("https://www.w3schools.com/js/myTutorials.txt") == null);
        assertTrue(parseJson("https://jsonplaceholder.typicode.com/posts/1") == null);

    }

    @Test
    public void testParseLink() throws Exception {
        String url = "https://httpbin.org/links/7/0";
        Queue<String> links = new LinkedList<>();

        assertTrue(parseUrl(url, links) != null);

        //links.forEach(link -> System.out.println(link));
        assertThat(links.size() == 7);

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

        links.clear();
        assertTrue(parseUrl("https://www.w3schools.com/js/myTutorials.txt", links) != null);
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
        assertTrue(result.getTotalUrl() == 12 && result.getFailedUrl() == 0);
    }

    @Test
    public void testUri() throws Exception {
        String url = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";
        long startTime = System.currentTimeMillis();
        System.out.println(CrawlerUtils.bfsConcurrentLinks(parseJson(url).getLinks()));
        System.out.println("time for concurrent is:" + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
//        System.out.println(CrawlerUtils.bfsLinks(parseJson(url).getLinks()));
//        System.out.println("time for non thread is:" + (System.currentTimeMillis() - startTime));
    }

}
