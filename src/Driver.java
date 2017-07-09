/**
 * Created by leon on 7/8/2017.
 */

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class Driver implements IDriver {

    private String channel = "https://clubattendancesjam.firebaseio.com/";
    private String key = "";//"MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC46WyayA9+p7+J\nfIModA7rdR91kstk7ooroNYhP0krPJF7bV1CQGKK3THrV6kgSJqDyEMj1ZRp62N3\niH8ztYjwh4xnMVPXrvHELMhwUVUzy4Ou0RZObUGB2QHiialq9P9WWXI8luj1GKHE\nkMikLDihQcF9360IfPje310lR/YJLBsmCcQeH71ke470VXvq8KYirzyViH8J5w3G\nWE3PP7e+qcQNKpRVzVeczWTE1Zoji9u1yS61YYmXPrwuL/kKWHRwC+zf74r496w6\n0jM6tuUxMLA1AHUbD7ivX5wDylfS77bce7lcHlzFgobf4UzAy56c+8flQe6GW1P9\n4JS96sgfAgMBAAECggEAEzxn3uYxrnRW7uo0CWZMEwnZHvW0EP47rh7fh7qPVbR0\nUfRmP452cd8MOAujfXHwGQYhQz4PCsHA/t4FQDosp7usyyNiMc1yOyfe0Mhf5lvP\n3sTDiZ8WByMDGNpHSegjaTaJisIMM/W/BmAlbzwRHBK8xT0alrJ21VXug61Wa/es\nX6BFJ6NK/UcQHMPcaU8Bg77cmJ37yJR0MCo8LlUOoKIbEiYmNcHay8Nj/Fp5hZGU\npzFzM/jotZk88gZhuYyBFH1OyVLRtp9XmJzHyEB62nirOlEnbDqAzdjsnlc/Hb4S\n/JQEzBQJHUgTtf2F+FZ4vFwjvD3BTm9kYi9Yiv+UIQKBgQDbo6JU0AgcxGrSc88T\nvLrrP5mdN12TM0BLYyE1DdS4NYqLRlHhK2qXicNSY3uYUWjXrYrXd3xrprxbRdNg\nC7epakMZE75oUN7sF3L2jgRAbUQq0jZhkzT+6JTDZXgNjdjeVtgiNr/b3/CaGyew\ngu0CeEj3PkTE/VkExkFUMMqcLwKBgQDXhgjhfx15YIrLjafk/RrAR0o6pGBAZC3Y\nmyxRzAXkMt5Fslx4/gzEJOGAsqQ43eYAgtOFyYzGYUjx5sW4eetnxFieeolc4Rt1\nWpFzbt51vh8mKS/FJodjvMoT4vrB8MiQs+/26ylDepjIwYZIGTRlMJGeIkh3Swav\nPBaOoPjnEQKBgQDAcH3Ri7gf5GSn4fokk+6+IappGlJtn7EMnF1DC8w+XgL2hnVF\nMXIiNf0h2bI3wdR3bEFDChjEhYFxAxvxgjK4SDdj6G2jHoBDIUSEC+hjvpjEYAxv\nr7f2RDfbf7MRRSAnprWWPJwbfPllPLeoqv4YdOWRY0iDxGpTiZVBu8I51wKBgQC0\nyHSYmiTiFG44Mnm75edTlDkamZk3ShD+2aLCdi4W7ehudB+HSirxOLKDXtXL8a4l\njZfaaGcSXLZwWxBNHs+suhFP1h907ko3FSJevogSIclFHFU6vYfhzJGBPuryVYQX\nhSfrafSx6ptQAq0duy6tFqF/EOjST/eqc+/D8hj20QKBgQCOyrjr9gwlhGhRqPce\nYfk3x5Y31uQjjqie9HrudsJcpjY6pG0PmAlt80quxFui63Sdt66CH7DTb4zjnux4\nWkOYJDcgQK6+hOXCek5FQCw/nmNhgWUbMaP0iNV+wsT+P5FhKrUk3YVVqVlnaQOA\npAsGYx68YFvNG21L/q8qphPFvQ==";
    private final static String QUATA = "\"";

    public boolean write(Map<String, String> map) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            for (String key : map.keySet()) {
                sb.append(QUATA);
                sb.append(key);
                sb.append(QUATA);
                sb.append(" : ");
                sb.append(QUATA);
                sb.append(map.get(key));
                sb.append(QUATA);
                sb.append(",");
            }
            String data = sb.toString();
            data = data.substring(0, data.length() - 1);
            String node = data + "}";
            HttpClient httpclient = new DefaultHttpClient();
//            node="{\"clubattendancesjam\":{\"alanisawesome\": {\"name\": \"Alan Leon\",\"birthday\": \"June 21, 1912\"}}}";//"\"Leon123\":{"+node+"}";
            HttpPut httppost = new HttpPut(getChannelUrl());
            StringEntity entity = new StringEntity(node);
            httppost.setEntity(entity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200)
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Reader read(String uri) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(channel);
            sb.append("/");
            sb.append(uri);
            sb.append(".json");
            addKey(sb);
            String url = sb.toString();
            return getResultReader(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean delete(String uri) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(channel);
            sb.append("/");
            sb.append(uri);
            sb.append(".json");
            addKey(sb);
            String url = sb.toString();
            HttpDelete delete = new HttpDelete(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(delete);
            if (response.getStatusLine().getStatusCode() == 200)
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getChannelUrl() {
        StringBuffer sb = new StringBuffer();
        sb.append(channel);
        sb.append(".json");
        addKey(sb);
        return sb.toString();
    }

    private void addKey(StringBuffer sb) {
        if (key != null) {
            sb.append("?key=");
            sb.append(key);
        }
    }

    public Reader getResultReader(String resource) throws Exception {
        URL url = new URL(resource);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);
        return new InputStreamReader(connection.getInputStream());
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

