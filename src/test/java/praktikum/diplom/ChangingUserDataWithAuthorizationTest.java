package praktikum.diplom;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class ChangingUserDataWithAuthorizationTest {

    private final RequestLoggingFilter requestFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseFilter = new ResponseLoggingFilter();
    static StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    String createdUserToken;
    String updatedUserToken;
    AuthorizationData authorizationData;
    static String newEmail = "testsecondemail@gmail.com";
    static String newPassword = "newPassword123";
    static String newName = "New-Magdalena";
    private final RegistrationData changingData;

    public ChangingUserDataWithAuthorizationTest(RegistrationData changingData) {
        this.changingData = changingData;
    }

    @Parameterized.Parameters
    public static Object[] changeData() {
        return new Object[] {
                new RegistrationData(newEmail, StellarBurgersAPIClient.password, StellarBurgersAPIClient.name),
                new RegistrationData(StellarBurgersAPIClient.email, newPassword, StellarBurgersAPIClient.name),
                new RegistrationData(StellarBurgersAPIClient.email, StellarBurgersAPIClient.password, newName),
                new RegistrationData(newEmail, newPassword, StellarBurgersAPIClient.name),
                new RegistrationData(StellarBurgersAPIClient.email, newPassword, newName),
                new RegistrationData(newEmail, StellarBurgersAPIClient.password, newName),
                new RegistrationData(newEmail, newPassword, newName)
        };
    }

    @BeforeClass
    public static void userRegistration() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Before
    public void clearDataAndCreateNewUser() {
        clearData();

        client.userRegistration();
    }

    @After
    public void deleteUsers() {
        clearData();
    }

    @Test
    public void shouldSuccessfullyChangeData() {
        SuccessChangeData answer = client.usersDataChange(
                client.getAuthorisationToken(),
                changingData);

        Assert.assertEquals(changingData.getEmail(), answer.getUser().getEmail());
        Assert.assertEquals(changingData.getName(), answer.getUser().getName());

        given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(changingData)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200);

    }

    public void clearData() {
        /*
        Похоже, что при изменении e-mail происходит ни его изменение,
        а добавление нового пользователя со старыми паролем, именем и новым e-mail.
        В связи с этим удаляю изначально созданного пользователя и пользователя с отредактированными данными
         */
        createdUserToken = client.getAuthorisationToken();
        if (createdUserToken != null) {
            client.userDelete(createdUserToken);
        }

        authorizationData = new AuthorizationData(changingData.getEmail(), changingData.getPassword());

        updatedUserToken = client.userGetTokenParam(authorizationData);
        if (updatedUserToken != null) {
            client.userDelete(updatedUserToken);
        }

    }
}
