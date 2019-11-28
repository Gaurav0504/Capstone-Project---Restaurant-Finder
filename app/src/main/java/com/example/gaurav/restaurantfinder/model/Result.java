package com.example.gaurav.restaurantfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result implements Parcelable {

    @SerializedName("results_found")
    @Expose
    private Integer resultsFound;
    @SerializedName("results_start")
    @Expose
    private Integer resultsStart;
    @SerializedName("results_shown")
    @Expose
    private Integer resultsShown;
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;

    protected Result(Parcel in) {
        if (in.readByte() == 0) {
            resultsFound = null;
        } else {
            resultsFound = in.readInt();
        }
        if (in.readByte() == 0) {
            resultsStart = null;
        } else {
            resultsStart = in.readInt();
        }
        if (in.readByte() == 0) {
            resultsShown = null;
        } else {
            resultsShown = in.readInt();
        }
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public Integer getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(Integer resultsFound) {
        this.resultsFound = resultsFound;
    }

    public Integer getResultsStart() {
        return resultsStart;
    }

    public void setResultsStart(Integer resultsStart) {
        this.resultsStart = resultsStart;
    }

    public Integer getResultsShown() {
        return resultsShown;
    }

    public void setResultsShown(Integer resultsShown) {
        this.resultsShown = resultsShown;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        if (resultsFound == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(resultsFound);
        }
        if (resultsStart == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(resultsStart);
        }
        if (resultsShown == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(resultsShown);
        }
    }
}
