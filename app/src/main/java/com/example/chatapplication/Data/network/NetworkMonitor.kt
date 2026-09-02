package com.example.chatapplication.Data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkMonitor(
    context: Context
) {

    private val connectivityManager =
        context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

    private val _isConnected =
        MutableStateFlow(checkInitialConnection())

    val isConnected: StateFlow<Boolean>
        get() = _isConnected

    // ------------------------------------------------------------
    // RECONNECT EVENT
    // Every time we go:
    //
    // OFFLINE -> ONLINE
    //
    // this value increases.
    // ------------------------------------------------------------

    private val _networkRestored =
        MutableStateFlow(0)

    val networkRestored: StateFlow<Int>
        get() = _networkRestored

    private var callbackRegistered = false

    private val networkCallback =
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {

                println(
                    "NETWORK MONITOR: INTERNET AVAILABLE"
                )

                val wasConnected =
                    _isConnected.value

                _isConnected.value = true

                // ------------------------------------------------
                // IMPORTANT
                // Only trigger when the actual transition is:
                //
                // OFFLINE -> ONLINE
                // ------------------------------------------------

                if (!wasConnected) {

                    println(
                        "NETWORK MONITOR: OFFLINE -> ONLINE"
                    )

                    _networkRestored.value =
                        _networkRestored.value + 1
                }
            }

            override fun onLost(network: Network) {

                println(
                    "NETWORK MONITOR: INTERNET LOST"
                )

                _isConnected.value = false
            }
        }

    private fun checkInitialConnection(): Boolean {

        val network =
            connectivityManager.activeNetwork
                ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(
                network
            )
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }

    fun start() {

        if (callbackRegistered) {

            println(
                "NETWORK MONITOR: ALREADY STARTED"
            )

            return
        }

        connectivityManager.registerDefaultNetworkCallback(
            networkCallback
        )

        callbackRegistered = true

        println(
            "NETWORK MONITOR: STARTED"
        )

        println(
            "NETWORK MONITOR: INITIAL CONNECTION = " +
                    _isConnected.value
        )
    }

    fun stop() {

        if (!callbackRegistered) {
            return
        }

        connectivityManager.unregisterNetworkCallback(
            networkCallback
        )

        callbackRegistered = false

        println(
            "NETWORK MONITOR: STOPPED"
        )
    }
}