package org.onassignment.compassinterview.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.onassignment.compassinterview.pojo.CrawlerResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;


public class ConcurrentCrawler {

    private static AtomicInteger atomic = new AtomicInteger(0);
    List<Thread> threads;
    private CrawlerResult result;
    private ConcurrentHashMap<String, Integer> map;
    private Queue<String> queue;

    public ConcurrentCrawler(String[] urls) {

        result = new CrawlerResult(0, 0, 0);
        map = new ConcurrentHashMap<>();
        queue = new ConcurrentLinkedQueue<>();
        queue.addAll(Arrays.asList(urls));
        threads = new ArrayList<>();
    }


    public String parseUrl(String url) {
        if (url == null) return null;
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            //queue.addAll((Collection<? extends String>) links.stream().map(link -> link.attr("abs:href")).filter(linkurl -> !map.containsKey(linkurl)));
            links.forEach(link -> {
                String linkurl = link.attr("abs:href");
                if (!map.containsKey(linkurl))
                    this.queue.add(linkurl);
            });

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
                    break;
                else {
                    sleep(10);
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
                threads.add(thread);
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
