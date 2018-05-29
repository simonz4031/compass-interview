package org.onassignment.compassinterview.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.pojo.JsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class ConcurrentCrawler {
    //use atomic to check is all the thread done with url parse
    private AtomicInteger atomic;

    private CrawlerResult result;

    //store the visited url, 1 as successful -1 as failed ,0 as visited
    private Map<String, Integer> visitedMap;

    private Queue<String> urlQueue;
    private String[] urlLinks;

    private static final Logger logger = LoggerFactory.getLogger(ConcurrentCrawler.class);

    public ConcurrentCrawler(String jsonUrl) throws Exception {
        this.urlLinks = parseJson(jsonUrl);
        InitialData();
    }

    public ConcurrentCrawler(String[] urls) {
        this.urlLinks = urls;
        InitialData();
    }

    private void InitialData() {
        result = new CrawlerResult(0, 0, 0);
        atomic =  new AtomicInteger(0);
        visitedMap = new ConcurrentHashMap<>();
        urlQueue = new ConcurrentLinkedQueue<>();
        urlQueue.addAll(Arrays.stream(urlLinks)
                .filter(url -> (url != null && url.length() > 0))
                .collect(Collectors.toList()));
    }

    /**
     * @param url
     * @return String[] from JsonData classs
     * @throws Exception
     */
    private String[] parseJson(String url) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String content = Jsoup.connect(url).get().body().text();
        JsonData jsonData = objectMapper.readValue(content, JsonData.class);

        return jsonData.getLinks();

    }

    /**
     * @param url the url need be pasrse, get all <a></a> links from the given url
     * @return the url after HTTP 2XX or HTTP 3XX redirect , null if failed for HTTP 4XX HTTP 5XX
     * also it will add all the links to process queue
     * catch all exception in case of some thread may failed, but Main thread and others will still continue
     */
    private String parseUrl(String url) {
        if (url == null && url.length() == 0) return null;
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            urlQueue.addAll(links.stream()
                    .map(link -> link.attr("abs:href"))
                    .filter(link -> !visitedMap.containsKey(link))
                    .collect(Collectors.toList()));

            return document.location();
        } catch (Exception e) {
            logger.info("parse " + url + " failed");
        }

        return null;
    }

    /**
     * impletement using map
     * treat json urls as graph, roaming the graph with bfs with queue
     * a map to record what url is been visited , sucess or failed
     */
    public CrawlerResult bfsLinks() throws InterruptedException {
        while (true) {
            if (urlQueue.isEmpty()) {
                if (atomic.get() == 0)
                    // all thread is finished, safe to quit, else keep waiting
                    break;
                else {
                    Thread.sleep(10);
                    continue;
                }
            }

            String url = urlQueue.poll();

            if (!visitedMap.containsKey(url)) {
                Thread thread = new Thread(() -> {

                    String location = null;

                    atomic.incrementAndGet();
                    visitedMap.put(url, 0);
                    location = parseUrl(url);

                    if (location == null)
                        visitedMap.put(url, -1);
                    else {
                        visitedMap.put(location, 1);
                        if (!location.equals(url))
                            visitedMap.put(url, 0);
                    }
                    atomic.decrementAndGet();
                });

                thread.start();
                Thread.sleep(10);
            }
        }

        for (Integer v : visitedMap.values()) {
            if (v.equals(1))
                result.incSuccessful();
            else if (v.equals(-1))
                result.incFailed();

        }
        result.setTotalUrl(result.getFailedUrl() + result.getSuccessfulUrl());

        return this.result;
    }
}
