package org.onassignment.compassinterview.service;

import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.springframework.stereotype.Service;

import static org.onassignment.compassinterview.utils.CrawlerUtils.bfsConcurrentLinks;
import static org.onassignment.compassinterview.utils.CrawlerUtils.bfsLinks;
import static org.onassignment.compassinterview.utils.CrawlerUtils.parseJson;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
@Service
public class CrawlerService {
    public CrawlerResult getCrawlerResult(String url) {
        //return bfsLinks(parseJson(url).getLinks());
        return bfsConcurrentLinks(parseJson(url).getLinks());
    }
}
