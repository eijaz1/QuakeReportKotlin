package com.example.android.quakereport

import android.content.AsyncTaskLoader
import android.content.Context
import android.util.Log
import com.example.android.quakereport.QueryUtils.fetchEarthquakeData

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

/**
 * Constructs a new [EarthquakeLoader].

 * @param context of the activity
 * *
 * @param url to load data from
 */

class EarthquakeLoader(context: Context, val url: String?) : AsyncTaskLoader<ArrayList<Earthquake>>(context) {

    override fun onStartLoading() {

        forceLoad()
    }

    /**
     * This is on a background thread.
     */
    override fun loadInBackground(): ArrayList<Earthquake>? {

        if (url == null) {
            return null
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        var earthquakes: ArrayList<Earthquake> = fetchEarthquakeData(url)
        return earthquakes
    }

    companion object {

        /** Tag for log messages  */
        private val LOG_TAG = EarthquakeLoader::class.java.name
    }
}