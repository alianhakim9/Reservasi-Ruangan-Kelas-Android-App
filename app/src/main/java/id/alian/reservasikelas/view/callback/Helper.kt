package id.alian.reservasikelas.view.callback

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import com.google.android.material.snackbar.Snackbar
import id.alian.reservasikelas.view.callback.Constants.SHARED_PREF

fun <T> Context.openActivity(
    it: Class<T>,
    nameString: String? = null,
    valueString: String? = null,
    nameInt: String? = null,
    valueInt: Int? = null,
) {
    Intent(this, it).also {
        it.putExtra(nameString, valueString)
        it.putExtra(nameInt, valueInt)
        startActivity(it)
    }
}

internal fun View.shortSnackBar(message: String, action: (Snackbar.() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let { snackbar.it() }
    snackbar.show()
}

fun Context.checkInternetConnection(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

fun Context.getReservasiSharedPref(): SharedPreferences {
    return this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
}
