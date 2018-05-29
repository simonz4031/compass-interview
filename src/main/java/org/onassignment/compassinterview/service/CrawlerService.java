package org.onassignment.compassinterview.service;

import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.utils.ConcurrentCrawler;
import org.springframework.stereotype.Service;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
@Service
public class CrawlerService {
    public CrawlerResult getCrawlerResult(String url) throws Exception {
        return new ConcurrentCrawler(url).bfsLinks();
    }
}
