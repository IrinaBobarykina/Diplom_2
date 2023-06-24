package utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import user.User;
import user.UserEditedData;

public class Generator {

    static Faker faker = new Faker();

    public static User generateUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6, 10);
        String name = faker.name().fullName();
        return new User(email, password, name);
    }

    public static UserEditedData generateUserEditedData() {
        String newEmail = faker.internet().emailAddress();
        String newPassword = faker.internet().password(6, 10);
        String newName = faker.name().fullName();
        return new UserEditedData(newEmail, newPassword, newName);
    }

    public static int generateSizeForIngredientSublist(int max) {
        int sublistIndex = RandomUtils.nextInt(1, max);
        return sublistIndex;
    }
}
