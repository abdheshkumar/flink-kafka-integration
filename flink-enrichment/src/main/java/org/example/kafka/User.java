package org.example.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonCreator
    public User(@JsonProperty("user_id") String userId, @JsonProperty("user_name") String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }

    public String userId;
    public String userName;
}