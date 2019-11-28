package com.example.gaurav.restaurantfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantInfo implements Parcelable {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("switch_to_order_menu")
    @Expose
    private Integer switchToOrderMenu;
    @SerializedName("cuisines")
    @Expose
    private String cuisines;
    @SerializedName("average_cost_for_two")
    @Expose
    private Integer averageCostForTwo;
    @SerializedName("price_range")
    @Expose
    private Integer priceRange;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("offers")
    @Expose
    private List<Object> offers = null;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("user_rating")
    @Expose
    private UserRating userRating;
    @SerializedName("photos_url")
    @Expose
    private String photosUrl;
    @SerializedName("menu_url")
    @Expose
    private String menuUrl;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("has_online_delivery")
    @Expose
    private Integer hasOnlineDelivery;
    @SerializedName("is_delivering_now")
    @Expose
    private Integer isDeliveringNow;
    @SerializedName("deeplink")
    @Expose
    private String deeplink;
    @SerializedName("has_table_booking")
    @Expose
    private Integer hasTableBooking;
    @SerializedName("book_url")
    @Expose
    private String bookUrl;
    @SerializedName("events_url")
    @Expose
    private String eventsUrl;
    @SerializedName("establishment_types")
    @Expose
    private List<Object> establishmentTypes = null;

    public RestaurantInfo(String id, String name, Location location, String cuisines, Integer averageCostForTwo, String currency, Integer hasOnlineDelivery, String featuredImage, UserRating  rating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cuisines = cuisines;
        this.averageCostForTwo = averageCostForTwo;
        this.currency = currency;
        this.hasOnlineDelivery = hasOnlineDelivery;
        this.featuredImage = featuredImage;
        this.userRating = rating;
    }

    protected RestaurantInfo(Parcel in) {
        apikey = in.readString();
        id = in.readString();
        name = in.readString();
        url = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        if (in.readByte() == 0) {
            switchToOrderMenu = null;
        } else {
            switchToOrderMenu = in.readInt();
        }
        cuisines = in.readString();
        if (in.readByte() == 0) {
            averageCostForTwo = null;
        } else {
            averageCostForTwo = in.readInt();
        }
        if (in.readByte() == 0) {
            priceRange = null;
        } else {
            priceRange = in.readInt();
        }
        currency = in.readString();
        thumb = in.readString();
        userRating = in.readParcelable(UserRating.class.getClassLoader());
        photosUrl = in.readString();
        menuUrl = in.readString();
        featuredImage = in.readString();
        if (in.readByte() == 0) {
            hasOnlineDelivery = null;
        } else {
            hasOnlineDelivery = in.readInt();
        }
        if (in.readByte() == 0) {
            isDeliveringNow = null;
        } else {
            isDeliveringNow = in.readInt();
        }
        deeplink = in.readString();
        if (in.readByte() == 0) {
            hasTableBooking = null;
        } else {
            hasTableBooking = in.readInt();
        }
        bookUrl = in.readString();
        eventsUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apikey);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeParcelable(location, flags);
        if (switchToOrderMenu == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(switchToOrderMenu);
        }
        dest.writeString(cuisines);
        if (averageCostForTwo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(averageCostForTwo);
        }
        if (priceRange == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(priceRange);
        }
        dest.writeString(currency);
        dest.writeString(thumb);
        dest.writeParcelable(userRating, flags);
        dest.writeString(photosUrl);
        dest.writeString(menuUrl);
        dest.writeString(featuredImage);
        if (hasOnlineDelivery == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hasOnlineDelivery);
        }
        if (isDeliveringNow == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isDeliveringNow);
        }
        dest.writeString(deeplink);
        if (hasTableBooking == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hasTableBooking);
        }
        dest.writeString(bookUrl);
        dest.writeString(eventsUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RestaurantInfo> CREATOR = new Creator<RestaurantInfo>() {
        @Override
        public RestaurantInfo createFromParcel(Parcel in) {
            return new RestaurantInfo(in);
        }

        @Override
        public RestaurantInfo[] newArray(int size) {
            return new RestaurantInfo[size];
        }
    };

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getSwitchToOrderMenu() {
        return switchToOrderMenu;
    }

    public void setSwitchToOrderMenu(Integer switchToOrderMenu) {
        this.switchToOrderMenu = switchToOrderMenu;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public Integer getAverageCostForTwo() {
        return averageCostForTwo;
    }

    public void setAverageCostForTwo(Integer averageCostForTwo) {
        this.averageCostForTwo = averageCostForTwo;
    }

    public Integer getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(Integer priceRange) {
        this.priceRange = priceRange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Object> getOffers() {
        return offers;
    }

    public void setOffers(List<Object> offers) {
        this.offers = offers;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public String getPhotosUrl() {
        return photosUrl;
    }

    public void setPhotosUrl(String photosUrl) {
        this.photosUrl = photosUrl;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public Integer getHasOnlineDelivery() {
        return hasOnlineDelivery;
    }

    public void setHasOnlineDelivery(Integer hasOnlineDelivery) {
        this.hasOnlineDelivery = hasOnlineDelivery;
    }

    public Integer getIsDeliveringNow() {
        return isDeliveringNow;
    }

    public void setIsDeliveringNow(Integer isDeliveringNow) {
        this.isDeliveringNow = isDeliveringNow;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public Integer getHasTableBooking() {
        return hasTableBooking;
    }

    public void setHasTableBooking(Integer hasTableBooking) {
        this.hasTableBooking = hasTableBooking;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public List<Object> getEstablishmentTypes() {
        return establishmentTypes;
    }

    public void setEstablishmentTypes(List<Object> establishmentTypes) {
        this.establishmentTypes = establishmentTypes;
    }


}
