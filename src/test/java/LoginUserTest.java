import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserOperations;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {

    private static String domain_name;
    private static String email;
    private static String password;
    private static String name;


    User user;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
        domain_name = "@gmail.com";
        email = RandomStringUtils.randomAlphabetic(8) + domain_name;
        password = RandomStringUtils.randomAlphabetic(8);
        name = RandomStringUtils.randomAlphabetic(8);
    }

    @After
    public void tearDown() {
        UserOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Log in using correct data")
    public void logInGetSuccessResponse() {
        user = new User(email, password, name);
        Response responseCreating = UserOperations.createUser(user);
        //accessToken нужен для последующего удаления курьера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        Response responseLogin = UserOperations.logInUser(user);
        responseLogin.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Log in with an incorrect password")
    public void logInWithIncorrectPasswordGetError() {
        user = new User(email, password, name);
        UserOperations.createUser(user);
        User incorrectUser = new User(user.getEmail(), RandomStringUtils.randomAlphabetic(10), user.getName());
        UserOperations.logInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Log in with an incorrect email")
    public void logInWithIncorrectEmailGetError() {
        user = new User(email, password, name);
        UserOperations.createUser(user);
        User incorrectUser = new User(RandomStringUtils.randomAlphabetic(10) + domain_name, user.getPassword(), user.getName());
        UserOperations.logInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
