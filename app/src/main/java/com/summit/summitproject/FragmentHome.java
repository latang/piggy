package com.summit.summitproject;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ContextWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import 	android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.summit.summitproject.prebuilt.recycler.ItemRecipe;
import com.summit.summitproject.prebuilt.recycler.RecipeAdapter;
import com.summit.summitproject.prebuilt.recycler.RecyclerTouchListener;
import com.summit.summitproject.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Dytstudio.
 */

public class FragmentHome extends Fragment {
    private List<ItemRecipe> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecipeAdapter mAdapter;
    private AppCompatActivity appCompatActivity;
    private Activity rootRoot;

    public FragmentHome(Activity root){
        setHasOptionsMenu(true);
        rootRoot = root;
    }
    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);

        ((CustomerBalanceActivity)getActivity()).setupToolbar(R.id.toolbar, "STORE CREDIT", R.color.colorPink, R.color.colorWhiteTrans, R.drawable.ic_burger);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //start get firebase data

        String phoneNumber = PiggyBApplication.applicationState.phoneNumber;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("data");
        mDatabase.child("customerInformation").child(phoneNumber).child("Stores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final ArrayList<String> stores = new ArrayList<String>();
                    ArrayList<Double> storeBalance = new ArrayList<Double>();

                    HashMap<String, String> merchantLogos = new HashMap<String, String>();

                    merchantLogos.put("Capital One", "https://media.licdn.com/dms/image/C4E0BAQH1WUsgUQF5uQ/company-logo_200_200/0?e=2159024400&v=beta&t=d7N3nQtFNKTpmLVd0NCCA5Y7NpZiw0Aoy1GGheQy2FY");
                    merchantLogos.put("Starbucks", "https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/0002/1075/brand.gif?itok=Y4thjsx8");
                    merchantLogos.put("CVS", "https://d1yjjnpx0p53s8.cloudfront.net/styles/logo-thumbnail/s3/0001/9828/brand.gif?itok=jH0GIqpO");
                    merchantLogos.put("Target", "https://natific.com/wp-content/uploads/2018/09/1-target-logo.jpg");
                    merchantLogos.put("Best Buy", "https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/0023/5388/brand.gif?itok=6YcMRAjS");

                    for (DataSnapshot store : dataSnapshot.getChildren()) {
                        stores.add(store.getKey());
                        storeBalance.add(store.getValue(Double.class));
                    }

                    ArrayList<ItemRecipe> itemStores = new ArrayList<>();
                    for(int i = 0; i < stores.size(); i++) {
                        ItemRecipe item = new ItemRecipe();
                        item.setRecipe(stores.get(i));
                        item.setImg(merchantLogos.get(stores.get(i)));

                        double rounded = (double)Math.round(storeBalance.get(i)*100)/100;
                        item.setTime(rounded);
                        itemStores.add(item);
                    }

                    mAdapter = new RecipeAdapter(itemStores, getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                    mLayoutManager.setAutoMeasureEnabled(true);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                    appCompatActivity = (AppCompatActivity) getActivity();

                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Log.d("test",position + "");

                            String company = stores.get(position);

                            PiggyBApplication.applicationState.merchant = company;

                            startActivity(new Intent(rootRoot, RedeemActivity.class));
                            //Detail.navigate(appCompatActivity, view.findViewById(R.id.iv_recipe));
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));

                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }

        });

//        mAdapter = new RecipeAdapter(setupRecipe(), getActivity());
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
//        mLayoutManager.setAutoMeasureEnabled(true);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//        appCompatActivity = (AppCompatActivity) getActivity();

        return view;
    }

    private List<ItemRecipe> setupRecipe(){
        itemList = new ArrayList<>();
        String recipe[] = {"BLOOD ORANGE CAKE", "SEMIFREDDO TIRAMISU", "MARBLE CAKE", "RICE PUDDING", "RAINBOW CAKE", "ICE CREAM", "STROWBERRY CAKE", "CUPCAKE FRUIT"};
        String img[] = {"https://images.pexels.com/photos/53468/dessert-orange-food-chocolate-53468.jpeg?h=350&auto=compress&cs=tinysrgb",
                "https://images.pexels.com/photos/159887/pexels-photo-159887.jpeg?h=350&auto=compress",
                "https://images.pexels.com/photos/136745/pexels-photo-136745.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb",
                "https://images.pexels.com/photos/39355/dessert-raspberry-leaf-almond-39355.jpeg?h=350&auto=compress&cs=tinysrgb",
                "https://images.pexels.com/photos/239578/pexels-photo-239578.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb",
                "https://images.pexels.com/photos/8382/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb",
                "https://images.pexels.com/photos/51186/pexels-photo-51186.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb",
                "https://images.pexels.com/photos/55809/dessert-strawberry-tart-berry-55809.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb"};
//        String time[] = {"1h 5'", "30m", "1h 10'", "50m", "20m", "1h 20'", "20m", "1h 20'"};
//        float rating[] = {3, 4, 4, 3, 5, 4, 4, 3};

        for (int i = 0; i<recipe.length; i++){
            ItemRecipe item = new ItemRecipe();
            item.setRecipe(recipe[i]);
//            item.setTime(time[i]);
//            item.setRating(rating[i]);
            item.setImg(img[i]);
            itemList.add(item);
        }
        return itemList;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
    }

    public void testing(){

        String phoneNumber = PiggyBApplication.applicationState.phoneNumber;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("data");
        mDatabase.child("customerInformation").child(phoneNumber).child("Stores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> stores = new ArrayList<String>();
                    ArrayList<Double> storeBalance = new ArrayList<Double>();

                    for (DataSnapshot store : dataSnapshot.getChildren()) {
                        stores.add(store.getKey());
                        storeBalance.add(store.getValue(Double.class));
                    }

                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }

        });
    }
}
