package ntnu.idatt2106.group8.gidd.model.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/*
    x description: ''
    x Latitude & Longitude:
    x start and end time-date
    x max participants

    title
    creator
    activity-type
    level
    equipment

    should activity-type and level be changeable?

    TODO:
     - konstruktør / bygger
     - sjekke at visse felt alltid har en verdi evt. en default verdi (ikke null)
 */

/**
 * @author Torstein Øvstedal
 */
class ActivityTest {

    Activity validActivity;

    @BeforeEach
    void setUp() {
        String title = "Motbakkeløp";
        AccountInfo accountInfo = new AccountInfo.Builder()
                .setFirstname("Foo")
                .setSurname("Bar")
                .setImageURL(null)
                .setProfileDescription(null)
                .setPoints(0)
                .build();
        Account creator = new Account("foo@bar.com", "password", accountInfo);
        ActivityType type = new ActivityType();
        Level level = new Level("Low");
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusMinutes(45);
        int maxParticipants = 5;

        validActivity = new Activity.Builder(title, creator, type, level, start, end, maxParticipants)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setInvalidEndTime() {
        LocalDateTime invalidEndTime = validActivity.getStartTime();
        assertAll(
                () -> assertThrows (
                    IllegalArgumentException.class,
                    () -> validActivity.setEndTime(invalidEndTime),
                    "Set activity end at the same time as start"
                ),
                () -> assertThrows(
                    IllegalArgumentException.class,
                    () -> validActivity.setEndTime(invalidEndTime.minusSeconds(1)),
                    "Set activity end before start"
                )
        );
    }

    @Test
    void setInvalidStartTime() {
        LocalDateTime invalidStartTime = validActivity.getEndTime();
        assertAll(
                () -> assertThrows (
                        IllegalArgumentException.class,
                        () -> validActivity.setStartTime(invalidStartTime),
                        "Set activity start at the same time as end"
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> validActivity.setEndTime(invalidStartTime.plusSeconds(1)),
                        "Set activity start after end"
                )
        );
    }

    @Test
    void setNullDescription() {
        validActivity.setDescription(null);
        assertEquals("", validActivity.getDescription());
    }

    @Test
    void setInvalidMaxParticipants() {
        Executable assertThrowsNegativeParticipants = () ->
            assertThrows(
                    IllegalArgumentException.class,
                    () -> validActivity.setMaxParticipants(-1),
                    "Set negative participants");

        Executable assertThrowsZeroParticipants = () ->
                assertThrows(
                        IllegalArgumentException.class,
                        () -> validActivity.setMaxParticipants(0),
                        "Set zero participants");

        assertAll(assertThrowsNegativeParticipants, assertThrowsZeroParticipants);
    }

    @Test
    void setInvalidLocation() {
        // FIXME: Does not check for decimal error
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLatitude(-91), "Set invalid Latitude"),
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLatitude(91), "Set invalid Latitude"),
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLongitude(-181), "Set Invalid Longitude"),
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLongitude(181), "Set Invalid Longitude")
        );
    }
}