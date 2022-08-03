package com.example.pdforganiser;

import java.io.File;
import java.net.URI;

public class FileIndividual {
    String name;
    String path;
    File file;


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    FileIndividual(String name, String path, File file){
        this.path = path;
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
