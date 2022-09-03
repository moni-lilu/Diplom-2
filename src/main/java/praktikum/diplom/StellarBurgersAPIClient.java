package praktikum.diplom;

import com.google.gson.JsonObject;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StellarBurgersAPIClient {
    public static final String baseURI = "https://stellarburgers.nomoreparties.site";
    private final RequestLoggingFilter requestFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseFilter = new ResponseLoggingFilter();
    public static String email = "right-second@gmail.com";
    public static String password = "123456";
    public static String name = "Super-Star";

    public static String bun = "61c0c5a71d1f82001bdaaa6d";
    public static String main = "61c0c5a71d1f82001bdaaa6f";
    public static String sauce = "61c0c5a71d1f82001bdaaa72";
    String[] ingredients = {bun, main, sauce};
    IngredientsSet ingredientsSet = new IngredientsSet(ingredients);

    RegistrationData registrationData = new RegistrationData(email, password, name);
    AuthorizationData authorizationData = new AuthorizationData(email, password);
    SuccessfulLogInAnswer authorizationAnswer;

    public RequestLoggingFilter getRequestFilter() {
        return requestFilter;
    }
    public ResponseLoggingFilter getResponseFilter() {
        return responseFilter;
    }
    public String getBaseURI() {
        return baseURI;
    }
    public Response userRegistration() {
        this.justWait();
        return  given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .and()
                .body(registrationData)
                .when()
                .post("/api/auth/register");
    }

    public Response userRegistrationWithParam(RegistrationData registrationData) {
        this.justWait();
        return  given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .and()
                .body(registrationData)
                .when()
                .post("/api/auth/register");
    }

    public Response userAuthorization() {
        this.justWait();
        return given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .and()
                .body(authorizationData)
                .when()
                .post("/api/auth/login");
    }
    public String getAuthorisationToken() {

        this.justWait();

        authorizationAnswer = userAuthorization()
                .then()
                .extract()
                .as(SuccessfulLogInAnswer.class);

        this.justWait();

        return authorizationAnswer.getAccessToken();
    }
    public String userGetTokenParam(AuthorizationData authorizationData) {

        this.justWait();

        authorizationAnswer = given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .and()
                .body(authorizationData)
                .when()
                .post("/api/auth/login")
                .then()
                .extract()
                .as(SuccessfulLogInAnswer.class);

        this.justWait();

        return authorizationAnswer.getAccessToken();
    }
    public Response userDelete(String token) {

        this.justWait();
        return given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .delete("/api/auth/user");
    }

    public SuccessChangeData usersDataChange(String token, RegistrationData changedData){

        JsonObject json = new JsonObject();
        if (!changedData.getEmail().equals(email)) {
            json.addProperty("email", changedData.getEmail());
        }
        if (!changedData.getPassword().equals(password)) {
            json.addProperty("password", changedData.getPassword());
        }
        if (!changedData.getName().equals(name)) {
            json.addProperty("name", changedData.getName());
        }

        this.justWait();

        return given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(json)
                .patch("/api/auth/user")
                .then()
                .extract()
                .as(SuccessChangeData.class);
    }

    public Response makeOrder(String token, IngredientsSet ingredientsSet) {

        this.justWait();

        if (token != null) {
            return given()
                    .filters(requestFilter, responseFilter)
                    .baseUri(baseURI)
                    .header("Content-type", "application/json")
                    .header("Authorization", token)
                    .and()
                    .body(ingredientsSet)
                    .post("/api/orders");
        } else {
            return given()
                    .filters(requestFilter, responseFilter)
                    .baseUri(baseURI)
                    .header("Content-type", "application/json")
                    .and()
                    .body(ingredientsSet)
                    .post("/api/orders");
        }
    }

    public Response ordersList(String token) {

        this.justWait();

        if (token == null) {
            return given()
                    .filters(requestFilter, responseFilter)
                    .baseUri(baseURI)
                    .header("Content-type", "application/json")
                    .when()
                    .get("/api/orders");
        }
        return given()
                .filters(requestFilter, responseFilter)
                .baseUri(baseURI)
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .get("/api/orders");

    }


    public void justWait()
    {
        try {
            Thread.sleep(400);
        } catch (Exception e) {
            //
        }
    }
}
