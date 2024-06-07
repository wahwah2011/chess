package net;

import model.AuthData;

import java.io.*;
import java.net.*;
public class ClientCommunicator {

    public String doPost(String baseURL, String endpointPath, String jsonInputString, String authToken) throws IOException {
        String urlString = baseURL + endpointPath;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("authorization", authToken);

        connection.connect();
        System.out.println("POST connected");

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
            //System.out.println("status == HTTP_OK\n");
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
            //System.out.println("Response has been read\n");
        } finally {
            connection.disconnect();
        }

        // Return the response as a JSON string
        //System.out.println("Response: \n" + response.toString());
        return response.toString();
    }

    public String doDelete(String baseURL, String endpointPath, String authToken) throws IOException {
        String urlString = baseURL + endpointPath;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(false); // Typically DELETE requests do not have a body
        connection.setRequestProperty("authorization", authToken);

        // Connect to the server
        connection.connect();
        System.out.println("DELETE connected");

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
        //System.out.println("response: " + response.toString());
        return response.toString();
    }

    public String doGet(String baseURL, String endpointPath, String authToken) throws IOException {
        String urlString = baseURL + endpointPath;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("authorization", authToken);

        // Connect to the server
        connection.connect();
        System.out.println("GET connected");

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
        //System.out.println("Response: " + response.toString());
        return response.toString();
    }

    public String doPut(String baseURL, String endpointPath, String authToken, String jsonInputString) throws IOException {
        String urlString = baseURL + endpointPath;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("authorization", authToken);

        connection.connect();
        System.out.println("PUT connected");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        InputStream responseStream;

        if (status == HttpURLConnection.HTTP_OK) {
            responseStream = connection.getInputStream();
        } else {
            responseStream = connection.getErrorStream();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } finally {
            connection.disconnect();
        }

        System.out.println("PUT Response: " + response.toString());
        return response.toString();
    }
}
