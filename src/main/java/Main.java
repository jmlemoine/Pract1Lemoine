import org.jsoup.Connection;
import java.io.IOException;
import org.jsoup.Jsoup;
import java.util.Scanner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

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

        //Se conecta a la página mediante la librería jsoup, creando una conexión HTTP.
        Connection.Response conexion = Jsoup.connect(pagina).execute();

        // a) Indicar la cantidad de líneas del recurso retornado.
        // body(): Se obtiene el cuerpo del recurso como un string.
        // length: Se muestra el tamaño del cuerpo despues de la función split("\n") haber divido el cuerpo por cada salto de línea.
        String cuerpo = conexion.body();
        int cantlineas = cuerpo.split("\n").length;

        System.out.println("\na) La cantidad de líneas del recurso retornado es: " + cantlineas );


        // b) Indicar la cantidad de párrafos (p) que contiene el documento HTML.
        // get(): obtiene el documento HTML de una URL dada.
        // connect(): se conecta a la página al igual que como se utilizó para ejeuctar una conexión con execute() anteriormente.
        // getElementsByTag("p"): obtiene todos los elementos que tengan una etiqueta <p>,
        // size(): se obtiene cuantos hay en ese arreglo de elementos.
        Document docHTML = Jsoup.connect(pagina).get();
        int cantparrafos = docHTML.getElementsByTag("p").size();
        System.out.println("\nb) La cantidad de párrafos que contiene el documento HTML es: " + cantparrafos + "\n");

        // c) Indicar la cantidad de imágenes (img) dentro de los parrafos del HTML
        // select(): filtra seleccionando desde un elemento en específico, en este caso los parrafos y busca dentro de ellos img que es la etiqueta de imágenes.
        System.out.print("C) Cantidad de imágenes dentro de los párrafos del HTML: " + docHTML.select("p img").size());


        // d) Indicar la cantidad de formularios (form) que contiene el HTML categorizando por el metodo POST o GET.
        // form.attr: facilita para poder tomar un atributo que tenga un form.
        // equalsIgnoreCase: facilita para poder ver a qué String está igualado sin tomar en cuenta mayúsculas y minúsculas.
        // FormElement: para los formularios.
        // getElementsByTag: obtiene todos los elementos con el nombre de la etiqueta específica.
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



        // e) Para cada formulario mostrar los campos de tipo input y su respectivo tipo que contiene en el documento HTML.
        System.out.println("\n\ne) Inputs dentro de los forms con sus respectivos tipos: ");
        System.out.println("Número del formulario\t| Número del input\t| Tipo del input");
        System.out.println("---------------------------------------------------------");
        for(FormElement form : docHTML.getElementsByTag("form").forms()){
            for(Element inputsEncontrados: form.getElementsByTag("input")){
                System.out.println("  Formulario #" + countform + "\t\t\t\tInput #" + continput + "\t\t\t" + inputsEncontrados.attr("type"));
                continput++;
            }
            countform++;
        }





        // f) ) Para cada formulario “parseado”, identificar que el método de envío del formulario sea POST y enviar una petición al servidor con el
        // parámetro llamado asignatura y valor practica1 y un header llamado matricula con el valor correspondiente a matrícula asignada.
        // formulario.getElementsByAttributeValueContaining: permite mostrar un parametro que tenga el for y a quá valor es igualado.
        // elementosPOST.absUrl("action"): permite mostrar la URL absoluta del parámetro que se le pase, es decir,
        // Si el valor del atributo ya es absoluto (es decir, comienza con un protocolo, como http: // o https: //, etc.) y analiza correctamente como una URL,
        // el atributo se devuelve directamente. De lo contrario, se trata como una URL relativa al elemento.
        // .data("asignatura", "practica1).header("matricula", "20140795").post(): permite enviar una solicitud de tipo POST con esos parametros y ese header.
        // documentoNuevoAImprimir.body().toString(): primero muestra el cuerpo de la petición hecha y luego lo cambia a string.\
        // try { } catch { }: sirve en caso de que se encuentre un error que haga que no se salga el programa o cause un error si no que lo muestre en la consola.
        // isEmpty() --> te dice si el arreglo de elementos está vacio o no, se uso para imprimir un mensaje en caso de que lo esté.
        System.out.println("\n\nf) La peticion al servidor con los parametros y el header dado:");
        for (Element formulario: docHTML.getElementsByTag("form").forms()) {
            Elements tiposdeMetodosQueSeanPost = formulario.getElementsByAttributeValueContaining("method", "post");
            if(tiposdeMetodosQueSeanPost.isEmpty()){
                System.out.println("No hay ningún form con el metodo POST.");
            }
            for (Element elementosPOST : tiposdeMetodosQueSeanPost) {
                try {
                    System.out.println("El formulario # " + contadorDeFormularios + " encontrado:");
                    String conseguirURLAbsoluta = elementosPOST.absUrl("action");
                    documentoNuevoAImprimir = Jsoup.connect(conseguirURLAbsoluta).data("asignatura", "practica1")
                            .header("matricula", "20140795").post();
                    System.out.println("Resultado:");
                    System.out.println("\n\n");
                    System.out.println(documentoNuevoAImprimir.body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            contadorDeFormularios++;
        }

    }

}
