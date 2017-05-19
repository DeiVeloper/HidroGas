package mx.com.desoft.hidrogas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

import mx.com.desoft.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager view;
    private ViewPagerAdapter viewPagerAdapter;
    private SharedPreferences preferences;
    private ImportarDatos importarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        view = (ViewPager) findViewById(R.id.viewpager);
        Bundle bundle;
        bundle = getIntent().getExtras();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(view);
        int position = 0;
        if(bundle != null) {
            if (bundle.containsKey("viewpager_position")) {
                position = bundle.getInt("viewpager_position");
            }
        }
        view.setCurrentItem(position);
        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        importarDatos = new ImportarDatos();

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TapLiquidacionUnidades(), "Liquidar");
        viewPagerAdapter.addFragment(new ListaPersonal(), "Personal");
        viewPagerAdapter.addFragment(new ListaPipas(), "Pipas");
        viewPagerAdapter.addFragment(new TapReportes(), "Variación");
        viewPagerAdapter.addFragment(new ListaLiquidaciones(), "Liquidaciones");
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
                try {
                    LoginActivity.conexionBlueTooth.closeBT();
                }catch (IOException ioe){
                    Toast.makeText(view.getContext(), "Error cerrando conexion BlueTooth" + ioe.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Error","Cerrando Conexion",ioe);
                }
                return true;
            case R.id.impotarPipas:
                try {
                    importarDatos.importarPipas();
                    Toast.makeText(view.getContext(), "Se importaron los datos con éxito", Toast.LENGTH_LONG).show();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Error al importar datos," + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.impotarEmpleados:
                try {
                    importarDatos.importarEmpleados();
                    Toast.makeText(view.getContext(), "Se importaron los datos con éxito", Toast.LENGTH_LONG).show();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Error al importar datos," + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.impotarClaves:
                try {
                    importarDatos.importarClavesPipas();
                    Toast.makeText(view.getContext(), "Se importaron los datos con éxito", Toast.LENGTH_LONG).show();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Error al importar claves," + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.activarImpresora:
                try{
                    LoginActivity.conexionBlueTooth.findBT();
                    LoginActivity.conexionBlueTooth.openBT();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Error al conectar con la impresora," + e.getMessage(), Toast.LENGTH_LONG).show();
                }
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
