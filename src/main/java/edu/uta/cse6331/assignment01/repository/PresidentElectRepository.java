package edu.uta.cse6331.assignment01.repository;

import edu.uta.cse6331.assignment01.model.PresidentElect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "president-elect", path = "president-elect")
@CrossOrigin
public interface PresidentElectRepository extends JpaRepository<PresidentElect, BigInteger> {

    public Page<PresidentElect> findAll(Pageable pageable);
    public List<PresidentElect> findByYearEqualsAndStatePoEqualsOrderByCandidateVotesDesc(BigInteger year, String statePo);
    public List<PresidentElect> findByCandidateVotesBetweenAndYearBetween(BigInteger startVotes, BigInteger endVotes, BigInteger startYear, BigInteger endYears);
    public Page<PresidentElect> findByCandidateContaining(Pageable pageable, String name);
    public List<PresidentElect> findByYearBetweenAndStatePoEquals(BigInteger startYear, BigInteger endYear, String statePo);
    public List<PresidentElect> findByYearBetweenAndStatePoEqualsOrderByYear(BigInteger startYear, BigInteger endYear, String statePo);




}
