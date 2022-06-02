package net.intuit.profilevalidation.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductValidationRequest {
    private String userId;
    private int productId;
    private String status;
}
