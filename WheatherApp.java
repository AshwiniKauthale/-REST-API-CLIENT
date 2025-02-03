
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {

    private static final String API_KEY = "your_api_key_here"; // Replace with your OpenWeatherMap API key
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    // Method to fetch weather data for a given city
    public static String getWeatherData(String city) {
        try {
            // Construct the full API URL with the city name and API key
            String urlString = BASE_URL + city + "&appid=" + API_KEY + "&units=metric"; // units=metric for Celsius
            URL url = new URL(urlString);

            // Open connection and send GET request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get response code to ensure the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return "Error: Unable to fetch weather data.";
            }

            // Read the response using a BufferedReader
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Return the response as a String
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // Method to display weather data in a structured format
    public static void displayWeather(String jsonResponse) {
        try {
            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract main weather data
            String cityName = jsonObject.getString("name");
            JSONObject mainData = jsonObject.getJSONObject("main");
            double temperature = mainData.getDouble("temp");
            int humidity = mainData.getInt("humidity");

            // Extract weather condition data
            JSONObject weatherData = jsonObject.getJSONArray("weather").getJSONObject(0);
            String description = weatherData.getString("description");

            // Print the weather data in a structured format
            System.out.println("Weather Data for: " + cityName);
            System.out.println("----------------------------------");
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Description: " + description);
            System.out.println("----------------------------------");
        } catch (Exception e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Ask user for the city name
        System.out.print("Enter the city name: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String city = scanner.nextLine();
        scanner.close();

        // Fetch the weather data
        String weatherData = getWeatherData(city);

        // Display the fetched weather data in structured format
        if (!weatherData.startsWith("Error")) {
            displayWeather(weatherData);
        } else {
            System.out.println(weatherData);
        }
    }
}
