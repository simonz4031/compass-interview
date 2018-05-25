package org.onassignment.compassinterview.utils;

import org.junit.Test;
import org.onassignment.compassinterview.pojo.CrawlerResult;

import static org.junit.Assert.assertTrue;

public class ConcurrentCrawlerTest {


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

        Thread.sleep(2000);
        urls = new String[]{"http://www.not-google.com", "", "http", "http://192.168.11.300", "file:///tmp", "192.168.11.254"};
        result = new ConcurrentCrawler(urls).bfsLinks();
        System.out.println(result);
        assertTrue(result.getFailedUrl() == 5);


        Thread.sleep(2000);
        urls = new String[]{"https://httpbin.org/links/10", "https://httpbin.org/links/2"};
        result = new ConcurrentCrawler(urls).bfsLinks();
        System.out.println(result);
        assertTrue(result.getTotalUrl() == 12 && result.getFailedUrl() == 0);
        long startTime = System.currentTimeMillis();
        System.out.println(new ConcurrentCrawler( CrawlerUtils.parseJson(jsonurl).getLinks()).bfsLinks());
        System.out.println("time for concurrent is:" + (System.currentTimeMillis() - startTime));

    }
}