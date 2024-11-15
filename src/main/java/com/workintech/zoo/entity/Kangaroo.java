package com.workintech.zoo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Kangaroo {
    private long id;
    private String name;
    private double weight;
    private double height;
    private Gender gender;


    private boolean isAggressive;

}
