package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import  org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

//in the third day first record
public class UserTests {

    //declare these variables here to use them in all tests in this class
    String baseURL = "https://gorest.co.in/public/v2/";
    String endPoint = "users/";
    Map<String,String> headers = new HashMap<>();
    String id="";

    public Response createUser(String name,String email,String gender,String status)
    {
       return   RestAssured.given().log().all().contentType(ContentType.JSON).headers(headers)
            .body("{\n" +
                    "    \"name\": \""+name+"\",\n" +
                    "    \"email\": \""+email+"\",\n" +
                    "    \"gender\": \""+gender+"\",\n" +
                    "    \"status\": \""+status+"\"\n" +
                    "}").post(baseURL + endPoint);
    }

    //to initialize values needed as preconditions for the request like headers
    @BeforeClass
    public void setup()
    {
        headers.put("Authorization","Bearer 43afb5b858c0e581db0fef6b911e6cc225cc07e2a1276d8c728e96e40e8db35b");
    }


    @Test
    public  void createUserTest()
    {
        int random =(int)(Math.random()*50+1);
        //the name we need to make sure of
        String name="sohaila";
        String gender="male";
        String status ="active";
        //String ID = "6160538";

        Response response = RestAssured.given().log().all().contentType(ContentType.JSON).headers(headers)
                .body("{\n" +
                        "    \"name\": \""+name+"\",\n" +
                        "    \"email\": \"se"+random+"a1l@gmail.com\",\n" +
                        "    \"gender\": \"female\",\n" +
                        "    \"status\": \""+status+"\"\n" +
                        "}").post(baseURL + endPoint);


        response.prettyPrint();
        response.then().statusCode(201);
        //return the value of status code without validation
        response.statusCode();

        JsonPath jsonPath = response.jsonPath();
         id= jsonPath.get("id").toString();

         //must be here before the assertions
         //take the user id generated from the previous request and update this user data
        Response response2 = RestAssured.given().log().all().contentType(ContentType.JSON).headers(headers)
                .body("{\n" +
                        "    \"name\": \""+name+"\",\n" +
                        "    \"email\": \"so"+random+"l@gmail.com\",\n" +
                        "    \"gender\": \""+gender+"\",\n" +
                        "    \"status\": \""+status+"\"\n" +
                        "}").put(baseURL + endPoint+id);

        Response response3 = RestAssured.given().log().all().contentType(ContentType.JSON).headers(headers)
                .get(baseURL + endPoint+id);

        response2.prettyPrint();
        response3.prettyPrint();
        response3.then().statusCode(200);

         //status code assertion must be hard assertion because we don't want it to continue when the request fails
         Assert.assertEquals(response.statusCode(),201,"status code is not 201");

         //to compare the name with the name in the response
        Assert.assertEquals(jsonPath.getString("name"),name,"name is not as expected");

        //check hard assertion when this assertion fails
        //won't make the coming next assertions
        //Assert.assertEquals(jsonPath.getString("name"),"sasa","name is not as expected");

        // to make sure that id is generated and not null
        Assert.assertNotNull(jsonPath.get("id"),"id is null");

        //soft assertion will complete all assertions
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(jsonPath.getString("name"),name,"name is not as expected");
        softAssert.assertEquals(jsonPath.getString("name"),"sasa","name is not as expected");
        softAssert.assertEquals(jsonPath.getString("gender"),gender,"gender is not as expected");
        softAssert.assertAll();

        //these requests in here are not visible and won't run because they are after the assertions
        /*
          Response response2 = RestAssured.given().log().all().contentType(ContentType.JSON).headers(headers)
                .body("{\n" +
                        "    \"name\": \""+name+"\",\n" +
                        "    \"email\": \"so"+random+"l@gmail.com\",\n" +
                        "    \"gender\": \""+gender+"\",\n" +
                        "    \"status\": \""+status+"\"\n" +
                        "}").put(baseURL + endPoint+id);

        Response response3 = RestAssured.given().log().all().contentType(ContentType.JSON).headers(headers)
                .get(baseURL + endPoint+id);
         */

    }

    @Test
    public void validateNameIsNull()
    {
        int random =(int)(Math.random()*50+1);

        String name2="Ahmed";
        String gender2="male";
        String email="s"+random+"a1l@gmail.com";
        String status="active";

        //Response response22 = createUser(name2,email,gender2,status);

        //to check that the name field returns error and with same message we entered
        Response response = createUser("","so"+random+"@gmail.com","male","active");
        response.then().statusCode(422);
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();

        //when the response returns an error it is usually in the form field : .. and message : ..
        //validate that the error is in name field
        softAssert.assertEquals(jsonPath.getString("[0].field"),"name");

        //validate that the error message is "can't be blank"
        //we get this message from the requirements
        softAssert.assertEquals(jsonPath.getString("[0].message"),"can't be blank","message is not as expected");
        //we must write this in order to see the results of the soft assertions
        softAssert.assertAll();
    }

    @Test
    public void checkValidationOfAllFields()
    {
        Response response = createUser("","","","");
        response.then().statusCode(422);
        JsonPath jsonPath=response.jsonPath();
        SoftAssert softAssert = new SoftAssert();

        //we will have 4 error messages here because we send all the fields as blank
        for(int i=0;i<=3;i++)
        {
            //we used contains and assert true to validate that it contains "can't be blank" in general
            //this will pass because all messages contains "can't be blank"
            softAssert.assertTrue(jsonPath.getString("["+i+"].message").contains("can't be blank"));

            //to print the field name that has the problem
            //this will fail
            softAssert.assertEquals(jsonPath.getString("["+i+"].message"),"can't be blank","message is not as expected in "+jsonPath.getString("["+i+"].field")+" field ");
        }
        softAssert.assertAll();

    }
}
