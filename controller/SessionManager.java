package org.example.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static final Set<String> activeUsers = new HashSet<>();
    private static DataBaseManager dataBaseManager;

    public static void initialize(DataBaseManager dbManager) {
        dataBaseManager = dbManager;
    }

    public static boolean login(String login, String password) {
        try {
            if (dataBaseManager.authenticate(login, password)) {
                activeUsers.add(login);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при попытке авторизации: " + e.getMessage());
        }
        return false;
    }

    public static boolean register(String login, String password) {
        try {
            return dataBaseManager.registerUser(login, password);
        } catch (SQLException e) {
            System.err.println("Ошибка при регистрации: " + e.getMessage());
            return false;
        }
    }

    public static boolean isAuthorized(String login) {
        return activeUsers.contains(login);
    }

    public static void logout(String login) {
        activeUsers.remove(login);
    }

    public static void clearAllSessions() {
        activeUsers.clear();
    }
}
