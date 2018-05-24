package org.onassignment.compassinterview.controller;

import org.onassignment.compassinterview.pojo.CrawlerResult;
import org.onassignment.compassinterview.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
@RestController
public class CrawlerController {
    @Autowired
    CrawlerService crawlerService;

    @Value("${RemoteJson}")
    private String jsonUrl;

    @GetMapping("/")
    @ResponseBody
    public CrawlerResult parseUrl() {
        //System.out.println(jsonUrl);
        return crawlerService.getCrawlerResult(jsonUrl);
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    public CrawlerResult putParseUrl(@RequestParam("jsonurl") String url) {
        return crawlerService.getCrawlerResult(url);
    }
}
