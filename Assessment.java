//Clayton Lawrence CODE2040 Assessment

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.httpclient.HttpConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Assessment {

    private static String token;
    protected static final String REGISTRATION_ENDPOINT = "http://challenge.code2040.org/api/register";
    protected static final String REVERSE_ENDPOINT = "http://challenge.code2040.org/api/getstring";
    protected static final String REVERSE_VALIDATE = "http://challenge.code2040.org/api/validatestring";

//    JsonObject object = Json.createObjectBuilder()
//            .add("email", "ca.lawrence03@gmail.com")
//            .add("github", "https://github.com/clawrence8/code2040")
//            .build();

    public static String stringReverse(String s) {
        if (s.equals(null)) {
            return null;
        }
        char[] letters = s.toCharArray();
        int front = 0;
        int back = letters.length - 1;
        while (front < back) {
            char temp = letters[front];
            letters[front] = letters[back];
            letters[back] = temp;
            front++;
            back--;
        }

        return new String(letters);
    }

    public static int needleInHaystack(String string, String[] haystack) {
        if (string.equals(null) || haystack.equals(null)) {
            return -1;
        }
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i].equals(string)) {
                return i;
            }
        }
        return -1;

    }

    public static String[] prefix(String myPrefix, String[] strings) {
        if (myPrefix.equals(null) || strings.equals(null)) {
            return null;
        }
        String[] temp = new String[strings.length];
        int size = 0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].startsWith(myPrefix)) {
                temp[i] = strings[i];
                size++;
            }
        }

        String[] ans = new String[size];
        size = 0;
        for (String s: strings) {
            if (!s.equals(null)) {
                ans[size] = s;
                size++;
            }
        }
        return ans;
    }

    public static String datingGame(String datestamp, int seconds) {
        if (datestamp.equals(null)) {
            return null;
        }


        return null; //TODO finish this code
    }

    public static String post(String myUrl, JsonObject json) {
        String jsonString = null;
        try {
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed! Error code: " + connection.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            connection.disconnect();
            reader.close();
            jsonString = sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static String get(String myUrl) {
        try {
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed! Error code: " + connection.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            connection.disconnect();
            return builder.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseJson(String json) {
        String ans = null;
        JsonParser parser = Json.createParser(new StringReader(json));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_ARRAY:
                case END_ARRAY:
                case START_OBJECT:
                case END_OBJECT:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
                    System.out.println(event.toString());
                    break;
                case KEY_NAME:
                    System.out.print(event.toString() + " " +
                            parser.getString() + " - ");
                    break;
                case VALUE_STRING:
                case VALUE_NUMBER:
                    System.out.println(event.toString() + " " +
                            parser.getString());
                    ans = parser.getString();

                    break;
            }
        }
        return ans;
    }



    public static void main(String[] args) {
        JsonObject registrationJson = Json.createObjectBuilder()
                .add("email", "ca.lawrence03@gmail.com")
                .add("github", "https://github.com/clawrence8/code2040")
                .build();


        Assessment assessment = new Assessment();
        token = parseJson(post(REGISTRATION_ENDPOINT, registrationJson));

        JsonObject getstring = Json.createObjectBuilder().add("token", token).build();
        String myString = parseJson(post(REVERSE_ENDPOINT, getstring));
        String rev = stringReverse(myString);
        JsonObject validateString = Json.createObjectBuilder().add("token", token)
                .add("string", rev).build();
        String revResult = parseJson(post(REVERSE_VALIDATE,validateString));


    }






}
