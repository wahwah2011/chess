package net;

import model.AuthData;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {

    private HttpURLConnection openConnection(String urlString, String requestMethod, String authToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("authorization", authToken);
        connection.setDoOutput(!requestMethod.equals("GET"));

        return connection;
    }

    private StringBuilder readResponse(InputStream responseStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response;
    }

    public String doPost(String baseURL, String endpointPath, String jsonInputString, String authToken) throws IOException {
        String urlString = baseURL + endpointPath;
        HttpURLConnection connection = openConnection(urlString, "POST", authToken);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        InputStream responseStream = (status == HttpURLConnection.HTTP_OK) ? connection.getInputStream() : connection.getErrorStream();
        StringBuilder response = readResponse(responseStream);
        connection.disconnect();

        return response.toString();
    }

    public String doDelete(String baseURL, String endpointPath, String authToken) throws IOException {
        String urlString = baseURL + endpointPath;
        HttpURLConnection connection = openConnection(urlString, "DELETE", authToken);

        int status = connection.getResponseCode();
        InputStream responseStream = (status == HttpURLConnection.HTTP_OK) ? connection.getInputStream() : connection.getErrorStream();
        StringBuilder response = readResponse(responseStream);
        connection.disconnect();

        return response.toString();
    }

    public String doGet(String baseURL, String endpointPath, String authToken) throws IOException {
        String urlString = baseURL + endpointPath;
        HttpURLConnection connection = openConnection(urlString, "GET", authToken);

        int status = connection.getResponseCode();
        InputStream responseStream = (status == HttpURLConnection.HTTP_OK) ? connection.getInputStream() : connection.getErrorStream();
        StringBuilder response = readResponse(responseStream);
        connection.disconnect();

        return response.toString();
    }

    public String doPut(String baseURL, String endpointPath, String authToken, String jsonInputString) throws IOException {
        String urlString = baseURL + endpointPath;
        HttpURLConnection connection = openConnection(urlString, "PUT", authToken);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        InputStream responseStream = (status == HttpURLConnection.HTTP_OK) ? connection.getInputStream() : connection.getErrorStream();
        StringBuilder response = readResponse(responseStream);
        connection.disconnect();

        return response.toString();
    }
}
