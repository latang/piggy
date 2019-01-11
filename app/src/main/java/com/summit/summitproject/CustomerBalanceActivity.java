package com.summit.summitproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.design.widget.NavigationView;
import com.bumptech.glide.Glide;
import com.summit.summitproject.prebuilt.utils.CircleGlide;
import com.summit.summitproject.prebuilt.utils.CustomTypefaceSpan;
import com.summit.summitproject.prebuilt.utils.AppBarStateChangeListener;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Customer;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;
import com.summit.summitproject.prebuilt.login.LoginListener;
import com.summit.summitproject.prebuilt.login.LoginManager;
import com.summit.summitproject.prebuilt.model.Transaction;

import org.w3c.dom.Text;


public class CustomerBalanceActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CustomerActivity";


    private DrawerLayout mDrawerLayout;
    private DatabaseReference mDatabase;

    private String customerPhoneNum;
    private TextView balance;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_balance);
        //setContentView(R.layout.nav_header_main);

        setupToolbar(R.id.toolbar, "COOK IT", R.color.colorPink, R.color.colorWhiteTrans, R.drawable.ic_burger);

        FragmentTransaction ft;
        FragmentHome fragmentHome = new FragmentHome();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragmentHome).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //customerPhoneNum = findViewById(R.id.username);
        customerPhoneNum = PiggyBApplication.applicationState.phoneNumber;

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_burger);

        mDrawerLayout = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        balance = headerView.findViewById(R.id.BalanceView);
        name = headerView.findViewById(R.id.CustomerName);
        Log.d(TAG, "name" + name +" balance: " + balance);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.search) {
                            // Handle the camera action
                        } else if (id == R.id.redeem) {

                        } else if (id == R.id.send) {

                        } else if (id == R.id.sign_out) {
                            startActivity(new Intent(CustomerBalanceActivity.this, LoginActivity.class));
                        } else if (id == R.id.setting) {

                        }
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });



        View header = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) header.findViewById(R.id.imageView);
        Glide.with(this)
                .load(Uri.parse("https://cdn3.iconfinder.com/data/icons/avatars-9/145/Avatar_Pig-512.png"))
                .transform(new CircleGlide(this))
                .into(imageView);

        // Firebase stuff
        mDatabase = FirebaseDatabase.getInstance().getReference("data");

        mDatabase.child("customerInformation").child(customerPhoneNum).addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String firstName = (String) dataSnapshot.child("mFirstName").getValue();
                final String lastName = (String) dataSnapshot.child("mLastName").getValue();
                final Double b = (Double) dataSnapshot.child("totalBalance").getValue();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI

                    //name.setText(getString(R.string.CustomerName, firstName, lastName));
                    //balance.setText(getString(R.string.balance, b));
                    name.setText(firstName + " " + lastName);
                    balance.setText("$" + String.valueOf((double)Math.round(b*100)/100));
                }
            });
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Retrieve failed.");
            }
        });





//                            for(DataSnapshot store: dataSnapshot.getChildren()){
//                                String storeKey = store.getKey();
//                                double storeVal = store.getValue(Double.class);
//                                Log.d(“test”, “key: ” + storeKey + “, val: ” + storeVal);
//
//                            }





        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Respond when the drawer's position changes
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Respond when the drawer is opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Respond when the drawer is closed
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Respond when the drawer motion state changes
            }
        });



        //final String phoneNumber = PiggyBApplication.applicationState.phoneNumber;

//        String customerNum = customerPhoneNum.getText().toString();
//        System.out.println(customerNum);

        //Log.d(TAG, customerPhoneNum.toString());

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            // Handle the camera action
        } else if (id == R.id.redeem) {

        } else if (id == R.id.send) {

        } else if (id == R.id.sign_out) {

        } else if (id == R.id.setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Semibold.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}