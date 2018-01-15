package com.example.tharani.movieapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

//created class Movie_Handler
public class Movie_Handler {
    private static final String TAG = Movie_Handler.class.getSimpleName();

    public Movie_Handler() {
    }
    //making service call which request url
    public String makeServiceCall(String reqUrl){
        String response = null;//giving null
        try {//taking try block
            URL url = new URL(reqUrl);//creating new URL object
            /*
             HttpURLConnection will follow up to five HTTP redirects.
             It will follow redirects from one origin server to another
             */
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            // Reading the response
            InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
            //creating new BufferedInputStream object ,class is used to read information from stream
            response = convertStreamToString(inputStream);
        } catch (MalformedURLException e) {//exception which cant handle in try block will handle in catch block
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
             /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
        } catch (ProtocolException e) {//catches ProtocolException
            Log.e(TAG, "ProtocolException: " + e.getMessage());
            //taking log for protocolException
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());//taking log for IoException
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());//taking log for Exception
        }
        return response;//returns response
    }
    /** To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * * and returned as String*/
    private String convertStreamToString(InputStream inputS) {
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputS));
        //creating new BufferReader object
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {//taking try block
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');//taking while loop
            }
        } catch (IOException e) {//catches IOException which is not handled in try block
            e.printStackTrace();//where "e" is the exception. It prints the stack trace

        } finally {
            /*try block is followed by catch or finally block
            *  finally block is a block that is used to execute important code such as closing connection, stream etc*/
            try {
                inputS.close();
            } catch (IOException e) {
                e.printStackTrace();// It prints several lines in the output console
            }
        }

        return stringBuilder.toString();//returning stringBuilder
    }
}
