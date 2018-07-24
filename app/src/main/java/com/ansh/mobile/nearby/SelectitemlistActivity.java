package com.ansh.mobile.nearby;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SelectitemlistActivity extends Activity implements AdapterView.OnItemSelectedListener{
    Boolean listvalue = true;
   private InterstitialAd interstitial;
    String lat, lng, name, vicinity;
    ProgressDialog PD;
    RelativeLayout listlayout, maplayout;
    String place_id = "";
    RatingBar rating;
    GoogleMap map;
    String addressText;
    LocationManager lm;
    double mLatitude = 0;
    double mLongitude = 0;
    String[] mPlaceType = null;
    String web;
    Integer[] imageId;
    String radius = "5000";
    List<HashMap<String, String>> listData;
    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    ListView list;
    Spinner spinner1, spinner2;
    Button bt1, bt2;
    private String[] strings = {"1", "2", "3", "4","5", "6", "7", "8", "9",
            "10", "15", "20", "25"};
    private String[] values = {"Km", "Mi"};
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectitemlist);

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


        Intent i = getIntent();
        // getting attached intent data
        web = i.getStringExtra("web");
        mLatitude = i.getDoubleExtra("lat", mLatitude);
        mLongitude = i.getDoubleExtra("lon", mLongitude);
        // displaying selected product name

        isInternetPresent = isConnectingToInternet();
        if (!isInternetPresent) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectitemlistActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Internet Connection Error");

            // Setting Dialog Message
            alertDialog
                    .setMessage("Please connect to working Internet connection.");

            // On pressing Settings button
            alertDialog.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


            // Showing Alert Message
            alertDialog.show();
        }
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        list = (ListView) findViewById(R.id.listview);
        bt1 = (Button) findViewById(R.id.list123);
        bt2 = (Button) findViewById(R.id.mapbtn);
        listlayout = (RelativeLayout) findViewById(R.id.listlayout);
        maplayout = (RelativeLayout) findViewById(R.id.maplayout);
       // MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        // map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // map.setMapType(GoogleMap.MAP_TYPE_NONE);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(mLatitude, mLongitude))
                .icon(bitmapDescriptor)
                .title(new LatLng(mLatitude, mLongitude).toString()));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                //action to be taken after click on info window
                LatLng lt = marker.getPosition();
                Double latVal = lt.latitude;
                Double lonVal = lt.longitude;
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).get("lat").toString().equals(String.valueOf(latVal)) && listData.get(i).get("lng").toString().equals(String.valueOf(lonVal))) {
                        try {

                            place_id = listData.get(i).get("place_id").toString();
                            lat = listData.get(i).get("lat").toString();
                            lng = listData.get(i).get("lng").toString();
                            name = listData.get(i).get("name").toString();
                            vicinity = listData.get(i).get("vicinity").toString();
                            break;
                        } catch (Exception e) {

                        }
                    }
                }
                Intent intent = new Intent(getApplicationContext(),PlaceDetailsActivity.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("name", name);
                intent.putExtra("vicinity", vicinity);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,R.layout.spiner_item, strings);

        spinner1.setAdapter(adapter_state);
        spinner1.setOnItemSelectedListener(SelectitemlistActivity.this);

        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this,R.layout.spiner_item, values);

        spinner2.setAdapter(adapter_state1);
        spinner2.setOnItemSelectedListener(SelectitemlistActivity.this);


        spinner1.setSelection(1);

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listlayout.setVisibility(View.VISIBLE);
                maplayout.setVisibility(View.GONE);
               // bt2.setBackgroundResource(R.drawable.buttonthik);
               // bt1.setBackgroundResource(R.drawable.button);
                listvalue = true;
                createListAndMap();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
              //  bt2.setBackgroundResource(R.drawable.button);
              //  bt1.setBackgroundResource(R.drawable.buttonthik);
                listlayout.setVisibility(View.GONE);
                maplayout.setVisibility(View.VISIBLE);

                listvalue = false;
                createListAndMap();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                try {
                    place_id = listData.get(position).get("place_id").toString();
                    lat = listData.get(position).get("lat").toString();
                    lng = listData.get(position).get("lng").toString();
                    name = listData.get(position).get("name").toString();
                    vicinity = listData.get(position).get("vicinity").toString();
                } catch (Exception e) {

                }
                Intent intent = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("name", name);
                intent.putExtra("vicinity", vicinity);
                startActivity(intent);
            }
        });


        callWebService();


    }

   private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }

    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog pdia;
        String data = null;

        protected void onPreExecute() {

            super.onPreExecute();
            pdia = new ProgressDialog(SelectitemlistActivity.this);
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

        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pdia.dismiss();
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
            //PD.dismiss();
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner1) {
            spinner1.setSelection(position);
            String selState = (String) spinner1.getSelectedItem();
            String value1 = spinner1.getSelectedItem().toString();
            String value2 = spinner2.getSelectedItem().toString();
            int number = Integer.parseInt(value1);
            if (value2.equals("Km")) {
                if (spinner1.getSelectedItemPosition() != 0) {
                    radius = String.valueOf((number * 1000) - 500);
                } else {
                    radius = String.valueOf((number * 1000));
                }
            } else {
                if (spinner1.getSelectedItemPosition() != 0) {
                    radius = String.valueOf((number * 1609) - 500);
                } else {
                    radius = String.valueOf((number * 1000));
                }
            }
            callWebService();

        } else if (spinner.getId() == R.id.spinner2) {
            spinner2.setSelection(position);
            String value1 = spinner1.getSelectedItem().toString();
            String value2 = spinner2.getSelectedItem().toString();
            int number = Integer.parseInt(value1);
            if (value2.equals("Km")) {
                if (spinner1.getSelectedItemPosition() != 0) {
                    radius = String.valueOf((number * 1000) - 500);
                }

            } else {
                if (spinner1.getSelectedItemPosition() != 0) {
                    radius = String.valueOf((number * 1609) - 500);
                }
            }
            callWebService();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;

        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list1) {

            listData = list1;

            createListAndMap();
            //		 Collections.sort(listData,"km" );


        }

    }


    public void callWebService() {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=" + radius);
        sb.append("&types=" + web);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyChbHv-ygqDzHwe6Kpd1wOI8lF7QbxtomA");
        PlacesTask placesTask = new PlacesTask();

        placesTask.execute(sb.toString());

    }

    public class MyBaseAdapter extends BaseAdapter {


        LayoutInflater inflater;
        Context context;


        public MyBaseAdapter(Context context) {

            this.context = context;
            inflater = LayoutInflater.from(this.context);        // only context can also be used
        }

        @Override
        public int getCount() {
            try {
                return listData.size();
            } catch (Exception e) {
                return 0;
            }
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                MyViewHolder mViewHolder;

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listitem, null);
                    mViewHolder = new MyViewHolder();
                    convertView.setTag(mViewHolder);
                } else {
                    mViewHolder = (MyViewHolder) convertView.getTag();
                }

                mViewHolder.name = (TextView) convertView.findViewById(R.id.name);
                mViewHolder.address = (TextView) convertView.findViewById(R.id.address);
                mViewHolder.distance = (TextView) convertView.findViewById(R.id.distance);

                try {

                    mViewHolder.name.setText(listData.get(position).get("name").toString());

                } catch (Exception e) {

                }
                try {
                    mViewHolder.address.setText(listData.get(position).get("vicinity").toString());
                } catch (Exception e) {

                }

                try {
                    if (spinner2.getSelectedItemPosition() == 1) {
                        mViewHolder.distance.setText(listData.get(position).get("mi").toString() + "MI");
                    } else {
                        mViewHolder.distance.setText(listData.get(position).get("km").toString() + "KM");
                    }

                } catch (Exception e) {

                }
            } catch (Exception e) {

            }

            return convertView;
        }


        private class MyViewHolder {
            TextView name, address, distance;

        }


        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public void createListAndMap() {
        try {
            final LatLngBounds bounds;
            if (listvalue) {

                Collections.sort(listData, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> a, HashMap<String, String> b) {
                        if (spinner2.getSelectedItemPosition() == 0)
                            return Double.compare(Double.parseDouble(a.get("km")), Double.parseDouble(b.get("km")));
                        else
                            return Double.compare(Double.parseDouble(a.get("mi")), Double.parseDouble(b.get("mi")));
                    }
                });

                if (listData.size() > 0)
                    list.setAdapter(new MyBaseAdapter(SelectitemlistActivity.this));

            } else {
                map.clear();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < listData.size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = listData.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String name = googlePlace.get("name");
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(name + " : " + vicinity);
                    map.addMarker(markerOptions);
                    builder.include(latLng);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    map.setMyLocationEnabled(true);


                }
                LatLng latLng1 = new LatLng(mLatitude, mLongitude);
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                if(spinner1.getSelectedItemPosition()<=1)
                    map.animateCamera(CameraUpdateFactory.zoomTo(14));
                if(spinner1.getSelectedItemPosition()<=2)
                    map.animateCamera(CameraUpdateFactory.zoomTo(13));
                else if (spinner1.getSelectedItemPosition()<=5)
                    map.animateCamera(CameraUpdateFactory.zoomTo(12));
                else if (spinner1.getSelectedItemPosition()<=9)
                    map.animateCamera(CameraUpdateFactory.zoomTo(11));
                else if (spinner1.getSelectedItemPosition()==10)
                    map.animateCamera(CameraUpdateFactory.zoomTo(10));
                else if (spinner1.getSelectedItemPosition()==11)
                    map.animateCamera(CameraUpdateFactory.zoomTo(9));
                else if (spinner1.getSelectedItemPosition()<=12)
                    map.animateCamera(CameraUpdateFactory.zoomTo(8));
                else if (spinner1.getSelectedItemPosition()==13)
                    map.animateCamera(CameraUpdateFactory.zoomTo(8));
                else
                {
                    map.animateCamera(CameraUpdateFactory.zoomTo(12));
                }
            }
        }
        catch(Exception e){

        }
    }
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }


}







