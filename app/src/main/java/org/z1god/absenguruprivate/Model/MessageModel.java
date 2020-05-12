package org.z1god.absenguruprivate.Model;

import com.google.gson.annotations.SerializedName;

public class MessageModel {
    @SerializedName("message")
    private String message;

    public MessageModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
