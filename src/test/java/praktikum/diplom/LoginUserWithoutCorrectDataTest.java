package praktikum.diplom;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class LoginUserWithoutCorrectDataTest {

    private final RequestLoggingFilter requestFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseFilter = new ResponseLoggingFilter();
    static String token;
    private static final StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    private final AuthorizationData authorizationData;

    public LoginUserWithoutCorrectDataTest(AuthorizationData authorizationData) {
        this.authorizationData = authorizationData;
    }

    @Parameterized.Parameters
    public static Object[] loginUser() {
        return new Object[] {
                new AuthorizationData(null, client.password),
                new AuthorizationData("wrong@gmail.com", client.password),
                new AuthorizationData(client.email, null),
                new AuthorizationData(client.email, "666666")
        };
    }

    @BeforeClass
    public static void userRegistration() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        client.userRegistration();
    }

    @AfterClass
    public static void userDelete() {
        token = client.getAuthorisationToken();
        client.userDelete(token);
    }

    @Test
    public void shouldReturnStatusCode401ForAuthorizationUserWithWrongData() {
        given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(authorizationData)
                .when()
                .post("/api/auth/login")
                .then()
                .assertThat().statusCode(401);

        client.justWait();
    }

    @Test
    public void shouldReturnMessageEmailOrPasswordAreIncorrectForAuthorizationUserWithWrongData() {
        given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(authorizationData)
                .when()
                .post("/api/auth/login")
                .then()
                .assertThat().body("message", equalTo("email or password are incorrect"));

        client.justWait();
    }
}
