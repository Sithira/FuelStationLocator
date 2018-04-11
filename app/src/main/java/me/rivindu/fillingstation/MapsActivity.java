package me.rivindu.fillingstation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener
{

	private GoogleMap mMap;
	
	private Context mContext;
	
	private LocationRequest mLocationRequest;
	
	private Location mLastlocation;
	
	private GoogleApiClient mGoogleApiClient;
	
	private double mDistancetraled, mLatitude, mLongitude;
	
	private boolean mLocationupdates = false;

	public static final int REQUEST_LOCATION_CODE =99;

	private Marker currentLocMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
		
		buildGoogleAPIClient();
		
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);


	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		//switch since you might need to add more permissions later on
		switch (requestCode)
		{
			case REQUEST_LOCATION_CODE:
				//permission granted
				if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
				{
					if(mGoogleApiClient == null)
					{
						buildGoogleApiClient();
					}
					mMap.setMyLocationEnabled(true);



				}
				else   //permission denied
				{
					Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
				}
				return;
		}

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
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			buildGoogleApiClient();
			mMap.setMyLocationEnabled(true);

		}

	}

	protected synchronized void buildGoogleApiClient()
	{
		mGoogleApiClient = new GoogleApiClient.Builder(this)
							.addConnectionCallbacks(this)
							.addOnConnectionFailedListener(this)
							.addApi(LocationServices.API)
							.build();

		mGoogleApiClient.connect();
	}
	
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		
		mLocationRequest = LocationRequest.create();
		
		mLocationRequest.setInterval(1000);

		mLocationRequest.setFastestInterval(1000);
		
		mLocationRequest.setSmallestDisplacement(1);
		
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
		else
		{
			System.out.println("NO PERMISSIONS");
		}
		
	}

	public boolean checkLocationPermission()
	{
		if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
		{
			if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
			{
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
			}
			else
			{
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
			}
			return false;



		}
		else
		{
			return true;
		}
	}

	
	@Override
	public void onConnectionSuspended(int i) {
	
	}
	
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
	
	}
	
	@Override
	public void onLocationChanged(Location location) {
		
        /*
		if (location == null)
		{
			Toast.makeText(this, "Location is null", Toast.LENGTH_LONG).show();
		}
		else
		{

			mLatitude = location.getLatitude();
			
			mLongitude = location.getLongitude();
			
			//System.out.println("LATS ARE : " +  mLatitude + "LGNS : " + mLongitude);
			
			//moveToCurrentLocationZoom(location.getLatitude(), location.getLongitude(), Takzy.mapZoomedValue);

//			if (mGoogleApiClient != null && !Takzy.locationUpdates)
//			{
//				//LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//
//				//mDistancetraled += location.distanceTo(mLastlocation);
//
//			}
			
			
		}
        */


		mLastlocation = location;

		if(currentLocMarker != null)
		{
			currentLocMarker.remove();
		}

		LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());

		MarkerOptions markerOptions = new MarkerOptions();


		markerOptions.position(latlng);
		markerOptions.title("Your Location");
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		currentLocMarker = mMap.addMarker(markerOptions);

		mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		mMap.animateCamera(CameraUpdateFactory.zoomBy(16.0f));

		if(mGoogleApiClient!= null)
		{
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
		}

		
	}
	
	protected synchronized void buildGoogleAPIClient()
	{
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		
		mGoogleApiClient.connect();
		
	}
}

