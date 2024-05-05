package requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.Constants;

import static utils.Constants.*;

public class PostsRequests
{
    public static Response createPost(String user_id,String title,String body)
    {
        return   RestAssured.given().log().all().contentType(ContentType.JSON).headers(Constants.generalheaders())
                .body("{\n" +
                        "    \"title\": \""+title+"\",\n" +
                        "    \"body\": \""+body+"\"\n" +
                        "}").post(baseURL + endPoint + user_id + userIdSlash + postsEndPoint);

    }

    public static Response deletePost(String postId)
    {
        return  RestAssured.given().log().all().headers(Constants.generalheaders())
                .delete(baseURL + postsEndPoint + postId);
    }

    public static Response getPost(String postId)
    {
        return  RestAssured.given().log().all().headers(Constants.generalheaders())
                .get(baseURL + postsEndPoint + postId);
    }

    public static Response getSingleUserPosts(String userId)
    {
        return  RestAssured.given().log().all().headers(Constants.generalheaders())
                .get(baseURL + endPoint + userId + userIdSlash + postsEndPoint );
    }

}
