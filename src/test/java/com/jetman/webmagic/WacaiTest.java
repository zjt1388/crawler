package com.jetman.webmagic;

import com.jetman.webmagic.pipeline.ArticlePipeline;
import com.jetman.webmagic.processor.WaCaiProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

/**
 * Created by zhoujt
 * on 2017/8/23.
 */
public class WacaiTest {

    public static void main(String[] args) {
        String[] urls = {
                "http://bbs.wacai.com/forum.php",

        };
        Spider spider = Spider.create(new WaCaiProcessor())
                .addUrl(urls).thread(5).addPipeline(new ArticlePipeline());
        spider.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000))).run();
    }
}
