import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserEditedData;
import user.UserOperations;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class EditUserTest {
    private static String domain_name;
    private static String email;
    private static String password;
    private static String name;
    private static String newEmail;
    private static String newPassword;
    private static String newName;

    User user;
    UserEditedData userEditedData;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
        domain_name = "@gmail.com";
        email = RandomStringUtils.randomAlphabetic(8) + domain_name;
        password = RandomStringUtils.randomAlphabetic(8);
        name = RandomStringUtils.randomAlphabetic(8);
        newEmail = RandomStringUtils.randomAlphabetic(8) + domain_name;
        newPassword = RandomStringUtils.randomAlphabetic(8);
        newName = RandomStringUtils.randomAlphabetic(8);
    }

    @After
    public void tearDown() {
        UserOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Edit an email of an authorized user")
    public void editEmailAuthorizedUserGetSuccessResponse() {
        user = new User(email, password, name);
        userEditedData = new UserEditedData(newEmail, newPassword, newName);

        Response responseCreating = UserOperations.createUser(user);
        //accessToken нужен для редактирования и последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserOperations.editAuthorizedUser(accessToken, userEditedData).then().assertThat().statusCode(HttpStatus.SC_OK).and().body("success", equalTo(true)).and().body("user", notNullValue());
    }

    @Test
    @DisplayName("Edit an email of an unauthorized user")
    public void editEmailUnauthorizedUserGetError() {
        user = new User(email, password, name);
        userEditedData = new UserEditedData(newEmail, newPassword, newName);

        Response responseCreating = UserOperations.createUser(user);
        //accessToken нужен для редактирования и последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserOperations.editUnauthorizedUser(accessToken, userEditedData).then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED).and().body("success", equalTo(false)).and().body("message", equalTo("You should be authorised"));
    }
}
