package utils;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static utils.Specifications.requestSpec;
import static utils.Specifications.requestSpecWithToken;

import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class AuthorizedUser {
    private String userName;
    private String password;
    private String userID;
    private String token;

    private AuthorizedUser() {
        this.setUserName("User" + String.valueOf(System.nanoTime()).substring(8));
        this.setPassword("bBG5xsH!");
    }

    public static AuthorizedUser createAuthorizedUser(){
        AuthorizedUser authorizedUser = new AuthorizedUser();

        Map<String,String> request = new HashMap<>();
        request.put("userName", authorizedUser.getUserName());
        request.put("password", authorizedUser.getPassword());

        RequestSpecification accountReqSpec = requestSpec(Endpoint.ACCOUNT_BASE_URL);
        String userId = given(accountReqSpec)
                .body(request)
                .when().post("User")
                .then().log().all()
                .statusCode(201)
                .contentType(JSON)
                .extract()
                .jsonPath().getString("userID");

        authorizedUser.setUserID(userId);

        String token = given(accountReqSpec)
                .body(request)
                .when().post("GenerateToken")
                .then().log().all()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .jsonPath().getString("token");

        authorizedUser.setToken(token);
        return authorizedUser;
    }

    public void deleteUser() {
        // !! there is an error in Swagger for this request - wrong response codes for success, unauthorized and error
        given(requestSpecWithToken(Endpoint.ACCOUNT_BASE_URL, token))
                .when().delete("User/" + userID)
                .then().log().all()
                .statusCode(204);
        System.out.println("remove done");
    }
}
