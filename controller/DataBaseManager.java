package org.example.controller;

import java.sql.*;
import java.security.MessageDigest;
import java.util.*;
import org.example.models.*;

public class DataBaseManager {

    private Connection connection;

    private void initDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("""
            CREATE TABLE IF NOT EXISTS users (login TEXT PRIMARY KEY,
                password_md5 TEXT NOT NULL);
        """);

        statement.execute(""" 
                CREATE TABLE IF NOT EXISTS routes (id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                coordinates_x DOUBLE PRECISION,
                coordinates_y DOUBLE PRECISION,
                creation_date TIMESTAMP,
                from_x DOUBLE PRECISION,
                from_y DOUBLE PRECISION,
                from_name TEXT,
                to_x DOUBLE PRECISION,
                to_y DOUBLE PRECISION,
                to_z DOUBLE PRECISION,
                to_name TEXT,
                distance DOUBLE PRECISION,
                owner_login TEXT REFERENCES users(login));
        """);
    }

    public DataBaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        initDatabase();
    }

    public boolean registerUser(String login, String password) throws SQLException {
        String hashed = md5(password);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login, password_md5) VALUES (?, ?) ON CONFLICT DO NOTHING");
        ps.setString(1, login);
        ps.setString(2, hashed);
        return ps.executeUpdate() > 0;
    }

    public boolean authenticate(String login, String password) throws SQLException {
        String hashed = md5(password);
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE login = ? AND password_md5 = ?");
        ps.setString(1, login);
        ps.setString(2, hashed);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public long insertRoute(Route route, String ownerLogin) throws SQLException {
        String sql = """
        INSERT INTO routes (name, coordinates_x, coordinates_y, creation_date, from_x, from_y, from_name, to_x, to_y, to_z, to_name, distance, owner_login)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING id;
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, route.getName());
            ps.setDouble(2, route.getCoordinates().getX());
            ps.setDouble(3, route.getCoordinates().getY());
            ps.setTimestamp(4, Timestamp.valueOf(route.getCreationDate()));
            ps.setDouble(5, route.getFrom().getX());
            ps.setDouble(6, route.getFrom().getY());
            ps.setString(7, route.getFrom().getName());
            ps.setDouble(8, route.getTo().getX());
            ps.setDouble(9, route.getTo().getY());
            ps.setDouble(10, route.getTo().getZ());
            ps.setString(11, route.getTo().getName());
            ps.setDouble(12, route.getDistance());
            ps.setString(13, ownerLogin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Не получилось добавить маршрут");
                }
            }
        } catch (SQLException e){
            System.err.println("Ошибка при вставке маршрута: " + e.getMessage());
            throw e;
        }
    }

    public LinkedHashMap<Long, Route> loadRoutesCollection() throws SQLException {
        LinkedHashMap<Long, Route> routes = new LinkedHashMap<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM routes");
        while (rs.next()) {
            routes.put(Route.fromResultSet(rs).getId(), Route.fromResultSet(rs));
        }
        return routes;
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int updateRoute(Long id, Route newRoute, String ownerLogin) throws SQLException {
        String sql = "UPDATE routes SET " +
                "name = ?, coordinates_x = ?, coordinates_y = ?, from_x = ?, from_y = ?, from_name = ?, " +
                "to_x = ?, to_y = ?, to_z = ?, to_name = ?, distance = ? " +
                "WHERE id = ? AND owner_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newRoute.getName());
            ps.setDouble(2, newRoute.getCoordinates().getX());
            ps.setDouble(3, newRoute.getCoordinates().getY());
            ps.setDouble(4, newRoute.getFrom().getX());
            ps.setInt(5, newRoute.getFrom().getY());
            ps.setString(6, newRoute.getFrom().getName());
            ps.setLong(7, newRoute.getTo().getX());
            ps.setInt(8, newRoute.getTo().getY());
            ps.setDouble(9, newRoute.getTo().getZ());
            ps.setString(10, newRoute.getTo().getName());
            ps.setDouble(11, newRoute.getDistance());
            ps.setLong(12, id);
            ps.setString(13, ownerLogin);

            return ps.executeUpdate();
        }
    }

    public int clearRoutes(String ownerLogin){
        String sql = "DELETE FROM routes WHERE owner_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ownerLogin);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeRouteById(Long id) throws SQLException {
        String sql = "DELETE FROM routes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public int removeGreaterDistance(double distance) throws SQLException {
        String sql = "DELETE FROM routes WHERE distance > ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, distance);
            return ps.executeUpdate();
        }
    }

    public int removeGreaterKey(Long key) throws SQLException {
        String sql = "DELETE FROM routes WHERE id > ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);
            return ps.executeUpdate();
        }
    }

    public Route getRouteById(Long id) throws SQLException {
        String sql = "SELECT * FROM routes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Route(rs.getLong("id"),
                        rs.getString("name"),
                        new Coordinates(rs.getInt("coordinates_x"), rs.getFloat("coordinates_y")),
                        new LocationFrom(rs.getDouble("from_x"), rs.getInt("from_y"), rs.getString("from_name")),
                        new LocationTo(rs.getLong("to_x"), rs.getInt("to_y"), rs.getDouble("to_z"),
                        rs.getString("to_name")), rs.getDouble("distance"));
            }
            return null;
        }
    }

    public int removeRoutesByDistance(double distance) throws SQLException {
        String sql = "DELETE FROM routes WHERE distance >= ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, distance);
            return ps.executeUpdate();
        }
    }

    public boolean removeByDistance(double distance) throws SQLException {
        String sql = "DELETE FROM routes WHERE distance = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, distance);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении маршрутов по дистанции: " + e.getMessage());
            throw e;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с базой данных закрыто.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения с базой данных: " + e.getMessage());
        }
    }

}