package com.ansh.mobile.nearby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivityNearby extends AppCompatActivity {
    private InterstitialAd interstitial;
    AdView adView;
    Intent i;
    String pkgName;
    String AppSource;
    String Appname;
    String CompleteUrl;
    String[] AppName;
    String[] AppPkg;
    Drawable[] AppIcon;
    String[] AppPath;

    String[] text1;
    EditText editsearch;
    TextView text2;

    ApplicationInfo app;
    CheckBox cbox;
    static boolean isappShow = false;
    ImageView exit, cancel;
    private PackageManager packageManager = null;
    /**
     * Called when the activity is first created.
     */
    private ShareActionProvider mShareActionProvider;

    public Camera mCamera;
    ProgressDialog mProgressDialog;
    static ArrayList<HashMap<String, String>> myAppsList;
    static String RANK = "appname";
    static String COUNTRY = "appimage";
    static String POPULATION = "apppackage";
    static String FLAG = "appimage";
    RelativeLayout rel3;
    ImageView App1, App2, App3, App4, App5, App6;
    Intent i3, i4, i5, i6;
    static ImageLoader imageLoader;
    JSONObject jsonobject;
    JSONArray jsonarray;

    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;
    LocationManager locMan;
    ListView list;
    static double mLatitude = 0;
    static double mLongitude = 0;
    String mLocationClient;
    Location loc;
    ActionBar actionBar;

    GPSTracker gps;
    static Boolean isInternetPresent = false;
    String[] web = {
            "food",
            "atm",
            "bank",
            "hospital",
            "police",
            "school",
            "bus_station",
            "gas_station",
            "bakery",
            "bar",
            "fire_station", "gym", "doctor", "dentist", "hindu_temple", "church", "mosque", "grocery_or_supermarket", "park",
            "shopping_mall", "movie_theater", "hair_care", "health", "book_store", "jewelry_store", "clothing_store",
            "travel_agency", "train_station", "taxi_stand", "shoe_store", "museum", "pet_store", "aquarium", "florist",
            "car_wash", "car_repair", "car_rental", "car_dealer", "home_goods_store", "plumber", "stadium",
            "electrician", "electronics_store", "library", "courthouse", "post_office", "university", "lawyer", "art_gallery",
            "airport", "bowling_alley", "cafe", "bicycle_store", "insurance_agency", "zoo", "night_club"



           /* "accounting",
            "amusement_park",
            "beauty_salon",
            "campground",
            "casino",
            "cemetery",
            "city_hall",
            "convenience_store",
            "department_store",
            "embassy",
            "establishment",
            "finance",
            "funeral_home",
            "furniture_store",
            "general_contractor",
            "hardware_store",
            "laundry",
            "liquor_store",
            "local_government_office",
            "locksmith",
            "lodging",
            "meal_delivery",
            "meal_takeaway",
            "movie_rental",
            "moving_company",
            "painter",
            "parking",
            "pharmacy",
            "physiotherapist",
            "place_of_worship",
            "real_estate_agency",
            "restaurant",
            "roofing_contractor",
            "rv_park",
            "spa",
            "storage",
            "store",
            "subway_station",
            "synagogue",
            "veterinary_care"*/

    };
    String[] names = {
            "Food",
            "ATM",
            "Bank",
            "Hospital",
            "Police",
            "School",
            "Bus Station",
            "Petrol",
            "Bakery",
            "Bar",
            "Fire Station", "Gym", "Doctor", "Dentist", "Hindu Temple", "Church", "Mosque", "Grocery or Supermarket", "Park", "Shopping Mall",
            "Movie Theater", "Hair Care", "Health", "Book Store", "Jewelry Store", "Clothing Store", "Travel Agency",
            "Train Station", "Taxi Stand", "Shoe Store", "Museum", "Pet Store", "Aquarium", "Florist",
            "Car Wash", "Car Repair", "Car Rental", "Car Dealer", "Home Goods Store", "Plumber", "Stadium",
            "Electrician", "Electronics Store", "Library", "Courthouse", "Post Office", "University", "Lawyer", "Art Gallery",
            "Airport", "Bowling Alley", "Cafe", "Bicycle Store", "Insurance Agency", "Zoo", "Night Club",

            /*"Accounting",
            "Amusement Park",
            "Beauty Salon",
            "Campground",
            "Casino",
            "Cemetery",
            "City Hall",
            "Convenience Store",
            "Department Store",
            "Embassy",
            "Establishment",
            "Finance",
            "Funeral Home",
            "Furniture Store",
            "General Contractor",
            "Hardware Store",
            "Laundry",
            "Liquor Store",
            "Local Government Office",
            "Locksmith",
            "Lodging",
            "Meal Delivery",
            "Meal Takeaway",
            "Movie Rental",
            "Moving Company",
            "Painter",
            "Parking",
            "Pharmacy",
            "Physiotherapist",
            "Place Of Worship",
            "Real Estate Agency",
            "Restaurant",
            "Roofing Contractor",
            "Rv park",
            "Spa",
            "Storage",
            "Store",
            "Subway Station",
            "Synagogue",
            "Veterinary Care",*/

    };
    Integer[] imageId = {
            R.drawable.food,
            R.drawable.atms,
            R.drawable.banks,
            R.drawable.hospital,
            R.drawable.police,
            R.drawable.school,
            R.drawable.bus,
            R.drawable.petrol,
            R.drawable.bakery,
            R.drawable.bar,
            R.drawable.fire, R.drawable.gym, R.drawable.doctor, R.drawable.dentist, R.drawable.temple, R.drawable.chhurchh, R.drawable.mossque,
            R.drawable.supermarket, R.drawable.park, R.drawable.shopping, R.drawable.theatre, R.drawable.haircare,
            R.drawable.health, R.drawable.bookstore, R.drawable.jewelary, R.drawable.clothstore, R.drawable.travel,
            R.drawable.trains, R.drawable.taxxi, R.drawable.shoetore, R.drawable.musium, R.drawable.petstore, R.drawable.aquerium,
            R.drawable.florist, R.drawable.carwash, R.drawable.carrepare, R.drawable.carrent, R.drawable.cardelar,
            R.drawable.store, R.drawable.plumber, R.drawable.stadium, R.drawable.electronics, R.drawable.electornicstore,
            R.drawable.library, R.drawable.court, R.drawable.postoffice, R.drawable.university, R.drawable.lawwer, R.drawable.artgallery,
            R.drawable.airports, R.drawable.boling, R.drawable.cafe, R.drawable.bycicle, R.drawable.insurancyagency,
            R.drawable.zoo, R.drawable.nightclub


           /* R.drawable.accounting, R.drawable.amuse,
    R.drawable.salon, R.drawable.campground, R.drawable.casino,R.drawable.cemetary,R.drawable.cityhall,R.drawable.convience,R.drawable.departmental,R.drawable.embsy, R.drawable.establishment,R.drawable.finance,
            R.drawable.funralhome,
            R.drawable.furniture,
            R.drawable.generalcontaractor,
            R.drawable.hardware,
            R.drawable.laundry,
            R.drawable.liqor,
            R.drawable.governament,
            R.drawable.lock,
            R.drawable.lodging,
            R.drawable.mealdelivery,
            R.drawable.mealtakeaway,
            R.drawable.movierental,
            R.drawable.movingcompany,
            R.drawable.painter,
            R.drawable.parking,
            R.drawable.pharmacy,
            R.drawable.physotharipist,
            R.drawable.palceworship,
            R.drawable.realestate,
            R.drawable.rest,
            R.drawable.roffingcontractor,
            R.drawable.rvpark,
            R.drawable.spa,
            R.drawable.storage,
            R.drawable.storeeeeee,
            R.drawable.subwaystation,
            R.drawable.synagogue,
            R.drawable.veternary,
          */


    };
    ImageButton share_1, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nearby);
        adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
// full ads


        try {
            try {
                interstitial = new InterstitialAd(getApplicationContext());
                interstitial.setAdUnitId("ca-app-pub-1868794594099718/7709995182");

                // Create ad request.
                AdRequest adRequest1 = new AdRequest.Builder().build();

                // Begin loading your interstitial.
                interstitial.loadAd(adRequest1);
            } catch (Exception e) {

            }
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    // Call displayInterstitial() function
                    displayInterstitial();
                }
            });
        } catch (Exception e) {

        }

        share_1 = (ImageButton) findViewById(R.id.share_im);
        rate = (ImageButton) findViewById(R.id.rate_im);
        share_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ansh.mobile.nearby");
                startActivity(Intent.createChooser(intent, "Share with"));
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", "com.ansh.mobile.nearby")));
                startActivity(i3);

            }
        });


        CustomList adapter = new CustomList(MainActivityNearby.this, names, imageId);
        list = (ListView) findViewById(R.id.list);
        try {

            isInternetPresent = isConnectingToInternet();
            if (!isInternetPresent) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityNearby.this);

                // Setting Dialog Title
                alertDialog.setTitle("Internet Connection Error");

                // Setting Dialog Message
                alertDialog.setMessage("Please connect to working Internet connection.");

                // On pressing Settings button
                alertDialog.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                // Showing Alert Message
                alertDialog.show();


            }
        } catch (Exception e) {

        }

        try {
            gps = new GPSTracker(this);

            // check if GPS location can get
            if (gps.canGetLocation()) {
                //Toast.makeText(getApplicationContext(), "Your Location latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude(), 5000).show();

                Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            } else {
                // Can't get user's current location
                gps.showSettingsAlert();
                // stop executing code by return
                return;
            }
        } catch (Exception e) {

        }


        list.setAdapter(adapter);
        try {


            mLatitude = gps.getLatitude();
            mLongitude = gps.getLongitude();

        } catch (Exception e) {

        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {


                    if (mLatitude == 0 && mLongitude == 0) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityNearby.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Location not found");

                        // Setting Dialog Message
                        alertDialog.setMessage("Please go to settings menu and enable it.\nor Try after sometime");

                        // On pressing Settings button
                        alertDialog.setPositiveButton("Settings",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }
                                });

                        // on pressing cancel button
                        alertDialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });


                        // Showing Alert Message
                        alertDialog.show();


                    } else {
                        Intent intent = new Intent(getApplicationContext(), SelectitemlistActivity.class);
                        intent.putExtra("web", web[position]);
                        intent.putExtra("lat", mLatitude);
                        intent.putExtra("lon", mLongitude);
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }


            }
        });

           /* if(isInternetPresent)
            {
                    new DownloadJSON().execute();
            }*/
    }

    private void displayInterstitial() {


        if (interstitial.isLoaded()) {
            interstitial.show();
        }


    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public void onBackPressed() {
        // showAppsDialog();
        showApps();

        return;
    }

    private void showApps() {


        final android.app.Dialog dialog = new android.app.Dialog(MainActivityNearby.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.moreapps);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        exit = (ImageView) dialog.findViewById(R.id.exit);
        cancel = (ImageView) dialog.findViewById(R.id.cancel);

       /* App1 = (ImageView) dialog.findViewById(R.id.app1);
        App2 = (ImageView) dialog.findViewById(R.id.app2);
        App3 = (ImageView) dialog.findViewById(R.id.app3);
        App4 = (ImageView) dialog.findViewById(R.id.app4);
        App5 = (ImageView) dialog.findViewById(R.id.app5);
        App6 = (ImageView) dialog.findViewById(R.id.app6);

        exit = (ImageView) dialog.findViewById(R.id.exit);
        cancel = (ImageView) dialog.findViewById(R.id.cancel)



        ;*/


        if (isappShow) {
            isappShow = false;
            exit.setVisibility(View.GONE);
            //cancel.setText("Skip");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }

        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });


        dialog.show();


    }


}
