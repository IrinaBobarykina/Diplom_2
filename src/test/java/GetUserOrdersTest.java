import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.OrderOperations;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserOperations;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetUserOrdersTest {

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
    @DisplayName("Get orders of an authorized user")
    public void getOrdersAuthorizedUserGetSuccessResponse() {
        user = new User(email, password, name);
        Response response = UserOperations.createUser(user);
        //accessToken нужен для создания заказа и последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        OrderOperations.getOrdersAuthorizedUser(accessToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Get orders of an unauthorized user")
    public void getOrdersUnauthorizedUserGetError() {
        user = new User(email, password, name);
        Response response = UserOperations.createUser(user);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        OrderOperations.getOrdersUnauthorizedUser()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
