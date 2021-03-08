package edu.uta.cse6331.assignment01.model;

import javax.persistence.*;
import java.math.BigInteger;

//@Entity
//@Table(name="DATA")
@lombok.Data
public class Data {
//Name]
//      ,[Nsize]
//      ,[Distance]
//      ,[Author]
//      ,[Picture]
//      ,[Keywords]
    @Id
    @Column(name = "Name")
    private String name;

    @Column(name = "Nsize")
    private BigInteger nsize;

    @Column(name = "Distance")
    private BigInteger distance;

    @Column(name = "Author")
    private String author;

    @Column(name = "Picture")
    private String picture;

    @Column(name = "Keywords")
    private String keywords;
}
