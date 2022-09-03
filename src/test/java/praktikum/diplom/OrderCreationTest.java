package praktikum.diplom;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderCreationTest {
    StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    String token;

    @Before
    public void clearData() {
        token = client.getAuthorisationToken();
        if (token != null) {
            client.userDelete(token);
        }

        client.userRegistration();

        token = client.getAuthorisationToken();
    }

    @After
    public void deleteUser() {

        token = client.getAuthorisationToken();
        client.userDelete(token);

    }

    @Test
    public void shouldReturnStatusCode200ForOrderCreationWithAuthorization() {

        client.makeOrder(token, client.ingredientsSet)
                .then()
                .statusCode(200);

    }

    @Test
    public void shouldReturnSuccessTrueForOrderCreationWithAuthorization() {

        boolean isSuccess = client.makeOrder(token, client.ingredientsSet)
                .then()
                .extract()
                .as(SuccessOrderWithAuthorization.class)
                .isSuccess();
        Assert.assertEquals(true, isSuccess);

    }

    @Test
    public void shouldReturnCustomerEmailForOrderCreationWithAuthorization() {

        String customerEmail = client.makeOrder(token, client.ingredientsSet)
                .then()
                .extract()
                .as(SuccessOrderWithAuthorization.class)
                .getOrder()
                .getOwner()
                .getEmail();
        Assert.assertEquals(client.email, customerEmail);

    }

    @Test
    public void shouldReturnStatusCode200ForOrderCreationWithoutAuthorization() {

        client.makeOrder(null, client.ingredientsSet)
                .then()
                .statusCode(200);

    }

    @Test
    public void shouldReturnSuccessTrueForOrderCreationWithoutAuthorization() {

        boolean isSuccess = client.makeOrder(null, client.ingredientsSet)
                .then()
                .extract()
                .as(SuccessOrderWithoutAuthorisation.class)
                .isSuccess();
        Assert.assertEquals(true, isSuccess);

    }

    @Test
    public void shouldReturnSuccessTrueForOrderCreationWithBunMainSouse() {

        IngredientsSet ingredientsSet = new IngredientsSet(new String[]{client.bun, client.main, client.sauce});
        boolean isSuccess = client.makeOrder(null, ingredientsSet)
                .then()
                .extract()
                .as(SuccessOrderWithoutAuthorisation.class)
                .isSuccess();
        Assert.assertEquals(true, isSuccess);

    }

    @Test
    public void shouldReturnStatusCode200ForOrderCreationWithBunMainSouse() {

        IngredientsSet ingredientsSet = new IngredientsSet(new String[]{client.bun, client.main, client.sauce});
        client.makeOrder(null, ingredientsSet)
                .then()
                .statusCode(200);

    }

    @Test
    public void shouldReturnSuccessFalseForOrderWithoutIngredients() {

        IngredientsSet ingredientsSet = new IngredientsSet(new String[]{});
        boolean isSuccess = client.makeOrder(null, ingredientsSet)
                .then()
                .extract()
                .as(SuccessOrderWithoutAuthorisation.class)
                .isSuccess();
        Assert.assertEquals(false, isSuccess);

    }

    @Test
    public void shouldReturnStatusCode400ForOrderWithoutIngredients() {

        IngredientsSet ingredientsSet = new IngredientsSet(new String[]{});
        client.makeOrder(null, ingredientsSet)
                .then()
                .statusCode(400);

    }

}
