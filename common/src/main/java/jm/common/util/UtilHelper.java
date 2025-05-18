package jm.common.util;

import java.util.UUID;

public class UtilHelper {
    public static final String USER_NAME = "User";
    public static final String CART_NAME = "Cart";
    public static final String PRODUCT_NAME = "Product";

    public static void checkNullOrEmpty(UUID id, String name) {
        checkNull(id, name);
        checkEmpty(id, name);
    }

    public static void checkNull(UUID id, String name) {
        if (id == null) {
            throw new IllegalArgumentException(name + " ID must not be null");
        }
    }

    public static void checkEmpty(UUID id, String name) {
        if (id.toString().isEmpty()) {
            throw new IllegalArgumentException(name + " ID must not be blank");
        }
    }
}
