package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Constants
{
    public static String baseURL = "https://gorest.co.in/public/v2/";
    public static String endPoint = "users/";
    public static String postsEndPoint = "posts/";
    public static String userIdSlash = "/";
    public static String token="Bearer 43afb5b858c0e581db0fef6b911e6cc225cc07e2a1276d8c728e96e40e8db35b";

    //method that returns a Map
    public static Map<String,String> generalheaders()
    {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);
        return headers;
    }

    public static String getCleanObject(Object object) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }


}
