package com.twparser.processing;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.List;

/**
 * handles
 */
public class URLManager {
    private static String url = "https://www.reddit.com/r/funny/";
    private static String startUrl = "https://www.reddit.com";
    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.68";

    public static void main(String[] args) throws Exception {
        CookieHandler.setDefault(new CookieManager());
        URL obj = new URL(startUrl);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");
        connection.setUseCaches(false);

        connection.setRequestProperty("user-agent", USER_AGENT);
        connection.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        List<String> cookies = connection.getHeaderFields().get("Set-Cookie");

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);



        // Then use the same cookies on all subsequent requests.
        //connection = (HttpsURLConnection) new URL(url).openConnection();




        BufferedReader in =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Get the response cookies
        System.out.println(response);
    }
}
