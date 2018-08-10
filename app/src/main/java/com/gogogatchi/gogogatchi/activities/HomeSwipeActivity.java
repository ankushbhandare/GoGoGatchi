package com.gogogatchi.gogogatchi.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gogogatchi.gogogatchi.BuildConfig;
import com.gogogatchi.gogogatchi.Exceptions.LocationException;
import com.gogogatchi.gogogatchi.R;
import com.gogogatchi.gogogatchi.core.GoogleQuery;
import com.gogogatchi.gogogatchi.core.LocationCard;
import com.gogogatchi.gogogatchi.core.LocationData;
import com.gogogatchi.gogogatchi.util.MapUtil;
import com.gogogatchi.gogogatchi.util.Network;
import com.google.gson.Gson;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

public class HomeSwipeActivity extends AppCompatActivity {
    private SwipePlaceHolderView mSwipeView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context mContext;
    private Toolbar appBar;
    private List<LocationData> places;
    private MapUtil mapUtil;
    private String myResponse = null;
    private Location location;
    private List<String> keywords=new ArrayList<String>();
    public static List<LocationData> locationDataList=new ArrayList<>();

    public String getResponse() {
        return myResponse;
    }
    public Context getmContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_home_swipe2);
            mapUtil=new MapUtil(getApplicationContext());
            location=mapUtil.getLocation();
            keywords.add("art");
            mSwipeView = findViewById(R.id.swipeView);
            mContext = getApplicationContext();
            makeHttpCall(location,keywords);

            /*** Begin Menu Code ***/
            appBar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(appBar);
            // getSupportActionBar().setDisplayShowTitleEnabled(false);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
            // actionBar.setIcon(R.drawable.ic_newspaper);
            /*** End Menu Code ***/

            mSwipeView.getBuilder()
                    .setDisplayViewCount(3)
                    .setSwipeDecor(new SwipeDecor()
                            .setPaddingTop(-40)
                            .setPaddingLeft(30)
                            .setRelativeScale(0.01f)
                            .setSwipeInMsgLayoutId(R.layout.location_swipe_right)
                            .setSwipeOutMsgLayoutId(R.layout.location_swipe_left));

            findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeView.doSwipe(false);
                }
            });
            findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeView.doSwipe(true);
                }
            });
            findViewById(R.id.outOfIdeas).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keywords=new ArrayList<>();
                    keywords.add("history");
                    keywords.add("maritime");
                    keywords.add("aeronautical");
                    keywords.add("war");
                    makeHttpCall(location,keywords);

                }
            });

            /*** BEGIN MENU CODE ***/
            NavigationView navigationView = findViewById(R.id.navMenu);
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            // set item as selected to persist highlight
                           // menuItem.setChecked(true);
                            drawerLayout.closeDrawers();
                            switch(menuItem.getItemId()) {
                                case R.id.nav_profile: {
                                    //menuItem.setChecked(false);
                                    Intent intent= new Intent(getApplicationContext(),ProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                                case R.id.nav_settings: {
                                    Intent intent= new Intent(getApplicationContext(),SettingsActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                                case R.id.nav_logout:{
                                    //finishAffinity();
                                    Intent intent = new Intent (getApplicationContext(), SplitHomeActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                                case R.id.nav_feed:
                                {
                                    Intent intent = new Intent (getApplicationContext(), FeedActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                            }
                            return true;
                        }
                    });
        }
        catch(LocationException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage()+" Please relaunch app.",Toast.LENGTH_LONG).show();
        }
        catch(NullPointerException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage()+" Please relaunch app.",Toast.LENGTH_LONG).show();
        }

        /***END MENU CODE ***/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerLayout=findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
        System.exit(0);
    }

    public void makeHttpCall(Location location,List<String> keywords)
    {
        String concatedKeyWords="";
        for(int i=0;i<keywords.size();i++)
        {
            concatedKeyWords+=keywords.get(i);
            if(i!=keywords.size()-1)
                concatedKeyWords+="|";
        }
        String userQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location="
                + String.valueOf(location.getLatitude())+","
                + String.valueOf(location.getLongitude())
                + "&radius=12000"
                + "&type=museum"
                + "&keyword="+concatedKeyWords
                + "&key="
                + BuildConfig.ApiKey;

        /*** HTTP QUERY PLACES API***/
        Network task = new Network(userQuery,this);
        task.execute();
    }

    public void populateCards(String myResponse) {
        Gson gson = new Gson();

        for(LocationData profile : gson.fromJson(myResponse, GoogleQuery.class).getData()) {
            if (profile.getPhoto().isEmpty() == false && profile.getRating() > 3.4f) {
                // If no photo and low rating, don't make a card
                mSwipeView = findViewById(R.id.swipeView);
                mSwipeView.addView(new LocationCard(mContext, profile, mSwipeView));
            }
        }
    }


   /* public class Network extends AsyncTask<Void, Void, Integer> {
        String query;

        public Network(String userQuery) {
            query = userQuery;
        }

        public String queryGooglePlaces(String url) throws IOException, JSONException {
            int response_code;
            HttpsURLConnection con;
            con = (HttpsURLConnection) new URL(url).openConnection();
            con.connect();
            con.setRequestMethod("GET");
            response_code = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = null;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            myResponse = response.toString();
            looper();

            return null;
        }

        public void looper() {
            while(myResponse == null) {}
        }

        @Override
        protected Integer doInBackground(Void... Voids) {
            try {
                queryGooglePlaces(query);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer useless) {

            populateCards();
        }
    }*/
}


