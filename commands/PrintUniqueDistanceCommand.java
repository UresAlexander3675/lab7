package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.SessionManager;
import org.example.controller.RouteManager;

public class PrintUniqueDistanceCommand extends Commands {

    public PrintUniqueDistanceCommand(RouteManager routeManager, DataBaseManager dataBaseManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        try {
            routeManager.printUniqueDistances();
        } catch (Exception e) {
            System.err.println("Ошибка при извлечении уникальных значений дистанции: " + e.getMessage());
        }
    }
}
