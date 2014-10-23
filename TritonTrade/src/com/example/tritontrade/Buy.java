package com.example.tritontrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.tritontrade.JSONParser;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Buy extends ListActivity implements OnClickListener {
	Intent i;
	ListView lv;
	Button search;
	JSONParser jpar;
	JSONObject jobj;
	JSONArray jarray;
	List<NameValuePair> lst;
	ArrayList<HashMap<String, String>> classList;
	private ProgressDialog pDialog;
	
	
	private void initialize() {
		lv = getListView();
		search = (Button) findViewById(R.id.buyButtonSearch);
	    jpar = new JSONParser();
	    lst = new ArrayList<NameValuePair>();
	    classList = new ArrayList<HashMap<String, String>>();
	    i = getIntent();
	    search.setOnClickListener(this);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy);
		initialize();		
			
		new GetClasses().execute();

        lv.setOnItemClickListener(new OnItemClickListener() {
        	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
                             
                String name_number = ((TextView) view.findViewById(R.id.name)).getText()
                        .toString();
                
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        Books.class);
                
                // sending pid to next activity
                in.putExtra("class_id", pid);
                in.putExtra("user_id",i.getStringExtra("user_id"));
                in.putExtra("name&number",name_number);
                in.putExtra("status","1");
                startActivity(in);
                // starting new activity and expecting some response back
                //startActivityForResult(in, 100);
            }
        });
	}	
	
	public void onClick(View v) {

		switch( v.getId() ) {
		
		case R.id.buyButtonSearch:
			Intent a = new Intent(Buy.this, SearchClass.class);
			a.putExtra("not_found", "false");
			a.putExtra("user_id", i.getStringExtra("user_id"));
			startActivity(a);
			break;
		}
	}
	
	/*// Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }*/

   class GetClasses extends AsyncTask<String,String,String> {

	   protected void onPreExecute() {
			
			super.onPreExecute();
			pDialog = new ProgressDialog(Buy.this);
			pDialog.setMessage("Gathering Classes...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}
	   
	    @Override
	    protected String doInBackground(String... arg0) {
	    	jobj = jpar.makeHttpRequest("http://tritontrade.org/TritonTradeCRUD/read_all_class.php"
		             , "GET", lst);
	    	
	    	//Log.d("json class object?:",jobj.toString());
	    	
	    	try {
	    		jarray = jobj.getJSONArray("class");
	    		for (int i = 0; i < jarray.length(); i++){
	    			JSONObject c = jarray.getJSONObject(i);
	    			String class_id = String.valueOf(c.getInt("class_id")); 
	    			String name    = c.getString("name");
	    			String number  = String.valueOf(c.getInt("number"));
		            
	    			HashMap<String, String> map = new HashMap<String, String>();
	    			
	    			map.put("class_id",class_id);
	    			map.put("name&number", name+" "+number );
	    			
	    			classList.add(map);
	    		}
	    		
	    	} catch (JSONException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
		   return null;
	    }
	    
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter( Buy.this, classList,R.layout.classlist, new String[] { "name&number",
                                    								"class_id"},
                                    								new int[] { R.id.name, R.id.pid });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
   }
}
