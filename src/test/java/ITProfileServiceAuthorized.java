import okhttp3.*;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


/**
 * Created by mirco on 09.05.17.
 */
public class ITProfileServiceAuthorized extends AbstractKeycloakIT {


    private static String BEARER;


    @Before
    public void getBearer() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                "grant_type=password&client_id=account&username=test0r222&password=mirco&client_secret=4ed92746-f74c-4813-945f-3fab61f74632");
        Request req = new Request.Builder().post(body)
                .url(AUTH)
                .build();
        Response response = client.newCall(req)
                .execute();
        String jsonData = response.body()
                .string();
        JSONObject json = new JSONObject(jsonData);
        BEARER = json.get("access_token")
                .toString();
    }

    @Test
    public void getUserWithID() {

        given()
                .header("Authorization", "Bearer " + BEARER)
                .when()
                .get(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200)
                .body("given_name", is("Auth Test"))
                .body("family_name", is("Testmann1"))
                .body("preferred_username", is("test0r222"))
                .body("email", is("test@test.testing"))
                .body("avatar", is("test.png"));
    }

    @Test
    public void updateUserAndReset() {
        String userAUpdated = new JSONObject()
                .put("given_name", "Auth Test Updated")
                .put("family_name", "Testmann1 Updated")
                .put("preferred_username", "test0r222 Updated")
                .put("email", "test@test.testing Updated")
                .put("avatar", "test.png Updated")
                .toString();


        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(userAUpdated)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + BEARER)
                .when()
                .get(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200)
                .body("given_name", is("Auth Test Updated"))
                .body("family_name", is("Testmann1 Updated"))
                .body("preferred_username", is("test0r222 updated"))
                .body("email", is("test@test.testing updated"))
                .body("avatar", is("test.png Updated"));

        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(USER_A_ORIGINAL)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200);
    }

    @Test
    public void changeNameToAlreadyExisting() {

        String userAUpdated = new JSONObject()
                .put("given_name", "Auth Test")
                .put("family_name", "Testmann1")
                .put("preferred_username", "test0r222")
                .put("email", "test@test.testing")
                .put("avatar", "test.png")
                .toString();

        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(userAUpdated)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(400);
    }

    @Test
    public void updateUserWithoutNameChangeAndReset() {

        String userAUpdated = new JSONObject()
                .put("given_name", "Auth Test A")
                .put("family_name", "testman B")
                .put("preferred_username", "test0r222")
                .put("email", "test@test.testing C")
                .put("avatar", "test.png D")
                .toString();

        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(userAUpdated)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200);


        given()
                .header("Authorization", "Bearer " + BEARER)
                .when()
                .get(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200)
                .body("givenName", is("Auth Test A"))
                .body("lastName", is("Testmann1 B"))
                .body("userName", is("test0r222"))
                .body("email", is("test@test.testing C"))
                .body("avatar", is("test.png D"));

        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(USER_A_ORIGINAL)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(200);
    }

    @Test
    public void changeToEmptyUsername() {

        String userAUpdated = new JSONObject()
                .put("given_name", "Auth Test A")
                .put("family_name", "testman B")
                .put("preferred_username", "")
                .put("email", "test@test.testing C")
                .put("avatar", "test.png D")
                .toString();

        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(userAUpdated)
                .when()
                .put(SERVICE + "/{id}", USER_A_ID)
                .then()
                .statusCode(400);
    }


    @Test
    public void changeOtherUser() {


        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER)
                .body(USER_A_ORIGINAL)
                .when()
                .put(SERVICE + "/" + USER_B_ID)
                .then()
                .statusCode(403);


        ;
    }


}
