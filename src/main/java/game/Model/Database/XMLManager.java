package game.Model.Database;

import game.Model.Record;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Maneges the high score board's records. Which is stored in an xml file.
 */
public class XMLManager {
    /**
     * List of records.
     */
    ArrayList<Record> recordList=new ArrayList<>();
    /**
     * Input stream from the original records' xml file.
     */
    InputStream input = getClass().getResourceAsStream("/records.xml");
    /**
     * If is run though a JAR, then it is the file which is being loaded and overwritten, instead of the original records file.
     */
     private File inputJar=null;
    /**
     * It is what gets loaded and overwritten, when the game is not in a JAR file.
     */
    private File JARlessInput;

    /**
     * Constructor without parameters.
     */
    public XMLManager()  {
        try {
            if(!this.isInJAR())
            {
                JARlessInput=new File(getClass().getClassLoader().getResource("records.xml").getFile());
            }
            File outerJarFile=new File((XMLManager.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getParentFile();
            outerJarFile = new File(outerJarFile.getPath()+"/records.xml");
            if(isInJAR() && !outerJarFile.exists())
            {
                outerJarFile.createNewFile();
                Logger.info("Creating new records file in the JAR's directory.");
                //Files.copy(input,outerJarFile.toPath()
                byte[] buffer = new byte[input.available()];
                input.read(buffer);
                Files.write(outerJarFile.toPath(),buffer);

                Logger.info("Successfully created new records file.");
            }

            if(isInJAR()){
                this.inputJar=outerJarFile;
                Logger.info("Records file already exists. Loading file from JAR's directory.");
            }
        } catch (URISyntaxException e) {
           Logger.error("URI Syntax error while constructing XMLManager.");
        } catch (IOException e) {
            Logger.error("IO error while constructing outer XML to store records.");
        }

        loadRecords(recordList);
    }

    /**
     * Load the record from an xml file into a list of records.
     * @param list Where to load the records.
     */
    private void loadRecords(ArrayList<Record> list){

        try {

            InputStream inputFile;
            File file;
               inputFile= input;
                file =inputJar;


            SAXBuilder saxBuilder = new SAXBuilder();
            Document document;
            if(!this.isInJAR())
                document= saxBuilder.build(inputFile);
            else
            {
                Logger.info("Loading records, while is a JAR file.");
                document = saxBuilder.build(inputJar);
            }
            Element classElement = document.getRootElement();

            for(Element element:classElement.getChildren())
            {
                Record record=new Record(element.getChild("Name").getText(),Integer.parseInt(element.getChild("Score").getText()));
                this.recordList.add(record);


            }

        } catch(JDOMException e) {
            Logger.error("JDOM error while loading records.");
        } catch(IOException ioe) {
            Logger.error("IO error while loading records.");
        }
    }

    /**
     * Saves the records into a xml file.
     */
    private void saveRecords(){

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
            if(this.isInJAR())
            {
                xmlOutput.output(doc, new FileWriter(inputJar));
                Logger.info("Saving records, while is a JAR file.");
            }
            else
            xmlOutput.output(doc, new FileWriter(JARlessInput));

        } catch(IOException e) {
            Logger.error("IO exception while saving records.");
        }
    }

    /**
     * If is high enough, then it gets added into the xml file holding the high scores.
     * @param record record to be added if high enough
     */
    public void addRecord(Record record){
        Logger.info("Adding new record to records file.");
        this.recordList.sort(Comparator.comparing(Record::getScore,Comparator.reverseOrder()));
        if(this.isHighEnough(record))
        refreshHighScoreFile(record);
        Logger.info("Record has been added.");
    }

    /**
     * Refreshes the HighScore file.
     * @param newRecord Record to be added to HighScore file.
     */
    private void refreshHighScoreFile(Record newRecord){
        ArrayList<Record> loadedRecords;
        loadedRecords=this.recordList;
        if(loadedRecords.size()+1>10)
                loadedRecords.remove(loadedRecords.stream().min(Comparator.comparing(Record::getScore)).get());
                loadedRecords.add(newRecord);
                saveRecords();

    }

    /**
     * Determines whether or not the given record can be added to the highScore list.
     * @param record Record to be examined.
     * @return True, if it can be added, otherwise false.
     */
    public boolean isHighEnough(Record record)
    {
            if(recordList.stream().min(Comparator.comparing(Record::getScore)).get().getScore()<record.getScore() || this.recordList.size()<10 || record.getScore()>0)
            return true;
            else return false;
    }

    /**
     * Useful if is run in a JAR file.
     * @return Whether or not is in a JAR file.
     */
    public boolean isInJAR(){
        URL url = this.getClass().getClassLoader().getResource("records.xml");
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            Logger.error("IO exception while trying to check if it is in a jar.");
        }
        if (urlConnection instanceof JarURLConnection) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Getter of the RecordList field.
     * @return Returns the list containing the highScore.
     */
    public ArrayList<Record> getRecordList() {
        return recordList;
    }
}
