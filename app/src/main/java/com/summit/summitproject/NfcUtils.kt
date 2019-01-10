package com.summit.summitproject

import android.content.Context
import android.nfc.NfcAdapter
import android.os.Build

class NfcUtils(context: Context) {

    val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context.applicationContext)

    /**
     * Checks if NFC is fully supported for our needs in the current Android version
     *
     * @return boolean
     */
    private val isNfcSupportedByOs: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    /**
     * Checks if NFC is fully supported for our needs in the current Android version
     *
     * @return boolean
     */
    val isNfcSupportedByDevice: Boolean
        get() = isNfcSupportedByOs && nfcAdapter != null

    /**
     * Checks if NFC is enabled on the device
     *
     * @return boolean
     */
    val isNfcEnabled: Boolean
        get() = nfcAdapter != null && nfcAdapter.isEnabled

    val isNfcSupportedAndEnabled: Boolean
        get() = isNfcSupportedByOs && isNfcEnabled

}