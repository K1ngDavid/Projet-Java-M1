package service;

import jakarta.persistence.EntityManager;

public class CarService {

    private EntityManager entityManager;

    public CarService(EntityManager entityManager){
        this.entityManager = entityManager;
    }



}
