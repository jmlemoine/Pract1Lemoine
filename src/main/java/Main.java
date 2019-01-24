import org.jsoup.Connection;
import java.io.IOException;
import org.jsoup.Jsoup;

import java.util.Scanner;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.FormElement;

public class Main {

    public static void main(String[] args) throws IOException{

        int continput = 1;
        int contadorDeFormularios = 1;
        int countform = 1;
        Document documentoNuevoAImprimir;

        //Se digita la URL
        System.out.println("Escriba la URL de la página que desee: ");
        Scanner url = new Scanner(System.in);
        String pagina = url.next();

        System.out.println("\nURL: "+pagina);

        //Se conecta a la página mediante la librería jsoup, creando una conexión HTTP.
        Connection.Response conexion = Jsoup.connect(pagina).execute();

        // a) Indicar la cantidad de líneas del recurso retornado.
        // body(): Se obtiene el cuerpo del recurso como un string plano.
        // length: Se muestra el tamaño del cuerpo despues de la función split("\n") haber divido el cuerpo por cada salto de línea.
        String cuerpo = conexion.body();
        int cantlineas = cuerpo.split("\n").length;

        System.out.println("\na) La cantidad de lineas del recurso retornado es: " + cantlineas );


        // b) Indicar la cantidad de párrafos (p) que contiene el documento HTML.
        // get(): obtiene el documento HTML de una URL dada.
        // connect(): se conecta a la página al igual que como se utilizó para ejeuctar una conexión con execute() anteriormente.
        // getElementsByTag("p"): obtiene todos los elementos que tengan una etiqueta <p>,
        // size(): se obtiene cuantos hay en ese arreglo de elementos.
        Document docHTML = Jsoup.connect(pagina).get();
        int cantparrafos = docHTML.getElementsByTag("p").size();
        System.out.println("\nb) La cantidad de parrafos <p> que contiene el documento HTML es: " + cantparrafos + "\n");

        // c) Indicar la cantidad de imágenes (img) dentro de los parrafos del HTML
        // select(): filtra seleccionando desde un elemento en específico, en este caso los parrafos y busca dentro de ellos img que es la etiqueta de imágenes.
        System.out.print("C) Cantidad de imagenes dentro de los párrafos del HTML: " + docHTML.select("p img").size());


        // d) Indicar la cantidad de formularios (form) que contiene el HTML categorizando por el metodo POST o GET.
        // form.attr: facilita para poder tomar un atributo que tenga un form.
        // equalsIgnoreCase: facilita para poder ver a qué String está igualado sin tomar en cuenta mayúsculas y minúsculas.
        // FormElement: para los formularios.
        int[] cantGetPost = new int[]{0, 0};
        for(FormElement form : docHTML.getElementsByTag("form").forms()){
            if(form.attr("method").equalsIgnoreCase("get")){
                cantGetPost[0]++;
            }
            if(form.attr("method").equalsIgnoreCase("post")){
                cantGetPost[1]++;
            }
        }

        System.out.println("\n\nd)" +
                "\n     La cantidad de formularios que usan el metodo GET: " + cantGetPost[0] +
                "\n     La cantidad de formularios que usan el metodo POST: " + cantGetPost[1]);








    }

}
