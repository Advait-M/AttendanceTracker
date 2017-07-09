import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leon on 7/8/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        IDriver driver = new Driver();
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", "ll");
        data.put("user", "Loser");
        driver.setChannel("https://clubattendancesjam.firebaseio.com/123456798/user");
        boolean responseCode = driver.write(data);
    }
}
