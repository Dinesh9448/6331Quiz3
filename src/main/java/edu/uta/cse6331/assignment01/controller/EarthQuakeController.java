package edu.uta.cse6331.assignment01.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import edu.uta.cse6331.assignment01.model.ChartData;
import edu.uta.cse6331.assignment01.model.EarthQuake;
import edu.uta.cse6331.assignment01.model.EarthQuakeBody;
import edu.uta.cse6331.assignment01.model.QueryStatistic;
import edu.uta.cse6331.assignment01.repository.EarthQuakeRepository;
import edu.uta.cse6331.assignment01.util.GeoLocation;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.engine.internal.StatisticalLoggingSessionEventListener;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/*@BasePathAwareController
@RequestMapping("people")*/
@RepositoryRestController
@RequestMapping("earthquake")
@CrossOrigin
public class EarthQuakeController {

    private static final String COMMA_DELIMITER = ",";
    @Autowired @Setter private EarthQuakeRepository earthQuakeRepository;
    @Autowired @Setter private ExecutorService executorService;
    @Autowired @Setter private EntityManager entityManager;

    @PostConstruct
    public void init() throws FileNotFoundException {

        //persistEarthQuake(new FileReader("C:\\Users\\dines\\MS\\CSE-6331\\assignment2\\all_month.csv"));
    }

    @GetMapping("/countByMag")
    public @ResponseBody ResponseEntity<?> countByMag(){
        List<ChartData> counts = new ArrayList<>();
        counts.add(new ChartData("Less than 1", earthQuakeRepository.countByMagBetween(BigDecimal.valueOf(Integer.MIN_VALUE), BigDecimal.ONE)));
        counts.add(new ChartData("1 to 2", earthQuakeRepository.countByMagBetween(BigDecimal.ONE, BigDecimal.valueOf(2))));
        counts.add(new ChartData("2 to 3", earthQuakeRepository.countByMagBetween(BigDecimal.valueOf(2), BigDecimal.valueOf(3))));
        counts.add(new ChartData("3 to 4", earthQuakeRepository.countByMagBetween(BigDecimal.valueOf(3), BigDecimal.valueOf(4))));
        counts.add(new ChartData("4 to 5", earthQuakeRepository.countByMagBetween(BigDecimal.valueOf(4), BigDecimal.valueOf(5))));
        counts.add(new ChartData("Greater than 5", earthQuakeRepository.countByMagBetween(BigDecimal.valueOf(5), BigDecimal.valueOf(Integer.MAX_VALUE))));

        return ResponseEntity.ok(counts);
    }
    @GetMapping("/countByLocationSource")
    public @ResponseBody ResponseEntity<?> countByLocationSource() {
        return ResponseEntity.ok(earthQuakeRepository.countByLocationSourceOrderByLocationSource().stream().map(ary -> new ChartData(String.valueOf(ary[0]), ((Long)ary[1]).intValue())).collect(Collectors.toList()));
    }

    @GetMapping("/countByTime")
    public @ResponseBody ResponseEntity<?> countByTime() {
        return ResponseEntity.ok(earthQuakeRepository.countByTime().stream().map(ary -> new ChartData(String.valueOf(ary[0]), (Integer)ary[1])).collect(Collectors.toList()));
    }

    @GetMapping
    @Transactional
    public @ResponseBody ResponseEntity<?> findAll(@RequestParam("page") int page,@RequestParam("sort") String sort){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        Page<EarthQuake> earthQuakePage = earthQuakeRepository.findAll(pageable);
        return getResponseEntity(earthQuakePage);
    }

    private void clearStatistics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        statistics.clear();
    }

    @GetMapping("/findByMagGreaterThanEqual")
    @Transactional
    public @ResponseBody ResponseEntity<?> findByMagGreaterThanEqual(@RequestParam("page") int page, @RequestParam("sort") String sort, @RequestParam("mag") BigDecimal mag){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        Page<EarthQuake> earthQuakePage = earthQuakeRepository.findByMagGreaterThanEqual(pageable, mag);
        return getResponseEntity(earthQuakePage);
    }

    @GetMapping("/findByMagBetweenAndAndTimeBetween")
    @Transactional
    public @ResponseBody ResponseEntity<?> findByMagBetweenAndAndTimeBetween(@RequestParam("page") int page, @RequestParam("sort") String sort,
                                                                             @RequestParam("startMag") BigDecimal startMag, @RequestParam("endMag") BigDecimal endMag,
                                                                             @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime startDate,
                                                                             @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime endDate){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        Page<EarthQuake> earthQuakePage = earthQuakeRepository.findByMagBetweenAndAndTimeBetween(pageable, startMag, endMag, startDate, endDate);
        return getResponseEntity(earthQuakePage);
    }

    public ResponseEntity<?> getResponseEntity(Page<EarthQuake> earthQuakePage) {
        EarthQuakeBody earthQuakeBody = new EarthQuakeBody();
        earthQuakeBody.setEarthQuakes(earthQuakePage);
        earthQuakeBody.setQueryStatistics(getQueryStatistics());
        return ResponseEntity.ok(earthQuakeBody);
    }

    public Pageable getPageable(@RequestParam("page") int page, @RequestParam("sort") String sort) {
        return PageRequest.of(page, 20, Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
    }

    @PostMapping("/batch")
    public  @ResponseBody ResponseEntity<?> saveEarthQuakes(@Valid @RequestBody List<EarthQuake> earthQuakes){
        return ResponseEntity.ok(earthQuakeRepository.saveAll(earthQuakes));
    }

    @RequestMapping(path = "/upload",
            consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE},
            method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> uploadFile(HttpServletRequest request) throws IOException {
        return persistEarthQuake(request.getReader());
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try (InputStream initialStream = new ByteArrayInputStream(file.getBytes());
             Reader targetReader = new InputStreamReader(initialStream);){
            return persistEarthQuake(targetReader);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> persistEarthQuake(Reader targetReader) {
        CsvToBean csvToBean = new CsvToBeanBuilder(targetReader).withType(EarthQuake.class).build();
        List<EarthQuake> earthQuakes = new ArrayList<>();
        csvToBean.stream().forEach(earthQuake -> {
            if (50 == earthQuakes.size()) {
                EarthQuakePersistor earthQuakePersistor = new EarthQuakePersistor();
                earthQuakePersistor.getEarthQuakes().addAll(earthQuakes);
                executorService.submit(earthQuakePersistor);
                earthQuakes.clear();
            }
            earthQuakes.add((EarthQuake) earthQuake);
        });
        EarthQuakePersistor earthQuakePersistor = new EarthQuakePersistor();
        earthQuakePersistor.getEarthQuakes().addAll(earthQuakes);
        executorService.submit(earthQuakePersistor);

        return ResponseEntity.ok("Earth Quake Record Insertion started !!!");
    }

    @GetMapping("/findEarthQuakesByDistance")
    @Transactional
    public @ResponseBody ResponseEntity<?> findEartQuakesByDistance(@RequestParam("page") int page,@RequestParam("sort") String sort,
            @RequestParam double latitude, @RequestParam double longitude, @RequestParam double distance){
        clearStatistics();
        Pageable pageable = getPageable(page, sort);
        double earthRadius = 6371.01;
        GeoLocation location = GeoLocation.fromDegrees(latitude, longitude);
        GeoLocation[] boundingCoordinates =
                location.boundingCoordinates(distance, earthRadius);
        boolean meridian180WithinDistance =
                boundingCoordinates[0].getLongitudeInRadians() >
                        boundingCoordinates[1].getLongitudeInRadians();
        //if(meridian180WithinDistance)
        Page<EarthQuake> byRadiusMeridianWithin = earthQuakeRepository.findByRadiusMeridianWithin(pageable,
                BigDecimal.valueOf(boundingCoordinates[0].getLatitudeInDegrees()),
                BigDecimal.valueOf(boundingCoordinates[1].getLatitudeInDegrees()),
                BigDecimal.valueOf(boundingCoordinates[0].getLongitudeInDegrees()),
                BigDecimal.valueOf(boundingCoordinates[1].getLongitudeInDegrees()),
                BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude), BigDecimal.valueOf((distance / earthRadius)));

        return getResponseEntity(byRadiusMeridianWithin);
    }

    private List<QueryStatistic> getQueryStatistics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        String[] queries = statistics.getQueries();
        return Arrays.stream(queries).map(qu -> {
                QueryStatistics queryStatistics = statistics.getQueryStatistics(qu);
                QueryStatistic queryStatistic = new QueryStatistic();
                queryStatistic.setQuery(qu);
                queryStatistic.setExecutionTime(queryStatistics.getExecutionTotalTime());
                queryStatistic.setExecutionCount(queryStatistics.getExecutionCount());
                return queryStatistic;
            }).collect(Collectors.toList());
    }

    class EarthQuakePersistor implements Runnable{
        @Getter @Setter
        List<EarthQuake> earthQuakes = new ArrayList<>();
        @Override
        public void run() {
            System.out.println("ThreadId: " + Thread.currentThread().getName() + " \n Payload : " + earthQuakes);
            saveEarthQuakes(earthQuakes);
        }
    }

}
