package sample.utils;

import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class ConnAPI {

    private static HttpURLConnection conn;
    
    /**
     *  ConnAPI()
         Crea una nueva conexion con la API, recibe la ruta al endpoint y el metodo con el
         que se hara la llamada (GET, POST, ETC...). Dependiendo del valor de "isTest" conectará con
         HEROKU o con LOCALHOST
     * @param endpoint
     * @param method
     * @param isTest
     */
    public ConnAPI(String endpoint, String method, boolean isTest){

        try{
            URL url = null;
            if (!isTest) {url = new URL(Data.API_URL + endpoint);}
            else        {url = new URL(Data.LOCALHOST + endpoint);}

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     *  establishConn()
         Para conectar con la API, debe ponerse siempre al final a la hora de ejecutar la conexion
         en alguna parte del programa.
     */
    public void establishConn(){
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setData()
         Método para pasar la información a la API, este recibe un JSONObject ya hecho y se encarga de enviarselo
         a la API para que pueda leerlo.
     * @param jsonObject
     */
    public void setData(JSONObject jsonObject){
        conn.setRequestProperty("Content-Type","application/json");
        conn.setDoOutput(true);

        try {
            String dataJSON = jsonObject.toString();
            byte[] outputInBytes = dataJSON.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write( outputInBytes );
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getDataJSON()
         Obtiene la informacion obtenida por la API en formato JSON
     * @return
     */
    public JSONObject getDataJSON(){

         

        String rawData = "";

        try {
            Scanner sc = new Scanner(conn.getInputStream());
            while (sc.hasNext()){
                rawData += sc.nextLine();
            }

            JSONObject jsonObject = new JSONObject(rawData);
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * getStatus()
         Devuevle el STATUS de la conexion con la API
     * @return
     */
    public int getStatus(){

         

        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void closeConn(){
        conn.disconnect();
    }

}
