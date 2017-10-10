import java.io.Reader;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: reiz
 * Date: 4/21/12
 * Time: 11:56 AM
 *
 */
public interface IDriver {

    boolean writeA(String key, String[] all_data);

    boolean writeV2(Map<String, String> map);

    Reader read(String... uri);

    void setChannel(String... channel);

    void resetChannel();

    void setKey(String key);

}