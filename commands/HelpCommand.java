package org.example.commands;

import org.example.controller.DataBaseManager;
import org.example.controller.RouteManager;

public class HelpCommand extends Commands {

    public HelpCommand(DataBaseManager dataBaseManager, RouteManager routeManager){
        super(dataBaseManager, routeManager);
    }

    @Override
    public void execute(String[] args, String login) {
        System.out.println("Доступные команды:");
        System.out.println("help - вывести справку по доступным командам");
        System.out.println("info - вывести информацию о коллекции маршрутов");
        System.out.println("show - показать все маршруты");
        System.out.println("insert {element} - добавить маршрут в коллекцию с заданным ключом");
        System.out.println("update {id} {element} - обновить значение маршрута, id которого равен заданному");
        System.out.println("remove_key {id} - удалить маршрут по его ключу");
        System.out.println("clear - очистить коллекцию маршрутов");
        System.out.println("save - сохранить коллекцию маршрутов в файл");
        System.out.println("execute_script {file_name} - выполнить команды из файла");
        System.out.println("exit - выйти из программы");
        System.out.println("remove_greater {element} - удалить маршруты больше заданного");
        System.out.println("replace_if_lowe {key} {element} - заменить значение по маршруту, если новое значение меньше старого");
        System.out.println("remove_greater_key {key} - удалить маршруты с ключом больше заданного");
        System.out.println("remove_any_by_distance {distance} - удалить маршрут с указанным расстоянием");
        System.out.println("filter_starts_with_name {name} - фильтр по имени");
        System.out.println("print_unique_distance - вывести уникальные расстояния");
    }
}
