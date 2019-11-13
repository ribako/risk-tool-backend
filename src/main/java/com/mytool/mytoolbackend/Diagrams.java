package com.mytool.mytoolbackend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "models")
public class Diagrams {

    @Id
    private String id;
    private String name;
    private String data;


    public Diagrams(String id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;

    }

    public String getData() {
        return data;
    }

}