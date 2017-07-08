package org.suai.zabik.models;




/**
 * Created by zabik on 18.04.17.
 */

public class History {
    private String language;
    private String request;
    private String response;
    private int id;
    private boolean isFavorite;
    public History(){

    }
    public String getLanguage() {
        return language;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }
    public boolean getIsFavlorite(){
        return isFavorite;
    }
    public int getId() {
        return id;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof History)){
            return false;
        }
        if(o == this){
            return true;
        }
        History history = (History) o;
        if(history.id == this.id){
            return true;
        }
        return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
