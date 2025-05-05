package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.SessionManager;
import org.example.controller.RouteManager;
import org.example.models.Route;

import java.util.Scanner;

public class ReplaceIfLowerCommand extends Commands {

    public ReplaceIfLowerCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.err.println("Ошибка: пользователь не авторизован.");
            return;
        }

        if (args.length < 1) {
            System.out.println("Ошибка: укажите ID маршрута.");
            return;
        }

        try {
            Long id = Long.parseLong(args[0]);

            if (!routeManager.containsID(id)) {
                System.err.println("Ошибка: маршрут с ID " + id + " не найден.");
                return;
            }

            Route oldRoute = routeManager.getRouteByID(id);
            if (!oldRoute.getOwnerLogin().equals(login)) {
                System.err.println("Ошибка: вы не являетесь владельцем маршрута.");
                return;
            }

            Route newRoute = inputNewRoute(id);
            if (newRoute == null) {
                System.err.println("Ошибка: новый маршрут не был создан.");
                return;
            }

            if (newRoute.getDistance() < oldRoute.getDistance()) {
                newRoute.setOwnerLogin(login);
                routeManager.updateRoute(id, oldRoute, newRoute);
                System.out.println("Маршрут успешно заменён.");
            } else {
                System.err.println("Новый маршрут имеет большее или равное расстояние. Замена не выполнена.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Ошибка: ID должен быть числом.");
        } catch (Exception e) {
            System.err.println("Ошибка при замене маршрута: " + e.getMessage());
        }
    }

    private Route inputNewRoute(Long id) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Введите новое имя маршрута: ");
            String name = scanner.nextLine().trim();

            System.out.print("Введите координату X: ");
            int x = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Введите координату Y: ");
            float y = Float.parseFloat(scanner.nextLine().trim());

            System.out.print("Введите дистанцию маршрута (> 1): ");
            double distance = Double.parseDouble(scanner.nextLine().trim());

            if (distance <= 1) {
                System.err.println("Ошибка: дистанция должна быть больше 1.");
                return null;
            }

            Route route = new Route();
            route.setId(id);
            route.setName(name);
            route.getCoordinates().setX(x);
            route.getCoordinates().setY(y);
            route.setDistance(distance);

            return route;

        } catch (Exception e) {
            System.err.println("Ошибка при вводе данных: " + e.getMessage());
            return null;
        }
    }
}
