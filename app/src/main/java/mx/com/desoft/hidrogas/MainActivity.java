package mx.com.desoft.hidrogas;

import android.app.ActionBar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.desoft.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager view;
    private ViewPagerAdapter viewPagerAdapter;
    private Button btnImprimir;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        view = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(view);
        setSupportActionBar(toolbar);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(view);

        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TapLiquidacionUnidades(), "Liquidaciones");
        viewPagerAdapter.addFragment(new ListaPersonal(), "Personal");
        viewPagerAdapter.addFragment(new ListaPipas(), "Pipas");
        viewPagerAdapter.addFragment(new TapReportes(), "Variación");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                logOut();
                removeSharedPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void logOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences() {
        preferences.edit().clear().apply();
    }


}
