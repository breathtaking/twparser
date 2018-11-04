package com.twparser.controller.prototype;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by me
 */
public class UnirestScrapper {
    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.68";

    public static void main(String[] args) throws Exception{
        HttpResponse<String> response = Unirest.get("https://old.reddit.com/r/funny/")
                .header("user-agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .asString();

        List<String> cookies = response.getHeaders().get("Set-Cookie");
        int responseCode = response.getStatus();

        System.out.println("Status:" + responseCode);
        //System.out.println(response.getBody());

        Document jsoupResponse = Jsoup.parse(response.getBody());
        //Elements postsOnThePage = jsoupResponse.getElementsByAttributeValueStarting("class", "scrollerItem");
        Elements postsOnThePage = jsoupResponse.select("div#siteTable [class*='thing']");
        Element lastPostOnThePage = postsOnThePage.last();
        String lastPostId = lastPostOnThePage.id();
        int postsPerPage = postsOnThePage.size() - 1;
        Elements nextPageLinks = jsoupResponse.select("[class=next-button] a[href]");
        String link = nextPageLinks.last().attr("href");
        System.out.println("next page:" + nextPageLinks.attr("href"));

        for (Element post: postsOnThePage){
            System.out.println(post.id());
        }

        System.out.println("last post id:" + lastPostId);
        System.out.println("---------------------------");

        System.out.println("count:" + (postsOnThePage.size() - 1));
        HttpResponse<String> response2 = Unirest.get(link)
                .header("user-agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("accept-encoding", "gzip, deflate, br")
                .header("referer", "https://old.reddit.com/r/funny/")
                //.queryString("count", postsPerPage)
                //.queryString("after", lastPostId)
                .asString();

        Document jsoupResponse2 = Jsoup.parse(response2.getBody());
        Elements postsOnThePage2 = jsoupResponse2.select("div#siteTable [class*='thing']");
        Element lastPostOnThePage2 = postsOnThePage2.last();
        String lastPostId2 = lastPostOnThePage2.id();

        for (Element post: postsOnThePage2){
            System.out.println(post.id());
        }
        System.out.println("last: " + lastPostId2);
    }
}
