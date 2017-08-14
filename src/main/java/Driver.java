import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class Driver implements IDriver {
    // Channel is the parent object
    private String channel = "https://clubattendancesjam.firebaseio.com/"; // Channel Name defaults to this if nothing is set

    private String key = "";
    private final static String QUATA = "\""; // Escaped Quotation Mark

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
            HttpPut httppost = new HttpPut(getChannelUrl()); // Use PUT prevents key generation for each as a parent
            StringEntity entity = new StringEntity(node);
            httppost.setEntity(entity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            this.resetChannel();
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public Reader read(String... uri) {
        this.setChannel(uri);
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(channel); //Reads from a channel
//            sb.append("/");
//            sb.append(uri);
            sb.append(".json");
            addKey(sb);
            String url = sb.toString();
            this.resetChannel();
            return getResultReader(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.resetChannel();
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
            this.resetChannel();
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String getChannelUrl() {
        StringBuffer sb = new StringBuffer();
        sb.append(channel);
        sb.append(".json");
        addKey(sb);
        return sb.toString();
    }

    private void addKey(StringBuffer sb) {
        if (key != null) {
            sb.append("?auth=");
            sb.append(key);
        }
    }

    private Reader getResultReader(String resource) throws Exception {
        URL url = new URL(resource);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);
        return new InputStreamReader(connection.getInputStream());
    }

    public boolean channelExists() {
        try {
            String content;
            StringBuilder sb = new StringBuilder();
            Reader isr = getResultReader(getChannelUrl());
            BufferedReader br = new BufferedReader(isr);
            while ((content = br.readLine()) != null) {
                sb.append(content);
            }
            System.out.println("\nSB \""+sb.toString()+"\"\n");
            if (!sb.toString().equalsIgnoreCase("null")){
                System.out.println("==>PASS");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String... channel) {
        if (channel.length == 0) {
            return;
        }
        String finalLocation = "";
        for (String location :
                channel) {
            finalLocation += location + "/";
        }
        this.channel = this.channel.toLowerCase() + finalLocation.toLowerCase();
    }

    private void resetChannel() {
        String defaultChannel = "https://clubattendancesjam.firebaseio.com/";
        this.channel = defaultChannel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

