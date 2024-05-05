package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.GetSingleUserResponseModel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requests.UserRequests;

public class CreateUserTests
{
    String id="";

    //Serialization and Deserialization example

    @Test
    public void createUserTest() throws JsonProcessingException
    {
        int random = (int)(Math.random() * 500 + 1);

        String name = "Ahmed Agarwal";
        String gender = "male";
        String status = "active";
        String email = "test"+random+"@test.com";

        Response response = UserRequests.createUserUsingPojo(name,email,gender,status);
        response.prettyPrint();
        response.then().statusCode(201);
        JsonPath jsonPath = response.jsonPath();

        id = jsonPath.get("id").toString();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("name"),name, "Name is not as expected");
        softAssert.assertNotNull(jsonPath.getString("id"), "id is null");
        softAssert.assertEquals(jsonPath.getString("gender"),gender, "Gender is not as expected");
        UserRequests.deleteUser(id);
        softAssert.assertAll();

    }

    //Create user using the other function that uses both serialization "in request" and deserialization in "response"
    @Test
    public void createUserTest2() throws JsonProcessingException {
        int random = (int)(Math.random() * 500 + 1);
        String name = "Ahmed Agarwal";
        String gender = "male";
        String status = "active";
        String email = "test"+random+"@test.com";
        GetSingleUserResponseModel getSingleUserResponseModel = UserRequests.createUserUsingPOJO2(name,email,gender,status);

        id = getSingleUserResponseModel.id.toString();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(getSingleUserResponseModel.name,name, "Name is not as expeceted");
        softAssert.assertNotNull(getSingleUserResponseModel.id, "id is null");
        softAssert.assertEquals(getSingleUserResponseModel.gender,gender, "Gender is not as expeceted");
        UserRequests.deleteUser(id);
        softAssert.assertAll();
    }
}
