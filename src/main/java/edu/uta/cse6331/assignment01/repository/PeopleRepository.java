package edu.uta.cse6331.assignment01.repository;

import edu.uta.cse6331.assignment01.model.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;
import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "peoples", path = "people")
@CrossOrigin
public interface PeopleRepository /*extends JpaRepository<People, BigInteger>*/ {

    public List<People> findBySalaryLessThan(BigInteger salary);
    public List<People> findByName(String name);
}
