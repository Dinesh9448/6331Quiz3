package edu.uta.cse6331.assignment01.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

//@Entity
//@Table(name="PEOPLE")
@Data
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "PEOPLE_NAME")
    private String name;

    @Column(name = "STATE")
    private String state;

    @Column(name = "SALARY")
    private BigInteger salary;

    @Column(name = "GRADE")
    private BigInteger grade;

    @Column(name = "ROOM")
    private BigInteger room;

    @Column(name = "TELNUM")
    private BigInteger telnum;

    @Column(name = "PICTURE")
    private String picture;

    @Column(name = "PICTURE_LINK")
    private String pictureLink;

    @Column(name = "KEYWORD")
    private String keyword;
}
