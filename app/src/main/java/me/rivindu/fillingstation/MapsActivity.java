package me.rivindu.fillingstation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.LatLng;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		buildGoogleAPIClient();
		
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
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng(-34, 151);
		mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
	}
	
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		
		mLocationRequest = LocationRequest.create();
		
		mLocationRequest.setInterval(1000);
		
		mLocationRequest.setSmallestDisplacement(1);
		
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
		else
		{
			System.out.println("NO PERMS");
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
