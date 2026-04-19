package com.example.demo;

import com.example.demo.config.AppConfig;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan
public class Application {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx =
                     new AnnotationConfigApplicationContext("com.example.demo")) {

            UserService userService = ctx.getBean(UserService.class);

            // --- CLEAR USERS  ---
            userService.deleteAllUsers();

            // ---- CREATE ----
            User alice = userService.createUser("alice");
            User bob   = userService.createUser("bob");
            System.out.println("Created: " + alice + ", " + bob);

            // ---- READ ONE ----
            User fetchedAlice = userService.getUser(alice.getId());
            System.out.println("Fetched: " + fetchedAlice);

            // ---- READ ALL ----
            List<User> all = userService.getAllUsers();
            System.out.println("All users: " + all);

            // ---- DELETE ----
            userService.deleteUser(bob.getId());
            System.out.println("After delete, all: " + userService.getAllUsers());
        }
    }
}