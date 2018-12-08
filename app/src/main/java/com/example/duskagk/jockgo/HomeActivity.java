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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appbar;

    List<String> li;
    InputMethodManager imm;
    String[] lli=new String[]{"C언어","웹프로그래밍","수학"};
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
        pref.getString("hi", "");

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_regist) {
            AlertDialog.Builder mBulid = new AlertDialog.Builder(HomeActivity.this);
//            imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

            final ArrayAdapter<String> adapter=new ArrayAdapter<String>(HomeActivity.this,android.R.layout.select_dialog_singlechoice);
            adapter.add("수학");
            adapter.add("C언어");
            adapter.add("웹프로그래밍");

            LayoutInflater inf= (LayoutInflater)getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
            final View mv = inf.inflate(R.layout.add_exams, null);
            ListView listView=(ListView)mv.findViewById(R.id.seSub);
            Button bt=(Button)mv.findViewById(R.id.go_add_exam);

            mBulid.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName=adapter.getItem(which);
                    Intent intent=new Intent(HomeActivity.this,add_exam.class);
                    intent.putExtra("Name",strName);
                    startActivity(intent);


//                    AlertDialog.Builder innB=new AlertDialog.Builder(HomeActivity.this);
//                    innB.setMessage("당신이 선택한것은");
//                    innB.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    innB.show();
                }
            });



            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HomeActivity.this,add_exam.class);
                    startActivity(intent);
                }
            });

            mBulid.setView(mv);
            mBulid.create();
            mBulid.show();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
