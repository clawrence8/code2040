//Clayton Lawrence CODE2040 Assessment

import javax.json.Json;
import javax.json.JsonObject;

public class Assessment {

    JsonObject object = Json.createObjectBuilder()
            .add("email", "ca.lawrence03@gmail.com")
            .add("github", "repo").build();


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

    public static void main(String[] args) {

    }






}
