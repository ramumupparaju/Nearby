package com.ansh.mobile.nearby;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents.Insert;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

//import com.google.android.gms.internal.ml;

public class PlaceDetailsActivity extends Activity {
	private InterstitialAd interstitial;
	AdView adView;
	String formatted_address;
	GoogleMap mGoogleMap;
	Double lati, lon;
	String lat, lng, name, vicinity;
	HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
	TextView dname, daddress, phone, website, ratingbar, ip, url;
	ImageView direction, contact, share, call, web;
	String formatted_phone;
	String websit;


	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_details);



		adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);


// full ads


        try
        {
            try{
                interstitial = new InterstitialAd(getApplicationContext());
                interstitial.setAdUnitId("ca-app-pub-1868794594099718/7709995182");

                // Create ad request.
                AdRequest adRequest1 = new AdRequest.Builder().build();

                // Begin loading your interstitial.
                interstitial.loadAd(adRequest1);
            }catch(Exception e){

            }
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    // Call displayInterstitial() function
                    displayInterstitial();
                }
            });
        }catch(Exception e)
        {

        }






		// Getting reference to WebView ( wv_place_details ) of the layout activity_place_details

		dname = (TextView) findViewById(R.id.name);
		daddress = (TextView) findViewById(R.id.address);
		phone = (TextView) findViewById(R.id.phone);
		website = (TextView) findViewById(R.id.website);
		ratingbar = (TextView) findViewById(R.id.rating);
		ip = (TextView) findViewById(R.id.internationalphone);
		url = (TextView) findViewById(R.id.url);
		direction = (ImageView) findViewById(R.id.direction);
		contact = (ImageView) findViewById(R.id.contact);
		share = (ImageView) findViewById(R.id.share);
		call = (ImageView) findViewById(R.id.call);
		web = (ImageView) findViewById(R.id.web);

		Intent i = getIntent();
		// getting attached intent data
		String place_id = i.getStringExtra("place_id");
		lat = i.getStringExtra("lat");
		lng = i.getStringExtra("lng");
		name = i.getStringExtra("name");
		vicinity = i.getStringExtra("vicinity");
		lati = Double.parseDouble(lat);
		lon = Double.parseDouble(lng);
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


		if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment

			// Getting Google Map
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			//MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);


			//	mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// Enabling MyLocation in Google Map
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			mGoogleMap.setMyLocationEnabled(true);

			mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lati, lon)).title(name));
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, lon), 13.0f));


			BitmapDescriptor bitmapDescriptor
					= BitmapDescriptorFactory.defaultMarker(
					BitmapDescriptorFactory.HUE_AZURE);

			mGoogleMap.addMarker(new MarkerOptions()
					.position(new LatLng(lati, lon))
					.icon(bitmapDescriptor)
					.title(new LatLng(lati, lon).toString()));
			//	mGoogleMap.addMarker(new MarkerOptions().position(yourLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.image)));
			// Getting LocationManager object from System Service LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location From GPS


			mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
					String reference = mMarkerPlaceLink.get(arg0.getId());
					intent.putExtra("reference", reference);

					// Starting the Place Details Activity
					startActivity(intent);
				}
			});


			direction.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + MainActivityNearby.mLatitude + "," + MainActivityNearby.mLongitude + "&daddr=" + lati + "," + lon));
					startActivity(intent);


				}
			});

			contact.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (formatted_phone.equals("-NA-")) {
						Toast.makeText(getApplicationContext(), "Phone number not avilable", Toast.LENGTH_LONG).show();
					} else {
						Intent intent = new Intent(
								ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
								ContactsContract.Contacts.CONTENT_URI);
						intent.setData(Uri.parse("tel:" + formatted_phone));//specify your number here
						intent.putExtra(Insert.NAME, name);
						intent.putExtra(Insert.POSTAL,
								formatted_address);
						startActivity(intent);
					}
				}
			});
			share.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					String shareBody = name + "\n" + formatted_address + "\nPh no:" + formatted_phone + "\n" + websit;
					sharingIntent.putExtra(Intent.EXTRA_SUBJECT, name + "Address");
					sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
					startActivity(Intent.createChooser(sharingIntent, "Share via"));
				}
			});
			call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + formatted_phone));
					if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					startActivity(callIntent);
			}
		});
   		
		web.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				  Intent intent = new Intent();
			        intent.setAction(Intent.ACTION_VIEW);
			        intent.addCategory(Intent.CATEGORY_BROWSABLE);
			        intent.setData(Uri.parse(websit));
			        startActivity(intent);
			}
		});
		
		
		StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
		sb.append("placeid="+place_id);
			sb.append("&key=AIzaSyChbHv-ygqDzHwe6Kpd1wOI8lF7QbxtomA");
			System.out.println("Url======>"+sb.toString());
		//  newlys generated browser key
		//sb.append("&key=AIzaSyD8HIjVooDtDn_wLFuE0BRQI6Nux-QWbSQ");
		//sb.append("&key=AIzaSyAABXenr0sZc0pSM38jDaZ7Y-FLAQ2MCXY");

		
		// Creating a new non-ui thread task to download Google place details 
        PlacesTask placesTask = new PlacesTask();		        			        
        
		// Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());	
		
	};
	}

	private void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}

	}





	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                
                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();                

                // Connecting to url 
                urlConnection.connect();                

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();
                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
    }


	/** A class, to download Google Place Details */
	private class PlacesTask extends AsyncTask<String, Integer, String>{

		String data = null;
		private ProgressDialog pdia;
		
		protected void onPreExecute() {
			
			super.onPreExecute();
			 pdia = new ProgressDialog(PlaceDetailsActivity.this);
			 pdia.setTitle("Please Wait..");
			 pdia.setMessage("Loading...");
			 pdia.setCancelable(false);
			 pdia.show();    
		/*	PD = new ProgressDialog(Act3.this);
			PD.setTitle("Please Wait..");
			PD.setMessage("Loading...");
			PD.setCancelable(false);
			PD.show();*/
		// Invoked by execute() method of this object
		}
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				 Log.d("Background Task",e.toString());
			}
			return data;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){	
			super.onPostExecute(result);
			pdia.dismiss();
			ParserTask parserTask = new ParserTask();
			
			// Start parsing the Google place details in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}
	
	
	/** A class to parse the Google Place Details in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

		JSONObject jObject;
		
		// Invoked by execute() method of this object
		@Override
		protected HashMap<String,String> doInBackground(String... jsonData) {
		
			HashMap<String, String> hPlaceDetails = null;
			PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            // Start parsing Google place details in JSON format
	            hPlaceDetails = placeDetailsJsonParser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return hPlaceDetails;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(HashMap<String,String> hPlaceDetails){			
			
			
			 name = hPlaceDetails.get("name");
			String icon = hPlaceDetails.get("icon");
			String vicinity = hPlaceDetails.get("vicinity");
			String lat = hPlaceDetails.get("lat");
			String lng = hPlaceDetails.get("lng");
			formatted_address = hPlaceDetails.get("formatted_address");
			formatted_phone = hPlaceDetails.get("formatted_phone");
			websit = hPlaceDetails.get("website");
			String rating = hPlaceDetails.get("rating");
			String international_phone_number = hPlaceDetails.get("international_phone_number");
			String url1 = hPlaceDetails.get("url");
			try
			{
			dname.setText(name);
			}catch(Exception e){
				
			}
			try
			{
			daddress.setText("Address : "+formatted_address);
             }catch(Exception e){
				
			}try
			{
			phone.setText("Phone No : "+formatted_phone);
              }catch(Exception e){
				
			}try
			{
			website.setText("Website : "+websit);
			}catch(Exception e){
				
			}
			try
			{
			if(websit.equals("-NA-"))
				{
					web.setVisibility(View.GONE);
				}
			 }catch(Exception e){
					
				}
			try
			{
			if(formatted_phone.equals("-NA-"))
				{
					call.setVisibility(View.GONE);
				}
			 }catch(Exception e){
					
				}
			try{
			ratingbar.setText("Rating : "+rating);
      }catch(Exception e){
				
			}try
			{
			ip.setText("International Phone No : "+international_phone_number);
			}catch(Exception e){
				
			}
			try
			{
			url.setText("Url : "+url1);
			}
          catch(Exception e){
				
			}
				
		}
	}
	
}