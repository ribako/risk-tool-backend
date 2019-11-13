package com.mytool.mytoolbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;


@Controller
@CrossOrigin
public class ImageFileController {
    String content;

    @Autowired
    ImageRepository repository;

    @CrossOrigin
    @RequestMapping(path = "/images", method = RequestMethod.POST)
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        try {
            System.out.printf("File name=%s, size=%s\n", file.getOriginalFilename(), file.getSize());
            //creating a new file in some local directory
            //File fileToSave = new File("C:\\test\\" + file.getOriginalFilename());
            //copy file content from received file to new local file
            //file.transferTo(fileToSave);

            content = new String(file.getBytes());
            byte[] img =file.getBytes();


            repository.save(new Images(name, file.getOriginalFilename(), content, img));


        } catch (Exception e) {
            //if something went bad, we need to inform client about it
            System.out.println(e);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


        //everything was OK, return HTTP OK status (200) to the client
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping(path = "/images/{id}")
    public @ResponseBody
    ResponseEntity<byte[]> getById(@PathVariable String id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //System.out.println(repository.findById(id));

        byte[] img = repository.findById(id).get().getImg();


        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(img);
        //return new ResponseEntity<>(img, HttpStatus.OK);

    }
}
