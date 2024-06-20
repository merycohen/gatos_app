package org.example;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GatosService {

    private static String BASE_URL="https://api.thecatapi.com/v1/";
    private static String SEARCH_ENDPOINT = BASE_URL+"images/search";
    private static String FAVORITE_ENDPOINT=  BASE_URL+ "favourites";
    private static String FavoriteMenu = "Opciones Menú de Favoritos: \n"
                                        + "1. ver otro gato favorito \n"
                                        + "2. Eliminar Favorito \n "
                                        + "3. Volver \n";
    private static String ramdonCastMenu = "Opciones Principal: \n"
                                        + "1. ver otra imagen \n"
                                        + "2. Favorito \n "
                                        + "3. Volver \n";


        public static void verGatitos() throws IOException {

            //1. vamos a traer los datos de la api, codigo postman
              OkHttpClient client = new OkHttpClient();
              Request request = new Request.Builder()
                      .url(SEARCH_ENDPOINT)
                      .get().build();
              Response response = client.newCall(request).execute();
              //Guardar la respuesta en un string, eso es el jason que arroja postman
              String elJson = response.body().string();
              if (!response.isSuccessful()){
                     response.body().close();
              }
              //Vamos a cortar los corchetes de apertura y cierre del json
              elJson = elJson.substring(1, elJson.length());//cortar 1er corchete, primera posición del string
              elJson = elJson.substring(0, elJson.length() - 1);//cortar el ultimo corchete, ultima posición del string

              //Crear un objeto de la clase Json, sirve para manejar objetos tipo json
              Gson gson = new Gson();
              //convierto el jason en la clase Gato
              System.out.println("Json "+elJson);
              Gato gato = gson.fromJson(elJson, Gato.class);

              //Redimensionar la imagen en caso de que llegue muy grande
              Image image = null;
              try {
                  URL url = new URL(gato.getUrl());
                  image = ImageIO.read(url);
                  ImageIcon fondoGato = new ImageIcon(image);
                  if (fondoGato.getIconWidth() > 800) {
                      Image fondo = fondoGato.getImage();
                      Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                      fondoGato = new ImageIcon(modificada);
                  }
                  String[] botones = {"ver otra imagen", "Favorito", "Volver"};
                  String id_gato = gato.getId();
                  String opcion = (String) JOptionPane.showInputDialog(null, ramdonCastMenu,
                          id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]
                  );
                  int seleccion = -1;
                  for (int i = 0; i < botones.length; i++) {
                      if (opcion.equals(botones[i])) {
                          seleccion = i;
                      }
                  }
                  switch (seleccion) {
                      case 0:
                           verGatitos();
                           break;
                      case 1:
                          System.out.println("ID del gato que voy a asignar favorito"+id_gato);
                          AsignarfavoritoGatito(gato);
                          break;
                      default:
                          break;
                  }

              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
        }


    public static void AsignarfavoritoGatito(Gato gato) {

            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{ \n\t\"image_id\":\""+gato.getId()+"\"\n}");
                Request request = new Request.Builder()
                        .url(FAVORITE_ENDPOINT)
                        .post (body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("x-api-key", gato.getApikey())
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()){
                    response.body().close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public  static void verFavoritos(String apikey) throws IOException {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT)
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", apikey)
                    .build();

            Response response = client.newCall(request).execute();

            //Guardamos el string con la respuesta
            String elJason = response.body().string();
            System.out.println("Json"+elJason);
            if (!response.isSuccessful()){
                response.body().close();
            }
            //creamos el objeto Json
            Gson gson = new Gson();
            //En el arreglo metemos los json de los gatos favoritos
            GatoFavorito[] gatosFavoritos = gson.fromJson(elJason, GatoFavorito[].class);
            System.out.println("Tamaño Arreglo de gatos favoritos"+ gatosFavoritos.length);

            //si el tamaño del arreglo es mayor que cero quiere decir que hay gatos en el arreglo
            if (gatosFavoritos.length > 0) {
                //generamos un numero aletaorio entre 1 y el tamaño del arreglo, recuerda que hay que restar uno
                //para obtener el indice correcto
                int min = 1;
                int max = gatosFavoritos.length;
                int aleatorio = (int) (Math.random() * ((max - min) + 1)) + min;
                int indice = aleatorio - 1;
                System.out.println("gato aleatorio"+ indice);

                GatoFavorito elgatoFavorito = gatosFavoritos[indice];

                //Redimensionar la imagen en caso de que llegue muy grande
                Image image = null;
                try {
                    URL url = new URL(elgatoFavorito.image.getUrl());
                    image = ImageIO.read(url);
                    ImageIcon fondoGato = new ImageIcon(image);
                    if (fondoGato.getIconWidth() > 800) {
                        Image fondo = fondoGato.getImage();
                        Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                        fondoGato = new ImageIcon(modificada);

                    };

                    String[] botones = {"Ver otro Gato Favorito", "Eliminar Favorito", "Volver"};
                    String id_gato = elgatoFavorito.getId();
                    String opcion = (String) JOptionPane.showInputDialog(null, FavoriteMenu,
                            id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]
                    );
                    int seleccion = -1;
                    for (int i = 0; i < botones.length; i++) {
                        if (opcion.equals(botones[i])) {
                            seleccion = i;
                        }
                    }
                    switch (seleccion) {
                        case 0:
                            verFavoritos(apikey);
                            break;
                        case 1:
                            EliminarfavoritoGatito(elgatoFavorito);
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (HeadlessException e) {
                    throw new RuntimeException(e);
                }
            }
    }


    private static void EliminarfavoritoGatito(GatoFavorito gatoFavorito) {
        try {

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+gatoFavorito.getId())
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatoFavorito.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()){
                response.body().close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
