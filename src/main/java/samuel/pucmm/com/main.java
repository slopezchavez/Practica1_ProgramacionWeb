package samuel.pucmm.com;

import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;

public class main {
    public static void main(String args[]) throws Exception {
        int numInputType = 1;
        int numForm = 1;

        System.out.println("Ingrese URL:");

        Scanner screenReader = new Scanner(System.in);
        String urlInputted = screenReader.next();




        Document doc = Jsoup.connect(urlInputted).get();

        Connection.Response responseFromWebsite = Jsoup.connect(urlInputted).execute();
        String bodyOfPage = responseFromWebsite.body();
        System.out.println("Cantidad de lineas :" +  bodyOfPage.split("\n").length);

        System.out.println("Cantidad de parrafos (p): "+ doc.getElementsByTag("p").size());

        System.out.println("Cantidad de imagenes en parrafos: " + doc.select("p").select("img").size());

        System.out.println("Cantidad de formularios con el metodo get: "+ doc.select("form[method=get]").size());

        System.out.println("Cantidad de formularios con el metodo post: "+ doc.select("form[method=post]").size());

        System.out.println("Formulario " + "\t\tInput Name" + "\t\t" + "Input Type");
        for(Element formulario : doc.select("form")){
            for(Element inputsDelForm: formulario.getElementsByTag("input")){
                System.out.println("Form " + numForm + "\t\t\t"+ inputsDelForm.attr("name")+ "\t\t\t" + inputsDelForm.attr("type"));
                numInputType++;
            }
            numForm++;
        }


        numForm = 1;
        Document newDoc;

        System.out.println("\nEnviando peticion al servidor...");
        System.out.println("+*******+\n");
        for (Element formulario: doc.getElementsByTag("form")) {
            Elements allFormPost = formulario.getElementsByAttributeValueContaining("method", "post");
            if(allFormPost.isEmpty()){
                System.out.println("No existe ningun formulario con el metodo POST.");
            }
            for (Element formPost : allFormPost) {
                try {
                    System.out.println("Esperando Respuesta...");
                    String URLAbsoluta = formPost.absUrl("action");
                    newDoc = Jsoup.connect(URLAbsoluta).data("asignatura", "practica1")
                            .header("matricula", "20161542").post();


                    System.out.println("Formulario " + numForm + " :");
                    System.out.println("+*****+\n");
                    System.out.println(newDoc.body().toString());
                    System.out.println("+**Formulario Termino**+\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            numForm++;
        }

    }
}
