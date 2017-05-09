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
public class ITProfileServiceAuthorized {


    private static String BEARER;

    @Before
    public void getBearer() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                "grant_type=password&client_id=account&username=mirco1&password=mirco&client_secret=4ed92746-f74c-4813-945f-3fab61f74632");
        Request req = new Request.Builder().post(body)
                .url(Config.AUTH)
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

        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .when()
                .get(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200)
                .body("givenName", is("Auth Test"))
                .body("lastName", is("Testmann1"))
                .body("userName", is("mirco1"))
                .body("email", is("test@test.testing"))
                .body("avatar", is("test.png"));
    }

    @Test
    public void updateUserAndReset() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test+Updated&family_name=Testmann1+Updated&preferred_username=mirco1+Updated&email=test%40test.testing+Updated&avatar=test.png+Updated")
                .when()
                .put(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200);

        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .when()
                .get(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200)
                .body("givenName", is("Auth Test Updated"))
                .body("lastName", is("Testmann1 Updated"))
                .body("userName", is("mirco1 updated"))
                .body("email", is("test@test.testing updated"))
                .body("avatar", is("test.png Updated"));

        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test&family_name=Testmann1&preferred_username=mirco1&email=test%40test.testing&avatar=test.png")
                .when()
                .put(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200);
    }

    @Test
    public void changeNameToAlreadyExisting() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test+Updated&family_name=Testmann1+Updated&preferred_username=testman&email=test%40test.testing+Updated&avatar=test.png+Updated")
                .when()
                .put(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(400);
    }

    @Test
    public void updateUserWithoutNameChangeAndReset() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test+Updated&family_name=Testmann1+Updated&preferred_username=mirco1&email=test%40test.testing+Updated&avatar=test.png+Updated")
                .when()
                .put(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200);


        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .when()
                .get(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200)
                .body("givenName", is("Auth Test Updated"))
                .body("lastName", is("Testmann1 Updated"))
                .body("userName", is("mirco1"))
                .body("email", is("test@test.testing updated"))
                .body("avatar", is("test.png Updated"));

        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test&family_name=Testmann1&preferred_username=mirco1&email=test%40test.testing&avatar=test.png")
                .when()
                .put(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(200);
    }

    @Test
    public void changeToEmptyUsername() {
        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test+Updated&family_name=Testmann1+Updated&email=test%40test.testing+Updated&avatar=test.png+Updated")
                .when()
                .put(Config.SERVICE + "/{id}", Config.USER_A_ID)
                .then()
                .statusCode(400);
    }


    @Test
    public void changeOtherUser() {


        given().header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + BEARER)
                .body("given_name=Auth+Test&family_name=Testmann1&preferred_username=mirco1&email=test%40test.testing&avatar=test.png")
                .when()
                .put(Config.SERVICE + "/" + Config.USER_B_ID)
                .then()
                .statusCode(403);


        ;
    }


}
