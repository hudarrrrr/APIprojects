//Deserialization
package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "gender",
        "status"
})

public class GetSingleUserResponseModel
{
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("nme")
    public String name;
    @JsonProperty("email")
    public String email;
    @JsonProperty("gender")
    public String gender;
    @JsonProperty("status")
    public String status;

}
