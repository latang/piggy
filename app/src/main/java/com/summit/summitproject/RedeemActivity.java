package com.summit.summitproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RedeemActivity extends AppCompatActivity {

    private Button redeemButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        mDatabase = FirebaseDatabase.getInstance().getReference("data");
        redeemButton = findViewById(R.id.redeem_button);

        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference testRef = mDatabase.child("customerInformation");

                final RedeemActivity.Result result = new RedeemActivity.Result();

                final String phoneNum = PiggyBApplication.applicationState.phoneNumber;
                final String companyName = PiggyBApplication.applicationState.merchant;

                testRef.child(phoneNum).child("Stores").child(companyName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && !result.once) {
                            double balance = dataSnapshot.getValue(Double.class);

                            if(balance - 5.0 >= 0){
                                double newBalance = balance - 5.0;
                                DatabaseReference ref = mDatabase.child("customerInformation");

                                double rounded = (double)Math.round(newBalance*100)/100;
                                ref.child(phoneNum).child("Stores").child(companyName).setValue(rounded);
                                result.once = true;

                                showToast("Congratulations! You have $ " + rounded + " worth of store credit left.");
                            }
                            else{
                                showToast("Not enough store credit to redeem");
                            }

                            Intent intent = new Intent(RedeemActivity.this, CustomerBalanceActivity.class);
                            //                            intent.putExtra("phoneNumber",inputtedUsername);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

    }

    private void showToast(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class Result {
        public boolean once;

        public Result() {
            once = false;
        }
    }
}
