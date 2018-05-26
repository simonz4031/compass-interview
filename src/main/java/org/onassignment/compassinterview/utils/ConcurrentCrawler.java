package org.onassignment.compassinterview.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.onassignment.compassinterview.pojo.CrawlerResult;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class ConcurrentCrawler {
    //use atomic to check is all the thread done with url parse
    private AtomicInteger atomic = new AtomicInteger(0);

    private CrawlerResult result;

    //store the visited url, 1 as successful -1 as failed ,0 as visited
    private ConcurrentHashMap<String, Integer> map;

    private Queue<String> queue;

    public ConcurrentCrawler(String[] urls) {

        result = new CrawlerResult(0, 0, 0);
        map = new ConcurrentHashMap<>();
        queue = new ConcurrentLinkedQueue<>();
        queue.addAll(Arrays.stream(urls)
                .filter(url -> (url != null && url.length() > 0))
                .collect(Collectors.toList()));
    }

    /**
     * @param url the url need be pasrse, get all <a></a> links from the given url
     * @return the url after HTTP 2XX or HTTP 3XX redirect , null if failed for HTTP 4XX HTTP 5XX
     * also it will add all the links to process queue
     */
    public String parseUrl(String url) {
        if (url == null && url.length() == 0) return null;
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            queue.addAll(links.stream()
                    .map(link -> link.attr("abs:href"))
                    .filter(link -> !map.containsKey(link))
                    .collect(Collectors.toList()));

            return document.location();
        } catch (Exception e) {
            System.out.println("parse " + url + " failed");
        }

        return null;
    }

    public CrawlerResult bfsLinks() throws InterruptedException {
        while (true) {
            if (queue.isEmpty()) {
                if (atomic.get() == 0)
                    // all thread is finished, safe to quit, else keep waiting
                    break;
                else {
                    Thread.sleep(10);
                    continue;
                }
            }

            String url = queue.poll();

            if (!map.containsKey(url)) {
                Thread thread = new Thread(() -> {

                    String location = null;

                    atomic.incrementAndGet();
                    map.put(url, 0);
                    location = parseUrl(url);

                    if (location == null)
                        map.put(url, -1);
                    else {
                        map.put(location, 1);
                        if (!location.equals(url))
                            map.put(url, 0);
                    }
                    atomic.decrementAndGet();
                });

                thread.start();
                Thread.sleep(10);
            }
        }

        for (Integer v : map.values()) {
            if (v.equals(1))
                result.incSuccessful();
            else if (v.equals(-1))
                result.incFailed();

        }
        result.setTotalUrl(result.getFailedUrl() + result.getSuccessfulUrl());

        return this.result;
    }
}
