package game.Model.Database;

import game.Model.Record;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class XMLManager {

    ArrayList<Record> recordList=new ArrayList<>();


    public XMLManager(){
        loadRecords(recordList);

    }

    private void loadRecords(ArrayList<Record> list){
        String input="src/main/resources/records.xml";
        try {
            File inputFile = new File(input);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element classElement = document.getRootElement();

            for(Element element:classElement.getChildren())
            {
                Record record=new Record(element.getChild("Name").getText(),Integer.parseInt(element.getChild("Score").getText()));
                this.recordList.add(record);


            }

        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void saveRecords(){
        String outputPath="src/main/resources/records.xml";
        try{
            Element Records = new Element("Records");
            Document doc = new Document(Records);


            for(Record record:this.recordList)
            {
                Element Record=new Element("Record");
                Records.addContent(Record);

                Element name=new Element("Name");
                name.setText(record.getName());
                Record.addContent(name);

                Element score=new Element("Score");
                score.setText(record.getScore().toString());
                Record.addContent(score);

            }

            XMLOutputter xmlOutput = new XMLOutputter();

            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(outputPath));

        } catch(IOException e) {

            e.printStackTrace();
        }
    }

    public void addRecord(Record record){
        this.recordList.sort(Comparator.comparing(Record::getScore,Comparator.reverseOrder()));
        if(this.isHighEnough(record))
        refreshHighScoreFile(record);
    }

    public void refreshHighScoreFile(Record newRecord){
        ArrayList<Record> loadedRecords;
        loadedRecords=this.recordList;
        if(loadedRecords.size()+1>10)
                loadedRecords.remove(loadedRecords.stream().min(Comparator.comparing(Record::getScore)).get());
                loadedRecords.add(newRecord);
                saveRecords();

    }


    public boolean isHighEnough(Record record)
    {
            if(recordList.stream().min(Comparator.comparing(Record::getScore)).get().getScore()<record.getScore() || this.recordList.size()<10)
            return true;
            else return false;
    }

    public ArrayList<Record> getRecordList() {
        return recordList;
    }
}
