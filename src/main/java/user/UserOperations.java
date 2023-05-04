package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.APIs;

import static io.restassured.RestAssured.given;

public class UserOperations {

    @Step("Create an user")
    public static Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(APIs.REGISTER_PATH);
        return response;
    }

    @Step("Log in an user")
    public static Response logInUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(APIs.LOGIN_PATH);
        return response;
    }

    @Step("Delete an user")
    public static void deleteUser(String accessToken) {
        if (accessToken != null)
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(APIs.USER_PATH);
    }
}
