package utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import user.User;
import user.UserEditedData;

public class Generator {

    private static final String domain_name = "@gmail.com";

    public static User generateUser() {
        String email = RandomStringUtils.randomAlphabetic(8) + domain_name;
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }

    public static UserEditedData generateUserEditedData() {
        String newEmail = RandomStringUtils.randomAlphabetic(8) + domain_name;
        String newPassword = RandomStringUtils.randomAlphabetic(8);
        String newName = RandomStringUtils.randomAlphabetic(8);
        return new UserEditedData(newEmail, newPassword, newName);
    }

    public static int generateSizeForIngredientSublist(int max) {
        int sublistIndex = RandomUtils.nextInt(1, max);
        return sublistIndex;
    }
}
