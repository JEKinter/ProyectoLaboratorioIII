package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScoreCrediticioService {
    public static boolean verificarScore(long dni){
        Random random = new Random();
        return random.nextBoolean();
    }
}
