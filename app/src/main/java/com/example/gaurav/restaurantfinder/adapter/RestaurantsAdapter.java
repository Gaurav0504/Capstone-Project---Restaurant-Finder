package com.example.gaurav.restaurantfinder.adapter;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaurav.restaurantfinder.R;
import com.example.gaurav.restaurantfinder.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private static final String TAG = "RestaurantsAdapter";
    private ClickListener clickListener;
    private List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantsAdapter(LiveData<List<Restaurant>> restaurants, ClickListener onRecipeClicked) {
        if (restaurants != null) {
            this.restaurants = restaurants.getValue();
        }
        clickListener = onRecipeClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null) {
            Restaurant restaurant = restaurants.get(position);
            holder.restaurantTitle.setText(restaurant.getRestaurant().getName());
            String featuredImage = restaurant.getRestaurant().getFeaturedImage();
            if (featuredImage.isEmpty()) {
                holder.restaurantImage.setImageResource(R.drawable.recipe_icon_md);
                return;
            }
            Picasso.get().load(featuredImage)
                    .error(R.drawable.recipe_icon_md)
                    .placeholder(R.drawable.recipe_icon_md)
                    .into(holder.restaurantImage);
        }
    }


    @Override
    public int getItemCount() {
        if (restaurants != null) {
            return restaurants.size();
        }
        return 0;
    }

    public void updateRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.restaurant_image)
        ImageView restaurantImage;

        @BindView(R.id.restaurant_title)
        TextView restaurantTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.accept(getLayoutPosition(),restaurantImage);
        }
    }

    public interface ClickListener {
        void accept(int position, ImageView imageView);
    }
}
