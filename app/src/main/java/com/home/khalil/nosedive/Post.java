package com.home.khalil.nosedive;

import android.graphics.drawable.Drawable;
import android.view.DragEvent;

/**
 * Created by khalil on 8/17/18.
 */

public class Post {
    String name;
    double rating;
    String imageUri;
    String text;
    int image;
    double postRating;

    public Post(String name, double rating, String imageUri, String text, int image, double postRating) {
        this.name = name;
        this.rating = rating;
        this.imageUri = imageUri;
        this.text = text;
        this.image = image;
        this.postRating = postRating;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getText() {
        return text;
    }

    public int getImage() {
        return image;
    }

    public double getPostRating() {
        return postRating;
    }
}
