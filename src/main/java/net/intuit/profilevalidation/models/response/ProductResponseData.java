package net.intuit.profilevalidation.models.response;

import lombok.Getter;
import lombok.Setter;
import net.intuit.profilevalidation.models.Product;

import java.util.Map;

@Getter
@Setter
public class ProductResponseData {
    Map<String, Product> products;
}
