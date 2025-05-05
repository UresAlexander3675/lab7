package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;
import org.example.controller.SessionManager;

import java.util.Scanner;

public class RegisterCommand extends Commands {

    public RegisterCommand(DataBaseManager dataBaseManager, RouteManager routeManager) {
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String ignoredLogin) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите новый логин: ");
        String login = scanner.nextLine().trim();

        System.out.print("Введите новый пароль: ");
        String password = scanner.nextLine().trim();

        if (SessionManager.register(login, password)) {
            System.out.println("Пользователь успешно зарегистрирован.");
        } else {
            System.out.println("Регистрация не удалась. Возможно, пользователь уже существует.");
        }
    }
}
