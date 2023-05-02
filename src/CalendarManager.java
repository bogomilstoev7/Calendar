import javax.xml.bind.JAXBException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarManager {
    private final LocalDate MIN_DATE = convertToLocalDate("2023-01-01");
    private final LocalDate MAX_DATE = convertToLocalDate("2030-01-01");
    private final LocalTime MIN_TIME = convertToLocalTime("00:00");
    private final LocalTime MAX_TIME = convertToLocalTime("23:59");

    private MyCalendar myCalendar = null;
    private Scanner scanner = new Scanner(System.in);

    private XMLHelper xmlHelper = new XMLHelper();
    private String filePath = null;

    public CalendarManager() { }

    public void start() throws JAXBException, IOException, CustomException, ParseException {

        printDescription();

        while (true) {
            System.out.println("Enter a command (open, close, save, saveAs, book, unbook, " +
                    "agenda, displayAll, change, find, holiday, busydays, findslot, clear, exit): ");

            String command = scanner.next();

            if (command.equals("book")) {
                book();
            } else if (command.equals("unbook")) {
                unbook();
            } else if (command.equals("agenda")) {
                agenda();
            } else if (command.equals("displayAll")) {
                displayAll();
            } else if (command.equals("open")) {
                open();
            } else if (command.equals("close")) {
                close();
            } else if (command.equals("save")) {
                save();
            } else if (command.equals("saveAs")) {
                saveAs();
            } else if (command.equals("change")) {
                change();
            } else if (command.equals("find")) {
                find();
            } else if (command.equals("holiday")) {
                holiday();
            } else if (command.equals("busydays")) {
                busydays();
            } else if (command.equals("findslot")) {
                findslot();
            } else if (command.equals("clear")) {
                clear();
            }else if (command.equals("help")) {
                help();
            } else if (command.equals("exit")) {
                System.out.println("Exiting the program...");
                break;
            } else {
                System.out.println("Invalid command.");
            }
        }
    }
    

    public void book() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter date (YYYY-MM-DD):");
        String date = scanner.next();

        if(!checkForValidData(date)){
            throw new CustomException("Invalid date. Date must be between 01-01-2023 and 01-01-2030.");
        }

        System.out.println("Enter start time (HH:MM):");
        String startTime = scanner.next();

        if(!checkForValidHoursAndMinutes(startTime)){
            throw new CustomException("Invalid time. Time must be between 00:00 and 23:59.");
        }

        System.out.println("Enter end time (HH:MM):");
        String endTime = scanner.next();

        if(!checkForValidHoursAndMinutes(endTime)){
            throw new CustomException("Invalid time. Time must be between 00:00 and 23:59.");
        }

        if(!checkIfMinTimeBeforeMaxTime(startTime, endTime)){
            throw new CustomException("Invalid time. EndTime should be after StartTime.");
        }

        System.out.println("Enter name:");
        String name = scanner.next();

        System.out.println("Enter note:");
        String note = scanner.next();

        myCalendar.addAppointment(date, startTime, endTime, name, note);

        System.out.println("Appointment added.");
    }

    public void unbook() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter date (YYYY-MM-DD):");
        String date = scanner.next();

        System.out.println("Enter start time (HH:MM):");
        String startTime = scanner.next();

        System.out.println("Enter end time (HH:MM):");
        String endTime = scanner.next();

        myCalendar.removeAppointment(date, startTime, endTime);
        System.out.println("Appointment removed.");
    }

    public void agenda() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter date (YYYY-MM-DD):");
        String date = scanner.next();

        myCalendar.displayAppointmentsByDate(date);
    }

    public void displayAll() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        myCalendar.displayAll();
    }

    public void open() throws IOException, JAXBException {
        if (isCurrentFileOpened()) {
            System.out.println("A file is already opened, please select another option.\n");
            return;
        }

        System.out.println("Enter file path: ");
        filePath = scanner.next();

        xmlHelper.open(filePath);

        myCalendar = xmlHelper.read();

        System.out.println("Successfully opened " + filePath);
    }

    public void close() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        xmlHelper.close();

        System.out.println("Successfully closed " + filePath);
        myCalendar = null;
    }

    public void save() throws JAXBException, IOException, CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        xmlHelper.save(myCalendar);

        System.out.println("Successfully saved " + filePath);
    }

    public void saveAs() throws CustomException, JAXBException, IOException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter new file name:");
        String pathToSave = scanner.next();

        xmlHelper.saveAs(myCalendar, pathToSave);

        System.out.println("Successfully saved as: " + pathToSave);
    }

    public void help() {
        StringBuilder sb = new StringBuilder();
        sb.append("-List of the available commands: \n");
        sb.append("open \t\t\t opens <file>\n");
        sb.append("close \t\t\t closes currently opened file\n");
        sb.append("save \t\t\t saves the currently open file\n");
        sb.append("saveAs \t\t\t saves the currently open file in <file>\n");
        sb.append("help \t\t\t prints all available commands and information\n");
        sb.append("book \t\t\t saves an appointment in the calendar\n");
        sb.append("unbook \t\t\t removes an appointment in the calendar\n");
        sb.append("agenda \t\t\t prints all appointments a given day\n");
        sb.append("change \t\t\t changes an option for an appointment you choose\n");
        sb.append("find \t\t\t prints an appointment which contains a given note\n");
        sb.append("holiday \t\t marks the day as holiday\n");
        sb.append("busydays \t\t prints all busy days by given range of dates\n");
        sb.append("findslot \t\t finds a free time to book an appointment after given date\n");
        sb.append("exit \t\t\t exists the program\n\n");
        sb.append("-- Bonus commands, used to speed up and simplify program operation for the user:\n");
        sb.append("displayAll \t\t displays all appointments in current calendar\n");
        sb.append("clear \t\t\t clears all appointments in current calendar\n");

        System.out.println(sb);
    }

    public void find() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter note: ");
        String note = scanner.next();

        List<Appointment> appointments = myCalendar.getAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getNote().contains(note)
                    || appointment.getName().contains(note)) {
                System.out.println(appointment.toString());
            }
        }
    }

    public void change() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter date: ");
        String date = scanner.next();

        System.out.println("Enter startTime: ");
        String startTime = scanner.next();

        Appointment appointment = myCalendar.getAppointmentByDateAndStartTime(date, startTime);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("Enter option(date, startTime, endTime, name, note): ");
        String option = scanner.next();

        System.out.println("Enter newValue: ");
        String newValue = scanner.next();

        switch (option) {
            case "date":
                appointment.setDate(newValue);
                break;
            case "startTime":
                appointment.setStartTime(newValue);
                break;
            case "endTime":
                appointment.setEndTime(newValue);
                break;
            case "name":
                appointment.setName(newValue);
                break;
            case "note":
                appointment.setNote(newValue);
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        System.out.println("Appointment changed successfully.");
    }

    public void holiday() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter date: ");
        String date = scanner.next();

        List<Appointment> appointments = myCalendar.getAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date)) {
                appointment.setIsHoliday(true);

                System.out.println(appointment.toString());
                System.out.println("Successfully changed the date to holiday.");
                return;
            }
        }

        System.out.println("Did not find that date in the calendar.");
    }

    public void busydays() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter start date(from): ");
        String fromDate = scanner.next();

        System.out.println("Enter end date(to): ");
        String toDate = scanner.next();

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        List<Appointment> filteredAppointments = new ArrayList<Appointment>();

        List<Appointment> appointments = myCalendar.getAppointments();
        for (Appointment appointment : appointments) {
            LocalDate currentDate = LocalDate.parse(appointment.getDate());
            if (currentDate.isAfter(from) && currentDate.isBefore(to)) {
                filteredAppointments.add(appointment);
            }
        }

        filteredAppointments.sort((a1, a2) -> {
            LocalDate date1 = LocalDate.parse(a1.getDate());
            LocalDate date2 = LocalDate.parse(a2.getDate());
            return date1.compareTo(date2);
        });

        for (Appointment appointment : filteredAppointments) {
            System.out.println(appointment.toString());
        }
    }

    public void findslot() throws CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        System.out.println("Enter from date: ");
        String fromDate = scanner.next();

        LocalDate fromDateLocalDate = convertToLocalDate(fromDate);

        System.out.println("Enter hours: ");
        int hours = scanner.nextInt();
        int minutes = hours * 60;

        List<Appointment> appointments = myCalendar.getAppointments();
        for (Appointment appointment : appointments) {
            String[] startTimeInMinutesSplitted = appointment.getStartTime().split(":");

            int startTimeInMinutes = Integer.parseInt(startTimeInMinutesSplitted[0]) * 60
                    + Integer.parseInt(startTimeInMinutesSplitted[1]);

            String[] endTimeInMinutesSplitted = appointment.getEndTime().split(":");

            int endTimeInMinutes = Integer.parseInt(endTimeInMinutesSplitted[0]) * 60
                    + Integer.parseInt(endTimeInMinutesSplitted[1]);

            int totalFreeMinutes = (endTimeInMinutes - startTimeInMinutes);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime stTime = LocalTime.parse(appointment.getStartTime(), formatter);
            LocalTime eTime = LocalTime.parse(appointment.getStartTime(), formatter);

            LocalTime startBoundary = LocalTime.parse("08:00", formatter);
            LocalTime endBoundary = LocalTime.parse("17:00", formatter);

            LocalDate currentLocalDate = convertToLocalDate(appointment.getDate());

            if(!appointment.getIsHoliday()
                    && !stTime.isBefore(startBoundary)
                    && !eTime.isAfter(endBoundary)
                    && totalFreeMinutes >= minutes
                    && currentLocalDate.isAfter(fromDateLocalDate)) {
                System.out.println(appointment.toString());
            }
        }
    }

    public void clear() throws IOException, CustomException {
        if (!isCurrentFileOpened()) {
            throw new CustomException("File is not opened!");
        }

        myCalendar.clear();

        String xml = "<MyCalendar>\n" + "</MyCalendar>";

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(xml.getBytes());
        }

        System.out.println("Successfully cleared " + filePath);
    }


    private boolean checkIfMinTimeBeforeMaxTime(String minTime, String maxTime) {

        LocalTime time1 = convertToLocalTime(minTime);
        LocalTime time2 = convertToLocalTime(maxTime);

        return time1.isBefore(time2);
    }

    private boolean checkForValidHoursAndMinutes(String timeToBeChecked) {

        LocalTime time = convertToLocalTime(timeToBeChecked);

        return time.isAfter(MIN_TIME) && time.isBefore(MAX_TIME);
    }

    private static LocalTime convertToLocalTime(String timeToBeChecked){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return LocalTime.parse(timeToBeChecked, formatter);
    }

    private boolean checkForValidData(String dateToBeChecked) {
        LocalDate date = convertToLocalDate(dateToBeChecked);

        return date.isBefore(MAX_DATE) && date.isAfter(MIN_DATE);
    }

    private static LocalDate convertToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    private boolean isCurrentFileOpened() {
        if(myCalendar == null) {
            return false;
        }
        return true;
    }

    private void printDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome! This is a Calendar Manager Application.\n");
        sb.append("To get started, you can type 'help' to see a list of the available commands and their descriptions.\n");

        System.out.println(sb.toString());
    }
}
