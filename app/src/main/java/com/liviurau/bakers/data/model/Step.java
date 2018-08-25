package com.liviurau.bakers.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liviu Rau on 16-Apr-18.
 */

public class Step implements Parcelable {

    private int listPosition;
    private Integer id;
    private Integer recipeId;
    private String shortDescription;
    private String longDescription;
    private String videoUrl;
    private String thumbnailUrl;

    public Step() {
    }

    private Step(Parcel in) {
        listPosition = in.readInt();
        id = in.readInt();
        recipeId = in.readInt();
        shortDescription = in.readString();
        longDescription = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
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
        parcel.writeInt(recipeId);
        parcel.writeString(shortDescription);
        parcel.writeString(longDescription);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailUrl);
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

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
