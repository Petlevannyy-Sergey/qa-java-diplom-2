package utils;

import com.github.javafaker.Faker;
import user.User;

public class Generators {
    public static User getUser() {
        return  new User(
                getEmail(),
                getPassword(),
                getName());
    }

    public static String getName() {
        return new Faker().name().firstName();
    }

    public  static  String getPassword(){
        return new Faker().internet().password();
    }

    public static String getEmail(){
        return new Faker().internet().emailAddress();
    }

    public static  String getHash(){
        return new Faker().crypto().md5().substring(24);
    }
}

