package net;

import java.io.*;
import java.net.*;
public class ClientCommunicator {

    public String doPost(String baseURL, String endpointPath, String jsonInputString) throws IOException {
        System.out.println("Entered doPost");
        String urlString = baseURL + endpointPath;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();
        System.out.println("commection connected");

        // Write the JSON input string to the output stream
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        InputStream responseStream;

        // Check if the response code is HTTP OK
        if (status == HttpURLConnection.HTTP_OK) {
            responseStream = connection.getInputStream();
        } else {
            responseStream = connection.getErrorStream();
        }

        // Read the response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } finally {
            connection.disconnect();
        }

        // Return the response as a JSON string
        return response.toString();
    }
}
