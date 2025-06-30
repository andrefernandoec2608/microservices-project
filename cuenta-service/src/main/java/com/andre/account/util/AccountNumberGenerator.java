package com.andre.account.util;

import java.util.Random;

public class AccountNumberGenerator {

    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {
        int number = 10000000 + RANDOM.nextInt(90000000);
        return String.valueOf(number);
    }
}
