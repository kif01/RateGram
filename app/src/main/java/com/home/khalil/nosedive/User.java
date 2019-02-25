package com.home.khalil.nosedive;

/**
 * Created by khalil on 7/14/18.
 */

public class User {
    String name;
    String bio;
    double rating;
    String imageUri;
    int iRated;
    int ratedMe;
    boolean isProfilefilled;

    public User(){

    }

    public User(String name, String bio, double rating, String imagePath, boolean isProfilefilled, int iRated, int ratedMe){
      this.name=name;
      this.bio=bio;
      this.rating=rating;
      this.imageUri=imagePath;
      this.isProfilefilled=isProfilefilled;
      this.ratedMe= ratedMe;
      this.iRated=iRated;


    }

    public void setRating(double rate){
        this.rating=rate;
    }

    public void setRatedMe(int i){
        this.ratedMe=i;
    }

    public String print(){
        String str= name+","+bio+","+rating+","+imageUri+","+iRated+","+ratedMe+","+isProfilefilled;
        return str;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUri() {
        return imageUri;
    }


    public int getiRated() {
        return iRated;
    }

    public int getRatedMe() {
        return ratedMe;
    }

    public boolean isProfilefilled() {
        return isProfilefilled;
    }
}
