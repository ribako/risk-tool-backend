package com.mytool.mytoolbackend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(path = "/diagrams")
public class DiagramsController {
    String content;

    @Autowired
    DiagramRepository repository;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            System.out.printf("File name=%s, size=%s\n", file.getOriginalFilename(), file.getSize());
            //creating a new file in some local directory
            //File fileToSave = new File("C:\\test\\" + file.getOriginalFilename());
            //copy file content from received file to new local file
            //file.transferTo(fileToSave);

            content = new String(file.getBytes());
            System.out.println(content);
            repository.save(new Diagrams(file.getOriginalFilename(), file.getOriginalFilename(), content));


        } catch (Exception e) {
            //if something went bad, we need to inform client about it
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


        //everything was OK, return HTTP OK status (200) to the client
        return ResponseEntity.ok().build();
    }

    @CrossOrigin()
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<String> getById(@PathVariable String id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        System.out.println(repository.findById(id));

        String data = repository.findById(id).get().getData();

        System.out.println(data);


        repository.deleteById(id);

        return new ResponseEntity<String>(data, HttpStatus.OK);
    }
}
