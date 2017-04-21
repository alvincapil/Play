package play.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 public class PlayRestServiceClient {

	public static void main(String[] args) {
		String string = "";
		try {
			
			InputStream inputStream = new FileInputStream("/Users/Alvin/git/java-play-codetest/customers.json");
			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(inputReader);
			String line;
			while ((line = br.readLine()) != null) {
				string += line + "\n";
			}

			JSONArray jsonArray = new JSONArray(string); 
			JSONArray sortedJsonArray = new JSONArray();
			ArrayList<String> list = new ArrayList<String>();
		
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		    for (int i = 0; i < jsonArray.length(); i++) {
		        jsonValues.add(jsonArray.getJSONObject(i));
		    }
		    Collections.sort(jsonValues, new Comparator<JSONObject>() {
		        private static final String KEY_NAME = "duetime";

		        @Override
		        public int compare(JSONObject a, JSONObject b) {
		            String valA = new String();
		            String valB = new String();

		            try {
		                valA = (String) a.get(KEY_NAME);
		                valB = (String) b.get(KEY_NAME);
		            } 
		            catch (JSONException e) {
		            }

		            return valA.compareTo(valB);
		        }
		    });

		    for (int i = 0; i < jsonArray.length(); i++) {
		        sortedJsonArray.put(jsonValues.get(i));
		    }
			
			for (int i = 0; i < sortedJsonArray.length(); i++) {
				list.add(sortedJsonArray.get(i).toString());
			}
			
			Iterator<String> iter = list.iterator();
			while(iter.hasNext()){
            	 System.out.println("Customer :" + iter.next());
            }
			
			try {
				URL url = new URL("http://localhost:8888/PlayFramework/api/playService");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(sortedJsonArray.toString());
				out.close();
 
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
 
				while (in.readLine() != null) {
				}
				System.out.println("\n REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling REST Service");
				System.out.println(e);
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
