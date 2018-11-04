package com.twparser.processing;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Creak8.
 */
public class RequestProcessor {
    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.68";
    public static void main(String[] args) throws Exception {

        Unirest.setDefaultHeader("user-agent", USER_AGENT);
        //Unirest.setProxy(new HttpHost("217.30.71.118", 59341));

        HttpResponse response = Unirest.get("https://www.reddit.com/r/TalesFromYourServer/")
                .asString();
        System.out.println(response.getBody());
        final Document responseBody = Jsoup.parseBodyFragment(response.getBody()
                .toString());

        for (Element element: responseBody.select("div#siteTable")){
            System.out.println(element);
        }
    }
}
