package praktikum.diplom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreatingUserTest {

    StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    String token;

    @Before
    public void clearData() {
        token = client.getAuthorisationToken();
        if (token != null) {
            client.userDelete(token);
        }
    }

    @After
    public void deleteUser() {

        token = client.getAuthorisationToken();
        client.userDelete(token);

    }

    @Test
    public void creatingUniqueUserStatusCode200Test() {

        client.userRegistration()
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void creatingUniqueUserSuccessTrueTest() {

        client.userRegistration()
                .then()
                .assertThat().body("success", equalTo(true));

    }

    @Test
    public void shouldReturnStatusCode403IfUserAlreadyExists() {

        client.userRegistration();
        client.userRegistration()
                .then()
                .assertThat().statusCode(403);

    }

    @Test
    public void shouldReturnMessageUserAlreadyExistsIfUserAlreadyExists() {

        client.userRegistration();
        client.userRegistration()
                .then()
                .assertThat().body("message", equalTo("User already exists"));

    }
}
