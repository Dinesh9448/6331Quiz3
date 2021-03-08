package edu.uta.cse6331.assignment01.controller;

import edu.uta.cse6331.assignment01.model.People;
import edu.uta.cse6331.assignment01.repository.PeopleRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*@BasePathAwareController
@RequestMapping("people")*/
//@RepositoryRestController
//@RequestMapping("people")
@CrossOrigin
public class PeopleController {

    @Autowired @Setter private PeopleRepository peopleRepository;

    @PostMapping("/batch")
    public  @ResponseBody ResponseEntity<?> savePeoples(@RequestBody List<People> peopleList){
        return ResponseEntity.ok(""/*peopleRepository.saveAll(peopleList)*/);
    }
}
