package com.summit.summitproject;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MerchantTerminalActivity extends AppCompatActivity {

    private EditText customerPhoneNum;
    private EditText sendAmount;
    private Button sendButton;
    private ProgressBar progressBar;

    private NfcNdefReader nfcNdefReader;
    private VibrateFeedback tapFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_terminal);

        customerPhoneNum = findViewById(R.id.customerID);
        sendAmount = findViewById(R.id.sendAmount);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);

        nfcNdefReader = new NfcNdefReader(
                this,
                new NfcAdapter.ReaderCallback(){
                    @Override
                    public void onTagDiscovered(Tag tag) {
                        tapFeedback.vibrate();
                        handleTaggedDiscover(tag);
                    }
                });

        tapFeedback = new VibrateFeedback(this);

        setUpWidgets();
    }

    private void showToast(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleTaggedDiscover(Tag tag) {
        String message = new NfcNdefTagReader().readMessageFromTag(tag);
        if (message != null) {
            showToast("Message: $message");
            customerPhoneNum.setText(message);
        } else {
            showToast("Message was not valid");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcNdefReader.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcNdefReader.onResume(this);
    }

    private void setUpWidgets(){

    }


}
