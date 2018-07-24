package com.tiara.pokemonappbykotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        loadPockemon()

    }

    var ACCESLOCATION = 123

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESLOCATION)
            return

        }

        GetUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun GetUserLocation() {
        Toast.makeText(this, " user location acces on", Toast.LENGTH_LONG).show() // cara menampilkan toast tanpa anko

        //todo : will implement later
        val myLocation = MyLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
        val myThread = myThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESLOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, " we cannot load your location", Toast.LENGTH_LONG).show()
                }
            }
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
    }

    var location: Location? = null
    //get user location

    inner class MyLocationListener() : LocationListener {

        override fun onLocationChanged(p0: Location?) {
            location = p0

        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        init {
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.longitude = 0.0
        }

    }

    var oldLocation: Location? = null

    inner class myThread() : Thread() {

        override fun run() {
            while (true)
                try {

                    if (oldLocation!!.distanceTo(location) == 0f) {
                        continue
                    }

                    oldLocation = location

                    runOnUiThread {

                        mMap.clear()

                        //script show me
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("Me)")
                                .snippet("Here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bulbasaur)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 2f))

                        //show pockemon
                        for (i in 0..listPockemon.size - 1) {
                            val newPockemon = listPockemon[i]

                            if (newPockemon.isCatch == false) {
                                val pockemonLoc = LatLng(newPockemon.location!!.longitude, newPockemon.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                        .position(pockemonLoc)
                                        .title(newPockemon.name!!)
                                        .snippet(newPockemon.des!! + "Power" + newPockemon.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))

                                if (location!!.distanceTo(newPockemon.location) < 2) {
                                    newPockemon.isCatch = true
                                    listPockemon[i] = newPockemon
                                    playerPower += newPockemon.power!!
                                    Toast.makeText(applicationContext, "you catch new pockemon your new power is" + playerPower,Toast.LENGTH_LONG).show()
                                }
                            }

                        }

                    }

                    Thread.sleep(1000)

                } catch (ex: Exception) {
                }
        }

        init {
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.longitude = 0.0
        }
    }

    var playerPower = 0.0

}

var listPockemon = ArrayList<Pockemon>()

fun loadPockemon() {

    listPockemon.add(Pockemon(R.drawable.charmander, "Charmander", "Charmander living in jappan",
            55.0, 37.7789994893035, -122.4018846647263))
    listPockemon.add(Pockemon(R.drawable.squirtle, "Squirtle", "pingun living in usa",
            90.5, 37.7949568503667, -122.410494089127))
    listPockemon.add(Pockemon(R.drawable.mario, "Super Mario", "mario living in iraq",
            33.5, 37.7816621152613, -122.41225361824))

}
