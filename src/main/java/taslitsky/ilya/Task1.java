package taslitsky.ilya;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


public class Task1
{
    public static void main( String[] args ) {
        Task1 task1 = new Task1();
        task1.solution("output.xml", "input.xml");
    }

    private void solution(String outputFileName, String inputFileName) {
        XMLStreamReader parser = null;
        XMLStreamWriter writer = null;
        String dirName = "task1/";
        String path = "src/main/resources/" + dirName;

        try {
           parser = XMLInputFactory.newFactory()
                .createXMLStreamReader(new FileInputStream(path + inputFileName));

            writer = XMLOutputFactory.newFactory()
                .createXMLStreamWriter(new FileOutputStream(path + outputFileName));

            writer.writeStartElement("persons");

            while (parser.hasNext()) {
                int event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String name = parser.getAttributeValue(null, "name");
                    String surname = parser.getAttributeValue(null, "surname");
                    String birthdate = parser.getAttributeValue(null, "birthDate");

                    if(name != null && surname != null && birthdate != null) {
                        writer.writeStartElement("person");
                        writer.writeAttribute("name", name + " " + surname);
                        writer.writeAttribute("birthDate", birthdate);
                        writer.writeEndElement();
                    }
                }
            }
            writer.writeEndElement();
        } catch (XMLStreamException | FileNotFoundException e) {
           e.printStackTrace();
        }

        if (parser != null) {
            try {
                parser.close();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        if (writer != null) {
            try {
                writer.close();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

