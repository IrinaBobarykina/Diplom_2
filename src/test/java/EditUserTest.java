import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserEditedData;
import utils.Generator;
import user.UserOperations;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class EditUserTest {

    User user;
    UserEditedData userEditedData;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
        user = Generator.generateUser();
        userEditedData = Generator.generateUserData();
    }

    @After
    public void tearDown() {
        UserOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Edit an email of an authorized user")
    public void editEmailAuthorizedUserGetSuccessResponse() {

        Response responseCreating = UserOperations.createUser(user);
        //accessToken нужен для редактирования и последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserOperations.editAuthorizedUser(accessToken, userEditedData).then().assertThat().statusCode(HttpStatus.SC_OK).and().body("success", equalTo(true)).and().body("user", notNullValue());
    }

    @Test
    @DisplayName("Edit an email of an unauthorized user")
    public void editEmailUnauthorizedUserGetError() {

        Response responseCreating = UserOperations.createUser(user);
        //accessToken нужен для редактирования и последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserOperations.editUnauthorizedUser(accessToken, userEditedData).then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED).and().body("success", equalTo(false)).and().body("message", equalTo("You should be authorised"));
    }
}
