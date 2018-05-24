package org.onassignment.compassinterview;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompassInterviewApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CompassInterviewApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
