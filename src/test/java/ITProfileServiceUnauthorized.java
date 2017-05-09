import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

/**
 * Created by mirco on 09.05.17.
 */
public class ITProfileServiceUnauthorized {


    private final Config c = new Config();

    @Test
    public void getWithoutBearer() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .get(c.SERVICE + "/{id}", c.USER_A_ID)
                .then()
                .statusCode(401);
    }

    @Test
    public void updateWithoutBearer() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .body("given_name=Auth+Test+Updated&family_name=Testmann1+Updated&preferred_username=mirco1&email=test%40test.testing+Updated&avatar=test.png+Updated")
                .when()
                .put(c.SERVICE + "/{id}", c.USER_A_ID)
                .then()
                .statusCode(401);

    }

    @Test
    public void doPostRequest() {
        when().post(c.SERVICE + "/{id}", c.USER_A_ID)
                .then()
                .statusCode(405);
    }

    @Test
    public void doDeleteRequest() {
        when().delete(c.SERVICE + "/{id}", c.USER_A_ID)
                .then()
                .statusCode(405);
    }
}
