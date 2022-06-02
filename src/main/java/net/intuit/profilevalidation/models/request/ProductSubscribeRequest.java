package net.intuit.profilevalidation.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSubscribeRequest {
    private String userId;
    private int productId;
}
