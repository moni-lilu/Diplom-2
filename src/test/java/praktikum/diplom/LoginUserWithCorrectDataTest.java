package praktikum.diplom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserWithCorrectDataTest {
    StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    String token;

    @Before
    public void clearDataAndCreateUser() {
        token = client.getAuthorisationToken();
        if (token != null) {
            client.userDelete(token);
        }

        client.userRegistration();
    }

    @After
    public void deleteUser() {

        token = client.getAuthorisationToken();

        client.userDelete(token);

    }

    @Test
    public void shouldReturnStatusCode200ForLoginWithCorrectData() {

        client.userAuthorization()
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void shouldReturnSuccessTrueForLoginWithCorrectData() {

        client.userAuthorization()
                .then()
                .assertThat().body("success", equalTo(true));

    }

}
