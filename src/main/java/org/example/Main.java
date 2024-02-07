package org.example;

import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author Jordy Bautista
 */
public class Main {

    private static HashMap<String, String> cache = new HashMap<String, String>();   // Key: Movie name and Value: Info about the movie

    /**
     * Constructor
     * @param args por defecto
     * @throws IOException Esta clase es la clase general de excepciones producidas por operaciones de E/S fallidas o interrumpidas.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String uriStr ="";

            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            if (uriStr.startsWith("/Busqueda") && uriStr.length() > 12){  // Se asegura de que la uri no tenga busqueda vacia
                outputLine = cacheSearch(uriStr);
            } else {
                try {
                    outputLine = httpClientHtml(new URI(uriStr));
                } catch (Exception e) {
                    outputLine = httpError();
                }


            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Este metodo retorna la informacion de la pelicula que se consulte al servidor, para devolver esta informacion
     * busca en el cache y en caso de no tener registro de la pelicula, realiza la busqueda en el API publico y almacena
     * la informacion relacionada con esa pelicula
     * @param uriStr: url de la peticion que recibe el servidor
     * @return Informacion de la pelucula
     * @throws IOException Esta clase es la clase general de excepciones producidas por operaciones de E/S fallidas o interrumpidas.
     */
    public static String cacheSearch(String uriStr) throws IOException {
        String nameMovie = uriStr.substring(12).toLowerCase();          // de la Uri Obtiene el nombre de la pelicula
        if (!cache.containsKey(nameMovie)){
            System.out.println("No se encontro en el cache");
            uriStr =  makeRequest("http://www.omdbapi.com/?apikey=b1060e61&t=" + nameMovie);
            cache.put(nameMovie, uriStr);
            return uriStr;
        } else {
            System.out.println("Se encontro en el cache");
            return cache.get(nameMovie);
        }
    }

    /**
     * Este metodo realiza una petición get a la URL que se le proporcione y retorna la respuesta
     * @param url: Es la URL de la pagina a la cual se le desea realizar la petición
     * @return Es la respuesta de la peticion realizada en formato de String
     * @throws IOException Esta clase es la clase general de excepciones producidas por operaciones de E/S
     * fallidas o interrumpidas.
     */
    public static String makeRequest(String url) throws IOException {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        StringBuffer response = new StringBuffer();
        // Encabezado necesario en todas las peticiones
        response.append("HTTP/1.1 200 OK\r\n"
                + "Content-Type:application/json\r\n"
                + "Access-Control-Allow-Origin: *\r\n"
                + "\r\n");
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");

        return response.toString();
    }

    /**
     * Este metodo retorna una pagian vacia de HTML en caso de no consultar la URL apropiada
     * @return String de una pagina web vacia
     */
    public static String httpError() {
        return "HTTP/1.1 400 Not found\r\n" //encabezado necesario
                + "Content-Type:text/html\r\n"
                + "\r\n" //retorno de carro y salto de linea
                + "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>\n";
    }

    /**
     * Este metodo retorna una pagina de HTML con la cual se pueden realizar busquedas de peliculas y la informacion
     * @return String de una pagina web de busqueda
     */
    public static String httpClientHtml(URI requestedUri) throws IOException {

        File fileSrc = new File(requestedUri.getPath());
        String fileType = Files.probeContentType(fileSrc.toPath());

        Path file = Paths.get("target/classes/public" + requestedUri.getPath());
        String outputLine =  "HTTP/1.1 200 OK\r\n"
                + "Content-Type:" + fileType + "\r\n"
                + "\r\n"; // Necesario para los nuevos navegadores

        Charset charset = StandardCharsets.UTF_8;
        BufferedReader reader = Files.newBufferedReader(file, charset);
        String line = null;
        while ((line = reader.readLine()) != null){
            System.out.print(line);
            outputLine = outputLine + line;
        }

        return outputLine;
    }

}