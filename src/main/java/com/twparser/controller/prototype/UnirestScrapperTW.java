package com.twparser.controller.prototype;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.twparser.model.Post;
import com.twparser.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by me
 */

@Slf4j
public final class UnirestScrapperTW {
    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.68";
    private static String POST_ID_TEMPLATE = "https://twitter.com/Drake/status/722500710932328448?conversation_id=722500710932328448";
    private static List<String> cookies;
    private static String dataMinPosition;
    private static boolean hasNextPage;
    private static int totalPostEWritten;
    private static Random random;
    private String SINGLE_PHOTO_CSS_CLASS_CONT = ".AdaptiveMedia-singlePhoto";
    private static String SINGLE_PHOTO_CSS_CLASS = ".AdaptiveMedia-photoContainer";

    private static String SINGLE_VIDEO_CSS_CLASS = ".PlayableMedia--video";
    private String SINGLE_GIF_CSS_CLASS = ".PlayableMedia PlayableMedia--gif";

    private String FOUR_PHOTO_CSS_CLASS = ".AdaptiveMedia-quadPhoto";
    private String BIG_THIRD_PHOTO_CSS_CLASS = ".AdaptiveMedia-threeQuartersWidthPhoto";
    private String SMALL_ONE_OF_THREE_PHOTO_CSS_CLASS_CONT = ".AdaptiveMedia-thirdHeightPhotoContainer";
    private String SMALL_ONE_OF_THREE_PHOTO_CSS_CLASS = ".AdaptiveMedia-thirdHeightPhoto";

    private String TWO_PHOTO_CSS_CLASS = ".AdaptiveMedia-doublePhoto";
    private String TWO_PHOTO_CSS_CLASS_HALF = ".AdaptiveMedia-halfWidthPhoto";

    /**
     * Configures Standard cookie spec to allow
     * Unirest handle invalid 'expires';
     * The RFC 6265 compliant policy.
     */
    private static void configureCookieSpec() {
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpclient);
    }

    /**
     * Gets information about user profile.
     *
     * @param userId unique identifier of requested profile.
     * @return completed User entity or empty User in case of errors.
     */
    public static User grabUser(String userId) {
        configureCookieSpec();
        User user;
        try {
            HttpResponse<String> firstPageResponse = fireInitialGetFirstPageRequest(userId);
            if (firstPageResponse.getStatus() != 200) { return null; }
            Document jsoupResponse = Jsoup.parse(firstPageResponse.getBody());

            Elements navigationBarStuff = jsoupResponse.select(".ProfileNav");
            Elements profileHeaderStuff = jsoupResponse.select(".ProfileHeaderCard");

            int tweets = Integer.valueOf(navigationBarStuff.select(".ProfileNav-item--tweets .ProfileNav-value").first().attr("data-count"));
            int following = Integer.valueOf(navigationBarStuff.select(".ProfileNav-item--following .ProfileNav-value").first().attr("data-count"));
            int followers = Integer.valueOf(navigationBarStuff.select(".ProfileNav-item--followers .ProfileNav-value").first().attr("data-count"));
            int likes = Integer.valueOf(navigationBarStuff.select(".ProfileNav-item--favorites .ProfileNav-value").first().attr("data-count"));
            String userName = profileHeaderStuff.select(".ProfileHeaderCard-name .ProfileHeaderCard-nameLink").text();
            int dataUserId = Integer.valueOf(navigationBarStuff.attr("data-user-id"));
            String profileAvatarUrl = jsoupResponse.select(".ProfileAvatar-image").attr("src");
            System.out.println(profileAvatarUrl);
            return new User(dataUserId, userId, userName, true, followers, following, tweets, likes, profileAvatarUrl);
        } catch (UnirestException e) {
            System.out.println("Some kind of error occurred during User profile grabbing");
        }
        return new User();
    }

    /**
     * Gets all post for requested profile
     *
     * @param profileIdentifier unique identifier of requested profile.
     * @return response containing the next page content.
     * @throws UnirestException
     */
    public static List<Post> grabAllPosts (String profileIdentifier) throws UnirestException {
        hasNextPage = true;
        random = new Random(6L);
        configureCookieSpec();
        HttpResponse<String> firstPageResponse = fireInitialGetFirstPageRequest(profileIdentifier);
        cookies = firstPageResponse.getHeaders().get("Set-Cookie");

        if (firstPageResponse.getStatus() != 200) { return null; }
        else {
            List<Post> postList = new ArrayList<>();
            Document jsoupResponse = Jsoup.parse(firstPageResponse.getBody());
            Elements postsOnThePage = jsoupResponse.select("div#timeline [id*='stream-item-tweet']");
            int postsPerPage = postsOnThePage.size();
            totalPostEWritten += postsPerPage;
            dataMinPosition = jsoupResponse
                    .selectFirst("div#timeline .stream-container")
                    .attr("data-min-position");
            for (Element post: postsOnThePage) {
                boolean isMediaContentPresent = post.select(".AdaptiveMedia-singlePhoto").size() > 0;
                boolean isVideo = post.select(SINGLE_VIDEO_CSS_CLASS).size() > 0;
                //System.out.println("VIDEO POST SIZE" + post.select(SINGLE_VIDEO_CSS_CLASS).size());
                //TODO DELETE
                if (isVideo) {
                    String postId = post.attr("data-item-id");
                    String embededHtml = getEmbededPostHtml(profileIdentifier, postId);
                    Post videoPost = new Post();
                    videoPost.setImageUrl(embededHtml);
                }
                if (!isMediaContentPresent) { continue; }
                postList.add(createPhotoPost(post, profileIdentifier));
            }
            System.out.println("First Page Number Posts: " + postsPerPage);
            System.out.println("Total Page Number Posts: " + totalPostEWritten);

            //System.out.println(jsoupResponse.body());

            while (hasNextPage) {
                HttpResponse<JsonNode> responseNextPage = fireJsonGetNextPageRequest(profileIdentifier, dataMinPosition);

                int responseCode2 = responseNextPage.getStatus();
                System.out.println("Status:" + responseCode2);

                Document jsoupResponseNext = Jsoup.parse(responseNextPage.getBody().getObject().get("items_html").toString());
                String responseMinPosition = responseNextPage.getBody().getObject().get("min_position").toString();
                hasNextPage = Boolean.valueOf(responseNextPage.getBody().getObject().get("has_more_items").toString());

                //System.out.println(responseNextPage.getBody().getObject().get("items_html"));
                System.out.println("Current Min Position " + responseMinPosition);

                Elements postsOnTheNextPage = jsoupResponseNext.select("[id*='stream-item-tweet']");
                //String dataMinPosition2 = nextPageElement2.attr("data-min-position");
                int postsPerPage2 = postsOnTheNextPage.size();
                totalPostEWritten += postsPerPage2;

                for (Element post: postsOnTheNextPage) {
                    boolean isMediaContentPresent = post.select(".AdaptiveMedia-photoContainer").size() > 0;
                    //TODO DELETE
                    boolean isVideo = post.select(SINGLE_VIDEO_CSS_CLASS).size() > 0;
                    //System.out.println("VIDEO POST SIZE" + post.select(SINGLE_VIDEO_CSS_CLASS).size());
                    if (isVideo) {
                        String postId = post.attr("data-item-id");
                        String embededHtml = getEmbededPostHtml(profileIdentifier, postId);
                        System.out.println(embededHtml);
                        Post videoPost = new Post();
                        videoPost.setImageUrl(embededHtml);
                        videoPost.setPostUrl(postId);
                        videoPost.setUserId(profileIdentifier);
                        postList.add(videoPost);
                    }
                    if (!isMediaContentPresent) { continue; }
                    postList.add(createPhotoPost(post, profileIdentifier));
                }
                dataMinPosition = responseMinPosition.isEmpty() ? null : responseMinPosition;
                System.out.println("Next Min Position " + dataMinPosition);
                System.out.println("Current Page Number Posts: " + postsPerPage2);
                System.out.println("Total Page Number Posts: " + totalPostEWritten);

                try {
                    Thread.sleep(Math.abs(random.nextInt(30)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("------------------------------");
                System.out.println();
                //System.out.println(postsOnTheNextPage.get(0).toString());
            }
            totalPostEWritten = 0;
            return postList;
        }
    }

    private static String createVideoPost(Element post, String profileIdentifier) {
         return "";
    }

    /**
     * Creates post based on passed request Element.
     *
     * @param post raw html.
     * @param profileIdentifier unique identifier of requested profile.
     * @return Post.
     */
    private static Post createPhotoPost(Element post, String profileIdentifier) {
        String postId = post.attr("data-item-id");
        String postTitle = post.select("[class*='TweetTextSize']").text();
        String postUrl = String.format("https://twitter.com/%s/status/%s?conversation_id=%s", profileIdentifier, postId, postId);

        List <String> imgUrl = new ArrayList<>();
        Elements mediaContent = post.select(".AdaptiveMediaOuterContainer");
        //Elements imgUrls = post.select("[class*='AdaptiveMedia-photoContainer js-adaptive-photo']");
        Elements photoContainers = post.select(".AdaptiveMedia-photoContainer");

        if (photoContainers.size() > 0) {
            for (Element photoContainer : photoContainers) {
                String url = photoContainer.select("img").attr("src");
                imgUrl.add(url);
            }
        }

        LocalDateTime postTime = getParsedTime(
                post.selectFirst("[class*='tweet-timestamp']").attr("title"));
        int numberOfReplies = Integer.valueOf(
                post.select(".ProfileTweet-actionCount").get(0).attr("data-tweet-stat-count"));
        int numberOfReteets = Integer.valueOf(
                post.select(".ProfileTweet-actionCount").get(1).attr("data-tweet-stat-count"));
        int numberOfLikes = Integer.valueOf(
                post.select(".ProfileTweet-actionCount").get(2).attr("data-tweet-stat-count"));

        System.out.println("Time: " + postTime + " Post id: " + postId + " Likes " +  numberOfLikes + " Retweets:" + numberOfReteets + " Replies: " + numberOfReplies + " Post Title " + postTitle);

        if (imgUrl.size() > 0) {
            System.out.println("ImgURL: " + imgUrl.get(0));
        }
        return new Post(postId, profileIdentifier, postUrl, postTitle, numberOfLikes, numberOfReteets, numberOfReplies, imgUrl.get(0), postTime );
    }

    /**
     * Convert raw data from request to LocalDateTime format.
     *
     * @param rawDateTime unique identifier of requested profile.
     * @return parsed date.
     */
    private static LocalDateTime getParsedTime (String rawDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:m a - d MMM yyyy");
        return LocalDateTime.parse(rawDateTime, formatter);
    }

    /**
     * Fires initial request that returns first page HTML.
     *
     * @param profileIdentifier unique identifier of requested profile.
     * @return response containing the first page content.
     * @throws UnirestException
     */
    private static HttpResponse<String> fireInitialGetFirstPageRequest (String profileIdentifier) throws UnirestException {
        return Unirest.get("https://twitter.com/{profileIdentifier}")
                .routeParam("profileIdentifier", profileIdentifier)
                .header("user-agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9,ru;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("referer", "https://twitter.com/" + profileIdentifier)
                .asString();
    }

    /**
     * Fires get request that returns next page HTML as part of JSON object.
     *
     * @param profileIdentifier unique identifier of requested profile.
     * @param dataMinPosition data-min-position attribute indicating the last post on current page.
     * @return response containing the next page content.
     * @throws UnirestException
     */
    private static HttpResponse<JsonNode> fireJsonGetNextPageRequest(String profileIdentifier, String dataMinPosition) throws UnirestException {
        return Unirest.get("https://twitter.com/i/profiles/show/{profileIdentifier}/timeline/tweets")
                .routeParam("profileIdentifier", profileIdentifier)
                .header("user-agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9,ru;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("referer", "https://twitter.com/" + profileIdentifier)
                .queryString(getQueryParametersMap(dataMinPosition))
                .asJson();
    }

    private static String getEmbededPostHtml(String userIdentifier, String postId) throws UnirestException {
        HttpResponse<JsonNode> oEmbedResponse = Unirest.get("https://publish.twitter.com/oembed")
                .header("user-agent", USER_AGENT)
                .header("Accept", "application/json; charset=utf-8")
                .header("accept-encoding", "gzip")
                .header("content-disposition", "attachment; filename=json.json")
                .header("server", "tsa_o")
                .header("x-content-type-options", "nosniff")
                .header("x-frame-options", "SAMEORIGIN")
                .header("x-xss-protection", "1; mode=block; report=https://twitter.com/i/xss_report")
                .queryString(getEmbedQueryParametersMap(userIdentifier, postId))
                .asJson();
        return oEmbedResponse.getBody().getObject().get("html").toString();
    }

    private static Map<String, Object> getQueryParametersMap (String lastPostId) {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("include_available_features", "1");
        queryParameters.put("include_entities", "1");
        queryParameters.put("max_position", lastPostId);
        queryParameters.put("reset_error_state", "false");
        return queryParameters;
    }

    private static Map<String, Object> getEmbedQueryParametersMap(String userIdentifier, String postId) {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("url" , String.format("https://twitter.com/%s/status/%s", userIdentifier, postId));
        queryParameters.put("partner" , "");
        queryParameters.put("hide_thread" , "false");
        return queryParameters;
    }

    public static void main(String[] args) throws Exception {
        configureCookieSpec();
        //grabUser("MeekMill");
        grabAllPosts("MeekMill");
        //String Url = "https://api.twitter.com/1.1/videos/tweet/config/1033383340739424256.json";

        /**
        HttpResponse<JsonNode> oAuthResponsique = Unirest.post(Url)
                .header("user-agent", "My Twitter App v1.0.23")
                .header("Accept", "application/x-www-form-urlencoded;charset=UTF-8.")
                //.header("Content-Length", "29")
                .header("accept-encoding", "gzip")
                .header("host", "api.twitter.com")
                .header("Authorization", "Basic")
                .header("x-twitter-auth-type", "OAuth2Session")
                .body("grant_type=client_credentials")
                .asJson();

        HttpResponse<JsonNode> responsique = Unirest.get(Url)
                .header("user-agent", USER_AGENT)
                .header("Accept", "application/x-www-form-urlencoded;charset=UTF-8.")
                .header("Accept-Language", "en-US,en;q=0.9,ru;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("referer", "https://twitter.com/BigSean/status/1033383340739424256")
                .header("origin", "https://twitter.com")
                .header("Authorization", "Bearer")
                .header("x-twitter-auth-type", "OAuth2Session")
                .asJson();
**/

        //System.out.println(getEmbededPostHtml("BigSean", "1033383340739424256"));
    }
}
