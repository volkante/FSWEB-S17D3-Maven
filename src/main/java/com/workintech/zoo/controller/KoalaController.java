package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Gender;
import com.workintech.zoo.entity.Koala;
import com.workintech.zoo.exceptions.ZooException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/koalas")
public class KoalaController {
    private Map<Long,Koala> koalas;

    @PostConstruct
    public void init(){
        System.out.println("KoalaController initialized");
        this.koalas = new HashMap<>();
        koalas.put(1L, new Koala(1,"Arda",5.50,20, Gender.MALE) );
        koalas.put(2L, new Koala(2,"Başak",12.72,17, Gender.FEMALE) );
        //System.out.println(koalas);
    }

    @GetMapping
    public List<Koala> getAllKoalas(){
        return koalas.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Koala getKoalaById(@PathVariable long id){

        if(id<=0){
            throw new ZooException("Id must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(!koalas.containsKey(id)){
            throw new ZooException("Id not found", HttpStatus.NOT_FOUND);
        }
        return koalas.get(id);
    }

    @PostMapping()
    public Koala addKoala(@RequestBody Koala koala){
        if(koala.getId() <= 0 || koala.getName() == null
                || koala.getWeight() <= 0 || koala.getSleepHour() <= 0
                || koala.getGender() == null){
            throw new ZooException("Invalid koala data", HttpStatus.BAD_REQUEST);
        }
        Koala koalaToBeAdded = new Koala(koala.getId(),koala.getName(),koala.getWeight(),koala.getSleepHour(),koala.getGender());
        koalas.put(koalaToBeAdded.getId(),koalaToBeAdded);
        return koalas.get(koalaToBeAdded.getId());
    }

    @PutMapping("/{id}")
    public List<Koala> updateKoala(@PathVariable long id, @RequestBody Koala koala){
        if(id<=0){
            throw new ZooException("Id must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(!koalas.containsKey(id)){
            throw new ZooException("Id not found", HttpStatus.NOT_FOUND);
        }
        Koala koalaToReplaceOldKoala = new Koala(koala.getId(),koala.getName(),koala.getWeight(),koala.getSleepHour(),koala.getGender());
        koalas.put(id,koalaToReplaceOldKoala);
        return koalas.values().stream().toList();
    }

    @DeleteMapping("/{id}")
    public List<Koala> removeKoala(@PathVariable long id){
        if(id<=0){
            throw new ZooException("Id must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(!koalas.containsKey(id)){
            throw new ZooException("Id not found", HttpStatus.NOT_FOUND);
        }
        koalas.remove(id);
        return koalas.values().stream().toList();
    }


}
