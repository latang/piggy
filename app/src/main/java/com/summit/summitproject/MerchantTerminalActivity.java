package com.summit.summitproject;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MerchantTerminalActivity extends AppCompatActivity {

    private static final String TAG = "MerchantActivity";
    private TextView storeName;
    private EditText customerPhoneNum;
    private EditText sendAmount;
    private Button sendButton;
//    private ProgressBar progressBar;

    private ImageView storeIcon;

    private NfcNdefReader nfcNdefReader;
    private VibrateFeedback tapFeedback;

    private DatabaseReference mDatabase;

    private double currentBalance = 0.0;

    private RadioButton balance;
    private RadioButton storeCredit;

    private void printDbReference(DatabaseReference reference){
        Log.d(TAG, reference.toString());
        Log.d(TAG, "key merchant: " + reference.getKey());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_terminal);

        storeName = findViewById(R.id.storeName);
        storeIcon = findViewById(R.id.imageView3);

        customerPhoneNum = findViewById(R.id.customerID);
        sendAmount = findViewById(R.id.sendAmount);
        sendButton = findViewById(R.id.sendButton);
//        progressBar = findViewById(R.id.progressBar);

        storeCredit = findViewById(R.id.storeCredit);
        balance = findViewById(R.id.addBalance);

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

        mDatabase = FirebaseDatabase.getInstance().getReference("data");

        String phoneNumber = PiggyBApplication.applicationState.phoneNumber;

        DatabaseReference merchantInformation = mDatabase.child("merchantInformation");
        DatabaseReference merchant = merchantInformation.child(phoneNumber);

        merchant.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String value = dataSnapshot.getValue(String.class);
                final String img = (dataSnapshot.getValue(String.class)).replaceAll("\\s", "").toLowerCase();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        storeName.setText("Welcome, " + value);
                        storeIcon.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        final String message = new NfcNdefTagReader().readMessageFromTag(tag);
        if (message != null) {
//            showToast("Message: $message");
//            Log.d("debug","message: " + message);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    customerPhoneNum.setText(message);
                }
            });

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
        sendAmount.addTextChangedListener(textWatcher);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String inputPhoneNum = customerPhoneNum.getText().toString().trim();
                String inputSendAmount = sendAmount.getText().toString().trim();

                final double change = Double.parseDouble(inputSendAmount);

                DatabaseReference customersRef = mDatabase.child("customerInformation");

                if(storeCredit.isChecked()){
                    Log.d(TAG, "storeCredit");

                }
                else{
                    Log.d(TAG,"balance");

                    DatabaseReference testRef = mDatabase.child("customerInformation");

                    final Result result = new Result();

                    testRef.child(inputPhoneNum).child("totalBalance").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && !result.once) {
                                double balance = dataSnapshot.getValue(Double.class);
                                Log.d("test", "initial: " + balance);


                                String currStoreName = storeName.getText().toString();

                                currStoreName = currStoreName.substring(9, currStoreName.length());

                                double newBalance = balance + change;
                                currentBalance = balance;
                                DatabaseReference ref = mDatabase.child("customerInformation");
                                ref.child(inputPhoneNum).child("totalBalance").setValue(newBalance);

                                Log.d("test", "inside: " + newBalance);
                                result.once = true;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                }

               // Log.d("test","after: " + currentBalance);

//                progressBar.setVisibility(View.VISIBLE);

//                DatabaseReference accountsRef = mDatabase.child("customerInformation");
//
//                DatabaseReference newInformation = accountsRef.push();
//
//                newInformation.setValue(new Post(inputPhoneNum,change));

                customerPhoneNum.setText("");
                sendAmount.setText("");

//                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String inputPhoneNum = customerPhoneNum.getText().toString().trim();
            String inputSendAmount = sendAmount.getText().toString().trim();

            boolean enable = inputPhoneNum.length() > 0 && inputSendAmount.length() > 0;

            sendButton.setEnabled(enable);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public class Result {
        public boolean once;

        public Result() {
            once = false;
        }
    }

}
