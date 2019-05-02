package game.Model.Database;

import game.Model.Record;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XMLManagerTest {

    private XMLManager manager=new XMLManager();

    @Test
    void addRecord() {
        Integer size=manager.getRecordList().size();
        manager.addRecord(new Record("rec",-1));
        assertEquals(size+0, manager.getRecordList().size());
    }

    @Test
    void isHighEnough() {
        Record a=new Record("a",-1);
        assertEquals(false,manager.isHighEnough(a));
    }

}