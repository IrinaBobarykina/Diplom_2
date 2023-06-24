import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import utils.Generator;
import user.UserOperations;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {

    User user;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
        user = Generator.generateUser();
    }

    @After
    public void tearDown() {
        UserOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Log in using correct data")
    public void logInGetSuccess() {
        Response responseCreating = UserOperations.createUser(user);
        //accessToken нужен для последующего удаления юзера
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
        UserOperations.createUser(user);
        User incorrectUser = new User(RandomStringUtils.randomAlphabetic(10) + "@gmail.com", user.getPassword(), user.getName());
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
