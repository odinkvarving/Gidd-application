package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LevelRepoTest {

    @Autowired
    private LevelRepo levelRepo;

    private Level testLevel;

    @BeforeEach
    void setUp() {
        //wipe
        this.levelRepo.deleteAll(this.levelRepo.findAll());
        this.testLevel = new Level("alevel", null, null);
    }

    @Test
    @Order(2)
    void testSaveAndFindFunctionality() {
        try {
            this.levelRepo.save(testLevel);
            List<Level> elements = new ArrayList<>();
            this.levelRepo.findAll().forEach(elements::add);

            assertEquals(1, elements.size());
            //Id = 2 because of deletetest is first.
            Level foundLevel = this.levelRepo.findById(2).orElseThrow(NoSuchElementException::new);
            assertEquals(this.testLevel.getDescription(), foundLevel.getDescription());
        } catch (Exception e) {
            fail(e.toString());
        }
    }


    @Test
    @Order(1)
    void testDeleteFunctionality() {
        try {
            this.levelRepo.save(testLevel);
            Level foundLevel = this.levelRepo.findById(1).orElseThrow(NoSuchElementException::new);


            this.levelRepo.deleteById(1);
            List<Level> elements = new ArrayList<>();
            this.levelRepo.findAll().forEach(elements::add);
            assertTrue(elements.isEmpty());

            try {
                this.levelRepo.findById(1).orElseThrow(NoSuchElementException::new);
                fail("should not be reached");
            } catch (Exception e) {
            }

        } catch (Exception e) {
            fail(e.toString());
        }
    }
}