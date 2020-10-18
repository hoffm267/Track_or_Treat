package com.example.track_or_treat;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView textViewResult;
    private RequestQueue mQueue;
    private RequestQueue nQueue;

    double userLat = 0;
    double userLng = 0;
    double friendLat = 0;
    double friendLng = 0;
    double midLat = 0;
    double midLng = 0;
    double destintationLat = 0;
    double destinationLng = 0;
    int radius;

    boolean userDone = false;
    boolean friendDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public static double[] getLocationsGeoCoding(JSONObject in) throws JSONException, IOException {

        JSONArray array = in.getJSONArray("results");

        double[] location = new double[2];
        JSONObject geometry = new JSONObject();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jObj = array.getJSONObject(i);
            try {
                geometry = jObj.getJSONObject("geometry");
            } catch (JSONException e) {
                continue;
            }
        }

        location[0] = geometry.getJSONObject("location").getDouble("lat");
        location[1] = geometry.getJSONObject("location").getDouble("lng");

        return location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Message", "onMapReady");

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



        mQueue = Volley.newRequestQueue(this);

        //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        //url += "11616+Carroll+Cove,+Fort+Wayne,+IN";
        //url += "&key=AIzaSyAJEVLsJQ74ukMWGrYKhjQvYx262Fhy3Tw";
        String userUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=11616+Carroll+Cove,+Fort+Wayne,+IN&key=AIzaSyAJEVLsJQ74ukMWGrYKhjQvYx262Fhy3Tw";
        String friendUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=221+Mabry+Cove,+Fort+Wayne,+IN&key=AIzaSyAJEVLsJQ74ukMWGrYKhjQvYx262Fhy3Tw";

        System.out.println("Http Request for User Location");
        JsonObjectRequest userLocation = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            double[] userLocation = getLocationsGeoCoding(response);
                            userLat = userLocation[0];
                            userLng = userLocation[1];
                            mMap.addMarker(new MarkerOptions().position(new LatLng(userLat, userLng)).title("Your Location"));
                            userDone = true;
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(userLocation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                System.out.println("Sending Http Request for friendLocation");
                JsonObjectRequest friendLocation = new JsonObjectRequest(Request.Method.GET, friendUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    double[] userLocation = getLocationsGeoCoding(response);
                                    friendLat = userLocation[0];
                                    friendLng = userLocation[1];
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(friendLat, friendLng)).title("Your Friend's Location"));
                                    friendDone = true;
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(friendLocation);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        radius = 1000;

                        midLat = (userLat + friendLat) / 2;
                        midLng = (userLng + friendLng) / 2;

                        String nearByUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                        nearByUrl += "location=" + midLat + "," + midLng;
                        nearByUrl += "&radius=" +radius;
                        nearByUrl += "&type=restaurant&key=AIzaSyAJEVLsJQ74ukMWGrYKhjQvYx262Fhy3Tw";

                        System.out.println("Sending Http Request for closest type");
                        JsonObjectRequest closestLocation = new JsonObjectRequest(Request.Method.GET, nearByUrl, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            double[][] nearByLocations = getLocationsNearbySearch(response);
                                            destintationLat = nearByLocations[0][0];
                                            destinationLng = nearByLocations[0][1];

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mMap.addMarker(new MarkerOptions().position(new LatLng(destintationLat, destinationLng)).title("Resaurant or sm"));
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        mQueue.add(closestLocation);
                        System.out.println("After Http Request for Nearest Type");
                    }
                }, 7000);
            }
        }, 5000);   //5 seconds


        //221 Mabry Cove
        //Fort Wayne, IN









        midLat = 40.423298367915336;
        midLng = -86.90417305190029;


        double R = 6371e3; // metres
        double x1 = midLat * Math.PI/180; // φ, λ in radians
        double x2 = userLat * Math.PI/180;
        double y1 = (userLat-midLat) * Math.PI/180;
        double delt = (userLng-midLng) * Math.PI/180;

        double a = Math.sin(y1/2) * Math.sin(y1/2) +
                Math.cos(x1) * Math.cos(x2) *
                        Math.sin(delt/2) * Math.sin(delt/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        radius = (int) (R * c); // in metres


    }


    public static double[][] getLocationsNearbySearch(JSONObject in) throws JSONException {
        JSONArray results = in.getJSONArray("results");
        ArrayList<Double[]> locations = new ArrayList<Double[]>();


        JSONObject geometry = new JSONObject();
        ArrayList<JSONObject> geometries = new ArrayList<JSONObject>();

        boolean wasFound = false;
        for (int i = 0; i < results.length(); i++) {
            JSONObject jObj = results.getJSONObject(i);
            try {
                geometries.add(jObj.getJSONObject("geometry"));
                wasFound = true;
            } catch (JSONException e) {
                continue;
            }
        }

        double[][] locationsArray = new double[geometries.size()][2];
        if (wasFound) {
            for (int i = 0; i < geometries.size(); i++) {
                locationsArray[i][0] = geometries.get(i).getJSONObject("location").getDouble("lat");
                locationsArray[i][1] = geometries.get(i).getJSONObject("location").getDouble("lng");
            }
        }
        //coverts to doubl[][]
        for (int i = 0; i < locationsArray.length; i++) {
            System.out.println(locationsArray[i][0] + ", " + locationsArray[i][1]);
        }
        return locationsArray;
    }
}