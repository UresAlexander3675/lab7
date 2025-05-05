package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;

public class ExitCommand extends Commands {
    public ExitCommand(DataBaseManager dataBaseManager, RouteManager routeManager){
        super(dataBaseManager, routeManager);
    }
    @Override
    public void execute(String[] args, String login) {
        System.out.println("Выход из программы...");
        System.exit(0);
    }
}
