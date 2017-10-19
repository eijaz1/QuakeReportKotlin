package com.example.android.quakereport

/**
 * Created by eijazallibhai on 2017-09-10.
 */

import android.text.TextUtils
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset


/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
object QueryUtils {

    /** Tag for the log messages  */
    val LOG_TAG = QueryUtils::class.java!!.getSimpleName()


    /**
     * Returns new URL object from the given string URL.
     */
    private fun createUrl(stringUrl: String): URL {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "Problem building the URL ", e)
        }

        return url!!
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    @Throws(IOException::class)
    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse
        }

        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            urlConnection = url!!.openConnection() as HttpURLConnection
            urlConnection!!.setReadTimeout(10000 /* milliseconds */)
            urlConnection!!.setConnectTimeout(15000 /* milliseconds */)
            urlConnection!!.setRequestMethod("GET")
            urlConnection!!.connect()

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection!!.getResponseCode() === 200) {
                inputStream = urlConnection!!.getInputStream()
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection!!.getResponseCode())
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e)
        } finally {
            if (urlConnection != null) {
                urlConnection!!.disconnect()
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream!!.close()
            }
        }
        return jsonResponse
    }

    /**
     * Convert the [InputStream] into a String which contains the
     * whole JSON response from the server.
     */
    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }
        return output.toString()
    }


    /**
     * Return a list of [Earthquake] objects that has been built up from
     * parsing a JSON response.
     */
    fun extractFeatureFromJson(earthquakeJSON: String): List<Earthquake>? {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        val earthquakes: ArrayList<Earthquake> = ArrayList<Earthquake>()

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            val baseJsonResponse = JSONObject(earthquakeJSON)
            val earthquakeArray = baseJsonResponse.getJSONArray("features")

            //loop through each earthquake in the JSON response and pull out its attributes
//            var i = 0
            for (i in 0..earthquakeArray.length() - 1) {
                var currentEarthquake: JSONObject = earthquakeArray.getJSONObject(i)
                val properties = currentEarthquake.getJSONObject("properties")
                val magnitude = properties.getDouble("mag")
                val location = properties.getString("place")
                val time = properties.getLong("time")
                val url = properties.getString("url")

                //create a new earthquake instance and add it to the
                // earthquakes Array List for each earthquake
                val earthquake = Earthquake(magnitude, location, time, url)
                earthquakes.add(earthquake)
            }

        } catch (e: JSONException) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e)
        }

        // Return the list of earthquakes
        return earthquakes
    }

    /**
     * Query the USGS dataset and return a list of [Earthquake] objects.
     */
    fun fetchEarthquakeData(requestUrl: String): ArrayList<Earthquake> {

        // Create URL object
        val url = createUrl(requestUrl)

        // Perform HTTP request to the URL and receive a JSON response back
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e)
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        val earthquakes: ArrayList<Earthquake> = extractFeatureFromJson(jsonResponse!!) as ArrayList<Earthquake>

        // Return the list of {@link Earthquake}s
        return earthquakes
    }

}
/**
 * Create a private constructor because no one should ever create a [QueryUtils] object.
 * This class is only meant to hold static variables and methods, which can be accessed
 * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
 */

