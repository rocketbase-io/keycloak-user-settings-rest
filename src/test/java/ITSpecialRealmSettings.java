import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

/**
 * Created by mirco on 09.05.17.
 */
public class ITSpecialRealmSettings extends AbstractKeycloakIT {

    private String bearer;

    @Before
    public void init() throws IOException {
        bearer = "Bearer " + getBearer("tester", "12345", "5a051492-d016-4896-ba7d-6b28ab22ab1c", AUTH_NO_NAME_CHANGE);
    }

    @Test
    public void usernameChangeNotAllowed() {
        String userAUpdated = new JSONObject()
                .put("given_name", "A")
                .put("family_name", "B")
                .put("preferred_username", "der tester")
                .put("email", "test@test.testing")
                .put("avatar", "test.png")
                .toString();

        given().header("Content-Type", "application/json")
                .header("Authorization", bearer)
                .body(userAUpdated)
                .when()
                .put(SERVICE_NO_NAME_CHANGE + "/{id}", USER_NO_NAME_CHANGE)
                .then()
                .statusCode(400);
    }

}
