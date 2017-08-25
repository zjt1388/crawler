package com.jetman.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

public class GitHubProcessor implements PageProcessor{
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	
	public static void main(String[] args) {
		Spider spider = Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).
		addPipeline(new JsonFilePipeline("D:\\webmagic\\"));
		spider.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000))).run();
	}

	public void process(Page page) {
		
		System.out.println(page.getUrl());
		
		page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//div[@class='container repohead-details-container']/h1/strong/a/text()").toString());
        String name = page.getHtml().xpath("//div[@class='container repohead-details-container']/h1/strong/a/text()").toString();
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']"));
		
	}

	public Site getSite() {
		return site;
	}

}
