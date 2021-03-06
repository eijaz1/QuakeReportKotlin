/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport

import android.content.Loader
import android.app.LoaderManager.LoaderCallbacks
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.earthquake_activity.*
import java.util.ArrayList

class EarthquakeActivity : AppCompatActivity(), LoaderCallbacks<ArrayList<Earthquake>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    val EARTHQUAKE_LOADER_ID: Int = 1
    val USGS_REQUEST_URL: String = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=20"

    /** Tag for the log messages  */
    val LOG_TAG = EarthquakeActivity::class.java!!.getSimpleName()

    //initialize @earthquake_list with type recyclerView
    //iniialize @earthquakes with type ArrayList of Earthquake objects
    lateinit var rvEarthquakeList: RecyclerView
    var earthquakes:ArrayList<Earthquake> = ArrayList()

    // Initialize empty text view and progress bar
    lateinit var tvEmptyView: TextView
    lateinit var pbLoadingIndicator: ProgressBar

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<ArrayList<Earthquake>> {

        val result = EarthquakeLoader(this, USGS_REQUEST_URL)
        return result
    }

    override fun onLoadFinished(p0: Loader<ArrayList<Earthquake>>?, earthquakes: ArrayList<Earthquake>?) {

        pbLoadingIndicator = pb_loading_indicator
        pbLoadingIndicator!!.visibility = View.GONE

        if (earthquakes !=null && !earthquakes.isEmpty()) {

            //setting up RecyclerView to recycle earthquake_list_item in
            // rv_earthquake_list for each earthquake
            rvEarthquakeList = findViewById(R.id.rv_earthquake_list) as RecyclerView
            rvEarthquakeList.layoutManager = LinearLayoutManager(this@EarthquakeActivity)
            rvEarthquakeList.adapter = EarthquakeAdapter(earthquakes, this@EarthquakeActivity)

        } else {

            // Set empty state text to display "No earthquakes found."
            tvEmptyView = tv_empty_view
            tvEmptyView!!.text = "No earthquakes found"
            tvEmptyView!!.visibility = View.VISIBLE
        }
    }

    override fun onLoaderReset(p0: Loader<ArrayList<Earthquake>>?) {

        earthquakes.clear()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get details on the currently active default data network
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected()) {

            // Interact with loader
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this)

        } else {

            // Remove loading indicator view and show empty view with no internet connection
            pbLoadingIndicator = pb_loading_indicator
            pbLoadingIndicator!!.visibility = View.GONE

            tvEmptyView = tv_empty_view
            tvEmptyView!!.text = "No internet connection"
            tvEmptyView!!.visibility = View.VISIBLE
        }
    }

}
