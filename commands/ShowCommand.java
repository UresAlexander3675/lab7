package org.example.commands;

import org.example.controller.*;
import org.example.controller.DataBaseManager;

public class ShowCommand extends Commands{

    public ShowCommand(DataBaseManager dataBaseManager, RouteManager routeManager){
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login){
        routeManager.showRoutes();
    }
}
