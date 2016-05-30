package com.hongoctuan.admin.ungdungxemphim.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hongoctuan.admin.ungdungxemphim.DAO.DatabaseHelper;
import com.hongoctuan.admin.ungdungxemphim.DTO.RapDTO;
import com.hongoctuan.admin.ungdungxemphim.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListRap extends AppCompatActivity {
    ListView lv_vung, lv_rap;;
    ArrayList<String> arrVung;
    Handler handlerMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rap);
        lv_vung = (ListView) findViewById(R.id.lv_vung);
        lv_rap = (ListView) findViewById(R.id.lv_rap);
        arrVung = new ArrayList<String>();
        GetDanhSachVung getDanhSachVung = new GetDanhSachVung();
        getDanhSachVung.execute();
        dostart("all");
        final DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        final ArrayList<String> listrap = databaseHelper.getListRap();
        final ArrayAdapter<String> adapterrap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listrap);
        lv_rap.setAdapter(adapterrap);
        lv_rap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailRap.class);
                Bundle bundle = new Bundle();
                bundle.putString("tenrap", listrap.get(position));
                intent.putExtra("myData", bundle);
                startActivity(intent);
            }
        });

        DatabaseHelper dbVung = new DatabaseHelper(getApplicationContext());
        ArrayList<String> listvung = dbVung.getVung();
        ArrayAdapter<String> adaptervung = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listvung);
        lv_vung.setAdapter(adaptervung);

        lv_vung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    listrap.clear();
                    DatabaseHelper databaseHelper1 = new DatabaseHelper(getApplicationContext());
                    ArrayList<String> listtemp = databaseHelper1.getListRap();
                    for (int i = 0; i < listtemp.size(); i++) {
                        listrap.add(listtemp.get(i));
                    }
                    adapterrap.notifyDataSetChanged();
                }else {
                    listrap.clear();
                    DatabaseHelper databaseHelper1 = new DatabaseHelper(getApplicationContext());
                    ArrayList<String> listtemp = databaseHelper1.getRap(arrVung.get(position));
                    for (int i = 0; i < listtemp.size(); i++) {
                        listrap.add(listtemp.get(i));
                    }
                    adapterrap.notifyDataSetChanged();
                }
            }
        });
    }

    public void dostart(final String vung){
        final ArrayList<String> danhsachRap = new ArrayList<String>();
        Thread thCon=new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                BufferedReader in = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                URI website = null;
                try {
                    website = new URI("http://restfullapiservice.somee.com/api/getallrap?vung="+vung);
                    request.setURI(website);
                    HttpResponse response = null;
                    response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = null;
                    line = in.readLine();
                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(line);
                    JSONObject json_data = null;
                    for(int i=0; i < jsonArray.length() ; i++) {
                        json_data = jsonArray.getJSONObject(i);
                        RapDTO item = new RapDTO();
                        item.setId(json_data.getString("id"));
                        item.setNameRap(json_data.getString("TenRap"));
                        item.setVitri(json_data.getString("Vitri"));
                        item.setVung(json_data.getString("Vung"));
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        databaseHelper.insertRap(item.getNameRap(), item.getVung());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thCon.start();
    }

    public class GetDanhSachVung extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader in = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = null;
            try {
                website = new URI("http://restfullapiservice.somee.com/api/getallvung");
                request.setURI(website);
                HttpResponse response = null;
                response = httpclient.execute(request);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = null;
                line = in.readLine();
                return line;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                arrVung.add("Tất Cả");
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.insertVung(arrVung.get(0));
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrVung.add(jsonArray.get(i).toString());
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.insertVung(jsonArray.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}