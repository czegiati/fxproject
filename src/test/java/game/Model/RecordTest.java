package game.Model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    private Record testRecord;
    @BeforeEach
    void setUp() {
        testRecord=new Record("Name",1);
    }

    @Test
    void getName() {
        assertEquals("Name", testRecord.getName());
    }

    @Test
    void getScore() {
        Integer i=1;
        assertEquals(i, testRecord.getScore());
    }



}