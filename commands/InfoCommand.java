package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;

import java.sql.*;

public class InfoCommand extends Commands{
    public InfoCommand(DataBaseManager dataBaseManager, RouteManager routeManager){
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        try {
            routeManager.showInfo();
        } catch (Exception e) {
            System.err.println("Ошибка при получении информации:" + e.getMessage());
        }
    }
}
