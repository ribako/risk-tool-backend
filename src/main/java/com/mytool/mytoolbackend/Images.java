package com.mytool.mytoolbackend;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document(collection = "images")
public class Images {
    @Id
    private String id;
    private String name;
    private byte[] img;
    private String data;

    public Images(String id, String name, String data, byte[] img) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.img = img;
    }

    public String getData() {
        return data;
    }

    public byte[] getImg() {
        return img;
    }
}
