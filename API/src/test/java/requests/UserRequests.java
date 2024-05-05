package requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CreateUserRequestModel;
import models.GetSingleUserResponseModel;
import utils.Constants;

import static utils.Constants.*;

//class that contains user requests as (get,post,delete,put,patch,...)
public class UserRequests
{
    public static Response createUser(String name,String email,String gender,String status)
    {
        return   RestAssured.given().log().all().contentType(ContentType.JSON).headers(Constants.generalheaders())
                .body("{\n" +
                        "    \"name\": \""+name+"\",\n" +
                        "    \"email\": \""+email+"\",\n" +
                        "    \"gender\": \""+gender+"\",\n" +
                        "    \"status\": \""+status+"\"\n" +
                        "}").post(baseURL + endPoint);

    }

    public static Response getSingleUser(String id)
    {
        return RestAssured.given().log().all().headers(Constants.generalheaders())
                .get(baseURL + endPoint+id);

    }

    public static Response deleteUser(String id)
    {
        return  RestAssured.given().log().all().headers(Constants.generalheaders())
                .delete(baseURL + endPoint+id);
    }

    //Deserialization
    public static GetSingleUserResponseModel getSingleUserAsClass(String id)
    {
        return getSingleUser(id).as(GetSingleUserResponseModel.class);
    }

    //Serialization
    public static Response createUserUsingPojo(String name,String email,String gender,String status) throws JsonProcessingException
    {
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel();
        createUserRequestModel.name = name;
        createUserRequestModel.email = email;
        createUserRequestModel.gender = gender;
        createUserRequestModel.status = status;
        return   RestAssured.given().log().all().contentType(ContentType.JSON).headers(Constants.generalheaders())
                .body(Constants.getCleanObject(createUserRequestModel)).post(baseURL + endPoint);

    }

    //Create user with serialization and returns response as a java object
    public static GetSingleUserResponseModel createUserUsingPOJO2(String name,String email,String gender,String status) throws JsonProcessingException
    {
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel();
        createUserRequestModel.name = name;
        createUserRequestModel.email = email;
        createUserRequestModel.gender = gender;
        createUserRequestModel.status = status;
        return   RestAssured.given().log().all().contentType(ContentType.JSON).headers(Constants.generalheaders())
                .body(Constants.getCleanObject(createUserRequestModel)).post(baseURL + endPoint)
                .then().statusCode(201).extract()
                .as(GetSingleUserResponseModel.class);

    }
}
