package org.example.controller;

import org.example.models.*;
import java.sql.SQLException;
import java.util.*;

public class RouteManager {
    private LinkedHashMap<Long, Route> linkedHashMap;
    private final Date initializationDate = new Date();
    private DataBaseManager dataBaseManager;
    private String ownerLogin;

    public RouteManager(DataBaseManager dataBaseManager, String ownerLogin) throws SQLException {
        this.dataBaseManager = dataBaseManager;
        this.linkedHashMap = dataBaseManager.loadRoutesCollection();
        this.ownerLogin = ownerLogin;
    }


    public void addRoute(Route route, String ownerLogin) throws SQLException {
        long routeId = dataBaseManager.insertRoute(route, ownerLogin);
        if (routeId > 0) {
            route.setId(routeId);
            linkedHashMap.put(route.getId(), route);
        }
    }

    public boolean removeRoute(Long id) throws SQLException {
        boolean isRemovedFromDB = dataBaseManager.removeRouteById(id);
        if (isRemovedFromDB) {
            linkedHashMap.remove(id);
            return true;
        }
        return false;
    }

    public void updateRoute(Long id, Route oldRoute, Route newRoute) throws SQLException {
        int isUpdated = dataBaseManager.updateRoute(id, newRoute, newRoute.getOwnerLogin());
        if (isUpdated > 0) {
            linkedHashMap.remove(oldRoute.getId());
            linkedHashMap.put(id, newRoute);
        } else {
            System.err.println("Не удалось обновить маршрут в базе данных.");
        }
    }

    public void removeGreaterKey(Long id) throws SQLException {
        int isRemoved = dataBaseManager.removeGreaterKey(id);
        if (isRemoved > 0) {
            linkedHashMap.entrySet().removeIf(entry -> entry.getKey() > id);
        }
    }

    public void filterByName(String name) {
        for(Map.Entry<Long, Route> entry: linkedHashMap.entrySet()){
            if(entry.getValue().getName().contains(name)){
                System.out.println(entry.getValue());
            }
        }
    }

    public void printUniqueDistances() {
        Set<Double> distances = new HashSet<>();
        for(Map.Entry<Long, Route> entry: linkedHashMap.entrySet()){
            distances.add(entry.getValue().getDistance());
        }
        System.out.println("Уникальные дистанции: ");
        distances.forEach(System.out::println);
    }

    public void removeByDistance(double distance) throws SQLException {
        boolean isRemoved = dataBaseManager.removeByDistance(distance);
        if (isRemoved) {
            for(Map.Entry<Long, Route> entry: linkedHashMap.entrySet()){
                if(entry.getValue().getDistance() == distance){
                    linkedHashMap.remove(entry.getKey());
                }
            }
        }
    }

    public void showInfo() {
        System.out.println("Information about this Collection");
        System.out.println("Type: LinkedHashMap<Long, Route>");
        System.out.println("Date of initialization: " + getDateOfInitialization());
        System.out.println("Number of elements: " + linkedHashMap.size());
    }

    public void clearRoutes() {
        int isClean = dataBaseManager.clearRoutes(ownerLogin);
        if(isClean > 0){
            linkedHashMap.clear();
        }
    }

    public Long getLastID() {
        long id = 0;
        for(Map.Entry<Long, Route> entry: linkedHashMap.entrySet()){
            if(entry.getKey() > id){
                id = entry.getKey();
            }
        }
        return id;
    }

    public void removeGreaterDistance(double distance){
        for(Map.Entry<Long, Route> entry: linkedHashMap.entrySet()){
            if(entry.getValue().getDistance() > distance){
                linkedHashMap.remove(entry.getKey(), entry.getValue());
            }
        }
    }

    public void showRoutes(){
        for(Map.Entry<Long, Route> entry: linkedHashMap.entrySet()){
            System.out.println(entry.getValue());
        }
    }

    public LinkedHashMap<Long, Route> getLinkedHashMap() {
        return linkedHashMap;
    }

    public Date getDateOfInitialization() {
        return initializationDate;
    }

    public Route getRouteByID(Long id) {
        return linkedHashMap.get(id);
    }

    public boolean containsID(Long id) {
        return linkedHashMap.containsKey(id);
    }
}
