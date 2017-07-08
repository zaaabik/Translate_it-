package org.suai.zabik.network;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.suai.zabik.models.DataBase;
import org.suai.zabik.views.R;


/**
 *  Yandex api translate
 */

public class YandexTranslate implements Itranslate{
    final private String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170320T234439Z.7fc79b9e7558c2c3.14446ace48e11ccba5900917059e35b2247d34e2";
    private String result;
    private String lastResponse;
    private Context context;
    private String requ;
    private String langs;


    public YandexTranslate(Context context){
        this.context = context;
    }
    @Override
    public void makeRequest(final String text, String langFrom, String langTo, final TextView textView) {
        RequestQueue q = Volley.newRequestQueue(context);
        requ = text.replaceAll(" ","%20");
        if(!langFrom.isEmpty()){
            langTo = "-" + langTo;

        }
        else {
            langFrom = "";
        }
        langs = langFrom + "-" + langTo;
        final String req = url +"&text=" + requ + "&lang=" + langFrom + langTo;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, req, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lastResponse = response;
                try {
                    JSONObject tmp = new JSONObject(lastResponse);
                    JSONArray jsonArray =  tmp.getJSONArray("text");
                    for(int i = 0; i < jsonArray.length();++i){
                        result = jsonArray.getString(i);
                    }
                    //parsing lang from response
                    langs = tmp.getString("lang");
                    //set result to the textView
                    textView.setText(result);

                    DataBase.save(context, text, result, langs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,
                            context.getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        q.add(stringRequest);
    }




}
