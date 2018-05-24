package org.onassignment.compassinterview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.onassignment.compassinterview.pojo.JsonData;
import org.onassignment.compassinterview.utils.CrawlerUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author: Simon Zhang
 * @date: 5/23/18
 * @description: crawler the interview json file
 * impletement using map
 * treat json url as graph, roaming the graph with bfs with queue
 * a map to record what url is been visited , sucess or failed
 * count
 */
@Service
public class CrawlerService {




}
