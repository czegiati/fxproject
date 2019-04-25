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

    public void addRecordtoVisual(Record record){
        this.recordList.add(record);
        this.recordList.sort(Comparator.comparing(Record::getScore,Comparator.reverseOrder()));
    }

    public void refreshHighScoreFile(){
        ArrayList<Record> loadedRecords=new ArrayList<>();
        loadRecords(loadedRecords);


        if(recordList.stream().filter(o -> !loadedRecords.contains(o)).findFirst().isPresent())
        {
            Record newRecord = this.recordList.stream().filter(o -> !loadedRecords.contains(o)).findFirst().get();

            if(loadedRecords.stream().filter(o -> o.getScore()<newRecord.getScore()).findFirst().isPresent())
            {
                loadedRecords.remove(loadedRecords.stream().min(Comparator.comparing(Record::getScore)).get());
                loadedRecords.add(newRecord);
                replaceRecordInFile(loadedRecords);
            }

        }

    }


    private void replaceRecordInFile(ArrayList<Record> list){

        String input="src/main/resources/records.xml";
        try {
            File inputFile = new File(input);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element classElement = document.getRootElement();


            ArrayList<Record> readElements=new ArrayList<>();
            for(Element element:classElement.getChildren())
            {
                Record record=new Record(element.getChild("Name").getText(),Integer.parseInt(element.getChild("Score").getText()));
                if(list.stream().filter(o -> o.equals(record)).findFirst().isPresent())
                {
                    //list contains the read element
                    readElements.add(record);
                }
                else
                {
                    //list does not contain the read element
                    classElement.removeContent(element);
                }
            }

            for(Record rec: list)
            {
                if(!readElements.stream().filter(o -> o.equals(rec)).findFirst().isPresent())
                {
                    Element element=new Element("Record");
                    classElement.addContent(element);

                    Element name= new Element("Name");
                    name.setText(rec.getName());

                    Element score= new Element("Score");
                    score.setText(rec.getScore().toString());

                    element.addContent(name);
                    element.addContent(score);
                }
            }

        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
