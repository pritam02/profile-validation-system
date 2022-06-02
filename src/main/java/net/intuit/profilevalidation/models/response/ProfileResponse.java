package net.intuit.profilevalidation.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private int statusCode;
    private String status;
    private ProfileResponseData data;
}
