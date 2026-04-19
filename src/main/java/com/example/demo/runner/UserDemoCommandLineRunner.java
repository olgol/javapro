package com.example.demo.runner;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDemoCommandLineRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UserDemoCommandLineRunner.class);

    private final UserService userService;

    public UserDemoCommandLineRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        log.info("--- После миграций и seed: все пользователи ---");
        printAll(userService.getAllUsers());

        log.info("--- Поиск по username (@Query) seed_alice ---");
        userService.findByUsername("seed_alice")
                .ifPresentOrElse(u -> log.info("Найден: {}", u), () -> log.warn("Не найден"));

        log.info("--- Создание alice / bob (предварительно удаляем одноимённых, если остались от прошлых запусков) ---");
        userService.deleteByUsername("alice");
        userService.deleteByUsername("bob");
        User alice = userService.createUser("alice");
        User bob = userService.createUser("bob");
        log.info("Созданы: {}, {}", alice, bob);

        log.info("--- Чтение по id ---");
        log.info("{}", userService.getUser(alice.getId()));

        log.info("--- Список после добавления ---");
        printAll(userService.getAllUsers());

        log.info("--- Удаление по id (bob) ---");
        userService.deleteUser(bob.getId());
        printAll(userService.getAllUsers());

        log.info("--- Удаление по username (@Query) seed_bob ---");
        int removed = userService.deleteByUsername("seed_bob");
        log.info("Удалено строк: {}", removed);
        printAll(userService.getAllUsers());

        log.info("--- Демо завершено (deleteAllUsers доступен в UserService при необходимости) ---");
    }

    private static void printAll(List<User> users) {
        users.forEach(u -> log.info("  {}", u));
    }
}
