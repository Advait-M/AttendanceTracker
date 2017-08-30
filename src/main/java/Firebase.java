

import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;


public class Firebase {
    private IDriver driver;
    private DateFormat dateFormat;

    public Firebase() {
        this.driver = new Driver();
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String key = "";
        try {
            key = readFile(Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.driver.setKey(key);
    }

    private static String readFile(Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("key.txt"));
        return new String(encoded, encoding);
    }

    private String getDate() {
        return dateFormat.format(new Date().getTime());
    }

    public boolean createStudent(String number, String club) {
        addStudentToList(number);
        this.driver.setChannel(number, club);
        Map<String, String> data = new HashMap<>();
        data.put("meeting1", getDate());
        return this.driver.write(data);

    }

    private void addStudentToList(String number) {
        HashSet<String> all_students = new HashSet<>();
        all_students.add(number);
        this.driver.setChannel("all_students");
        Reader read = this.driver.read();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(read);
        if (element.toString().equalsIgnoreCase("null")) {
            this.driver.writeA("all_students", all_students.toArray(new String[0]));
        } else {
            for (JsonElement jsonElement : element.getAsJsonArray()) {
                all_students.add(jsonElement.getAsString());
            }
            System.out.println("in else");
            this.driver.setChannel("all_students");
            System.out.println(this.driver.writeA(all_students.toArray(new String[0])));
        }

    }


    private ArrayList<String> getAllStudents() {
        this.driver.setChannel("all_students");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(this.driver.read());
        if (!element.toString().equals("null")) {
            ArrayList<String> temp = new ArrayList<>();
            for (JsonElement e : element.getAsJsonArray()) {
                temp.add(e.getAsString());
            }
            return temp;
        } else {
            return null;
        }
    }

    private boolean SetMeetingDay(String number, String club) {
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

    boolean addMeetingDay(String number, String club) {
        ArrayList<String> allStudents = getAllStudents();
        if (allStudents == null) {
            return createStudent(number, club) == createStudent("club_master", club);
        } else if (!allStudents.contains(number)) {
            return createStudent(number, club) == SetMeetingDay("club_master", club);
        }
        return SetMeetingDay(number, club) == SetMeetingDay("club_master", club);

    }

    private boolean SetUserPaid(String number, String club) {
        Map<String, String> data = new HashMap<>();
        data.put("paid", getDate());
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

    boolean SetPaid(String number, String club) {
        return SetUserPaid(number, club) == SetUserPaid("club_master", club);
    }

    public static void main(String[] args) {
        Firebase firebase = new Firebase();
        firebase.addMeetingDay("641852934", "science");
        firebase.SetPaid("641852934", "science");
        firebase.SetPaid("64sd1852934", "sscience");


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


