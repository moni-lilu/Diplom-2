package praktikum.diplom;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class TakeOrdersListTest {
    StellarBurgersAPIClient client = new StellarBurgersAPIClient();

    @Test
    public void shouldReturnStatusCode200IfGetListForAuthorizedUserWithoutOrders() {

        String email = RandomStringUtils.random(10, true, true) + "@gmail.com";
        RegistrationData registrationData = new RegistrationData(email, client.password, client.name);
        client.userRegistrationWithParam(registrationData);
        client.justWait();
        String token = client.userGetTokenParam(new AuthorizationData(email, client.password));

        client.ordersList(token)
                .then()
                .statusCode(200);
        client.justWait();
        client.userDelete(token);
    }

    @Test
    public void shouldReturnSuccessTrueIfGetListForAuthorizedUserWithoutOrders() {

        String email = RandomStringUtils.random(10, true, true) + "@gmail.com";
        RegistrationData registrationData = new RegistrationData(email, client.password, client.name);
        client.justWait();
        client.userRegistrationWithParam(registrationData);
        String token = client.userGetTokenParam(new AuthorizationData(email, client.password));
        client.justWait();
        client.ordersList(token)
                .then()
                .assertThat()
                .body("success", equalTo(true));

        client.userDelete(token);
        client.justWait();
    }

    @Test
    public void shouldReturnStatusCode200IfGetListForAuthorizedUserWithOrders() {

        client.userRegistration();
        String token = client.getAuthorisationToken();

        client.justWait();

        client.makeOrder(token, client.ingredientsSet);

        client.justWait();

        client.ordersList(token)
                .then()
                .statusCode(200);

        client.justWait();

        client.userDelete(token);
    }

    @Test
    public void shouldReturnSuccessTrueIfGetListForAuthorizedUserWithOrders() {

        client.userRegistration();
        String token = client.getAuthorisationToken();
        client.justWait();

        client.makeOrder(token, client.ingredientsSet);

        client.justWait();

        client.ordersList(token)
                .then()
                .assertThat()
                .body("success", equalTo(true));

        client.justWait();

        client.userDelete(token);
    }

    @Test
    public void shouldReturnStatusCode401IfGetListForUnauthorizedUser() {

        client.ordersList(null)
                .then()
                .statusCode(401);

    }

    @Test
    public void shouldReturnSuccessFalseIfGetListForUnauthorizedUser() {

        client.ordersList(null)
                .then()
                .assertThat()
                .body("success", equalTo(false));


    }

}
