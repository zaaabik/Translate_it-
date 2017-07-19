package org.suai.zabik.views;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.suai.zabik.models.DataBase;
import org.suai.zabik.models.History;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
public class ExampleUnitTest {
    private History history = new History();
    @Before
    public void setUp(){
        history.setId(1);
        history.setFavorite(false);
        history.setRequest("Hello, world!");
        history.setResponse("Привет, мир");
        history.setLanguage("en-ru");
    }

    @Test
    public void addingOneElement(){
        DataBase.save(RuntimeEnvironment.application,"Hello, world!","Привет, мир!","en-ru");
        History result = DataBase.getAll(RuntimeEnvironment.application).get(0);
        assertEquals(result.getIsFavlorite(),history.getIsFavlorite());
        assertEquals(result.getId(),history.getId());
        assertEquals(result.getRequest(),history.getRequest());
        assertEquals(result.getResponse(),result.getResponse());
    }
    @Test
    public void changeItemState(){
        DataBase.save(RuntimeEnvironment.application,"Hello, world!","Привет, мир!","en-ru");
        DataBase.changeFavoriteState(RuntimeEnvironment.application,1);
        history.setFavorite(true);
        History result = DataBase.getAll(RuntimeEnvironment.application).get(0);
        assertEquals(result.getIsFavlorite(),history.getIsFavlorite());
    }
    @Test
    public void isEmptyDbTest(){
        ArrayList<History> allElements = DataBase.getAll(RuntimeEnvironment.application);
        assertEquals(allElements.size(),0);
    }
    @Test
    public void countAddingItemTest(){
        for(int i = 0; i < 10;++i){
            DataBase.save(RuntimeEnvironment.application,"Hello, world!" + i,"Привет, мир!","en-ru");
        }
        ArrayList<History> arrayList = DataBase.getAll(RuntimeEnvironment.application);
        assertEquals(arrayList.size(),10);

    }
    @Test
    public void deletingDbTest(){
        for(int i = 0; i < 10;++i){
            DataBase.save(RuntimeEnvironment.application,"Hello, world!" + i,"Привет, мир!","en-ru");
        }
        ArrayList<History> arrayList = DataBase.getAll(RuntimeEnvironment.application);
        assertEquals(arrayList.size(),10);
        DataBase.deleteDataBase(RuntimeEnvironment.application);
        arrayList = DataBase.getAll(RuntimeEnvironment.application);
        assertEquals(arrayList.size(),0);
    }
    @Test
    public void countFavoriteItemTest(){
        for(int i = 0; i < 10;++i){
            DataBase.save(RuntimeEnvironment.application,"Hello, world!" + i,"Привет, мир!","en-ru");
        }
        for(int i = 1; i < 11;++i){
            if(i % 2 == 0){
                DataBase.changeFavoriteState(RuntimeEnvironment.application,i);
            }
        }
        int countOfFavorite = DataBase.getAllFavorite(RuntimeEnvironment.application).size();
        assertEquals(countOfFavorite,5);
    }
    @Test
    public void addingTheSameItems(){
        for(int i = 0; i < 40;++i){
            DataBase.save(RuntimeEnvironment.application,"the","same","items");
        }
        int countDbItems = DataBase.getAll(RuntimeEnvironment.application).size();
        assertEquals(1,countDbItems);
    }
}