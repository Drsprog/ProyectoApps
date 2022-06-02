package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton btnAgregar;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        btnAgregar=findViewById(R.id.btnAgregar);

        getSupportFragmentManager().beginTransaction().add(R.id.content,new Pagina_Principal()).commit();
        setTitle("Menú Principal");

        setSupportActionBar(toolbar);

        toggle= setUpDrawerToggle();
        drawerLayout.addDrawerListener(toggle);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),AgregarRecordatorio.class);
                startActivity(i);
            }
        });


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.busqueda_menu,menu);
        return super.onCreateOptionsMenu(menu);
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
        switch (item.getItemId()){
            case R.id.nav_home:
                ft.replace(R.id.content,new Pagina_Principal()).commit();
                break;
            case R.id.nav_completados:
                ft.replace(R.id.content,new CompletadosFragment()).commit();
                break;
            case R.id.nav_atrasados:
                ft.replace(R.id.content,new AtrasadosFragment()).commit();
                break;
            case R.id.nav_ajustes:
                ft.replace(R.id.content,new AjustesFragment()).commit();
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