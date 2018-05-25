package org.onassignment.compassinterview.service;

import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.utils.ConcurrentCrawler;
import org.onassignment.compassinterview.utils.CrawlerUtils;
import org.springframework.stereotype.Service;


import static org.onassignment.compassinterview.utils.CrawlerUtils.parseJson;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
@Service
public class CrawlerService {
    public CrawlerResult getCrawlerResult(String url) {

        try {
            return new ConcurrentCrawler( CrawlerUtils.parseJson(url).getLinks()).bfsLinks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CrawlerResult(0,0,0);
    }
}
