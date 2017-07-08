package org.suai.zabik.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.suai.zabik.views.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseLanguageActivity extends AppCompatActivity {
    ListView availableLanguages;     //contain all translated languages

    private final int YANDEX_API = 10;
    private final int MICROSOFT_API = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        availableLanguages = (ListView) findViewById(R.id.avalibleLanguagesListView);
        String[] languages;
        Intent data = getIntent();
        int isFrom = data.getIntExtra("From",-1);
        String currentLang = data.getStringExtra("lang");
        currentLang = currentLang.toLowerCase();

        final int api = data.getIntExtra("api",-1);
        if(api == YANDEX_API){
            languages = getResources().getStringArray(R.array.languages);
        }
        else {
            languages = getResources().getStringArray(R.array.fullNamesForMicrosoftApi);
        }

        List<String> list = Arrays.asList(languages);
        final List<String> lanList = new ArrayList<>(list);
        if (isFrom == 0) {    //if isFrom == 1 its mean user choose language
                            //we dont need to show last item which name is detect
            lanList.remove(lanList.size() - 1);
        }
        final ArrayAdapter adapter = new ArrayAdapter(
                this,android.R.layout.simple_expandable_list_item_1,
                lanList);

        String[] shortLang;
        shortLang = getResources().getStringArray(R.array.shortLanguages);
        List<String> shortLangList = Arrays.asList(shortLang);
        shortLangList = new ArrayList<>(shortLangList);


        availableLanguages.setAdapter(adapter);
        availableLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String[] language;
                if(api == YANDEX_API){
                    language = getResources().getStringArray(R.array.shortLanguages);
                }
                else {
                    language = getResources().getStringArray(R.array.shortNamesForMicrosoftApi);
                }

                /*
                full language name have short equivalent in array.shortLanguages
                languages[i] == shortLanguages[i]
                **/
                intent.putExtra("lang",language[(int) id]);         //put result of user`s
                setResult(RESULT_OK,intent);                        //choose to extra

                finish();
            }
        });
        availableLanguages.setSelection(shortLangList.indexOf(currentLang));
    }
}
