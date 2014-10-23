package com.example.tritontrade;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Sell extends Activity implements OnClickListener {
	
	JSONParser jsonParser = new JSONParser();
	
	JSONArray course = null;
	
	Button takePic, submit, scan;
	EditText Ebookname, Eprice, Edescription;
	Spinner Econdition, Eclassabbreviation, Eclassnumber ;
	
	String userid;
	
	/* to insert a new book in book table */
	private static String url_create_book = "http://tritontrade.org/TritonTradeCRUD/create_book.php";
	
	/* to check if the class already exist */
	private static String url_check_class= "http://tritontrade.org/TritonTradeCRUD/check_class.php";
	
	/* to insert a class in the class table */
	private static String url_create_class = "http://tritontrade.org/TritonTradeCRUD/create_class.php";
	
	private void initialize() {
		
		//takePic = (Button) findViewById(R.id.sellButtonPicture);
		submit = (Button) findViewById(R.id.sellButtonSubmit);
		scan = (Button) findViewById(R.id.sellButtonBarcode);
		
		Ebookname = (EditText) findViewById(R.id.Bookname);
		Eprice = (EditText) findViewById(R.id.price);
		Edescription = (EditText) findViewById(R.id.description);
		Eclassabbreviation = (Spinner) findViewById(R.id.class_abb);
		Eclassnumber = (Spinner) findViewById(R.id.class_num);
	
		Econdition = (Spinner) findViewById(R.id.condition);
		
		
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell);
		initialize();
		
		//takePic.setVisibility(View.GONE);

		Bundle id = getIntent().getExtras();
			
		userid = id.getString("user_id");
		//Log.d("User id is: ", userid );
		
		Ebookname.setText(id.getString("title"));
		Edescription.setText(id.getString("description"));
		
		scan.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					
				Intent home = new Intent(getApplicationContext(), BarcodeScanner.class);
				home.putExtra("user_id", userid);
				startActivity(home);
				
			}
			
		});
		
		submit.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					
				new checkClass().execute();
				
			}
			
		});
		
		
	}
	
	 class checkClass extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			
			String classname, classnum, classid, bookname, description, price, condition, status;
			 
			
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			classname = Eclassabbreviation.getSelectedItem().toString();
			classnum = Eclassnumber.getSelectedItem().toString();
			//classname = (Eclassabbreviation.getText().toString()).toLowerCase();
			//classnum = Eclassnumber.getText().toString();
			
			
			
			param.add(new BasicNameValuePair("name", "\"" + classname + "\"" ));
			param.add(new BasicNameValuePair("number", "\"" + classnum + "\"" ));
			
			JSONObject json = jsonParser.makeHttpRequest(url_check_class, "GET", param);
			
			try
			{
				
				int success = json.getInt("success");
				
				//Log.d("success in if: ", Integer.toString(json.getInt("success")) );
				
				if(success == 1){
					
					course  = json.getJSONArray("class");
					
					//Log.d("you're inside : ", "the if");
					
					for(int i = 0; i < course.length(); i++){
					
						JSONObject userObj = course.getJSONObject(i);
						
						classname = userObj.getString("name");
						classnum = userObj.getString("number");
						classid = userObj.getString("class_id");
						
					
					
					json = null;
					
					
					bookname = Ebookname.getText().toString();
					description = Edescription.getText().toString();
					price = Eprice.getText().toString();
					condition = Econdition.getSelectedItem().toString();
					
					int condVal;
					if(condition.equals("Excellent")){
						condVal = 3;
					}
					else if(condition.equals("Good")){
						condVal = 2;
					}
					else {
						condVal = 1;
					}
					
					//Log.d("class_id: ", classid);
					//Log.d("seller_id: ", userid);
					//Log.d("bookname: ", bookname);
					//Log.d("description: ", description);
					//Log.d("price: ", price);
					//Log.d("Condition: ", condition);
					//Log.d("Status: ", "1");
					
					param.add(new BasicNameValuePair("class_id",  classid  ));
					param.add(new BasicNameValuePair("seller_id",  userid ));
					param.add(new BasicNameValuePair("bookname",  bookname  ));
					param.add(new BasicNameValuePair("description",  description  ));
					param.add(new BasicNameValuePair("price",  price  ));
					param.add(new BasicNameValuePair("condition", Integer.toString(condVal) ));
					param.add(new BasicNameValuePair("status", "1" ));
				
					
					json = jsonParser.makeHttpRequest(url_create_book, "GET", param);
					
					success = json.getInt("success");
					
					//Log.d("Success after create book: ", Integer.toString(success));
					
					Intent home = new Intent(getApplicationContext(), Homepage.class);
					home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle b = new Bundle();
					b.putString("user_id", userid);
					home.putExtras(b);
					startActivity(home);
					
					}
					
				}
				
				else {
					//class was not found so add it first before creating the book
					
					List<NameValuePair> paramClass = new ArrayList<NameValuePair>();
					
					//Log.d("Class name: ", classname);
					//Log.d("Class number: ", classnum);
					paramClass.add(new BasicNameValuePair("name",  classname  ));
					paramClass.add(new BasicNameValuePair("number", classnum  ));
					
					json = jsonParser.makeHttpRequest(url_create_class, "GET", paramClass);
					
					
					//Log.d("success in else: ", Integer.toString(json.getInt("success")) );
					
					try
					{
						
						success = json.getInt("success");
						
						if(success == 1) {
							
						
							course = json.getJSONArray("class");
							
							for(int i = 0; i < course.length(); i++){
								
								JSONObject userObj = course.getJSONObject(i);
								
								int cid;
								cid = userObj.getInt("classid");
								
								param.add(new BasicNameValuePair("class_id",  Integer.toString(cid)  ));
								
								bookname = Ebookname.getText().toString();
								description = Edescription.getText().toString();
								price = Eprice.getText().toString();
								condition = Econdition.getSelectedItem().toString();
								
								
								int condVal;
								if(condition.equals("Perfect")){
									condVal = 3;
								}
								else if(condition.equals("Good")){
									condVal = 2;
								}
								else {
									condVal = 1;
								}
								
								//param.add(new BasicNameValuePair("class_id",  classid  ));
								param.add(new BasicNameValuePair("seller_id",  userid ));
								param.add(new BasicNameValuePair("bookname",  bookname  ));
								param.add(new BasicNameValuePair("description",  description  ));
								param.add(new BasicNameValuePair("price",  price  ));
								param.add(new BasicNameValuePair("condition", Integer.toString(condVal) ));
								param.add(new BasicNameValuePair("status", "1" ));
								
								//Log.d("class_id: ", Integer.toString(cid));
								//Log.d("seller_id: ", userid);
								//Log.d("bookname: ", bookname);
								//Log.d("description: ", description);
								//Log.d("price: ", price);
								//Log.d("Condition: ", condition);
								//Log.d("Status: ", "1");
								
								json = jsonParser.makeHttpRequest(url_create_book, "GET", param);
								
								success = json.getInt("success");
								
								//Log.d("Success after create book in else: ", Integer.toString(success));
								
								Intent home = new Intent(getApplicationContext(), Homepage.class);
								home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								Bundle b = new Bundle();
								b.putString("user_id", userid);
								home.putExtras(b);
								startActivity(home);
							
							}
						}
						
					} catch (JSONException e){
						
						e.printStackTrace();
					}
					
				}
				
			} catch (JSONException e){
				e.printStackTrace();
			}
			
			
			return null;
		}
	 
	 }
	
	/* the attributes in the XML */
	public void onClick(View v) {

		switch( v.getId() ) {
		
		case R.id.sellButtonBarcode:
			//HOW TO ACCESS BARCODE READER OR PROMPT USER TO DOWNLOAD 
			
			/*Intent a = new Intent(Sell.this, Sell.class);
			startActivity(a);
			*/
			break;
		
		//case R.id.sellButtonPicture:
			// HOW TO ACCESS AND SAVE PICTURE 
			
			/*Intent b = new Intent(Sell.this, Buy.class);
			startActivity(b);
			*/
			//break;
		
		case R.id.sellButtonSubmit:
			Intent c = new Intent(Sell.this, Homepage.class);
			startActivity(c);
			break;
		}
	}
}