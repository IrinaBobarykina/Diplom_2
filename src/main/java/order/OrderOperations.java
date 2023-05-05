package order;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import user.User;
import user.UserOperations;
import utils.APIs;

import static io.restassured.RestAssured.given;

public class OrderOperations {

    @Step
    public static Response getOrdersAuthorizedUser(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .when()
                .get(APIs.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response getOrdersUnauthorizedUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(APIs.ORDERS_PATH);
        return response;
    }
}
