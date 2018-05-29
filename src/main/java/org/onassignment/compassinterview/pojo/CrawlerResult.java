package org.onassignment.compassinterview.pojo;

/**
 * @author: qzhanghp
 * @date: 5/23/18
 * @description:
 */
public class CrawlerResult {

    private int totalUrl;
    private int successfulUrl;
    private int failedUrl;

    public CrawlerResult(int totalUrl, int successfulUrl, int failedUrl) {

        this.totalUrl = totalUrl;
        this.successfulUrl = successfulUrl;
        this.failedUrl = failedUrl;
    }


    public int getTotalUrl() {
        return totalUrl;
    }

    public void setTotalUrl(int totalUrl) {
        this.totalUrl = totalUrl;
    }

    public int getSuccessfulUrl() {
        return successfulUrl;
    }

    public void setSuccessfulUrl(int successfulUrl) {
        this.successfulUrl = successfulUrl;
    }

    public int getFailedUrl() {
        return failedUrl;
    }

    public void setFailedUrl(int failedUrl) {
        this.failedUrl = failedUrl;
    }

    public void incTotal(){
        this.totalUrl++;
    }

    public void incSuccessful(){
        this.successfulUrl++;
    }

    public void incFailed(){
        this.failedUrl++;
    }


    @Override
    public String toString() {
        return "Resultz; " +
                 "total number of http requests performed = " + totalUrl +
                ", total number of successful requests = " + successfulUrl +
                ", total number of failed requests = " + failedUrl ;
    }
}
