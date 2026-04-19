package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao dao) {
        this.userDao = dao;
    }

    public User createUser(String username) {
        return userDao.create(new User(username));
    }

    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

    public User getUser(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void deleteAllUsers() {
        userDao.deleteAll();
    }
}