package com.mytool.mytoolbackend;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiagramRepository extends MongoRepository<Diagrams, String> {

}
