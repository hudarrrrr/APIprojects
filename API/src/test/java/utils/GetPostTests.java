package utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requests.PostsRequests;
import requests.UserRequests;

import static utils.Constants.*;
import static utils.Constants.postsEndPoint;

public class GetPostTests
{
    String userId="";
    String postId="";

    @BeforeClass
    public void precondition()
    {
        int random =(int)(Math.random()*50+1);
        //get the id generated from the post request defined in the function (createUser) located in class (UserRequests)
        userId = UserRequests.createUser("ali","s"+random+"a1l@gmail.com","male","active")
                .jsonPath().get("id").toString();

        postId = PostsRequests.createPost(userId,"rtyyh","ghii").jsonPath().get("id").toString();
    }

    @Test
    public void getPostTest()
    {
        Response response= PostsRequests.getPost(postId);
        response.then().statusCode(200);
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getString("id"),postId,"id is not as expected");

    }

    @Test
    public void getPostByInvalidId()
    {
        Response response= PostsRequests.getPost("1876578");
        response.then().statusCode(404);
        response.prettyPrint();
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("message"),"Resource not found","message is not as expected");
        softAssert.assertAll();
    }

    @Test
    public void getSingleUserPostsTest()
    {
        Response response= PostsRequests.getSingleUserPosts(userId);
        response.then().statusCode(200);
        response.prettyPrint();
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        //it returns array of objects, so we used "[0].user_id"
        softAssert.assertEquals(jsonPath.getString("[0].user_id"),userId,"user id is not as expected");
        softAssert.assertAll();
    }

    @Test
    public void getUserPostByInvalidId()
    {
        Response response= PostsRequests.getSingleUserPosts("456785");
        response.then().statusCode(404);
        response.prettyPrint();
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("message"),"Resource not found","message is not as expected");
        softAssert.assertAll();
    }

    @Test
    public void getUserPostByBlankId()
    {
        Response response= PostsRequests.getSingleUserPosts(" ");
        response.then().statusCode(404);
        response.prettyPrint();
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("message"),"Resource not found","message is not as expected");
        softAssert.assertAll();
    }

    @AfterClass
    public void postconditions()
    {
        PostsRequests.deletePost(postId);
        UserRequests.deleteUser(userId);
    }
}
