import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLHelper {
    private File file;
    public XMLHelper() { }

    public void open(String filePath) throws IOException {
        file = new File(filePath);

        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.println("File created: " + filePath);

                String xml = "<MyCalendar>\n" + "</MyCalendar>";

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(xml.getBytes());
                } catch (IOException e) {
                    throw new IOException("Failed to write to file.");
                }
            } else {
                throw new IOException("Failed to create file.");
            }
        } else {
            System.out.println("File exists: " + filePath);
        }
    }

    public void close() {
        file = null;
    }

    public MyCalendar read() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MyCalendar.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (MyCalendar) unmarshaller.unmarshal(file);
    }

    public void save(MyCalendar object) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MyCalendar.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            marshaller.marshal(object, fos);
        }
    }

    public void saveAs(MyCalendar object, String filePath) throws JAXBException, IOException {
        File newFile = new File(filePath);

        JAXBContext jaxbContext = JAXBContext.newInstance(MyCalendar.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            marshaller.marshal(object, fos);
        }
    }
}