package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Gender;
import com.workintech.zoo.entity.Kangaroo;
import com.workintech.zoo.exceptions.ZooException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kangaroos")
public class KangarooController {
    Map<Long, Kangaroo> kangaroos;

    @PostConstruct
    public void init(){
        System.out.println("Kangaroo controller initialized");
        kangaroos = new HashMap<>();
        //burada map içine birkaç Kangaroo atılabilir deneme için:
        kangaroos.put(1L,new Kangaroo(1,"crazyKangaroo",41.2, 43 ,Gender.FEMALE,true ));
        kangaroos.put(2L,new Kangaroo(2,"normalKangaroo",35.7, 324,Gender.MALE,false ));
        //System.out.println(kangaroos);
    }

    @GetMapping("")
    public List<Kangaroo> getAllKangaroos(){
        return kangaroos.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Kangaroo findKangarooById(@PathVariable long id){
        if(id<=0){
            throw new ZooException("Id must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(!kangaroos.containsKey(id)){
            throw new ZooException("Id not found", HttpStatus.NOT_FOUND);
        }
        return kangaroos.get(id);
    }

    @PostMapping()
    public List<Kangaroo> createKangaroo(@RequestBody Kangaroo kangaroo){
        if(kangaroo.getId() <= 0 || kangaroo.getName() == null
                || kangaroo.getWeight() <= 0 || kangaroo.getHeight() <= 0
                || kangaroo.getGender() == null){
            throw new ZooException("Invalid kangaroo data", HttpStatus.BAD_REQUEST);
        }
        Kangaroo kangarooToBeCreated = new Kangaroo(kangaroo.getId(), kangaroo.getName(), kangaroo.getWeight(), kangaroo.getHeight(),kangaroo.getGender(),kangaroo.isAggressive());
        kangaroos.put(kangarooToBeCreated.getId(),kangarooToBeCreated);
        return kangaroos.values().stream().toList();
    }

    @PutMapping("/{id}")
    public Kangaroo updateKangaroo(@PathVariable long id, @RequestBody Kangaroo kangaroo){
        if(id<=0){
            throw new ZooException("Id must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(!kangaroos.containsKey(id)){
            throw new ZooException("Id not found", HttpStatus.NOT_FOUND);
        }
        Kangaroo kangarooToReplaceOldKangaroo = new Kangaroo(kangaroo.getId(), kangaroo.getName(), kangaroo.getWeight(), kangaroo.getHeight(),kangaroo.getGender(),kangaroo.isAggressive());
        kangaroos.put(id, kangarooToReplaceOldKangaroo);
        return kangaroos.get(id);
    }

    @DeleteMapping("/{id}")
    public List<Kangaroo> removeKangaroo(@PathVariable long id){
        if(id<=0){
            throw new ZooException("Id must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(!kangaroos.containsKey(id)){
            throw new ZooException("Id not found", HttpStatus.NOT_FOUND);
        }
        kangaroos.remove(id);
        return kangaroos.values().stream().toList();
    }



}
