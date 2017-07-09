import java.io.IOException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leon on 7/8/2017.
 */
public class Main {
    public static void createStudent(IDriver driver,String number,String club){
        driver.setChannel("https://clubattendancesjam.firebaseio.com/"+number+"/"+club);
        Map<String, String> data = new HashMap<String, String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        data.put("Meeting",dateFormat.format(date));
        boolean responseCode = driver.write(data);
    }
    public static void main(String[] args) throws IOException {
        IDriver driver = new Driver();
        createStudent(driver,"789789789","Computer_Science");
        Map<String, String> data = new HashMap<String, String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        data.put("Meeting",dateFormat.format(date));
        driver.setChannel("https://clubattendancesjam.firebaseio.com/"+"789789789"+"/Science");
        driver.write(data);


    }
}
