package com.example.capteurs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

    SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        holder.setTitle(sliderItems.get(position).getTitle());

        // Gestion du clic pour ouvrir l'activité associée
        holder.buttonOpenActivity.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, sliderItems.get(position).getTargetActivity());
            context.startActivity(intent);
        });

        if (position == getItemCount() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;
        private TextView titleView;
        private Button buttonOpenActivity;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            titleView = itemView.findViewById(R.id.titleSlide);
            buttonOpenActivity = itemView.findViewById(R.id.buttonOpenActivity);
        }

        void setImage(SliderItem sliderItem) {
            imageView.setImageResource(sliderItem.getImage());
        }

        void setTitle(String title) {
            titleView.setText(title);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}
