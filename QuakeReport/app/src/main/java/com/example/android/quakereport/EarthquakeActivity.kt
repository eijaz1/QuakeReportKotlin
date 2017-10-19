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

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.util.ArrayList

class EarthquakeActivity : AppCompatActivity() {

    private val USGS_REQUEST_URL: String = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=20"

    /** Adapter for the list of earthquakes  */
    private val mAdapter: EarthquakeAdapter? = null

    //initialize @earthquake_list with type recyclerView
    //iniialize @earthquakes with type ArrayList of Earthquake objects
    lateinit var earthquake_list : RecyclerView
    var earthquakes:ArrayList<Earthquake> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        //setting up RecyclerView to recycle earthquake_list_item in
        // rv_earthquake_list for each earthquake
        earthquake_list = findViewById(R.id.rv_earthquake_list) as RecyclerView
        earthquake_list.layoutManager = LinearLayoutManager(this)
        earthquake_list.adapter = EarthquakeAdapter(earthquakes, this)

        // Start the AsyncTask to fetch the earthquake data
        val task: EarthquakeAsyncTask = EarthquakeAsyncTask()
        task.execute(USGS_REQUEST_URL)

    }

    inner class EarthquakeAsyncTask : AsyncTask<String, Void, ArrayList<Earthquake>>() {

        override fun doInBackground(vararg urls: String?): ArrayList<Earthquake>? {

            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.size < 1 || urls[0] == null) {
                return null
            }

            //list of earthquakes pulled from JSON response
            earthquakes = QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL) as ArrayList<Earthquake>
            return earthquakes
        }

        override fun onPostExecute(earthquakes: ArrayList<Earthquake>?) {
            //setting up RecyclerView to recycle earthquake_list_item in
            // rv_earthquake_list for each earthquake
            earthquake_list = findViewById(R.id.rv_earthquake_list) as RecyclerView
            earthquake_list.layoutManager = LinearLayoutManager(this@EarthquakeActivity)
            earthquake_list.adapter = EarthquakeAdapter(earthquakes!!, this@EarthquakeActivity)

        }

    }

}
