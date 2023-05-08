import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderOperations;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserOperations;
import utils.BaseURI;
import utils.Generator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    User user;
    Order order;
    String accessToken;
    int ingredientSublistSize;
    List<String> ingredients;
    List<String> allIngredients;


    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
        allIngredients = OrderOperations.getAllIngredients();

        user = Generator.generateUser();
        Response response = UserOperations.createUser(user);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
    }

    @After
    public void tearDown() {
        UserOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create a correct order with ingredients by an authorized user")
    public void createCorrectOrderWithIngredientsAuthorizedUserGetSuccess() {
        ingredientSublistSize = Generator.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        order = new Order(ingredients);
        OrderOperations.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Create an order without ingredients by an authorized user")
    public void createOrderWithoutIngredientsAuthorizedUserGetError() {
        order = new Order(ingredients);
        OrderOperations.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Create an order with an invalid ingredient by an authorized user")
    public void createOrderInvalidIngredientAuthorizedUserGetSuccess() {
        ingredients = new ArrayList<>();
        ingredients.add(RandomStringUtils.randomAlphabetic(24));
        order = new Order(ingredients);
        OrderOperations.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    //в документации нет информации по ошибке при создании заказа без авторизации, но как будто бы это совсем не ок
    @Test
    @DisplayName("Create an order with ingredients by an unauthorized user")
    public void createOrderWithIngredientsUnauthorizedUserGetError() {
        ingredientSublistSize = Generator.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        order = new Order(ingredients);
        OrderOperations.createOrderWithoutAuth(order)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
