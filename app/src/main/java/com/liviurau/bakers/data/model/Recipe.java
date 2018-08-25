package com.liviurau.bakers.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liviu Rau on 16-Apr-18.
 */

public class Recipe implements Parcelable {

    private int listPosition;
    private Integer id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private Integer servings;
    private String imageUrl;

    public Recipe() {
    }

    private Recipe(Parcel in) {
        listPosition = in.readInt();
        id = in.readInt();
        name = in.readString();

        this.ingredients = new ArrayList<>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());

        this.steps = new ArrayList<>();
        in.readList(this.steps, Step.class.getClassLoader());

        servings = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(listPosition);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(this.ingredients);
        parcel.writeList(this.steps);
        parcel.writeInt(this.servings);
        parcel.writeString(this.imageUrl);
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
