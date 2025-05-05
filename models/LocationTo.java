package org.example.models;

public class LocationTo {
    private Long x;
    private Integer y;
    private double z;
    private String name;

    public LocationTo(Long x, Integer y, double z, String name){
        this.z = z;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public LocationTo(){
        this.x = 0L;
        this.y = 0;
        this.z = 0.0;
        this.name = "default";
    }



    public void setZ(double z){
        this.z = z;
    }
    public void setX(Long x){
        this.x = x;
    }
    public void setY(Integer y){
        this.y = y;
    }
    public void setName(String name){
        this.name = name;
    }

    public Long getX() {
        return x;
    }
    public Integer getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "LocationTo{" + "x=" + x + ", y=" + y + ", z=" + z + ", name='" + name + '\'' + '}';
    }
}
