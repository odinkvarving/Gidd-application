package ntnu.idatt2106.group8.gidd.controller;

import ntnu.idatt2106.group8.gidd.dao.ActivityDao;
import ntnu.idatt2106.group8.gidd.model.entities.*;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepo;
import ntnu.idatt2106.group8.gidd.service.ActivityService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ActivityServiceTest {
    @Autowired
    private ActivityRepo activityRepo;

    @Test
    void getActivities() {
    }

    @Test
    void getActivity() {
    }

    @Test
    void addActivity() {
    }

    @Test
    void updateActivity() {
    }

    @Test
    void deleteActivity() {
    }
}