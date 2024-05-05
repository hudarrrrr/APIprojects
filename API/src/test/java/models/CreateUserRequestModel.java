//Serialization
package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


    @JsonInclude(JsonInclude.Include.NON_NULL)

    @JsonPropertyOrder({
            "name",
            "email",
            "gender",
            "status"
    })

    public class CreateUserRequestModel
    {
        @JsonProperty("nme")
        public String name;
        @JsonProperty("email")
        public String email;
        @JsonProperty("gender")
        public String gender;
        @JsonProperty("status")
        public String status;

    }


