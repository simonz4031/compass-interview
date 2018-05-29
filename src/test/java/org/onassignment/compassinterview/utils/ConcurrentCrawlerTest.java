package org.onassignment.compassinterview.utils;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.jsoup.UnsupportedMimeTypeException;
import org.junit.Test;
import org.onassignment.compassinterview.pojo.CrawlerResult;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import static org.junit.Assert.assertTrue;

public class ConcurrentCrawlerTest {
    @Test
    public void testParseJson() throws Exception {
        String jsonurl = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";

        ConcurrentCrawler crawler = new ConcurrentCrawler(jsonurl);
        assertTrue(crawler != null);

    }

    @Test(expected = IOException.class)
    public void testWrongJsonUrl() throws Exception {
        String url = "http://nosuchhost.io/data.json";
        ConcurrentCrawler crawler = new ConcurrentCrawler(url);
        System.out.println("crawler should be null and no print for this line");
        assertTrue(crawler == null);
    }

    @Test(expected = UnsupportedMimeTypeException.class)
    public void testWrongJson() throws Exception {
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        ConcurrentCrawler crawler = new ConcurrentCrawler(url);
    }

    @Test(expected = MismatchedInputException.class)
    public void testWrongJsonFormat() throws Exception {
        String url = "https://www.w3schools.com/js/myTutorials.txt";
        ConcurrentCrawler crawler = new ConcurrentCrawler(url);
    }

    @Test
    public void testParseLink() throws Exception {
        String [] urls = {};

        ConcurrentCrawler crawler = new ConcurrentCrawler(urls);

        Field queueField = crawler.getClass().getDeclaredField("urlQueue");
        queueField.setAccessible(true);
        Queue<String> queue = (Queue<String>) queueField.get(crawler);

        Method parseUrl = crawler.getClass().getDeclaredMethod("parseUrl", String.class);

        parseUrl.setAccessible(true);

        String url = "https://httpbin.org/links/7/0";
        queue.clear();
        String retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  == 6 && retUrl.equals( url ));

        url = "https://httpbin.org/links/1/0";
        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  == 0 && retUrl.equals( url ));

        url = "https://httpbin.org/links/5/0";
        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  == 4 && retUrl.equals( url ));

        url = "https://httpbin.org/links/98/0";
        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  == 97 && retUrl.equals( url ));

        url = "https://www.google.com";
        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  > 0 && retUrl.equals( url ));

        url = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";
        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  == 0 && retUrl.equals( url ));


        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, "");
        assertTrue(queue.size() == 0 && retUrl == null);

        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, "https://httpbin.org/status/404");
        assertTrue(queue.size()  == 0 && retUrl == null );

        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, "https://httpbin.org/status/500");
        assertTrue(queue.size()  == 0 && retUrl == null );

        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, "www.google.com");
        assertTrue(queue.size()  == 0 && retUrl == null );

        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, "ftp://test.rebex.net/");
        assertTrue(queue.size()  == 0 && retUrl == null );

        queue.clear();
        retUrl = (String) parseUrl.invoke(crawler, "ssh://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json");
        assertTrue(queue.size()  == 0 && retUrl == null );

        queue.clear();
        url = "http://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";
        retUrl = (String) parseUrl.invoke(crawler, url);
        assertTrue(queue.size()  == 0 && !retUrl.equals(url)  );

    }

    @Test
    public void testBfsLinks() throws Exception {
        String jsonurl = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";

        String[] urls = {"https://httpbin.org/status/404", "http://www.yahoo.com/notfound"};
        ConcurrentCrawler crawler = null;
        CrawlerResult result = null;

        crawler = new ConcurrentCrawler(urls);
        result = crawler.bfsLinks();
        System.out.println(result);
        assertTrue(result.getTotalUrl() == 2 && result.getFailedUrl() == 2);

        //Thread.sleep(2000);
        urls = new String[]{"http://www.not-google.com", "", "http", "http://192.168.11.300", "file:///tmp", "192.168.11.254"};
        result = new ConcurrentCrawler(urls).bfsLinks();
        System.out.println(result);
        assertTrue(result.getFailedUrl() == 4);



        urls = new String[]{"https://httpbin.org/links/8", "https://httpbin.org/links/2"};
        result = new ConcurrentCrawler(urls).bfsLinks();
        System.out.println(result);
        assertTrue(result.getTotalUrl() == 10 && result.getFailedUrl() == 0);

        urls = new String[]{"https://httpbin.org/links/10", "http://www.not-google.com", "", null, "http", "http://192.168.11.300","https://httpbin.org/links/2"};
        result = new ConcurrentCrawler(urls).bfsLinks();
        System.out.println(result);
        assertTrue(result.getTotalUrl() == 15 && result.getFailedUrl() == 2);


        long startTime = System.currentTimeMillis();
        System.out.println(new ConcurrentCrawler( jsonurl).bfsLinks());
        System.out.println("time for concurrent is:" + (System.currentTimeMillis() - startTime));

    }
}