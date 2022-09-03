package praktikum.diplom;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class WrongIngredientTest {
    private final RequestLoggingFilter requestFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseFilter = new ResponseLoggingFilter();
    static StellarBurgersAPIClient client = new StellarBurgersAPIClient();
    private final IngredientsSet ingredientsSet;

    public WrongIngredientTest(IngredientsSet ingredientsSet) {
        this.ingredientsSet = ingredientsSet;
    }
    @Parameterized.Parameters
    public static IngredientsSet[] ingredientSet() {

        return new IngredientsSet[]  {
                new IngredientsSet(new String[]{"wrong666bun", client.main, client.sauce}),
                new IngredientsSet(new String[]{client.bun, client.main, "wrong666sauce"}),
                new IngredientsSet(new String[]{client.bun, "wrong666main", client.sauce}),
                new IngredientsSet(new String[]{"wrong666bunn", client.main, client.sauce}),
                new IngredientsSet(new String[]{client.bun, client.main, "wrong666sauc"}),
                new IngredientsSet(new String[]{client.bun, "wrong666main1", client.sauce}),
                new IngredientsSet(new String[]{"wrongbun24sumbols24sumbo", client.main, client.sauce}),
                new IngredientsSet(new String[]{client.bun, client.main, "wrongsauce24sumbols24sum"}),
                new IngredientsSet(new String[]{client.bun, "wrongmain24sumbols24sumb", client.sauce})
        };
    }
    @BeforeClass
    public static void baseUri() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void shouldReturnStatusCode500ForOrderWithWrongIngredientsId() {
        client.justWait();

        given()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .and()
                .body(ingredientsSet)
                .post("/api/orders")
                .then()
                .statusCode(500);

    }
}
