package com.mytool.mytoolbackend;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import si.JDEXi.Model;
import si.JDEXi.Attribute;
import si.JDEXi.Value;

import java.io.IOException;
import java.util.Arrays;


@Controller
@CrossOrigin
public class DexiFileController {

    @Autowired
    DexiRepository repository;

    @CrossOrigin
    @RequestMapping(path = "/files", method = RequestMethod.POST)
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            System.out.printf("File name=%s, size=%s\n", file.getOriginalFilename(), file.getSize());
            //creating a new file in some local directory
            //File fileToSave = new File("C:\\test\\" + file.getOriginalFilename());
            //copy file content from received file to new local file
            //file.transferTo(fileToSave);

            String content = new String(file.getBytes());
            System.out.println(content);
            repository.save(new Dexi(file.getOriginalFilename(), file.getOriginalFilename(), content));


        } catch (Exception e) {
            //if something went bad, we need to inform client about it
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


        //everything was OK, return HTTP OK status (200) to the client
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping(path = "/files/{id}")
    public @ResponseBody ResponseEntity<String> getById(@PathVariable String id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        System.out.println(repository.findById(id));

        String data = repository.findById(id).get().getData();

        System.out.println(data);

        Model m = new Model(data);

        /*System.out.println(Arrays.toString(m.listAttributes(m.getAllAttributes())));

        System.out.println(m.attributesToString(m.getAllAttributes()));

        System.out.println(Arrays.toString(m.inputs()));

        System.out.println(m.basic);*/

        String output = "";


        for (Attribute att : m.basic) {
            //att.print(System.out);
            output = output.concat(att.getName() + "=");

            for (int i = 0; i < att.getScaleSize(); i++) {
                output = output.concat(att.getScaleValue(i) + ",");
            }
            output = output.substring(0,output.length()-1);
            output = output.concat(";");
        }

        System.out.println("Output: " + output);


        // "\nAttributes with Inputs: " + Arrays.toString(m.inputs() "Attributes: " + Arrays.toString(m.listAttributes(m.getAllAttributes())) + "\nAttributes with assigned inputs: " + m.getInputValues()

        return new ResponseEntity<String>(output, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/calculate/{id}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> calculate(@PathVariable String id, @RequestBody String attributes) {

        System.out.println(attributes);

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String data = repository.findById(id).get().getData();

        Model m = new Model(data);

        String trim = attributes.replaceAll("([{\"}]*)","");
        trim = trim.replaceAll(",", ";");
        trim = trim.replaceAll(":", "=");
        System.out.println("Trim is: " + trim);

        m.setInputValuesByNames(trim);

        System.out.println(m.getInputValues());
        System.out.println("Explicitness is: " + m.getExplicitness());


        if (m.getExplicitness()) {
            m.evaluate();
        } else {
            m.evaluate(Model.Evaluation.FUZZY, false);
        }


        System.out.println(m.getOutputValues());
        String risk;
        try {
            risk = m.getOutputValue("Risk").getName();
            return new ResponseEntity<String>(risk, HttpStatus.OK);
        } catch (Exception e) {
            risk = "Error in calculation: Did not find aggregate attribute with name \"Risk\"";
            return new ResponseEntity<>(risk, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
