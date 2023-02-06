package apitests;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static utils.AuthorizedUser.createAuthorizedUser;
import static utils.Specifications.requestSpec;
import static utils.Specifications.requestSpecWithToken;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojos.AddBookToCollectionRequest;
import pojos.Books;
import pojos.BooksForCollectionRequest;
import utils.AuthorizedUser;
import utils.Endpoint;

public class CollectionsTests {

    private AuthorizedUser authorizedUser;

    @BeforeMethod
    public void prepareUser() {
        authorizedUser = createAuthorizedUser();
    }

    @Test
    public void addBookToCollectionTest() {
        String firstBookIsbn = given(requestSpec(Endpoint.BOOK_STORE_BASE_URL))
                .when().get("Books")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(JSON)
                .body("books", hasSize(greaterThan(0)))
                .extract()
                .jsonPath().getString("books[0].isbn");

        AddBookToCollectionRequest addBookReq = AddBookToCollectionRequest.builder()
                .userId(authorizedUser.getUserID())
                .collectionOfIsbns(singletonList(new BooksForCollectionRequest(firstBookIsbn)))
                .build();

        //!! there is an error in Swagger for this request - response example for 201 code has wrong Json structure
        String addedBookId = given(requestSpecWithToken(Endpoint.BOOK_STORE_BASE_URL, authorizedUser.getToken()))
                .body(addBookReq)
                .when().post("Books")
                .then().log().all()
                .statusCode(201)
                .contentType(JSON)
                .extract()
                .jsonPath().getString("books[0].isbn");

        List<Books> userBooks = given(requestSpecWithToken(Endpoint.ACCOUNT_BASE_URL, authorizedUser.getToken()))
                .get("User/" + authorizedUser.getUserID())
                .then().log().all()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .jsonPath().getList("books", Books.class);

        Assert.assertTrue(userBooks.stream().anyMatch(e -> e.getIsbn().equals(addedBookId)));
    }

    @AfterMethod(alwaysRun = true)
    private void deleteUser() {
        authorizedUser.deleteUser();
    }
}
