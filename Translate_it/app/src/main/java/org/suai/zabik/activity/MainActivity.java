package org.suai.zabik.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.suai.zabik.network.MicrosoftTranslate;
import org.suai.zabik.network.YandexTranslate;
import org.suai.zabik.network.*;
import org.suai.zabik.views.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final int LANGUAGE_FROM = 1;
    private final int LANGUAGE_TO = 2;

    YandexTranslate yandexTranslate;
    MicrosoftTranslate microsoftTranslate;

    private final int YANDEX_API = 10;
    private final int MICROSOFT_API = 20;
    private Itranslate translateEngine;
    private EditText editText;
    private TextView textView;
    private Button languageFrom;
    private Button languageTo;
    private ImageButton historyAndFavoriteBtn;
    private int currentStat = 0;
    private Spinner actionBarApiSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        microsoftTranslate = new MicrosoftTranslate(this);
        microsoftTranslate.getToken();
        yandexTranslate = new YandexTranslate(this);
        translateEngine = yandexTranslate;

        //find all view elements
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView)findViewById(R.id.textView);
        languageFrom = (Button) findViewById(R.id.languageFrom);
        languageTo = (Button)findViewById(R.id.languageTo);
        historyAndFavoriteBtn = (ImageButton) findViewById(R.id.historyAndFavoriteBtn);
        actionBarApiSpinner = (Spinner)findViewById(R.id.setApiSpinner);

        //setting listeners (listener is our class - MainActivity)
        languageFrom.setOnClickListener(this);
        languageTo.setOnClickListener(this);
        historyAndFavoriteBtn.setOnClickListener(this);
        setDefaultBtnValue();

        actionBarApiSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.spinnerApiList,
                R.layout.support_simple_spinner_dropdown_item));
        actionBarApiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    setDefaultBtnValue();
                    currentStat = YANDEX_API;
                    translateEngine = yandexTranslate;
                }
                if(i == 1){
                    setDefaultBtnValue();
                    currentStat = MICROSOFT_API;
                    translateEngine = microsoftTranslate;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //SQLiteDatabase db = dataBase.getWritableDatabase();
        //db.delete(dataBase.TABLE_NAME,null,null);
        //set text change listener for edit text
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER) {
                        String langFr;
                        if (languageFrom.getText().toString().length() > 2) {
                            langFr = "";
                        } else {
                            langFr = languageFrom.getText().toString();
                        }
                        try {
                            translateEngine.makeRequest(editText.getText().toString(),
                                    langFr,
                                    languageTo.getText().toString(),
                                    textView);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                return false;
            }
        });




    }

    //set default text on langFrom and langTo buttons
    private void setDefaultBtnValue() {
        //set  default values to buttons
        languageTo.setText(R.string.default_ToLang);
        languageFrom.setText(R.string.default_fromLang);
    }


    @Override
    public void onClick(View v) {
        Intent chooseLangFrom;
        switch (v.getId()){
            case R.id.languageFrom:
                chooseLangFrom = new Intent(this, ChooseLanguageActivity.class);
                chooseLangFrom.putExtra("From", 1);
                chooseLangFrom.putExtra("lang", languageFrom.getText().toString());
                chooseLangFrom.putExtra("api", currentStat);
                startActivityForResult(chooseLangFrom, LANGUAGE_FROM);
                break;
            case R.id.languageTo:
                chooseLangFrom = new Intent(this, ChooseLanguageActivity.class);
                chooseLangFrom.putExtra("From", 0);
                chooseLangFrom.putExtra("lang", languageTo.getText().toString());
                chooseLangFrom.putExtra("api", currentStat);
                startActivityForResult(chooseLangFrom, LANGUAGE_TO);
                break;
            case R.id.historyAndFavoriteBtn:
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
                Intent intent = new Intent(this,HistoryAndFavoriteActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            String lang;
            String langFr;
            switch (requestCode) {
                case LANGUAGE_FROM:
                    lang = data.getStringExtra("lang");
                    languageFrom.setText(lang);

                    if (languageFrom.getText().toString().length() > 2) {
                        langFr = "";
                    } else {
                        langFr = languageFrom.getText().toString();
                    }
                    try {
                        translateEngine.makeRequest(editText.getText().toString(),
                                langFr,
                                languageTo.getText().toString(),
                                textView);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case LANGUAGE_TO:
                    lang = data.getStringExtra("lang");
                    languageTo.setText(lang);

                    if (languageFrom.getText().toString().length() > 2) {
                        langFr = "";
                    } else {
                        langFr = languageFrom.getText().toString();
                    }
                    try {
                        translateEngine.makeRequest(editText.getText().toString(),
                                langFr,
                                languageTo.getText().toString(),
                                textView);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    }
}
