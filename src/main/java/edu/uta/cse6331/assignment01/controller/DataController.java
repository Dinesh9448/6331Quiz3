package edu.uta.cse6331.assignment01.controller;

import edu.uta.cse6331.assignment01.model.People;
import edu.uta.cse6331.assignment01.repository.DataRepository;
import edu.uta.cse6331.assignment01.repository.PeopleRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/*@BasePathAwareController
@RequestMapping("people")*/
//@RepositoryRestController
//@RequestMapping("data")
@CrossOrigin
public class DataController {

    @Autowired @Setter private DataRepository dataRepository;

    @GetMapping("/betweenDistance")
    public  @ResponseBody ResponseEntity<?> savePeoples(@RequestParam String start, @RequestParam String end){
        return ResponseEntity.ok(dataRepository.findAllByDistanceBetween(new BigInteger(start), new BigInteger(end)));
    }
}
