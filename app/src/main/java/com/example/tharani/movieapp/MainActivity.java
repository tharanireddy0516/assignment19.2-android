package com.example.tharani.movieapp;
/*import is libraries imported for writing the code
* AppCompatActivity is base class for activities
* Bundle handles the orientation of the activity
*/
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    /*onCreate is the first method in the life cycle of an activity
   savedInstance passes data to super class,data is pull to store state of application
 * setContentView is used to set layout for the activity
 *R is a resource and it is auto generate file
 * activity_main assign an integer value*/
    // Declaring variables
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    ArrayList<HashMap<String, String>> movieList;
    //JSON Node Names
    private static final String TAG_NAME = "name";
    private static final String TAG_VOTES = "vote_count";
    private static final String TAG_ID = "id";
    private static final String TAG_RESULTS = "results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList=new ArrayList<>();//creating object
        lv=(ListView)findViewById(R.id.list);//assigning unique Id
        new GetMovieLists().execute();//getting MovieList
    }
    /**creating private class GetMovieLists which extends AsyncTask
     *  AsyncTask is an abstract class provided by Android which gives us the liberty to perform heavy tasks in the background and
     *  keep the UI thread light thus making the application more responsive.
     *  onPreExecute:This method contains the code which is executed before the background processing starts
     *  doInBackground: This method contains the code which needs to be executed in background.
     *  In this method we can send results multiple times to the UI thread by publishProgress() method*/
    private class GetMovieLists extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();// super() can be used to invoke immediate parent class constructor
            pDialog=new ProgressDialog(MainActivity.this);//creating new progressDialog object giving reference using this keyword
            pDialog.setMessage("Please wait...");//displays Dialog Please wait
            pDialog.setCancelable(false);//false
            pDialog.show();//shows
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "http://api.themoviedb.org/3/tv/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";
            // representing a Uniform Resource Locator, a pointer to a "resource" on the World Wide Web,taking API link to display Movie information
            Movie_Handler movieHandler = new Movie_Handler();
            //created new object Movie_Handler using new keyword
            String handlerStr=movieHandler.makeServiceCall(url);
            //make service call
            Log.e(TAG, "Response from url: " + handlerStr);
             /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
            if (handlerStr != null) {//taking if statement to check weather the handlerStr is null or not
                try {//taking try block
                    JSONObject jsonObj = new JSONObject(handlerStr);
                    Log.e(TAG,"object of json" +jsonObj);
                     /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
                    Log.e(TAG,"object of json page" +jsonObj);

                    JSONArray jsonArray_results = jsonObj.getJSONArray(TAG_RESULTS);
                    Log.e(TAG,"object of json results" +jsonArray_results);
                     /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/

                    for (int i = 0; i <= jsonArray_results.length(); i++) {//taking for loop

                        JSONObject jsonObject = jsonArray_results.getJSONObject(i);
                        Log.e(TAG,"object of json in for loop" +jsonObject);
                         //taking TAG_NAME,TAG_ID,TAG_VOTES in string
                        String movie_name = jsonObject.getString(TAG_NAME);
                        String id = jsonObject.getString(TAG_ID);
                        String vote_count = jsonObject.getString(TAG_VOTES);
                        //Hash table based implementation of the Map interface.
                        // This implementation provides all of the optional map operations, and permits null values and the null key
                        HashMap<String, String> results = new HashMap<>();
                        results.put(TAG_ID, id);//results of ID
                        Log.e(TAG,"id" +id);
                         /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
                        results.put(TAG_NAME, movie_name);
                        //taking log for movie nae,vote count
                        Log.e(TAG,"movie name" +movie_name);
                        results.put(TAG_VOTES, vote_count);
                        Log.e(TAG,"vote count" +vote_count);

                        movieList.add(results);//adding results for movieList

                    }
                } catch (JSONException e) {//catches the exception which occurs in try block
                    e.printStackTrace();// It prints several lines in the output console
                }
            }
            return null;//returns null
        }

        @Override
        /**AsyncTask enables proper and easy use of the UI thread. This class allows you to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
         * onPostExecute invoked on the UI thread after the background computation finishes. The result of the background computation is passed to this step as a parameter.*/
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            // Dismissing the progress dialog
            if (pDialog.isShowing())//taking if statement to showing check weather the statement is true or false
                pDialog.dismiss();//dismiss

            //Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, movieList,
                    R.layout.list_item, new String[]{"name", "id",
                    "vote_count"}, new int[]{R.id.name,
                    R.id.vote_count, R.id.id});
            //Common base class of common implementation for an Adapter that can be used in both ListView (by implementing the specialized ListAdapter interface)

            lv.setAdapter(adapter);//In order to display items in the list, call setAdapter(ListAdapter) to associate an adapter with the list
        }
    }
}
