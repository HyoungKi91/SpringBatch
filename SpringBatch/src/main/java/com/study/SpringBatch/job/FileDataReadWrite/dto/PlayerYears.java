package com.study.SpringBatch.job.FileDataReadWrite.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Year;

@Data
public class PlayerYears implements Serializable {

    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
    private int yearsExperience;

    public PlayerYears(Player items){
        this.ID = items.getID();
        this.lastName = items.getLastName();
        this.firstName = items.getFirstName();
        this.position = items.getPosition();
        this.birthYear = items.getBirthYear();
        this.debutYear = items.getDebutYear();
        this.yearsExperience = Year.now().getValue() - items.getDebutYear();
    }
}
