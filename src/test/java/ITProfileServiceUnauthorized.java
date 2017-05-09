import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

/**
 * Created by mirco on 09.05.17.
 */
public class ITProfileServiceUnauthorized extends AbstractKeycloakIT {


    @Test
    public void getWithoutBearer() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .get(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(401);
    }

    @Test
    public void updateWithoutBearer() {
        given().header("Content-Type", "application/json")
                .body(USER_A_ORIGINAL)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(401);

    }

    @Test
    public void doPostRequest() {
        when().post(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(405);
    }

    @Test
    public void doDeleteRequest() {
        when().delete(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(405);
    }
}
