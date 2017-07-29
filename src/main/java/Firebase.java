
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;
import sun.util.calendar.BaseCalendar;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;


public class Firebase {
    IDriver driver;
    DateFormat dateFormat;

    public Firebase() {
        this.driver = new Driver();
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    private String getDate() {
        return dateFormat.format(new Date().getTime());
    }

    //    private String incrementMeeting(String Meeting){
//        return "Meeting" + (Integer.parseInt(Meeting.substring(Meeting.indexOf('g')+1)) + 1);
//    }
    public boolean createStudent(String number, String club) {
        driver.setChannel(number, club);
        Map<String, String> data = new HashMap<String, String>();
        data.put("Meeting1", getDate());
        return driver.write(data);
    }

    public Set<String> getAllStudents() {
        driver.setChannel();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(driver.read());
        return element.toString().equals("null") ? null : element.getAsJsonObject().keySet();
    }

    protected boolean SetMeetingDay(String number, String club){
        Map<String, String> data = new HashMap<String, String>();
        // Reads data and parse as JSON
        Reader read = driver.read(number, club);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(read);
        System.out.println("JsonArray Contents");
        if (element.toString().equals("null")){
            return createStudent(number,club);
        }
        Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet(); //will return members of your object
        String MaxKey = "Meeting" + (entries.size()); // Sets current meeting day that is in the database
        JsonElement MaxValue = null; // this will be assigned when the MaxKey is found
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            System.out.println(entry.getKey() + "~>" + entry.getValue());
            data.put(key, value.toString().substring(1, value.toString().length() - 1));
            if (key.equals(MaxKey)) {
                // Assigns max value which is date
                MaxValue = value;
            }
        }
        String MaxValueString;
        if (MaxValue == null) {
            return false;
        }
        MaxValueString = MaxValue.getAsString().split(" ")[0];
        String date = getDate().split(" ")[0];
        // Checks if max value is today if not then signs user in
        if (!(MaxValueString.equals(date))) {
            // Sign user in for  today
            data.put("Meeting" + (entries.size() + 1), getDate());
            driver.setChannel(number, club);
            return driver.write(data);
        }
        return true;

    }

    public boolean addMeetingDay(String number, String club) {
        Set<String> allStudents = getAllStudents();
        if (allStudents == null) {
            return createStudent(number, club)==createStudent("000000000", club);
        } else if (!allStudents.contains(number)) {
            return createStudent(number, club);
        }
        return SetMeetingDay(number,club)==SetMeetingDay("000000000",club);

    }

    public static void main(String[] args)  {
        Firebase firebase = new Firebase();
        firebase.addMeetingDay("741852936", "science");
        firebase.addMeetingDay("741852936", "computer-science");
        firebase.addMeetingDay("641852934", "science");
        firebase.addMeetingDay("641852934", "computer-science");
        firebase.addMeetingDay("641852934", "mathletes");




    }
}


