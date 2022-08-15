package model;

import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;

public class TestUtil {
    private static final Faker faker = new Faker();
    private static final int datasetSize = 20;
    
    public static Contato randomContato() {
        return new Contato(
            faker.number().numberBetween(1, 100), 
            faker.name().fullName(), 
            faker.internet().emailAddress(), 
            faker.address().fullAddress(), 
            faker.phoneNumber().cellPhone());
    }

    public static List<Contato> randomDataset() {
        var list = new ArrayList<Contato>();

        for (var i = 0; i < datasetSize; i++) {
            list.add(randomContato());
        }

        return list;
    }
}
