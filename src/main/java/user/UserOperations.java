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

    @Step("Edit an authorized user")
    public static Response editAuthorizedUser(String accessToken, UserEditedData userEditedData) {
        Response response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .header("Authorization", accessToken)
                    .and()
                    .body(userEditedData)
                    .when()
                    .patch(APIs.USER_PATH);
        return response;
    }

    @Step("Edit an unauthorized user")
    public static Response editUnauthorizedUser(String accessToken, UserEditedData userEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userEditedData)
                .when()
                .patch(APIs.USER_PATH);
        return response;
    }
}
