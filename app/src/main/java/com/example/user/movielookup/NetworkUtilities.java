package com.example.user.movielookup;

//This class is for retrieving data(json) from the url and returning data.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class
NetworkUtilities {

    public static String GetJson(String[] string) throws IOException {
            String data="",line="";
            URL jasonUrl= new URL(string[0]);
            HttpURLConnection httpURLConnection= (HttpURLConnection) jasonUrl.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            while (line!=null){
                line=bufferedReader.readLine();
                data=data+line;
            }
            return data;
    }

}