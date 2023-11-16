//import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

//import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IndexHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        System.out.println("hola");
        if ("GET".equals(requestMethod)) {
            System.out.println("holaGET");

            String rutaArchivo = "../html/index.html";
            String pagina_html = ""; //TODO

            try (FileReader fileReader = new FileReader(rutaArchivo);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                StringBuilder contenido = new StringBuilder();
                String linea;

                // Lee cada línea del archivo y agrega al StringBuilder
                while ((linea = bufferedReader.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }

                // Asigna el contenido del StringBuilder al string "pagina_html"
                pagina_html = contenido.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println(pagina_html);
            
            Server.response(exchange,200,pagina_html);
            //Server.response(exchange,200,"<!DOCTYPE html><html><head><title>Java Web App</title></head><body><h1>Hello, World! This is a Java Web Appe."+Server.getDia(1)+"</h1></body></html>"); // 200 es "OK"
        }
    }

}