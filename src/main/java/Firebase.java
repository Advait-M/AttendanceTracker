

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;
import sun.util.calendar.BaseCalendar;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;


public class Firebase {
    IDriver driver;
    DateFormat dateFormat;

    public Firebase() {
        this.driver = new Driver();
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String key = "";
        try {
            key = readFile("key.txt", Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.driver.setKey(key); // Set key;
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private String getDate() {
        return dateFormat.format(new Date().getTime());
    }

    public boolean createStudent(String number, String club) {
        this.driver.setChannel(number, club);
        Map<String, String> data = new HashMap<String, String>();
        data.put("meeting1", getDate());
        return this.driver.write(data);

    }

    public Set<String> getAllStudents() {
        this.driver.setChannel();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(this.driver.read());
        return element.toString().equals("null") ? null : element.getAsJsonObject().keySet();
    }

    protected boolean SetMeetingDay(String number, String club) {
        Map<String, String> data = new HashMap<String, String>();
        // Reads data and parse as JSON
        Reader read = this.driver.read(number, club);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(read);
//        System.out.println("JsonArray Contents");
        if (element.toString().equals("null")) {
            return createStudent(number, club);
        }
        Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet(); //will return members of your object
        String MaxKey = "meeting" + (entries.size()); // Sets current meeting day that is in the database
        JsonElement MaxValue = null; // this will be assigned when the MaxKey is found
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
//            System.out.println(entry.getKey() + "~>" + entry.getValue());
            data.put(key.toLowerCase(), value.toString().substring(1, value.toString().length() - 1).toLowerCase());
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
            data.put("meeting" + (entries.size() + 1), getDate());
            this.driver.setChannel(number, club);
            return this.driver.write(data);
        }
        return true;
    }

    public boolean addMeetingDay(String number, String club) {
        Set<String> allStudents = getAllStudents();
        if (allStudents == null) {
            return createStudent(number, club) == createStudent("club_master", club);
        } else if (!allStudents.contains(number)) {
            return createStudent(number, club) == SetMeetingDay("club_master", club);
        }
        return SetMeetingDay(number, club) == SetMeetingDay("club_master", club);

    }

    private boolean SetUserPaid(String number, String club) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("paid", getDate().toString());
        if (!getAllStudents().contains(number)) {
            return false;
        }
        Reader read = this.driver.read(number, club);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(read);
        Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet(); //will return members of your object
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            System.out.println(entry.getKey() + "~>" + entry.getValue());
            data.put(key.toLowerCase(), value.toString().substring(1, value.toString().length() - 1).toLowerCase());
        }
        this.driver.setChannel(number, club);
        return this.driver.write(data);
    }

    public boolean SetPaid(String number, String club) {
        return SetUserPaid(number, club) == SetUserPaid("club_master", club);
    }

    public static void main(String[] args) {
        Firebase firebase = new Firebase();
        firebase.addMeetingDay("641852934", "science");
        firebase.SetPaid("641852934", "science");


//        Random r = new Random();
//
//        String[] clubs = new String[]{"Art Club Ideas", "Sculpture Club", "Photography Club", "Art History Club", "Drama Club Ideas ", "Shakespeare Club", "Classics Club", "Monologue Club", "Comedy Sportz Club", "Improv Club", "Film Club Ideas", "Foreign Film Club", "Screenwriting Club", "Directing Club", "48-Hour Film Festival Club", "Science Club Ideas", "Future Scientists Club", "Marine Biology Club", "Future Medical Professionals Club", "Math Club Ideas", "Math Homework Club", "Pi Club", "Literature Club Ideas", "Literature Magazine Club", "Creative Writing Club", "Book Club", "Foreign Book Club", "History Club Ideas", "Ancient History Club", "Language Club Ideas", "Anime Club", "Chess Club", "Video Games Club", "Skiing Club", "Religion Club", "Adventure Club", "Charity Club Ideas", "Save Endangered Species Club"};
//        for (int i = 0; i < 100; i++) {
//            firebase.addMeetingDay(String.valueOf(r.nextInt(900000000) + 100000000), clubs[r.nextInt(clubs.length)].replace(" ", "-"));
//
//        }
        firebase.addMeetingDay("741852936", "science");
        firebase.addMeetingDay("741852936", "computer-science");
        firebase.addMeetingDay("641852934", "science");
        firebase.addMeetingDay("641852934", "computer-science");
        firebase.addMeetingDay("641852934", "mathletes");


    }
}


