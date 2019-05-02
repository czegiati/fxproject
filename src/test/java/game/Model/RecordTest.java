package game.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTest {

    private Record testRecord;
    @Before
    public void setUp() {
        testRecord=new Record("Name",1);
    }

    @Test
    public void getName() {
        assertEquals("Name", testRecord.getName());
    }

    @Test
    public void getScore() {
        Integer i=1;
        assertEquals(i, testRecord.getScore());
    }


}