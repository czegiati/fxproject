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

public class XMLManager {

    ArrayList<Record> recordList=new ArrayList<>();


    public XMLManager(){
        loadRecords();

    }

    private void loadRecords(){
        String input="records.xml";
        int height=0;
        String name="";
        try {

            File inputFile = new File(input);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element classElement = document.getRootElement();






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

}
