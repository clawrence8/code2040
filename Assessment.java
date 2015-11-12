//Clayton Lawrence CODE2040 Assessment

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.httpclient.HttpConnection;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    public static int needleInHaystack(String string, ArrayList<String> haystack) {
        if (string.equals(null) || haystack.equals(null)) {
            return -1;
        }
        for (int i = 0; i < haystack.size() - 1; i++) {
            if (haystack.get(i).equals(string)) {
                return i;
            }
        }
        return -1;

    }

    public static JsonArray prefix(String myPrefix, ArrayList<String> strings) {
        if (myPrefix.equals(null) || strings.equals(null)) {
            return null;
        }
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (int i = 0; i < strings.size() - 1; i++) {
            if (!strings.get(i).startsWith(myPrefix)) {
                builder.add(strings.get(i));
            }
        }

        return builder.build();
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

    public static ArrayList<String> parseJson(String json) {
        ArrayList<String> list = new ArrayList<>();
        JsonParser parser = Json.createParser(new StringReader(json));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_ARRAY:
                    //System.out.println("Start Array: " +  event);
                case END_ARRAY:
                    //System.out.println("End Array: " +  event);
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
                    list.add(parser.getString());

                    break;
            }
        }
        return list;
    }



    public static void main(String[] args) {
        JsonObject registrationJson = Json.createObjectBuilder()
                .add("email", "ca.lawrence03@gmail.com")
                .add("github", "https://github.com/clawrence8/code2040")
                .build();


        Assessment assessment = new Assessment();
        ArrayList<String> result = parseJson(post(REGISTRATION_ENDPOINT, registrationJson));
        token = result.get(result.size() - 1);

        //Stage 1
        JsonObject getstring = Json.createObjectBuilder().add("token", token).build();
        String myString = parseJson(post(REVERSE_ENDPOINT, getstring)).get(0);
        String rev = stringReverse(myString);
        JsonObject validateString = Json.createObjectBuilder().add("token", token)
                .add("string", rev).build();
        String revResult = parseJson(post(REVERSE_VALIDATE,validateString)).get((0));

        //Stage2
        result = parseJson(post("http://challenge.code2040.org/api/haystack", getstring));
        String needle = result.get(result.size() - 1);
        int index = needleInHaystack(needle, result);
        JsonObject validateNeele = Json.createObjectBuilder().add("token", token).add("needle", index).build();
        String needleResult = parseJson(post("http://challenge.code2040.org/api/validateneedle", validateNeele)).get(0);
        System.out.println(needleResult);

        //Stage 3
        result = parseJson(post("http://challenge.code2040.org/api/prefix", getstring));
        String myprefix = result.get(result.size() - 1);
        JsonObject validatePrefix = Json.createObjectBuilder()
                .add("token", token)
                .add("array", prefix(myprefix, result)).build();
        String verify = parseJson(post("http://challenge.code2040.org/api/validateprefix", validatePrefix)).get(0);
        System.out.println(verify);

        //Stage 4
        //datestamp 1985-07-10T17:31:00.000Z
        //Interval 6724097
        String date = parseJson(post("http://challenge.code2040.org/api/time", getstring)).get(0);
        DateFormat df = new SimpleDateFormat(date);




    }






}
