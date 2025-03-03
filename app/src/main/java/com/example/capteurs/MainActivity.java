package com.example.capteurs;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.listecapteurs, getString(R.string.sensors_list), ListeCapteursActivity.class));
        sliderItems.add(new SliderItem(R.drawable.detection_indispo, getString(R.string.sensors_detection), SensorUnavailableActivity.class));
        sliderItems.add(new SliderItem(R.drawable.accelerometre, getString(R.string.accelerometer), AccelerometreActivity.class));
        sliderItems.add(new SliderItem(R.drawable.secouer, getString(R.string.shake), SecouerActivity.class));
        sliderItems.add(new SliderItem(R.drawable.proximite, getString(R.string.proximity), ProximiteActivity.class));
        sliderItems.add(new SliderItem(R.drawable.geolocalisation, getString(R.string.Geolocalisation), GeolocalisationActivity.class));
        sliderItems.add(new SliderItem(R.drawable.directions, getString(R.string.directions), DirectionActivity.class));


        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunner);
                sliderHandler.postDelayed(sliderRunner, 3000);
            }
        });
    }

    private Runnable sliderRunner = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunner, 3000);
    }
}
