package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.GetSingleUserResponseModel;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requests.UserRequests;


public class GetUserTests
{

    String id="";

    //in order to make the get user test independent , we need to create a user as a precondition to use it in
    // the get request
    @BeforeClass
    public void precondition()
    {
        int random =(int)(Math.random()*50+1);
        //get the id generated from the post request defined in the function (createUser) located in class (UserRequests)
        id = UserRequests.createUser("ali","s"+random+"a1l@gmail.com","male","active")
                .jsonPath().get("id").toString();
    }

    @Test
     public void getSingleUserSuccess()
     {
       Response response= UserRequests.getSingleUser(id);
       response.then().statusCode(200);
       response.prettyPrint();
       JsonPath jsonPath = response.jsonPath();
       Assert.assertEquals(jsonPath.getString("id"),id,"id is not as expected");

       UserRequests.deleteUser(id);

     }

     //we may need to run this test only ,so we need to remove the after test because it will generate an error in this case
    @Test
    public void getUserByInvalidId()
    {
        Response response= UserRequests.getSingleUser("6140426");
        response.then().statusCode(404);
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("message"),"Resource not found","message is not as expected");
        softAssert.assertAll();

    }

    //Deserialization
    @Test
    public void getSingleUserTest()
    {
        GetSingleUserResponseModel getSingleUserResponseModel = UserRequests.getSingleUserAsClass(id);
        Assert.assertEquals(getSingleUserResponseModel.id.toString(),id,"id is not as expected");
        Assert.assertNotNull(getSingleUserResponseModel.name);
        Assert.assertNotNull(getSingleUserResponseModel.status);
        Assert.assertNotNull(getSingleUserResponseModel.gender);
        Assert.assertNotNull(getSingleUserResponseModel.email);
    }

     //delete the dummy user we created
/*
    @AfterClass
    public void deleteUser()
     {
         UserRequests.deleteUser(id);
     }

 */

}
