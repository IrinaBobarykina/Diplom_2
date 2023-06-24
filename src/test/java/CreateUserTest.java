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

import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest {

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
    @DisplayName("Create a new user using correct data")
    public void createNewUserGetSuccess() {
        Response response = UserOperations.createUser(user);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Create 2 similar users")
    public void createTwoSimilarUsersGetError() {
        Response response = UserOperations.createUser(user);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        UserOperations.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create an user without an email")
    public void createUserWithoutEmailGetError() {

        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        user = new User(password, name);

        UserOperations.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Create an user without a password")
    public void createUserWithoutPasswordGetError() {

        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String name = RandomStringUtils.randomAlphabetic(8);
        user = new User(email, name);

        UserOperations.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Create an user without a name")
    public void createUserWithoutNameGetError() {

        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(8);

        user = new User(email, password);
        UserOperations.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }
}
