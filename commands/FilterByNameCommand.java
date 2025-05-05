package org.example.commands;

import org.example.controller.SessionManager;
import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;

public class FilterByNameCommand extends Commands {

    public FilterByNameCommand(RouteManager routeManager, DataBaseManager dataBaseManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        if (args.length < 1) {
            System.err.println("Ошибка: укажите имя для фильтрации маршрутов.");
            return;
        }

        String name = args[0];
        if (name.isEmpty()) {
            System.err.println("Ошибка: имя не может быть пустым.");
            return;
        }

        routeManager.filterByName(name);
    }
}
