package net.intuit.profilevalidation.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private int statusCode;
    private String status;
    private ProductResponseData data;
}
