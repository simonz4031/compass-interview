package org.onassignment.compassinterview.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.pojo.JsonData;

import java.io.IOException;
import java.util.*;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
public class CrawlerUtils {
    public static  JsonData parseJson(String url) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content  = Jsoup.connect(url).get().body().text();
        JsonData jsonData = objectMapper.readValue(content, JsonData.class);

        return jsonData;
    }

    /**
     * @param url  the url need be pasrse
     * @param linkLists  the queue
     * @return the url after HTTP 2XX or HTTP 3XX redirect , null if failed for HTTP 4XX HTTP 5XX
     */
    public static String parseUrl(String url, Queue<String> linkLists)  {
        if (linkLists == null ) return null;
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
    *  impletement using map
    * treat json urls as graph, roaming the graph with bfs with queue
    * a map to record what url is been visited , sucess or failed
    */
    public static CrawlerResult bfsLinks(String[] links) {
        CrawlerResult result = new CrawlerResult(0,0,0);
        Map<String, Integer> map = new HashMap<>();
        Queue<String> queue = new LinkedList<>(Arrays.asList(links));

        while (!queue.isEmpty()) {
            String url = queue.poll();

            //if never visited
            if (!map.containsKey(url)) {

                map.put(url, 0);
                result.incTotal();

                String location = CrawlerUtils.parseUrl(url, queue);
                if (location != null ) {

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

}
