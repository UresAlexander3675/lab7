package org.example.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.sql.*;

public class Route implements Comparable<Route>{
    private Long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate = LocalDateTime.now();
    private LocationFrom from;
    private LocationTo to;
    private double distance;
    private String ownerLogin;

    public Route(Long id, String name, Coordinates coordinates, LocationFrom from, LocationTo to, double distance){
        this.id = id;
        this.coordinates = coordinates;
        this.name = name;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route(){
    }

    public Long getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public LocationTo getTo() {
        return to;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return creationDate.format(formatter);
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTo(LocationTo to){
        this.to = to;
    }

    public void setFrom(LocationFrom from){
        this.from = from;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime localDateTime) {this.creationDate = localDateTime;}

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @Override
    public int compareTo(Route other){
        return Double.compare(this.getDistance(), other.getDistance());
    }

    public static Route inputNewRoute(Long idLast) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Введите название маршрута: ");
            String name = scanner.nextLine().trim();
            while (name.isEmpty() || name.equals(" ")) {
                System.out.println(("Error: Название не может быть пустым/пробелом. Введите снова: "));
                name = scanner.nextLine();
            }
            int x;

            System.out.print("Введите координату X (целое число > -372): ");
            while (true) {
                try {
                    x = scanner.nextInt();
                    if (x <= -372) {
                        System.err.print("Число должно быть больше -372. Попробуйте снова.");
                        continue;
                    }
                    System.out.println("Вы ввели корректное число: " + x);
                    break;
                } catch (InputMismatchException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное число.");
                    scanner.nextLine();
                }
            }
            float y;
            System.out.print("Введите координату Y (целое число > -527): ");
            while (true) {
                try {
                    y = scanner.nextFloat();
                    if (y <= -527) {
                        System.err.println("Число должно быть больше -527. Попробуйте снова.");
                        continue;
                    }
                    System.out.println("Вы ввели корректное число: " + y);
                    break;
                } catch (InputMismatchException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное число.");
                    scanner.nextLine();
                }
            }
            Coordinates coordinates2 = new Coordinates(x, y);
            System.out.print("Введите координату from (X): ");
            double fromX;
            while (true) {
                try {
                    fromX = scanner.nextDouble();
                    break;
                } catch (InputMismatchException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное число.");
                    scanner.nextLine();
                }
            }
            System.out.print("Введите координату from (Y): ");
            int fromY;
            while (true) {
                try {
                    fromY = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное число.");
                    scanner.nextLine();
                }
            }
            System.out.print("Введите точку начала маршрута (до 956 символов): ");
            scanner.nextLine();
            String fromName = scanner.nextLine().trim();
            while (fromName.isEmpty() || (fromName.length() > 956)) {
                System.err.print("Ошибка! Введите корректное точку начала маршрута: ");
                fromName = scanner.nextLine();
            }
            LocationFrom locationFrom = new LocationFrom(fromX, fromY, fromName);
            long toXcoordinate;
            System.out.print("Введите координату to (X): ");
            while (true) {
                try {
                    String toX = scanner.nextLine().trim();
                    if (toX.isEmpty()) {
                        System.err.println("Введите снова, координата не может быть пустой");
                        continue;
                    }
                    toXcoordinate = Long.parseLong(toX);
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное значение для координаты X.");
                }
            }
            int toYcoordinate;
            System.out.print("Введите координату to (Y): ");
            while (true) {
                try {
                    String toY = scanner.nextLine().trim();
                    if (toY.isEmpty()) {
                        System.err.println("Введите снова, координата не может быть пустой");
                        continue;
                    }
                    toYcoordinate = Integer.parseInt(toY);
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное значение для координаты Y.");
                }
            }
            double toZcoordinate = 0;
            System.out.print("Введите координату to (Z): ");
            while (true) {
                try {
                    String toZ = scanner.nextLine().trim();
                    if (toZ.isEmpty()) {
                        break;
                    }
                    toZcoordinate = Double.parseDouble(toZ);
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное значение для координаты Z.");
                }
            }
            String toName;
            System.out.print("Введите название to (может быть null, но не пустым): ");
            while (true) {
                toName = scanner.nextLine().trim();
                if (!toName.isEmpty()) {
                    break;
                } else {
                    System.err.println("Название не может быть пустым. Попробуйте снова.");
                }
            }
            LocationTo locationTo = null;
            try {
                locationTo = new LocationTo(toXcoordinate, toYcoordinate, toZcoordinate, toName);
            } catch (NumberFormatException e) {
                System.err.println("Ошибка при создании LocationTo: " + e.getMessage());
            }
            double distance2;
            System.out.print("Введите дистанцию маршрута (> 1): ");
            while (true) {
                try {
                    distance2 = scanner.nextDouble();
                    if (distance2 <= 1) {
                        System.err.println("Ошибка: дистанция должна быть больше 1. Попробуйте снова.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.err.println("Ошибка: введено не число. Пожалуйста, введите корректное значение для дистанции.");
                    scanner.nextLine();
                }
            }
            return new Route(idLast, name, coordinates2, locationFrom, locationTo, distance2);
        } catch (NoSuchElementException e) {
            System.err.println("Неверный ввод. Повторите снова");
            return null;
        }
    }

    public static Route fromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Coordinates coordinates = new Coordinates(rs.getInt("coordinates_x"), rs.getFloat("coordinates_y"));
        LocationFrom from = new LocationFrom(rs.getDouble("from_x"), rs.getInt("from_y"), rs.getString("from_name"));
        LocationTo to = new LocationTo(rs.getLong("to_x"), rs.getInt("to_y"), rs.getDouble("to_z"), rs.getString("to_name"));
        double distance = rs.getDouble("distance");

        LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        String ownerLogin = rs.getString("owner_login");

        Route route = new Route(id, name, coordinates, from, to, distance);
        route.setCreationDate(creationDate);
        route.setOwnerLogin(ownerLogin);
        return route;
    }

    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", name='" + name + '\'' + ", coordinates=" + coordinates + ", creationDate=" + creationDate +
                ", from=" + from + ", to=" + to + ", distance=" + distance + '}';
    }
}