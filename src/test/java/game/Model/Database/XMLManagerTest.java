package game.Model.Database;

import game.Model.Record;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class XMLManagerTest {

    private XMLManager manager=new XMLManager();

    @Test
    public void addRecord() {
        Integer size=manager.getRecordList().size();
        manager.addRecord(new Record("rec",-1));
        assertEquals(size+0, manager.getRecordList().size());
    }

    @Test
    public void isHighEnough() {
        Record a=new Record("a",-1);
        assertEquals(false,manager.isHighEnough(a));
    }

}