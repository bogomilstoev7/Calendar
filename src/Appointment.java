import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Appointment")
public class Appointment {
    private String date;
    private String startTime;
    private String endTime;
    private String name;
    private String note;
    private boolean isHoliday;

    public Appointment() {}

    public Appointment(String date, String startTime, String endTime, String name, String note) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
        this.isHoliday = false;
    }

    @XmlElement(name = "date")
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement(name = "startTime")
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @XmlElement(name = "endTime")
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "note")
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @XmlElement(name = "isHoliday")
    public boolean getIsHoliday() {
        return isHoliday;
    }
    public void setIsHoliday(boolean holiday) {
        this.isHoliday = holiday;
    }

    @Override
    public String toString() {
        return name + " (" + note + ")" + " - " + startTime + " to " + endTime + " - " + date + " [isHoliday: " + isHoliday + "] ";
    }
}