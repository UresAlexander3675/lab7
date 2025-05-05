package org.example.commands;

import org.example.controller.*;
import org.example.controller.DataBaseManager;
import java.sql.SQLException;

public class RemoveGreaterKeyCommand extends Commands{

    public RemoveGreaterKeyCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        if (args.length < 1) {
            System.out.println("Error: укажите ключ маршрута для удаления.");
            return;
        }
        try {
            Long key = Long.parseLong(args[0]);
            int removedCount = dataBaseManager.removeGreaterKey(key);
            routeManager.removeGreaterKey(key);
            if (removedCount > 0) {
                System.out.println("Маршруты с ID больше " + key + " успешно удалены.");
            } else {
                System.out.println("Нет маршрутов с ID больше " + key);
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: ключ должен быть числом.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
