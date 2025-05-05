package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.SessionManager;
import org.example.controller.RouteManager;

public class RemoveGreaterCommand extends Commands {

    public RemoveGreaterCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        if (args.length < 1) {
            System.err.println("Ошибка: укажите значение дистанции.");
            return;
        }

        try {
            double distance = Double.parseDouble(args[0]);
            routeManager.removeGreaterDistance(distance);
            System.out.println("Маршруты с дистанцией больше " + distance + " удалены.");
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: дистанция должна быть числом.");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении маршрутов: " + e.getMessage());
        }
    }
}
