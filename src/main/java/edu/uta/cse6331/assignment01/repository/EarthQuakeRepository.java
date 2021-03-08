package edu.uta.cse6331.assignment01.repository;

import edu.uta.cse6331.assignment01.model.ChartData;
import edu.uta.cse6331.assignment01.model.EarthQuake;
import edu.uta.cse6331.assignment01.model.People;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "earthquake", path = "earthquake")
@CrossOrigin
public interface EarthQuakeRepository extends JpaRepository<EarthQuake, String> {

    @Cacheable("earthQuake_findAll")
    public Page<EarthQuake> findAll(Pageable pageable);
    @Cacheable("earthQuake_findByMagGreaterThanEqual")
    public Page<EarthQuake> findByMagGreaterThanEqual(Pageable pageable, BigDecimal mag);
    @Cacheable("earthQuake_findByMagBetweenAndAndTimeBetween")
    public Page<EarthQuake> findByMagBetweenAndAndTimeBetween(Pageable pageable, BigDecimal startMag, BigDecimal endMag, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime startDate, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime endDate);
    @Cacheable("earthQuake_findByRadiusMeridianWithin")
    @Query(nativeQuery = true)
    public Page<EarthQuake> findByRadiusMeridianWithin(Pageable pageable, BigDecimal minLat, BigDecimal maxLat, BigDecimal minLon, BigDecimal maxLon, BigDecimal latitude, BigDecimal longitude, BigDecimal distance);

    public int countByMagBetween(BigDecimal startMag, BigDecimal endMag);
    @Query("select p.locationSource as name, count(p.id) as value "
            + "from EarthQuake p "
            + "group by p.locationSource")
    public List<Object[]> countByLocationSourceOrderByLocationSource();

    @Query(value = "SELECT cast(time AS DATE) as name, count(id) as value\n" +
            "  FROM EARTH_QUAKE group by cast(time AS DATE)", nativeQuery = true)
    public List<Object[]> countByTime();
}
