package edu.uta.cse6331.assignment01.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class EarthQuakeBody {

    Page<EarthQuake> earthQuakes;
    List<QueryStatistic> queryStatistics;

}

