package com.example.tritontrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

public class JSONParser {
	
	/*
	 * static input stream, JSONObject and String
	 *
	 */
	
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    /* constructor */
    public JSONParser() {
 
    }

    /*
     * This function gets JSON from the URL by creating POST and GET methods
     * url: 	the domain url of the server
     * method:	either POST or GET
     * params:  variables you pass to the server
     *
     */
     public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

     	try {
 
            // check for request method
            if(method == "POST")
            {

                // request method is POST

                /* create a new DefaultHttpClient and pass in the url and pass in the params */
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
     			/* Grab the response and the content */
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
 
            }
            else if(method == "GET")
            {

                // request method is GET

                /* create a new DafaultHttpClient and pass in the url and pass in the params*/
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
 
 				/* retrieve the responce and set it to is*/
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



     	try {

     		/* try to read/parse the input stream */
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            is.close();

            json = sb.toString();

        } catch (Exception e) {
        	 //Log.e("log_tag", "Error parsing data "+e.toString());
        	    //Log.e("log_tag", "Failed data was:\n" + e.toString());
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
        	 //Log.e("log_tag", "Error parsing data "+e.toString());
        	    //Log.e("log_tag", "Failed data was:\n" + e.toString() );
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
}