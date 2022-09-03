package praktikum.diplom;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreatingUserWithoutRequiredDataTest {
    private final RequestLoggingFilter requestFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseFilter = new ResponseLoggingFilter();
    StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    private final RegistrationData registrationData;

    public CreatingUserWithoutRequiredDataTest(RegistrationData registrationData) {
        this.registrationData = registrationData;
    }

    @Parameterized.Parameters
    public static Object[] registerUser() {
        return new Object[] {
                new RegistrationData(null, "123456", "User"),
                new RegistrationData("user@gmail.com", null, "User"),
                new RegistrationData("user@gmail.com", "123456", null)
        };
    }

    @BeforeClass
    public static void baseUri() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void shouldReturnStatusCode403ForCreatingUserWithoutRequiredData() {

        given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(registrationData)
                .when()
                .post("/api/auth/register")
                .then()
                .assertThat().statusCode(403);

        client.justWait();
    }

    @Test
    public void shouldReturnMessageEmailPasswordAndNameAreRequiredFieldsForCreatingUserWithoutRequiredData() {

        given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(registrationData)
                .when()
                .post("/api/auth/register")
                .then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));

        client.justWait();
    }
}
