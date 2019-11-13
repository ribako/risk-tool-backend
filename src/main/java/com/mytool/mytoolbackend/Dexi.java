package com.mytool.mytoolbackend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "dexi")
public class Dexi {
    @Id
    private String id;
    private String name;
    private String data;

    public Dexi(String id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public String getData() {
        return data;
    }

}

