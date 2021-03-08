package edu.uta.cse6331.assignment01.repository;

import edu.uta.cse6331.assignment01.model.Data;
import edu.uta.cse6331.assignment01.model.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;
import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "datas", path = "data")
@CrossOrigin
public interface DataRepository /*extends JpaRepository<Data, String>*/ {


    public List<Data> findAllByDistanceBetween(BigInteger start, BigInteger end);

    public List<Data> findAllByAuthorEqualsAndNsizeBetween(String author, BigInteger start, BigInteger end);

}
