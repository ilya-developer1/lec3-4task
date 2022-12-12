package taslitsky.ilya.task2;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Task2 {

  public static void main(String[] args) throws IOException {
    Task2 task2 = new Task2();
    task2.solution("output.xml", "input.json");
  }

  private void solution(String outputFileName, String inputFileName) throws IOException {
    Map<String, Double> statistics = new HashMap<>();
    String dirName = "task2/";
    String path = "src/main/resources/" + dirName;
    try (
        InputStream inputStream = Files.newInputStream(Path.of(path + inputFileName));
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
    ) {
      reader.beginArray();
      while (reader.hasNext()) {
        Fine fine = new Gson().fromJson(reader, Fine.class);
        calcStatistics(statistics, fine.getType(), fine.getAmount());
      }
      reader.endArray();
    }

    writeXmlStatistics(path + outputFileName, statistics);
  }

  private void calcStatistics(Map<String, Double> statistics, String type, Double amount) {
    if(statistics.containsKey(type)) {
      statistics.put(type, statistics.get(type) + amount);
      return;
    }
    statistics.put(type, amount);
  }

  private void writeXmlStatistics(String outputPath, Map<String, Double> statistics) {
    XMLStreamWriter writer = null;

    try {
      writer = XMLOutputFactory.newFactory()
          .createXMLStreamWriter(new FileOutputStream(outputPath));

      writer.writeStartElement("FineStat");

      for (Map.Entry<String, Double> fine :
      statistics.entrySet()) {
        writer.writeStartElement("Fine");
        writer.writeAttribute(fine.getKey(), String.valueOf(fine.getValue()));
        writer.writeEndElement();
      }

      writer.writeEndElement();
    } catch (XMLStreamException | FileNotFoundException e) {
      e.printStackTrace();
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

