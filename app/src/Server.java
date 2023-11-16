import com.sun.net.httpserver.HttpServer;
//import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.net.InetSocketAddress;
//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.util.Map;
//import java.util.ArrayList;
//import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;

public class Server {

    private static Connection connection = null;

    public static void response(HttpExchange exchange, int code, String response){
        try {
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDia(int id_dia) {
        ResultSet resultSet = null;

        String nombre_dia = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Dia WHERE id_dia=?");
            statement.setInt(1, id_dia);
            resultSet = statement.executeQuery();

            nombre_dia =  resultSet.getString("nombre");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return nombre_dia;
    } 

    public static void main(String[] args) throws IOException {

        // Create a SQL connection
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:../db/gimnasio.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create an HTTP server that listens on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Create a context for the user path
        server.createContext("/", new IndexHandler());

        // Start the server
        server.start();
    }
}
