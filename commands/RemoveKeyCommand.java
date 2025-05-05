package org.example.commands;

import org.example.controller.SessionManager;
import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;

public class RemoveKeyCommand extends Commands {

    public RemoveKeyCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        if (args.length < 1) {
            System.err.println("Ошибка: укажите ID маршрута для удаления.");
            return;
        }

        try {
            Long id = Long.parseLong(args[0]);
            if (!routeManager.containsID(id)) {
                System.err.println("Ошибка: маршрут с ID " + id + " не найден.");
                return;
            }

            if (!routeManager.getRouteByID(id).getOwnerLogin().equals(login)) {
                System.err.println("Ошибка: вы не можете удалить маршрут, созданный другим пользователем.");
                return;
            }

            boolean removed = routeManager.removeRoute(id);
            if (removed) {
                System.out.println("Маршрут с ID " + id + " успешно удалён.");
            } else {
                System.err.println("Ошибка при удалении маршрута.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
