package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.proyectoapps.Adaptadores.AdapterRecordatorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton btnAgregar;


    ActionBarDrawerToggle toggle;
    Fragment Principal,Completados,Atrasados,Ajustes,Ayuda;
//    Fragment FragActual=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        btnAgregar=findViewById(R.id.btnAgregar);

        String valor=getIntent().getStringExtra("valor");
        if(valor!=null){
            getSupportFragmentManager().beginTransaction().add(R.id.content,new CompletadosFragment()).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().add(R.id.content,new Pagina_Principal()).commit();
        }
        setTitle("Menú Principal");

        setSupportActionBar(toolbar);

        toggle= setUpDrawerToggle();
        drawerLayout.addDrawerListener(toggle);

        Principal= new Pagina_Principal();
        Completados= new CompletadosFragment();
        Atrasados= new AtrasadosFragment();
        Ayuda= new AyudaFragment();
        Ajustes= new AjustesFragment();


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),AgregarRecordatorio.class);
                startActivity(i);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }



    private ActionBarDrawerToggle setUpDrawerToggle(){
        return new ActionBarDrawerToggle(
                this, drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);
        return true;
    }

    private void selectItemNav(MenuItem item) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
//        boolean Seleccionado=false;
        switch (item.getItemId()){
            case R.id.nav_home:{
                ft.replace(R.id.content,new Pagina_Principal()).commit();
//                FragActual= new Pagina_Principal();
//                Seleccionado=true;
//                if(Seleccionado){
//                    getSupportFragmentManager().beginTransaction().replace(R.id.content,FragActual).commit();
//                }
                break;
            }
            case R.id.nav_completados:
                ft.replace(R.id.content,new CompletadosFragment()).commit();
                break;
            case R.id.nav_atrasados:
                ft.replace(R.id.content,new AtrasadosFragment()).commit();
                break;
            case R.id.nav_ayuda:
                ft.replace(R.id.content,new AyudaFragment()).commit();
                break;
        }
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent i= new Intent(Menu.this,Perfil.class);
        switch (item.getItemId()){
            case R.id.btnPerfil:
                startActivity(i);
             return true;
        }
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}