package com.mytool.mytoolbackend;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Images, String> {

}