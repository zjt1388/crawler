package com.jetman.webmagic;

import com.jetman.webmagic.po.WacaiArticlePO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jetman.webmagic.dao.ArticleDao;


/**
 * User: cairne
 * Date: 13-5-13
 * Time: 下午8:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/applicationContext-*.xml"})
public class WebMagicTest {
    @Autowired
    private ArticleDao articleDao;

    @Test
    public void test() {
    	WacaiArticlePO wacaiArticleModel = new WacaiArticlePO();
    	wacaiArticleModel.setReplyNum(1);
    	wacaiArticleModel.setTitle("测试");
    	wacaiArticleModel.setUrl("http://www.baidu.com");
    	wacaiArticleModel.setViewNum(2);
    	wacaiArticleModel.setSource(1);
    	wacaiArticleModel.setUrlMd5("1235");
    	System.err.println(wacaiArticleModel.toString());
        try {
            int add = articleDao.add(wacaiArticleModel);
            System.out.println(add);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
