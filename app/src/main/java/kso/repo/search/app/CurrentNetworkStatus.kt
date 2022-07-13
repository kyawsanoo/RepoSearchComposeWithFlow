package kso.repo.search.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CurrentNetworkStatus : ConnectivityManager.NetworkCallback() {

    //private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    //Returns network connectivity state
    fun getNetwork(context: Context) : Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), this)
        }

        var isConnected = false

        // Retrieve current status of connectivity
        /*connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    isConnected = if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.d("CurrentNetworkStatus", "wifi connected")
                        true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.d("CurrentNetworkStatus", "cellular network connected")
                        true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        Log.d("CurrentNetworkStatus", "VPN network connected")
                        true
                    } else {
                        Log.d("CurrentNetworkStatus", "internet not connected")
                        false
                    }
                }
            }
        }
        return isConnected
    }



    //Returns instance of [LiveData] which can be observed for network changes.
    /*fun getNetworkLiveData(context: Context): LiveData<Boolean> {

        networkLiveData.postValue(getNetwork(context))

        return networkLiveData
    }

    override fun onAvailable(network: Network) {
        networkLiveData.postValue(true)
    }

    override fun onLost(network: Network) {
        networkLiveData.postValue(false)
    }
    */
}
