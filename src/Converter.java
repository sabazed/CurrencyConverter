import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class Converter {

    private static final String apiKey = "6fdeb1c0-9ca2-11ec-89cd-f9f8714170ad";
    private static final String url = "https://freecurrencyapi.net/api/v2/latest?apikey=" + apiKey + "&base_currency=";

    public static void main(String[] args) {

        System.out.println("Welcome to Currency Converter!");
        System.out.println("====================================================");

        System.out.println("Enter starting currency code (ex. USD, GEL, EUR...):");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine().toUpperCase();

        String rates = "";
        while (rates.isEmpty()) {
            try {
                rates = getRates(code);
                if (rates.equals("0")) {
                    System.out.println("Encountered an error, exiting...");
                    return;
                }
                else if (rates.charAt(0) == '<') {
                    rates = "";
                    throw new Exception();
                }
            }
            catch (Exception e) {
                System.out.println("Please enter a valid currency code!");
                code = scanner.nextLine().toUpperCase();
            }
        }

        JSONObject json = new JSONObject(rates);

        System.out.println("Enter result currency code (ex. USD, GEL, EUR...):");
        String resCode = scanner.nextLine().toUpperCase();
        double data = 0;
        while (data == 0) {
            try {
                data = json.getJSONObject("data").getDouble(resCode);
            } catch (Exception e) {
                System.out.println("No such code available, please try again:");
                resCode = scanner.nextLine().toUpperCase();
                if (resCode.length() != 3) {
                    System.out.println("Please enter a valid currency code!");
                    resCode = "";
                }
            }
        }

        System.out.println("Enter the amount of value you would like to convert:");
        double value = scanner.nextDouble();
        System.out.println(code + " " + value + " to " + resCode + " is " + String.format("%.2f", value * data));

    }

    private static String getRates(String code) {
        try {
            URL request = new URL(url + code);
            StringBuilder data = new StringBuilder();
            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    data.append(line);
                    line = reader.readLine();
                }
                reader.close();
                return data.toString();
            }
            else throw new Exception();
        }
        catch (Exception e) {
            return "0";
        }
    }

}
