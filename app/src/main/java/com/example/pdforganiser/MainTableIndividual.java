package com.example.pdforganiser;

public class MainTableIndividual {

    String name;
    String path;
    //int id;

    //public int getId() {
        //return id;
    //}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //public void setId(int id) {
        //this.id = id;
    //}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    MainTableIndividual(String path, String name) {
        this.name = name;
        this.path = path;
        //this.id = id;
    }
}
