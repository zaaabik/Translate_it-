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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.suai.zabik.models.DataBase;
import org.suai.zabik.views.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * microsoft api translate
 */

public class MicrosoftTranslate implements Itranslate {
    static private String lastResponse;
       private final String httpPart = "https://api.microsofttranslator.com/v2/http.svc/Translate?appid=";
    private final String httpPartDetect =     "https://api.microsofttranslator.com/v2/http.svc/Detect?appid=";
    private String url = " https://api.cognitive.microsoft.com/sts/v1.0/issueToken?Subscription-Key=9e35e2b2b4064074aba7a0da60bb431c";
    private Context context;
    private String token;

    public MicrosoftTranslate(final Context context) {
        this.context = context;
    }

    public void getToken() {
        RequestQueue q = Volley.newRequestQueue(context);
        //when app start add new token
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                token = response;

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

    @Override
    public void makeRequest(final String text, final String langFrom, final String langTo, final TextView textView) throws InterruptedException {
        final RequestQueue q = Volley.newRequestQueue(context);
        String finalUrl = httpPart + "Bearer " + token + "&text=" + text + "&from=" + langFrom + "&to=" + langTo;
        finalUrl = finalUrl.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lastResponse = response;
                final String res = response.substring(response.indexOf('>') + 1, response.lastIndexOf('<'));
                textView.setText(res);
                String langUrl = httpPartDetect + "Bearer " + token + "&text=" + text;
                langUrl = langUrl.replaceAll(" ", "%20");
                StringRequest stringRequestLang = new StringRequest(Request.Method.GET, langUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lastResponse = response;
                        final String lang = response.substring(response.indexOf('>') + 1, response.lastIndexOf('<'));
                        DataBase.save(context, text, res, lang + "-" + langTo);
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
                q.add(stringRequestLang);

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
