
import com.google.gson.JsonArray;

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

    boolean write(Map<String, String> map);

    Reader read(String... uri);

    boolean delete(String uri);

    void setChannel(String... channel);

    void setKey(String key);

}