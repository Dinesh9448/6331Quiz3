package edu.uta.cse6331.assignment01.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "EARTH_QUAKE")
@Data
@NamedNativeQueries({
        @NamedNativeQuery(name = "EarthQuake.findByRadiusMeridianWithin",
                query = "SELECT * FROM EARTH_QUAKE WHERE (latitude >= :minLat AND latitude <= :maxLat) AND (longitude >= :minLon " +
                        "OR" + " longitude <= :maxLon) AND " +
                        "acos(sin(:latitude) * sin(latitude) + cos(:latitude) * cos(latitude) * cos(longitude - :longitude)) <= :distance",
                resultClass = EarthQuake.class),
        @NamedNativeQuery(name = "EarthQuake.findByRadiusMeridianWithout",
                query = "SELECT * FROM EARTH_QUAKE WHERE (latitude >= :minLat AND latitude <= :maxLat) AND (longitude >= :minLon " +
                        "AND" + " longitude <= :maxLon) AND " +
                        "acos(sin(:latitude) * sin(latitude) + cos(:latitude) * cos(latitude) * cos(longitude - :longitude)) <= :distance",
                resultClass = EarthQuake.class)
})
public class EarthQuake {

    @CsvBindByName @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") @NotNull private LocalDateTime time;
    @CsvBindByName @NotNull private BigDecimal latitude;
    @CsvBindByName @NotNull private BigDecimal longitude;
    @CsvBindByName @NotNull private BigDecimal depth;
    @CsvBindByName private BigDecimal mag;
    @CsvBindByName @NotNull private String magType;
    @CsvBindByName private BigDecimal nst;
    @CsvBindByName(column = "gap") private BigDecimal gap;
    @CsvBindByName(column = "dmin") private BigDecimal dmin;
    @CsvBindByName(column = "rms") private BigDecimal rms;
    @CsvBindByName(column = "net") @NotNull private String net;
    @CsvBindByName(column = "id") @NotNull @Id private String id;
    @CsvBindByName(column = "updated") @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") @NotNull private LocalDateTime updated;
    @CsvBindByName(column = "place") @NotNull private String place;
    @CsvBindByName(column = "type") @NotNull private String type;
    @CsvBindByName(column = "horizontalError") private BigDecimal horizontalError;
    @CsvBindByName(column = "depthError") private BigDecimal depthError;
    @CsvBindByName(column = "magError") private BigDecimal magError;
    @CsvBindByName(column = "magNst") private String magNst;
    @CsvBindByName(column = "status") @NotNull private String status;
    @CsvBindByName(column = "locationSource") @NotNull private String locationSource;
    @CsvBindByName(column = "magSource") @NotNull private String magSource;

}
