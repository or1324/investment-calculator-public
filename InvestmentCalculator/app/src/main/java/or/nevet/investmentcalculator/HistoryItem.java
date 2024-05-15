package or.nevet.investmentcalculator;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryItem extends androidx.appcompat.widget.AppCompatTextView {
    Calculation calculation;
    public HistoryItem(Calculation calculation, Graphics graphics) {
        super(graphics.activity);
        this.calculation = calculation;
        if (calculation == null) {
            setGravity(Gravity.LEFT);
            setTextSize(20f);
            setTextColor(Color.WHITE);
            setText("You have no calculations in the history");
        } else {
            setGravity(Gravity.LEFT);
            setTextSize(20f);
            setClickable(true);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    graphics.loadCalculation(calculation);
                }
            });
            setTextColor(Color.WHITE);
            setText("Investment: "+calculation.investment+"\nPercents: "+calculation.percents+"\nPeriod type: "+Graphics.getPeriodName(calculation.periodType) + "\nPeriod length: "+calculation.period);
        }
    }
}
