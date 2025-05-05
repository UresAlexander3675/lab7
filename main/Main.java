package org.example.main;

import org.example.controller.*;
import org.example.commands.*;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DataBaseManager dataBaseManager = new DataBaseManager();
        SessionManager.initialize(dataBaseManager);
        String login = null;

        RouteManager routeManager = new RouteManager(dataBaseManager, login);

        Map<String, Commands> commands = new HashMap<>();
        commands.put("help", new HelpCommand(dataBaseManager, routeManager));
        commands.put("show", new ShowCommand(dataBaseManager, routeManager));
        commands.put("insert", new InsertCommand(dataBaseManager, routeManager));
        commands.put("info", new InfoCommand(dataBaseManager, routeManager));
        commands.put("update", new UpdateCommand(dataBaseManager, routeManager));
        commands.put("remove_key", new RemoveKeyCommand(dataBaseManager, routeManager));
        commands.put("clear", new ClearCommand(dataBaseManager, routeManager));
        commands.put("execute_script", new ExecuteScriptCommand(routeManager, dataBaseManager, commands));
        commands.put("exit", new ExitCommand(dataBaseManager, routeManager));
        commands.put("remove_greater", new RemoveGreaterCommand(dataBaseManager, routeManager));
        commands.put("replace_if_lower", new ReplaceIfLowerCommand(dataBaseManager, routeManager));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand(dataBaseManager, routeManager));
        commands.put("remove_any_by_distance", new RemoveByDistanceCommand(dataBaseManager, routeManager));
        commands.put("filter_starts_with_name", new FilterByNameCommand(routeManager, dataBaseManager));
        commands.put("print_unique_distance", new PrintUniqueDistanceCommand(routeManager, dataBaseManager));

        System.out.println("Введите команду (help для списка всех команд):");
        while (true) {

            System.out.println("<<1 — Войти\n<<2 — Зарегистрироваться");
            String option = scanner.nextLine().trim();

            System.out.print(">>Введите логин: ");
            login = scanner.nextLine().trim();

            System.out.print(">>Введите пароль: ");
            String password = scanner.nextLine().trim();

            if ("1".equals(option)) {
                if (SessionManager.login(login, password)) {
                    System.out.println("Вход выполнен.");
                } else {
                    System.err.println("Неверный логин или пароль.");
                }
            } else if ("2".equals(option)) {
                if (SessionManager.register(login, password)) {
                    System.out.println("Регистрация прошла успешно. Выполнен вход.");
                    SessionManager.login(login, password);
                } else {
                    System.err.println("Ошибка регистрации (возможно, пользователь уже существует).");
                }
            } else {
                System.err.println("Неизвестная опция.");
            }

            System.out.print(">>> ");
            if (!scanner.hasNextLine()) break;

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String commandName = parts[0];
            String[] commandArgs;
            if(parts.length > 1){
                commandArgs = parts[1].split(" ");
            } else {
                commandArgs = new String[]{};
            }

            Commands command = commands.get(commandName);
            if (command != null) {
                command.execute(commandArgs, login);
            } else {
                System.err.println("Неизвестная команда. Введите 'help' для списка команд.");
            }
        }

        dataBaseManager.closeConnection();
        scanner.close();
    }
}
