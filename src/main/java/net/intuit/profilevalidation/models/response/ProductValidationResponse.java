package net.intuit.profilevalidation.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductValidationResponse {
    private int statusCode;
    private String status;
    private String userId;
    private ProductValidationResponseData data;
}
