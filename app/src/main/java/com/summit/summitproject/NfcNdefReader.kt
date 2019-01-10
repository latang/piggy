package com.summit.summitproject

import android.Manifest.permission.NFC
import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
class NfcNdefReader(
    context: Context,
    private val nfcReaderCallback: NfcAdapter.ReaderCallback
) {

    var nfcUtils: NfcUtils = NfcUtils(context.applicationContext)
 
    @RequiresPermission(NFC)
    fun onResume(activity: Activity) {
        enableReaderMode(activity)
    }

    fun onPause(activity: Activity) {
        nfcUtils.nfcAdapter?.disableReaderMode(activity)
    }

    private fun enableReaderMode(activity: Activity) {
        if (nfcUtils.isNfcSupportedAndEnabled) {
            nfcUtils.nfcAdapter?.enableReaderMode(activity, { tag ->
                nfcReaderCallback.onTagDiscovered(tag)
                enableReaderMode(activity)
            },
                READER_FLAGS, null)
        }
    }

    companion object {

        // reader mode flags: listen for type A (not B), skipping ndef check (NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK)
        private const val READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS
    }
}