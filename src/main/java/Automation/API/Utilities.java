package Automation.API;

import java.util.Random;

public class Utilities {

    public static String generateRandomnumber()
    {
        String numbers = "0123456789";
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        int length = 4;
        for(int i = 0;i < length; i++)
        {
            int index = random.nextInt(numbers.length());
            char randomChar = numbers.charAt(index);
            builder.append(randomChar);
        }
        String randomString = builder.toString();
        return randomString;
    }

    public static String generateRandomString(int stringLength)
    {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String generated  = upperCase + lowerCase;
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < stringLength; i++)
        {
            int index = random.nextInt(generated.length());
            char randomChar = generated.charAt(index);
            builder.append(randomChar);
        }
        String randomString = builder.toString();
        return randomString;
    }
    
}
