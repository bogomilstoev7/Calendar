import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        try {
            CalendarManager calendarManager = new CalendarManager();
            calendarManager.start();
        } catch (IOException | JAXBException | CustomException | ParseException e) {
            e.printStackTrace();
        }
    }
}