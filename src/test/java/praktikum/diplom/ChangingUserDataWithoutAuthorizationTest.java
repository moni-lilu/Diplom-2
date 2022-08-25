package praktikum.diplom;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class ChangingUserDataWithoutAuthorizationTest {
    private final RequestLoggingFilter requestFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseFilter = new ResponseLoggingFilter();
    private final RegistrationData changingData;
    static StellarBurgersAPIClient client = new StellarBurgersAPIClient();

    static String newEmail = "testsecondemail@gmail.com";
    static String newPassword = "newPassword123";
    static String newName = "New-Magdalena";
    static String token;
    JsonObject jsonForRequest;


    public ChangingUserDataWithoutAuthorizationTest(RegistrationData changingData) {
        this.changingData = changingData;
    }

    @Parameterized.Parameters
    public static Object[] changeData() {
        return new Object[] {
                new RegistrationData(newEmail, client.password, client.name),
                new RegistrationData(client.email, newPassword, client.name),
                new RegistrationData(client.email, client.password, newName)
        };
    }

    @BeforeClass
    public static void dataPreparation() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        token = client.getAuthorisationToken();
        if (token != null) {
            client.userDelete(token);
        }
        client.userRegistration();
    }

    @AfterClass
    public static void dataClear() {
        token = client.getAuthorisationToken();
        client.userDelete(token);
    }

    @Test
    public void shouldReturnStatusCode403IfChangingDataWithoutAuthorization() {

        dataChange()
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void shouldReturnSuccessFalseIfChangingDataWithoutAuthorization() {

        dataChange()
                .then()
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    public void shouldReturnMessageYouShouldBeAuthorisedIfChangingDataWithoutAuthorization() {

        dataChange()
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"));

    }

    public Response dataChange() {

        JsonObject json = new JsonObject();
        if (!changingData.getEmail().equals(client.email)) {
            json.addProperty("email", changingData.getEmail());
        }
        if (!changingData.getPassword().equals(client.password)) {
            json.addProperty("password", changingData.getPassword());
        }
        if (!changingData.getName().equals(client.name)) {
            json.addProperty("name", changingData.getName());
        }

        return given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .patch("/api/auth/user");


    }
}
