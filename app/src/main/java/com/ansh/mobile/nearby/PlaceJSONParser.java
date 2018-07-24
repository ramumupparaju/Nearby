package com.ansh.mobile.nearby;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.google.android.maps.GeoPoint;

public class PlaceJSONParser {
	
	int kmInDec,meterInDec;
	double meter, mi,km;
	/** Receives a JSONObject and returns a list */
	public List<HashMap<String,String>> parse(JSONObject jObject){		
		
		JSONArray jPlaces = null;
		try {			
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		return getPlaces(jPlaces);
	}
	
	
	private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> place = null;	

		/** Taking each place, parses and adds to list object */
		for(int i=0; i<placesCount;i++){
			try {
				/** Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject)jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}
	
	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlaceDetails){
		
		
		HashMap<String, String> hPlaceDetails = new HashMap<String, String>();
		
		String name = "";
		String icon = "";
		String vicinity="";
		String latitude="";
		String longitude="";
		String formatted_address="";
		String formatted_phone="";
		String website="";
		String rating="";
		String international_phone_number="";
		String url="";
		String place_id="";
		
		
		try {
			// Extracting Place name, if available
			if(!jPlaceDetails.isNull("name")){
				name = jPlaceDetails.getString("name");
			}
			
			// Extracting Icon, if available
			if(!jPlaceDetails.isNull("icon")){
				icon = jPlaceDetails.getString("icon");
			}
			
			// Extracting Place Vicinity, if available
			if(!jPlaceDetails.isNull("vicinity")){
				vicinity = jPlaceDetails.getString("vicinity");
			}	
			
			// Extracting Place formatted_address, if available
			if(!jPlaceDetails.isNull("formatted_address")){
				formatted_address = jPlaceDetails.getString("formatted_address");
			}
			
			// Extracting Place formatted_phone, if available
			if(!jPlaceDetails.isNull("formatted_phone_number")){
				formatted_phone = jPlaceDetails.getString("formatted_phone_number");
			}	
			
			// Extracting website, if available
			if(!jPlaceDetails.isNull("website")){
				website = jPlaceDetails.getString("website");
			}	
			
			// Extracting rating, if available
			if(!jPlaceDetails.isNull("rating")){				
				rating =jPlaceDetails.getString("rating");
			}
			
			// Extracting rating, if available
			if(!jPlaceDetails.isNull("international_phone_number")){				
				international_phone_number = jPlaceDetails.getString("international_phone_number");
			}
			
			// Extracting url, if available
			if(!jPlaceDetails.isNull("url")){				
				url = jPlaceDetails.getString("url");
			}
			if(!jPlaceDetails.isNull("place_id")){
				place_id=jPlaceDetails.getString("place_id");
			}
			try{
			latitude = jPlaceDetails.getJSONObject("geometry").getJSONObject("location").getString("lat");
			}catch(Exception e){
				
			}
			try{
			longitude = jPlaceDetails.getJSONObject("geometry").getJSONObject("location").getString("lng");
			}catch(Exception e){
				
			}
			try{
				distance(MainActivityNearby.mLatitude, MainActivityNearby.mLongitude, Double.parseDouble(latitude),  Double.parseDouble(longitude));
		
}catch(Exception e){
				
			}
			DecimalFormat df = new DecimalFormat("##.##");
			hPlaceDetails.put("name", name);
			hPlaceDetails.put("icon", icon);			
			hPlaceDetails.put("vicinity", vicinity);
			hPlaceDetails.put("lat", latitude);
			hPlaceDetails.put("lng", longitude);
			hPlaceDetails.put("formatted_address", formatted_address);
			hPlaceDetails.put("formatted_phone", formatted_phone);
			hPlaceDetails.put("website", website);
			hPlaceDetails.put("rating", rating);
			hPlaceDetails.put("international_phone_number", international_phone_number);
			hPlaceDetails.put("url", url);
			hPlaceDetails.put("place_id", place_id);
			hPlaceDetails.put("km", df.format(km));
			hPlaceDetails.put("mi", df.format(mi));
		} catch (JSONException e) {			
			e.printStackTrace();
		}
				
		return hPlaceDetails;
	}
	
	
	private void distance(double mLatitude, double mLongitude, double lat, double lng) {
		  
	    double theta = mLongitude - lng;
	 
	    double dist = Math.sin(deg2rad(mLatitude)) * Math.sin(deg2rad(lat)) + Math.cos(deg2rad(mLatitude)) * Math.cos(deg2rad(lat)) * Math.cos(deg2rad(theta));
	  
	    dist = Math.acos(dist);
	  
	    dist = rad2deg(dist);
	  
	    mi = dist * 60 * 1.15;
	  
	   
	  
	      km = dist * 1.61*100;
	  
	   
	    
	  
	  }
  private double deg2rad(double deg) {
	  
	    return (deg * Math.PI / 180.0);
	  
	  }

  private double rad2deg(double rad) {
	  
	    return (rad * 180 / Math.PI);
	  
	  }
}
