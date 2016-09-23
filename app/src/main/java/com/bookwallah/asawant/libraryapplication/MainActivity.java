package com.bookwallah.asawant.libraryapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanBar(View v) {
        try {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.initiateScan();
        } catch (ActivityNotFoundException anfe) {
            anfe.printStackTrace();
            showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

                if(scanResult!=null){
                    getBookDetails(scanResult.getContents());
                }
                else{
                    System.out.println("that didnt work");
                }

            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getBookDetails(String isbn) {

        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        final TextView bookAuthorsView = (TextView) findViewById(R.id.textView);
        final TextView bookTitleView = (TextView) findViewById(R.id.textView2);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject bookInformation= (JSONObject) response.getJSONArray("items").get(0);
                            JSONObject volumeInfo= bookInformation.getJSONObject("volumeInfo");
                            String bookTitle= volumeInfo.getString("title");
                            String bookAuthors= String.valueOf(volumeInfo.getJSONArray("authors"));

                            bookTitleView.setText(bookTitle);
                            bookAuthorsView.setText(bookAuthors);
                        } catch (JSONException e) {
                            Intent intent = new Intent(MainActivity.this, ManualEntryActivity.class);
                            startActivity(intent);
                            finish();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }
}
