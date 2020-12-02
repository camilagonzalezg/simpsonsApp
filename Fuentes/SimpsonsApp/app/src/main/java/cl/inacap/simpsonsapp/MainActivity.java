package cl.inacap.simpsonsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.inacap.simpsonsapp.adapters.CitasListAdapter;
import cl.inacap.simpsonsapp.dto.Cita;

public class MainActivity extends AppCompatActivity {

    //Referencia al listview de citas
    private ListView listViewCitas;
    //Referencia al Spinner
    private Spinner spNumeroCitas;
    //Referencia al botón
    private Button solicitarCitasBtn;
    //Referencia cola (volley)
    private RequestQueue queue;
    //Referencia Lista de citas
    private List<Cita> citas = new ArrayList<>();
    //Adaptador de citas
    private CitasListAdapter adapter;

    // Metodo que asegurará que codigo se ejecutará siempre, cuando se suspenda y vuelva suspensión
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtener dato del Spinner
        this.spNumeroCitas = findViewById(R.id.spnNumCitas);
        //Spinner con lista de 10 opciones
        String[] list = new String[10];
        for (int i = 1; i < 11; i++) {
            list[i - 1] = "" + i;
        }
        //Usar ArrayAdapter de tipo String con la lista de 10 opciones, para el view
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,list);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNumeroCitas.setAdapter(adapterSpinner);
        this.listViewCitas = findViewById(R.id.listView);
        //Generar el adaptador para el listview
        this.adapter = new CitasListAdapter(this, R.layout.citas_list,this.citas);
        this.listViewCitas.setAdapter(this.adapter);
        //Boton que ingresa datos
        this.solicitarCitasBtn = findViewById(R.id.btnSolicitar);
        this.solicitarCitasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Generar instancia de queue, recibe el contexto, que es el activity
                queue = Volley.newRequestQueue(MainActivity.this);
                //Referencia a lista de citas
                String cantidadCitas = spNumeroCitas.getSelectedItem().toString().trim();
                //Genrar request de tipo JSON, tipo GET, obtener datos
                JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET
                        //Direccion url API, con la cantidad de citas seleccionada
                        , "https://thesimpsonsquoteapi.glitch.me/quotes?count="+cantidadCitas
                        ,null
                        //Listener 1
                        , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Para la conversión de datos, se usa un try catch
                        try {
                            //Limpiar la lista
                            citas.clear();
                            //Parsear para convertirlo en un arreglo de citas
                            Cita[] citaObt = new Gson()
                                    //se obtiene resultado
                                    .fromJson(response.toString(),
                                            Cita[].class);
                            //Convertir arreglo en lista
                            citas.addAll(Arrays.asList(citaObt));
                        } catch (Exception ex) {
                            citas = null;
                        } finally {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                //Listener 2
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        citas = null;
                        adapter.notifyDataSetChanged();
                    }
                });
                //Agregar la peticion al queue
                queue.add(jsonReq);
            }
        });

    }
}