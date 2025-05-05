package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.SessionManager;
import org.example.controller.RouteManager;

public class RemoveByDistanceCommand extends Commands {

    public RemoveByDistanceCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        if (args.length < 1) {
            System.err.println("Ошибка: укажите дистанцию маршрута для удаления.");
            return;
        }

        try {
            double distance = Double.parseDouble(args[0]);
            routeManager.removeByDistance(distance);
            System.out.println("Удаление маршрутов с дистанцией " + distance + " завершено.");
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: дистанция должна быть числом.");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении маршрутов: " + e.getMessage());
        }
    }
}
