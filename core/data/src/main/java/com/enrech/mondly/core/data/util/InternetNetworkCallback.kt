package com.enrech.mondly.core.data.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject
import kotlin.properties.Delegates

class InternetNetworkCallback (
    private val connectivityManager: ConnectivityManager
) : ConnectivityManager.NetworkCallback() {

    private val availableNetworkSet by lazy { HashSet<String>() }
    private var networkRequest = NetworkRequest
        .Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private var networkMap = HashMap<String, String>()

    var internetCallback: ((isInternetConnected: Boolean, isWifiConnected: Boolean) -> Unit)? = null

    private var _internetConnectivityState: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    var isInternetConnected: Boolean? by Delegates.observable( null) { _, _, newValue ->
        _internetConnectivityState.value = newValue
    }
        private set

    /**
     * Provides live updates of changes to the internet connection state of the device.
     */
    val internetConnectivityState by ::_internetConnectivityState
    var isWifiConnected = false
        private set

    init {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onAvailable(network: Network) {
        val isWifi = connectivityManager.getNetworkCapabilities(network)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        val netType = if (isWifi == true) "WiFi" else if (isWifi == false) "Mobile Data" else "N/A"

        networkMap[network.toString()] = netType

        availableNetworkSet.add(network.toString())
        isInternetConnected = true
        isWifiConnected = isWifi ?: false
        alertApp()
    }

    override fun onLost(network: Network) {
        availableNetworkSet.remove(network.toString())

        val netType = networkMap[network.toString()]
        val jsonObject = JSONObject()

        if (availableNetworkSet.isEmpty()) {
            jsonObject.put(
                "Disconnected From",
                netType
            )

            isInternetConnected = false
            isWifiConnected = false
            alertApp()
        } else {
            jsonObject.put(
                "Switched to",
                netType
            )
        }
    }

    override fun onUnavailable() {
        isInternetConnected = false
        isWifiConnected = false
        alertApp()
    }

    private fun alertApp() {
        internetCallback?.invoke(isInternetConnected ?: false, isWifiConnected)
    }
}