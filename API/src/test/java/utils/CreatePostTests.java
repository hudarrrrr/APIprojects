package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requests.PostsRequests;
import requests.UserRequests;

public class CreatePostTests
{
    String userId = "";
    String postId = "";


    @BeforeClass
    public void precondition()
    {
        int random =(int)(Math.random()*50+1);
        //get the id generated from the post request defined in the function (createUser) located in class (UserRequests)
        userId = UserRequests.createUser("ali","s"+random+"a1l@gmail.com","male","active")
                .jsonPath().get("id").toString();
    }



    @Test
    public void createPostTest()
    {
        /*
         int random =(int)(Math.random()*50+1);
        //get the id generated from the post request defined in the function (createUser) located in class (UserRequests)
        userId = UserRequests.createUser("ali","s"+random+"a1l@gmail.com","male","active")
                .jsonPath().get("id").toString();
         */

        Response response= PostsRequests.createPost(userId,"post title","my first post hi");
        response.then().statusCode(201);
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        postId = jsonPath.getString("id");
        Assert.assertEquals(jsonPath.getString("user_id"),userId,"user id is not as expected");
/*
        Response response1 = PostsRequests.deletePost(postId);
        response1.then().statusCode(204);
        Response response2 = UserRequests.deleteUser(userId);
        response2.then().statusCode(204);

 */

    }

    @Test
    public void createPostByInvalidUserId()
    {
        Response response2= PostsRequests.createPost("6140426","the title","the post body");
        response2.then().statusCode(422);
        JsonPath jsonPath = response2.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("[0].field"),"user");
        softAssert.assertEquals(jsonPath.getString("[0].message"),"must exist","message is not as expected");
        softAssert.assertAll();

    }

    @Test
    public void createPostByBlankData()
    {
        /*
         int random =(int)(Math.random()*50+1);
        //get the id generated from the post request defined in the function (createUser) located in class (UserRequests)
        userId = UserRequests.createUser("ali","s"+random+"a1l@gmail.com","male","active")
                .jsonPath().get("id").toString();
         */

        Response response= PostsRequests.createPost(userId,"","");
        response.then().statusCode(422);
        JsonPath jsonPath = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();

        for(int i=0;i<2;i++)
        {
            //to print the field name that has the problem
            //this will fail
            softAssert.assertEquals(jsonPath.getString("["+i+"].message"),"can't be blank","message is not as expected in "+jsonPath.getString("["+i+"].field")+" field ");
        }
        softAssert.assertAll();

        /*
        Response response2 = UserRequests.deleteUser(userId);
        response2.then().statusCode(204);
         */

    }

    @Test
    public void createPostByInvalidUserId2()
    {
        Response response2= PostsRequests.createPost("  ","the title","the post body");
        response2.then().statusCode(422);
        JsonPath jsonPath = response2.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("[0].field"),"user");
        softAssert.assertEquals(jsonPath.getString("[0].message"),"must exist","message is not as expected");
        softAssert.assertEquals(jsonPath.getString("[1].field"),"user_id");
        softAssert.assertEquals(jsonPath.getString("[1].message"),"is not a number","message is not as expected");
        softAssert.assertAll();

    }

    @Test
    public void createPostByInvalidUserId3()
    {
        Response response2= PostsRequests.createPost("1!%23","the title","the post body");
        response2.then().statusCode(422);
        JsonPath jsonPath = response2.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("[0].field"),"user");
        softAssert.assertEquals(jsonPath.getString("[0].message"),"must exist","message is not as expected");
        softAssert.assertAll();

    }

    @AfterClass
    public void postconditions()
    {
        UserRequests.deleteUser(userId);
    }
}
