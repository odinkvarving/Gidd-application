package ntnu.idatt2106.group8.gidd.model.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

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
        Account creator = new Account("foo@bar.com", "password");
        ActivityType type = new ActivityType();
        Level level = new Level("Low");
        String start = "2021-07-01 1800";
        String end = "2021-07-01 1930";
        int maxParticipants = 5;

        validActivity = new Activity.Builder(title, creator, type, level, start, end, maxParticipants)
                .build();
    }

    @AfterEach
    void tearDown() {
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
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLatitude(-91), "Set invalid Latitude"),
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLatitude(91), "Set invalid Latitude"),
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLongitude(-181), "Set Invalid Longitude"),
                () -> assertThrows(IllegalArgumentException.class, () -> validActivity.setLongitude(181), "Set Invalid Longitude")
        );
    }
}