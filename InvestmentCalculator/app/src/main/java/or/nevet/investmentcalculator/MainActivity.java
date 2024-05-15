package or.nevet.investmentcalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.ads.AdInspectorError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnAdInspectorClosedListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("639F35D3A8811328A528D7981F22437E")).build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdView adView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }
                });
            }
        });

        createGraphics();
    }

    private void createGraphics() {
        Graphics graphics = new Graphics(this);
        graphics.createMainLayout(findViewById(R.id.main));
    }

}