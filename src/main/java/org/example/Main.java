package org.example;

import java.net.*;
import java.io.*;
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
    public static void main(String[] args) throws IOException {
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
            if(uriStr.startsWith("/Cliente")){
                outputLine = httpClientHtml();
            }else if (uriStr.startsWith("/Busqueda") && uriStr.length() > 12){  // Se asegura de que la uri no tenga busqueda vacia
                outputLine = cacheSearch(uriStr);
            }
            else {
                outputLine = httpError();
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
        response.append("HTTP/1.1 200 OK\r\n" + "Content-Type:application/json\r\n" + "\r\n");
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
    public static String httpClientHtml() {
        return "HTTP/1.1 200 OK\r\n" //encabezado necesario
                + "Content-Type:text/html\r\n"
                + "\r\n" //retorno de carro y salto de linea
                + "<!DOCTYPE html>"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>MoviesSearch</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "     <style>\n" +
                "        h1 {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        form {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        input[type=\"text\"] {\n" +
                "            display: inline-block;\n" +
                "            width: 300px;\n" +
                "            margin: 10px auto;\n" +
                "        }\n" +
                "        input[type=\"button\"] {\n" +
                "            display: inline-block;\n" +
                "            margin: 10px auto;\n" +
                "        }\n" +
                "    </style>"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Busca información de tu película favorita</h1>\n"
                + "        <form action=\"/Busqueda\">\n"
                + "            <label for=\"name\">Name:</label><br>\n"
                + "            <input type=\"text\" id=\"name\" name=\"t\" value=\"John\"><br><br>\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                + "        </form> \n"
                + "        <div id=\"getrespmsg\"></div>\n"
                + "\n"
                + "        <script>\n"
                + "            function loadGetMsg() {\n"
                + "                let nameVar = document.getElementById(\"name\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    var jsonResponse = JSON.parse(this.responseText);"
                + "                if (jsonResponse.Response === \"False\") {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML = \"No information found for the movie.\";\n"
                + "                } else {"
                + "                    var htmlContent = \"<b>Title: </b>\" + jsonResponse.Title + \"<br>\";"
                + "                    htmlContent += \"<b> Year: </b>\" + jsonResponse.Year + \"<br>\";"
                + "                    htmlContent += \"<b> RatedRated: </b>\" + jsonResponse.Rated + \"<br>\";"
                + "                    htmlContent += \"<b> Released: </b>\" + jsonResponse.Released + \"<br>\";"
                + "                    htmlContent += \"<b> Runtime: </b>\" + jsonResponse.Runtime + \"<br>\";"
                + "                    htmlContent += \"<b> Genre: </b>\" + jsonResponse.Genre + \"<br>\";"
                + "                    htmlContent += \"<b> Director: </b>\" + jsonResponse.Director + \"<br>\";"
                + "                    htmlContent += \"<b> Writer: </b>\" + jsonResponse.Writer + \"<br>\";"
                + "                    htmlContent += \"<b> Actors: </b>\" + jsonResponse.Actors + \"<br>\";"
                + "                    htmlContent += \"<b> Plot: </b>\" + jsonResponse.Plot + \"<br>\";"
                + "                    htmlContent += \"<b> Language: </b>\" + jsonResponse.Language + \"<br>\";"
                + "                    htmlContent += \"<b> Country: </b>\" + jsonResponse.Country + \"<br>\";"
                + "                    htmlContent += \"<b> Awards: </b>\" + jsonResponse.Awards + \"<br>\";"
                // Lista
                + "                    htmlContent += '<b> Ratings: </b>' + formatRanking(jsonResponse.Ratings) + '<br>';"
                + "                    htmlContent += \"<b> Metascore: </b>\" + jsonResponse.Metascore + \"<br>\";"
                + "                    htmlContent += \"<b> imdbRating: </b>\" + jsonResponse.imdbRating + \"<br>\";"
                + "                    htmlContent += \"<b> imdbVotes: </b>\" + jsonResponse.imdbVotes + \"<br>\";"
                + "                    htmlContent += \"<b> imdbID: </b>\" + jsonResponse.imdbID + \"<br>\";"
                + "                    htmlContent += \"<b> imdbVotes: </b>\" + jsonResponse.imdbVotes + \"<br>\";"
                + "                    htmlContent += \"<b> Type: </b>\" + jsonResponse.Type + \"<br>\";"
                + "                    htmlContent += \"<b> DVD: </b>\" + jsonResponse.DVD + \"<br>\";"
                + "                    htmlContent += \"<b> BoxOffice: </b>\" + jsonResponse.BoxOffice + \"<br>\";"
                + "                    htmlContent += \"<b> Production: </b>\" + jsonResponse.Production + \"<br>\";"
                + "                    htmlContent += \"<b> Website: </b>\" + jsonResponse.Website + \"<br>\";"
                + "                    htmlContent += \"<b> Response: </b>\" + jsonResponse.Response + \"<br>\";"
                // Cartel de la pelicula
                + "                    htmlContent += \"<b> Poster: </b>\" + \"<img src=\" + jsonResponse.Poster + \"> <br>\";"
                + "                    htmlContent += \"<b> Info: </b>\"  +  JSON.stringify(jsonResponse) + \"<br>\";"

                + "                    document.getElementById(\"getrespmsg\").innerHTML = htmlContent \n"
                + "                  }\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/Busqueda?t=\"+nameVar);\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "     function formatRanking(ranking) {\n" +
                "        let html = '<table border=\"1\">';\n" +
                "        html += '<tr><th>Source</th><th>Value</th></tr>';\n" +
                "\n" +
                "        for (let i = 0; i < ranking.length; i++) {\n" +
                "            html += '<tr>';\n" +
                "            html += '<td>' + ranking[i].Source + '</td>';\n" +
                "            html += '<td>' + ranking[i].Value + '</td>';\n" +
                "            html += '</tr>';\n" +
                "        }\n" +
                "\n" +
                "        html += '</table>';\n" +
                "        return html;\n" +
                "    }"
                + "        </script>\n"
                + "\n"
                + "    </body>\n"
                + "</html>";
    }

}