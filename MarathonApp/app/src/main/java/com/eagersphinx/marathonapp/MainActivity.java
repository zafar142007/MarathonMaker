package com.eagersphinx.marathonapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.eagersphinx.marathonapp.dto.BibResponse;
import com.eagersphinx.marathonapp.dto.LapResponse;
import com.eagersphinx.marathonapp.dto.Response;
import com.eagersphinx.marathonapp.dto.RecordLap;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String backend = "https://seagull-grand-coral.ngrok-free.app/marathon-maker/v1/";
    public static final String recordLapUrl = "bib/lap";
    private String awardDoneUrl = "bib/%s/award";
    public static final String getStatusUrl = "bib";
    public static final String TAG = "MARATHON_MAKER";

    RequestQueue requestQueue;
    LocationManager locationManager;
    private static Double lat, longitude;
    private final Integer marathonId =1;
    Gson gson = new Gson();
    private String checkpoint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }

        LocationProvider provider =
                locationManager.getProvider(LocationManager.GPS_PROVIDER);

        LocationListener listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                longitude = location.getLongitude();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10000,          // 10-second interval.
                10,             // 10 meters.
                listener);

        Button recordLap = findViewById(R.id.recordLap);
        EditText bib = findViewById(R.id.bib);
        Spinner spinner = findViewById(R.id.getStatusButton);

        List<String> categories = new ArrayList<>();
        categories.add("All events");
        categories.add("1 Miler");
        categories.add("2.5K");
        categories.add("5K");
        categories.add("7.5K");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        Button awardDone = findViewById(R.id.awardDone);
        awardDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bib.getText().toString().isEmpty()){
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, backend + String.format(awardDoneUrl, bib.getText()),
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "read complete" + response);
                                LapResponse lapResponse = gson
                                        .fromJson(response, LapResponse.class);
                                Log.d(TAG, lapResponse.toString());
                                Toast.makeText(getApplicationContext(),
                                        "bib "+  bib.getText().toString() + " award marked done ",
                                        Toast.LENGTH_SHORT).show();
                            }


                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "failed", error.fillInStackTrace());
                                Toast.makeText(getApplicationContext(),
                                        "Operation failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(stringRequest);
                Log.d(TAG, "awarded");
            }
        });

        spinner.setOnItemSelectedListener(this);

        recordLap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bib.getText().toString().isEmpty()){
                    return;
                }

                RecordLap recordLapData = new RecordLap(
                        Integer.parseInt(bib.getText().toString()),
                        lat,
                        longitude,
                        checkpoint, marathonId);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, backend + recordLapUrl,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "read complete" + response);
                                LapResponse lapResponse = gson
                                        .fromJson(response, LapResponse.class);
                                Log.d(TAG, lapResponse.toString());
                                Toast.makeText(getApplicationContext(),
                                        "bib "+  bib.getText().toString() + " completed lap " + lapResponse.getFinishedLap(),
                                        Toast.LENGTH_SHORT).show();
                            }


                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "failed ", error.fillInStackTrace());
                                Toast.makeText(getApplicationContext(),
                                        "Operation failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public byte[] getBody() {
                        return gson.toJson(recordLapData).getBytes(StandardCharsets.UTF_8);
                    }
                };
                requestQueue.add(stringRequest);
                Log.d(TAG, "recorded lap");
            }
        });
    }

    private void setCheckpoint(TextView tv, int i) {
        tv.setText("C"+(i+1));
    }

    private String getTime(Timestamp createdAt) {
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss a");
        fmt.setTimeZone(TimeZone.getTimeZone(ZoneId.of("+05:30")));
        return fmt.format(new Date(createdAt.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
        ListView listView = findViewById(R.id.list_view);
        RecordLap recordLapData = new RecordLap(
                    null,
                    lat,
                    longitude,
                    checkpoint, marathonId, item);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, backend + getStatusUrl,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "read complete");
                            BibResponse bibResponse = gson
                                    .fromJson(response, BibResponse.class);
                            ArrayAdapter<BibResponse.LoopResp> adapter = new ArrayAdapter<BibResponse.LoopResp>(getApplicationContext(),
                                    R.layout.list_view_item,
                                    bibResponse.getLoops().values().toArray(new BibResponse.LoopResp[0])){
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    View v = getLayoutInflater().inflate(R.layout.list_view_item, null);
                                    TextView textView = v.findViewById(R.id.bib_number);
                                    Log.i(TAG, getItem(position).toString());
                                    textView.setText(getItem(position).getBib().toString() + " | " + getItem(position).getName() + " | " +
                                            (getItem(position).getEventName().length()>8 ?
                                                    getItem(position).getEventName().substring(0, 8):
                                                    getItem(position).getEventName()) +
                                            (getItem(position).getAwarded()!= null && getItem(position).getAwarded() ? " | Awarded": ""));
                                    if (getItem(position).getAwarded()!=null && getItem(position).getAwarded()) {
                                        textView.setTextColor(Color.GREEN);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=1) {
                                        TextView textView3 = v.findViewById(R.id.lap1_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(0).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap1);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 0);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=2) {
                                        TextView textView3 = v.findViewById(R.id.lap2_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(1).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap2);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 1);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=3) {
                                        TextView textView3 = v.findViewById(R.id.lap3_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(2).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap3);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 2);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=4) {
                                        TextView textView3 = v.findViewById(R.id.lap4_time);
                                        textView3.setText(getTime(getItem(position).getLoops().get(3).getCreatedAt()));
                                        textView3.setTextColor(Color.GREEN);
                                        TextView tv = v.findViewById(R.id.lap4);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 3);

                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=5) {
                                        TextView textView3 = v.findViewById(R.id.lap5_time);
                                        textView3.setTextColor(Color.GREEN);
                                        TextView tv = v.findViewById(R.id.lap5);
                                        tv.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(4).getCreatedAt()));
                                        setCheckpoint(tv, 4);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=6) {
                                        TextView textView3 = v.findViewById(R.id.lap6_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(5).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap6);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 5);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=7) {
                                        TextView textView3 = v.findViewById(R.id.lap7_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(6).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap7);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 6);
                                    }

                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=8) {
                                        TextView textView3 = v.findViewById(R.id.lap8_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(7).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap8);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 7);
                                    }

                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=9) {
                                        TextView textView3 = v.findViewById(R.id.lap9_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(8).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap9);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 8);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=10) {
                                        TextView textView3 = v.findViewById(R.id.lap10_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(9).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap10);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 9);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=11) {
                                        TextView textView3 = v.findViewById(R.id.lap11_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(10).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap11);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 10);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=12) {
                                        TextView textView3 = v.findViewById(R.id.lap12_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(11).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap12);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 11);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=13) {
                                        TextView textView3 = v.findViewById(R.id.lap13_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(12).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap13);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 12);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=14) {
                                        TextView textView3 = v.findViewById(R.id.lap14_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(13).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap14);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 13);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=15) {
                                        TextView textView3 = v.findViewById(R.id.lap15_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(14).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap15);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 14);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=16) {
                                        TextView textView3 = v.findViewById(R.id.lap16_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(15).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap16);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 15);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=17) {
                                        TextView textView3 = v.findViewById(R.id.lap17_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(16).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap17);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 16);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=18) {
                                        TextView textView3 = v.findViewById(R.id.lap18_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(17).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap18);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 17);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=12) {
                                        TextView textView3 = v.findViewById(R.id.lap12_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(11).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap12);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 11);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=12) {
                                        TextView textView3 = v.findViewById(R.id.lap12_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(11).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap12);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 11);
                                    }
                                    if (getItem(position).getLoops()!=null && getItem(position).getLoops().size()>=12) {
                                        TextView textView3 = v.findViewById(R.id.lap12_time);
                                        textView3.setTextColor(Color.GREEN);
                                        textView3.setText(getTime(getItem(position).getLoops().get(11).getCreatedAt()));
                                        TextView tv = v.findViewById(R.id.lap12);
                                        tv.setTextColor(Color.GREEN);
                                        setCheckpoint(tv, 11);
                                    }

                                    return v;
                                }
                            };
                            listView.setAdapter(adapter);
                        }

                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "failed", error.fillInStackTrace());
                            Toast.makeText(getApplicationContext(),
                                    "Operation failed",
                                    Toast.LENGTH_SHORT).show();                        }
                    }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() {
                    return gson.toJson(recordLapData).getBytes(StandardCharsets.UTF_8);
                }
            };
            requestQueue.add(stringRequest);
            Log.d(TAG, "got status");


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}