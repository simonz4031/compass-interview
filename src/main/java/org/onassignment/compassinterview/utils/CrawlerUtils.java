package org.onassignment.compassinterview.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.pojo.JsonData;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Thread.sleep;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
public class CrawlerUtils {
    public static JsonData parseJson(String url) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String content = Jsoup.connect(url).get().body().text();
            JsonData jsonData = objectMapper.readValue(content, JsonData.class);

            return jsonData;
        } catch (MismatchedInputException e) {

        } catch (IllegalArgumentException e) {

        } catch (JsonParseException e) {

        } catch (IOException e) {
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * @param url       the url need be pasrse
     * @param linkLists the queue
     * @return the url after HTTP 2XX or HTTP 3XX redirect , null if failed for HTTP 4XX HTTP 5XX
     */
    public static String parseUrl(String url, Queue<String> linkLists) {
        if (linkLists == null) return null;
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            for (Element link : links) {

                linkLists.add(link.attr("abs:href"));
            }
            return document.location();
        } catch (Exception e) {
            System.out.println("parse " + url + " failed");
        }
        return null;
    }

    /**
     * impletement using map
     * treat json urls as graph, roaming the graph with bfs with queue
     * a map to record what url is been visited , sucess or failed
     */
    public static CrawlerResult bfsLinks(String[] links) {
        CrawlerResult result = new CrawlerResult(0, 0, 0);
        Map<String, Integer> map = new HashMap<>();
        Queue<String> queue = new LinkedList<>(Arrays.asList(links));

        while (!queue.isEmpty()) {
            String url = queue.poll();

            //if never visited
            if (!map.containsKey(url)) {

                //map.put(url, 0);
                String location = parseUrl(url, queue);
                result.incTotal();
                if (location != null) {

                    map.put(location, 1);
                    result.incSuccessful();
                    //System.out.println(location);

                    if (!location.equals(url))
                        map.put(url, 1);
                } else {

                    map.put(url, -1);
                    result.incFailed();
                }
            }
        }
        return result;
    }

    public static CrawlerResult bfsConcurrentLinks(String[] links) {
        CrawlerResult result = new CrawlerResult(0, 0, 0);
        Map<String, Integer> map = new ConcurrentHashMap<>();
        Queue<String> queue = new ConcurrentLinkedQueue<>(Arrays.asList(links));
        List<Thread> threads = new ArrayList<>();

        while (true) {
            if (queue.isEmpty()) break;
            String url = queue.poll();

            //if never visited
            if (!map.containsKey(url)) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String location = null;
                        synchronized (this ) {
                            location = parseUrl(url, queue);

                        }
                        try {
                            sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (location != null) {

                            synchronized (this) {
                                map.put(location, 1);


                                if (!location.equals(url)) {
                                    map.put(url, 0);
                                }
                            }
                        } else {

                            synchronized (this) {
                                map.put(url, -1);
                                //result.incFailed();
                            }
                        }
                    }
                });

                thread.start();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threads.add(thread);

            }
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Integer v : map.values()){
            if (v.equals(1))
                result.incSuccessful();
            else if (v.equals(-1))
                result.incFailed();

        }
        result.setTotalUrl(result.getFailedUrl() + result.getSuccessfulUrl());
        return result;
    }

}
