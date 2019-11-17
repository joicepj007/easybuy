package easybuy.com.easybuy.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import easybuy.com.easybuy.Controller.AppSession;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.util.PermissionUtils;

public class LocationMapActivity extends RootActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener,
        ResultCallback<LocationSettingsResult> {

    protected Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    boolean mIsGeocodercalled;
    private Geocoder mGeocoder;
    protected static final int REQUEST_CHECK_SETTINGS = 2;
    final int PLACE_PICKER_REQUEST=1;
    protected static int PERMISSION_LOCATION_REQUEST_CODE = 100;
    protected LocationRequest mLocationRequest;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected LocationSettingsRequest mLocationSettingsRequest;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 117;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean is_textChanged=false;
    private boolean mAddressRequested;
    private String mAddressOutput;
    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";
    private boolean mPermissionDenied = false;
    LinearLayout ll_back_arrow;
    public static final String MyPREFERENCES_Order = "MyPrefsOrder";
    private SharedPreferences sharedpreferenceOrder;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_map_activity);

        AppSession.getInstance().setOrderListFlag(true);

        ll_back_arrow =(LinearLayout)findViewById(R.id.ll_back_arrow);
        sharedpreferenceOrder = getSharedPreferences(MyPREFERENCES_Order, Context.MODE_PRIVATE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        getLocationAddress();
        location=sharedpreferenceOrder.getString("CustomerAddress","") + " ," + sharedpreferenceOrder.getString("CustomerCity","");
//Toast.makeText(getApplicationContext(), (CharSequence) mCurrentLocation,Toast.LENGTH_LONG).show();

        ll_back_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LocationMapActivity.this,OrderActivity.class);
                // i.putExtra("to","account");
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PLACE_PICKER_REQUEST:
                        Place place = PlacePicker.getPlace(data, this);
                        *//*mPlaceName = place.getName().toString();
                        mAddress = place.getAddress().toString();
                        latlongt=place.getLatLng().toString();
                        loca= String.valueOf(place.getLatLng().latitude);
                        longt= String.valueOf(place.getLatLng().longitude);
                        lolo =place.getLatLng();
                        edt_Current_Location.setText(mPlaceName);
                        edt_Address.setText(mAddress);*//*

                        try {
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions()
                                    .position(place.getLatLng()));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                    new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)).zoom(16).build();

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
            else if(requestCode == REQUEST_CHECK_SETTINGS)
            {

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        getLocationAddress();
                        break;
                }

            }
        }*/
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                //  Log.i(TAG, "Place:" + place.toString());
                String address = String.format("%s", place.getAddress());
               // search.setText(address);
               // searchLocation();
                is_textChanged=false;
               // AppSession.getInstance().setLOCATION(address);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void getLocationAddress() {

        // if (mGoogleApiClient == null) {


        if (checkSDK()) {
            if (checkPermission(LocationMapActivity.this))
                checkLocationSettings();

            else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_LOCATION_REQUEST_CODE);
            }
        } else {

            checkLocationSettings();
        }

        //}


    }

    private void checkLocationSettings() {

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkSDK() {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        return currentapiVersion >= 23;

    }

    private void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGeocoder = new Geocoder(this, Locale.getDefault());

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (mCurrentLocation == null) {
            try {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                updateLocationUI();
            } catch (SecurityException se) {

            }
        }
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            List<Address> addressList = null;
            String edt_location = String.valueOf(location);
            if (edt_location != "") {
                Geocoder geocoder = new Geocoder(LocationMapActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(edt_location, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                if (addressList.size() != 0) {

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(edt_location));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(address.getLatitude(), address.getLongitude())).zoom(16).build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    Toast.makeText(getApplicationContext(), "Currently no location", Toast.LENGTH_LONG).show();
                }

                // Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
            } else if (edt_location == "") {
                Toast.makeText(getApplicationContext(), "Currently location is not getting", Toast.LENGTH_LONG).show();
            }

            if (!mIsGeocodercalled) {
                getLocationAddressTask task = new getLocationAddressTask();
                task.execute();
            }
           /* latitude = String.valueOf(mCurrentLocation.getLatitude());
            longitude = String.valueOf(mCurrentLocation.getLongitude());


            try {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).zoom(16).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("getLocationAddressTask", "ms1: call 1");

            if(!mIsGeocodercalled){
                getLocationAddressTask task = new getLocationAddressTask();
                task.execute();
            }*/

           /* AppLog.logString("onConnected has getLastBestLocation mLastLocation!=null" + latitude + longitude);
            getLocationAddressTask task = new getLocationAddressTask();
            task.execute();*/
           /* mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
                    mLastUpdateTime));*/
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }


        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                //Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                // getLocationDetails();
                checkLocationSettings();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(LocationMapActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }

    }

    private void startLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest,
                    LocationMapActivity.this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    // mRequestingLocationUpdates = true;

                }
            });
        } catch (SecurityException se) {

        }

    }

    @Override
    public void onLocationChanged(Location location) {


        mCurrentLocation = location;

        updateLocationUI();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

    }

    private class getLocationAddressTask extends AsyncTask<Void, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(Void... voids) {
            String strAddress = null;
            Log.d("getLocationAddressTask", "ms1: ");
            List<Address> addresses = null;

            try {
                // Using getFromLocation() returns an array of Addresses for the area immediately
                // surrounding the given latitude and longitude. The results are a best guess and are
                // not guaranteed to be accurate.
                addresses = mGeocoder.getFromLocation(
                        mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude(),
                        // In this sample, we get just a single address.
                        1);

                //  Log.d("getLocationAddressTask", "ms2: " + addresses.size() + "@@" + addresses.get(0).getAddressLine(0));

            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (IllegalArgumentException illegalArgumentException) {
                illegalArgumentException.printStackTrace();


            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            try {
                if (addresses.size() > 0) {
                    mIsGeocodercalled = true;
                    /*mPlaceName = addresses.get(0).getLocality();
                    mAddress = addresses.get(0).getAddressLine(0);
                    mStreet=addresses.get(0).getSubAdminArea();
                    mState=addresses.get(0).getAdminArea();
                    mTest=addresses.get(0).getPremises();*/
                    // tv_location.setText(mTest +"," + mPlaceName+", "+ mStreet );

                    //  mAddressString = s.replace("\n", "");  // s.replaceAll("^\\s+", "");

                }
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(LocationMapActivity.this,OrderActivity.class);
        // i.putExtra("to","account");
        startActivity(i);
        finish();


    }
}
