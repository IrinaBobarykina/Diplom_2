package utils;

public class APIs {

    //    создание юзера POST
    public static final String REGISTER_PATH = "/api/auth/register";

    //    логин юзера POST
    public static final String LOGIN_PATH = "/api/auth/login";

    //    изменение данных юзера PATCH + удаление пользователя DELETE
    public static final String USER_PATH = "/api/auth/user";

    //    создание заказа POST + получение заказов пользователя GET
    public static final String ORDERS_PATH = "/api/orders";

    //    получение всех ингредиентов GET
    public static final String INGREDIENTS_PATH = "/api/ingredients";

}
