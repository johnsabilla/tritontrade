package com.example.tritontrade;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Edit extends Activity {
        
        Button submit;
        EditText book_name, class_name, class_num, book_price, book_description;
        Spinner book_cond;
        String class_id, cs_name, cs_number;
        Intent i;
        JSONParser jpar_class = new JSONParser();
        JSONParser jpar_book = new JSONParser();
        JSONObject jobj_book, jobj_class ;
        JSONObject jobj_update;
        JSONParser jpar_update = new JSONParser();      
        JSONObject jobj_update_book;
        JSONParser jpar_update_book = new JSONParser();
        JSONArray jarray_book, jarray_class;
        private ProgressDialog pDialog;
        private static String url_read_class = "http://tritontrade.org/TritonTradeCRUD/find_class_by_book_id.php";
    private static String url_read_book  = "http://tritontrade.org/TritonTradeCRUD/read_book_by_bookid.php";
    private static String url_edit_book  = "http://tritontrade.org/TritonTradeCRUD/update_book_info.php" ;
    private static String url_edit_class = "http://tritontrade.org/TritonTradeCRUD/update_class_info.php" ;
    
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.edit);
                
                book_name = (EditText) findViewById(R.id.edit_Bookname);
                book_price = (EditText) findViewById(R.id.edit_price);
                book_description = (EditText) findViewById(R.id.edit_description);
                book_cond = (Spinner) findViewById(R.id.edit_condition);
                class_num = (EditText) findViewById(R.id.edit_class_num);
                class_name = (EditText) findViewById(R.id.edit_class_abb);
                
                i = getIntent();
                
                new GetInfo().execute();
                
                
                submit = (Button) findViewById(R.id.edit_sellButtonSubmit);
                submit.setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                                new UpdateInfo().execute();                     
                        }
                });
                        
        }
        
        class GetInfo extends AsyncTask<String,String,String> {

                protected void onPreExecute() {
                        
                        super.onPreExecute();
                        pDialog = new ProgressDialog(Edit.this);
                        pDialog.setMessage("Gathering Books Info...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        
                }
                protected String doInBackground(String... params) {
                                                
                        List<NameValuePair> class_values = new ArrayList<NameValuePair>();
                        class_values.add(new BasicNameValuePair("book_id",i.getStringExtra("book_id")));
                        jobj_class = jpar_class.makeHttpRequest(url_read_class, "GET", class_values);
                        
                      //  Log.d("class info: ", jobj_class.toString());
                        
                        List<NameValuePair> book_values = new ArrayList<NameValuePair>();
                        book_values.add(new BasicNameValuePair("book_id",i.getStringExtra("book_id")));
                        jobj_book = jpar_book.makeHttpRequest(url_read_book, "GET", book_values);
                    //    Log.d("book info: ", jobj_book.toString());
                        
                        runOnUiThread(new Runnable() {
                public void run() {
                        
                        try{
                                int s1 = jobj_book.getInt("success");
                                int s2 = jobj_class.getInt("success");
                                if (s1 == 1 && s2 == 1){
                                        
                                        jarray_book = jobj_book.getJSONArray("book");
                                        jarray_class = jobj_class.getJSONArray("class");
                                        
                                        JSONObject bk = jarray_book.getJSONObject(0);
                                        JSONObject cs = jarray_class.getJSONObject(0);
                                        
                                        book_name = (EditText) findViewById(R.id.edit_Bookname);
                                        book_price = (EditText) findViewById(R.id.edit_price);
                                        book_description = (EditText) findViewById(R.id.edit_description);
                                        book_cond = (Spinner) findViewById(R.id.edit_condition);
                                        class_num = (EditText) findViewById(R.id.edit_class_num);
                                        class_name = (EditText) findViewById(R.id.edit_class_abb);
                                        
                                        class_id = Integer.toString(cs.getInt("class_id"));
                                        cs_name = cs.getString("name");
                                        cs_number = String.valueOf(cs.getInt("number"));
                                        
                                        book_name.setText(bk.getString("bookname"));
                                        book_price.setText(String.valueOf(bk.getDouble("price")));
                                        book_description.setText(bk.getString("description"));
                                                                                
                                        
                                        if (bk.getInt("condition") == 3){
                                                book_cond.setSelection(0);
                                        }else if (bk.getInt("condition") == 2){
                                                book_cond.setSelection(1);
                                        }else
                                                book_cond.setSelection(2);
                                        
                                        
                                        class_name.setText(cs.getString("name"));
                                        class_num.setText(String.valueOf(cs.getInt("number")));
                                }
                                else{
                                        Intent i = new Intent(Edit.this, Homepage.class);
                                        i.putExtra("user_id",i.getStringExtra("user_id"));
                                        startActivity(i);
                                }
                        }catch (JSONException e){
                                e.printStackTrace();
                        }    
                }       
                        });                     
                        return null;
                }
                
                protected void onPostExecute(String file_url ) {
                        pDialog.dismiss();              
                }
        }
        
        class UpdateInfo extends AsyncTask<String,String,String>{
                //JSONObject jobj_update_book;
                //JSONParser parser1 = new JSONParser();
                
                protected String doInBackground(String... params) {

                        
                        String condition = book_cond.getSelectedItem().toString();
                        String condVal;
                        if(condition.equals("Perfect")){
                                condVal = "3";
                        }
                        else if(condition.equals("Good")){
                                condVal = "2";
                        }
                        else {
                                condVal = "1";
                        }
                        
                       // Log.d("old cs_name: ", cs_name);
                       // Log.d("old cs_number: ", cs_number);
                      //  Log.d("new cs name: ", class_name.getText().toString());
                      //  Log.d("new cs number: ", class_num.getText().toString());
                        
                        
                        if ( !cs_name.equals(class_name.getText().toString()) ||    
                                 !cs_number.equals(class_num.getText().toString())) {
                                
                                List<NameValuePair> update_class = new ArrayList<NameValuePair>();
                                update_class.add(new BasicNameValuePair("name", class_name.getText().toString()));
                                update_class.add(new BasicNameValuePair("number", class_num.getText().toString()));
                                update_class.add(new BasicNameValuePair("class_id", class_id));
                                
                                JSONParser parser = new JSONParser();
                                jobj_update = parser.makeHttpRequest(url_edit_class, "GET", update_class);
                                //Log.d("update_class info: ", jobj_update.toString());
                                
                                try{
                                        int success = jobj_update.getInt("success");
                                        if(success == 1){
                                                jarray_class = jobj_update.getJSONArray("class");
                                                JSONObject c = jarray_class.getJSONObject(0);
                                                class_id = Integer.toString(c.getInt("class_id"));
                                        }
                                        else if (success == 0 ){
                                                Intent i = getIntent();
                                                finish();
                                startActivity(i);
                                        }
                                        else{}
                                        
                                } catch (JSONException e){
                        e.printStackTrace();
                } 
                                
                        }
                        
                        
                        
                      //  Log.d("book-id"   ,i.getStringExtra("book_id") );
                      //  Log.d("class-id"  ,class_id );
                      //  Log.d("book-name" ,book_name.getText().toString().replace(" ", ""));
                     //   Log.d("book-des"  ,book_description.getText().toString().replace(" ", "") );
                      //  Log.d("book-price",book_price.getText().toString() );
                      //  Log.d("book-cond" ,condVal );
                        
                        
                        
                        
                        List<NameValuePair> update_book = new ArrayList<NameValuePair>();
                        
                        update_book.add(new BasicNameValuePair("book_id",i.getStringExtra("book_id") ));
                        update_book.add(new BasicNameValuePair("class_id",class_id));           
                        update_book.add(new BasicNameValuePair("bookname",book_name.getText().toString()));
                        update_book.add(new BasicNameValuePair("description",book_description.getText().toString()));
                        update_book.add(new BasicNameValuePair("price",book_price.getText().toString() ));
                        update_book.add(new BasicNameValuePair("bookcondition",condVal));
                        
                                                                        
                        
                        jobj_update_book = jpar_update_book.makeHttpRequest(url_edit_book, "GET", update_book);
                       // Log.d("update_book info: ", jobj_update_book.toString());
                                                
                        try{
                               // Log.d("before success: ", "before success");
                                
                                int success = jobj_update_book.getInt("success");
                                
                              //  Log.d("after success: ", "after success");
                                
                                if (success == 1){
                                        Intent home = new Intent(Edit.this, Homepage.class);
                                        home.putExtra("user_id", i.getStringExtra("user_id"));
                                        startActivity(home);
                                }
                                else{
                                        
                                }
                        }catch (JSONException e){
                                e.printStackTrace();
                        }
                        return null;
                }
                
        }
}