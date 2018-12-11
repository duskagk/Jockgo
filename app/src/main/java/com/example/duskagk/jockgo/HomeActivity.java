package com.example.duskagk.jockgo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.lang.reflect.Array;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Integer> b_no = new ArrayList<>();

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
//
//    List<String> headerList = new ArrayList<>();
//    HashMap<String, List<String>> childList = new HashMap<>();

    ArrayList<String> headerList;
    HashMap<String,List<String>> childList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readUser();

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewP);
        ViewAdapter adt=new ViewAdapter(getSupportFragmentManager());
        adt.AddFragment(new Fragcloud(),"클라우드");
        adt.AddFragment(new Fraghome(),"홈");
        adt.AddFragment(new Fragstudyline(),"스터디라인");
        viewPager.setAdapter(adt);
        tabLayout.setupWithViewPager(viewPager);


        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void readUser() {
        MyApplication myApp = (MyApplication)getApplication();
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        if (pref.getBoolean("login", false)){
            myApp.setLogin(true);
            myApp.setNo(pref.getInt("no", -1));
            myApp.setName(pref.getString("name", null));
            myApp.setSchool(pref.getString("school", null));
        }

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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareMenuData() {

        headerList = new ArrayList<String>();
        childList = new HashMap<String, List<String>>();
//        mChildListContent = new ArrayList<string>();


        headerList.add("정보변경");
//        childList.put(headerList(0),null);


        headerList.add("등록");

        List<String> 등록=new ArrayList<>();
        등록.add("문제등록");
        등록.add("과목등록");



        childList.put(headerList.get(1),등록);

    }


    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Toast.makeText(
                        v.getContext(),
                        headerList.get(groupPosition)
                                + " : " + childList.get(headerList.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
                        .show();

                Navigation(childList.get(headerList.get(groupPosition)).get(childPosition));

                return false;
            }
        });
    }

public void Navigation(String nav){
        if(nav=="문제등록"){
            AlertDialog.Builder mBulid = new AlertDialog.Builder(HomeActivity.this);

            NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/book", null, "GET");
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item);
            try {
                String result = networkTask.execute().get();
                JSONArray jsonArray=new JSONArray(result);
                for(int i = 0; i< jsonArray.length(); i++){
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    adapter.add(jsonObj.getString("b_name"));
                    b_no.add(jsonObj.getInt("b_no"));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LayoutInflater inf= (LayoutInflater)getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
            final View mv = inf.inflate(R.layout.add_exams, null);
            ListView listView=(ListView)mv.findViewById(R.id.seSub);


            mBulid.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName=adapter.getItem(which);
                    Intent intent=new Intent(HomeActivity.this,add_exam.class);
                    intent.putExtra("name",adapter.getItem(which));
                    intent.putExtra("num", b_no.get(which));
                    startActivity(intent);
                }
            });

            mBulid.setView(mv);
            mBulid.create();
            mBulid.show();

        }else if(nav=="과목등록"){
            Intent intent=new Intent(HomeActivity.this,add_subject.class);
            startActivity(intent);

        }
}






    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_regist) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
