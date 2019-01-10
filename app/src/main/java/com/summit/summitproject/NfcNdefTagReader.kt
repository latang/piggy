package com.summit.summitproject

import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.Arrays

class NfcNdefTagReader() {

    @Throws(UnsupportedEncodingException::class)
    private fun readMessageFromNdefRecord(record: NdefRecord): String? {
        val payload = record.payload

        val fullMessage = String(payload, Charset.forName("UTF-8"))

        return fullMessage.substring(3,fullMessage.length)
    }

    fun readMessageFromTag(tag: Tag): String? {
        var message: String? = null

        val techList = tag.techList

        if (techList != null) {
            for (str in techList)
                logIt("Tech: '$str'")
        }

        val ndef = Ndef.get(tag)

        if (ndef == null) {
            // NDEF is not supported by this Tag.
            logIt("NDEF is not supported by this Tag.")
        } else {
            val records: Array<NdefRecord>

            logIt("Got Ndef: type= '" + ndef.type + "'")
            val ndefMessage = ndef.cachedNdefMessage
            if (ndefMessage != null) {
                logIt("Got ndefMessage: describeContents= " + ndefMessage.describeContents() + ", byteArrayLength= " + ndefMessage.byteArrayLength + ", toString= " + ndefMessage.toString())

                records = ndefMessage.records
                logIt("Got records: length= " + records.size)

                if (records.isNotEmpty()) {
                    for (ndefRecord in records) {

                        if (ndefRecord.tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(
                                ndefRecord.type,
                                NdefRecord.RTD_TEXT
                            )
                        ) {
                            logIt("ActivateNewCard: Got a Text Record")

                            try {
                                message = readMessageFromNdefRecord(ndefRecord)
                                logIt("text= '" + message!!.trim { it <= ' ' } + "'")
                            } catch (e: Exception) {
                                logIt(e, "readMessageFromTag()")
                            }

                        }
                    }
                } else {
                    logIt("No Records!!!")
                }
            } else {
                logIt("No ndefMessage!!!")
            }
        }
        return message
    }

    private fun logIt(e: Throwable?, message: String) {
        Log.d("tagReader",message)
    }

    private fun logIt(message: String) {
        logIt(null, message)
    }
}