package utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {

    public static RequestSpecification requestSpec(Endpoint endpoint) {
        return new RequestSpecBuilder()
                .setBaseUri(endpoint.getBaseUrl())
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification requestSpecWithToken(Endpoint endpoint, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(endpoint.getBaseUrl())
                .addHeader("Authorization", "Bearer " + token)
                .setContentType(ContentType.JSON)
                .build();
    }
}
