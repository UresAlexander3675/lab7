package org.example.commands;

import org.example.controller.SessionManager;
import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class ExecuteScriptCommand extends Commands {
    private final Map<String, Commands> commands;

    public ExecuteScriptCommand(RouteManager routeManager, DataBaseManager dataBaseManager, Map<String, Commands> commands) {
        super(dataBaseManager, routeManager);
        this.commands = commands;
    }

    @Override
    public void execute(String[] args, String login) {
        if (!SessionManager.isAuthorized(login)) {
            System.out.println("Ошибка: пользователь не авторизован.");
            return;
        }

        if (args.length == 0) {
            System.err.println("Ошибка: укажите имя файла.");
            return;
        }

        String fileName = args[0];
        File scriptFile = new File(fileName);

        if (!scriptFile.exists() || !scriptFile.isFile()) {
            System.err.println("Ошибка: файл " + fileName + " не найден.");
            return;
        }

        try (Scanner scanner = new Scanner(scriptFile)) {
            System.out.println("Выполняем команды из файла " + fileName);

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                String[] parts = input.split(" ", 2);
                String commandName = parts[0];
                String[] commandArgs;
                if(parts.length > 1) {
                    commandArgs = parts[1].split(" ");
                } else {
                    commandArgs = new String[0];
                }

                Commands command = commands.get(commandName);
                if (command != null) {
                    command.execute(commandArgs, login);
                } else {
                    System.err.println("Ошибка: неизвестная команда '" + commandName + "'");
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Ошибка: не удалось открыть файл '" + fileName + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
