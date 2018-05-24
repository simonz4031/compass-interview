package org.onassignment.compassinterview;

import org.onassignment.compassinterview.utils.CrawlerUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompassInterviewApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CompassInterviewApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {

            String url = "https://raw.githubusercontent.com/OnAssignment/compass-interview/master/data.json";
            System.out.println(CrawlerUtils.bfsLinks(CrawlerUtils.parseJson(url).getLinks()));
        }

    }
}
