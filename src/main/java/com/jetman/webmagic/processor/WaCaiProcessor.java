package com.jetman.webmagic.processor;

import java.util.ArrayList;
import java.util.List;

import com.jetman.webmagic.po.WacaiArticlePO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jetman.utils.MD5;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;


public class WaCaiProcessor implements PageProcessor{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WaCaiProcessor.class);
	private static int pageNum = 0;
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	

	public void process(Page page) {
		
		LOGGER.info("页面链接 {}",page.getUrl());
		LOGGER.info("pageNum数量{}",pageNum);
		pageNum++;
		List<String> targetList = page.getHtml().links().regex("(http://bbs.wacai\\.com/\\w+/\\w+)").all();
		//主页板块
		List<String> homeList = page.getHtml().css("div#ct").css("div.m-plates").links().all();
		//翻页板块
		List<String> pageList = page.getHtml().css("span#fd_page_bottom").links().all();
		//标题，url板块
		List<String> htmlList = page.getHtml().xpath("//table[@id='threadlisttableid']/").all();
		
		List<WacaiArticlePO> arList = new ArrayList<WacaiArticlePO>();
		for (String tbody : htmlList) {
			//LOGGER.info("<table>"+tbody+"</table>");
			Html html = new Html("<table>"+tbody+"</table>");
			String title = html.xpath("//a[@class='s xst']/text()").toString();
			String url = html.xpath("//a[@class='s xst']/@href").toString();
			String reply = html.xpath("//a[@class='xi2']/text()").toString();
			String view = html.xpath("//td[@class='num']/em/text()").toString();
			LOGGER.info("{},{},{},{}",title,url,reply,view);
			if(StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(url)) {
				int replyNum = 0;
				int viewNum = 0;
				try {
					replyNum = Integer.parseInt(reply);
					viewNum = Integer.parseInt(view);
					
				} catch (Exception e) {
					LOGGER.error("数值转化出错{}",e);
				}
				 WacaiArticlePO model = new WacaiArticlePO();
				 model.setTitle(title);
				 model.setUrl(url);
				 model.setSource(1);
				 model.setReplyNum(replyNum);
				 model.setViewNum(viewNum);
				 model.setUrlMd5(MD5.md5(url));
				 arList.add(model);
			}
		}
		
        page.putField("articleList", arList);
        if (CollectionUtils.isEmpty(arList)){
            //skip this page
            page.setSkip(true);
        }
        
    	if(CollectionUtils.isNotEmpty(pageList)) {
    		page.addTargetRequests(pageList);
    	}
    	if(CollectionUtils.isNotEmpty(homeList)) {
    		page.addTargetRequests(homeList);
    	}
        
		
	}

	public Site getSite() {
		return site;
	}

}
