package com.app.wordpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.wordpress.data.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity {
    ProgressBar progressBar;
    private EditText email, password;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.progresslogin);
        sharedPref = new SharedPref(this);
    }

    public void Dangky(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Đăng ký");

        WebView wv = new WebView(this);
        wv.loadUrl("https://xiaomifirm.com/register");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void DefaultUser(View view){
        sharedPref.setYourName("XiaomiFirm");
        sharedPref.setYourEmail("user@xiaomifirm.info");
        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(i);
    }

    public void Dangnhap(View view) {
        final String username = email.getText().toString().trim();
        final String email = password.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        String uri = String.format("https://xiaomifirm.com/wp-json/custom-plugin/login?username=%1$s&password=%2$s",
                username,
                email);
        StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject reader = new JSONObject(response);
                    JSONObject sys = reader.getJSONObject("data");
                    String name = sys.getString("display_name");
                    String email = sys.getString("user_email");
                    sharedPref.setYourName(name);
                    sharedPref.setYourEmail(email);
                    Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ActivityLogin.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);
    }
}
