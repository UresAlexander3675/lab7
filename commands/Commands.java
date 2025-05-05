package org.example.commands;

import org.example.controller.*;
import java.sql.SQLException;

public abstract class Commands {
    protected final DataBaseManager dataBaseManager;
    protected final RouteManager routeManager;

    public Commands(DataBaseManager dataBaseManager, RouteManager routeManager){
        this.dataBaseManager = dataBaseManager;
        this.routeManager = routeManager;
    }
    public abstract void execute(String[] args, String login) throws SQLException;
}

