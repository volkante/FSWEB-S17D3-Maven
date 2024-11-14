package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Gender;
import com.workintech.zoo.entity.Kangaroo;
import jakarta.annotation.PostConstruct;
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
        kangaroos.put(1L,new Kangaroo(1,"crazyKangaroo",41.2, Gender.FEMALE,1.70,true ));
        kangaroos.put(2L,new Kangaroo(2,"normalKangaroo",35.7, Gender.MALE,1.55,false ));
        //System.out.println(kangaroos);
    }

    @GetMapping("")
    public List<Kangaroo> getAllKangaroos(){
        return kangaroos.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Kangaroo findKangarooById(@PathVariable long id){
        return kangaroos.get(id);
    }

    @PostMapping()
    public List<Kangaroo> createKangaroo(@RequestBody Kangaroo kangaroo){
        Kangaroo kangarooToBeCreated = new Kangaroo(kangaroo.getId(), kangaroo.getName(), kangaroo.getWeight(), kangaroo.getGender(),kangaroo.getHeight(),kangaroo.isAggressive());
        kangaroos.put(kangarooToBeCreated.getId(),kangarooToBeCreated);
        return kangaroos.values().stream().toList();
    }

    @PutMapping("/{id}")
    public Kangaroo updateKangaroo(@PathVariable long id, @RequestBody Kangaroo kangaroo){
        Kangaroo kangarooToReplaceOldKangaroo = new Kangaroo(kangaroo.getId(), kangaroo.getName(), kangaroo.getWeight(), kangaroo.getGender(),kangaroo.getHeight(),kangaroo.isAggressive());
        kangaroos.put(id, kangarooToReplaceOldKangaroo);
        return kangaroos.get(id);
    }

    @DeleteMapping("/{id}")
    public List<Kangaroo> removeKangaroo(@PathVariable long id){
        kangaroos.remove(id);
        return kangaroos.values().stream().toList();
    }



}
