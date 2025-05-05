package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.SessionManager;
import org.example.controller.RouteManager;

public class ClearCommand extends Commands {

    public ClearCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        try {
            routeManager.clearRoutes();
            System.out.println("Все маршруты пользователя " + login + " удалены.");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении маршрутов: " + e.getMessage());
        }
    }
}
