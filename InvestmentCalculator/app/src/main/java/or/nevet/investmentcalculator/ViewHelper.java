package or.nevet.investmentcalculator;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Timer;
import java.util.TimerTask;

public class ViewHelper {

    public static boolean tick = true;
    public static Timer timer = null;

    public static void fitInAnotherView (View fit, int parentWidth, int parentHeight) {
        int fitHeight = (int) (((double)parentHeight)*0.7d);
        int fitWidth = (int) (((double)parentWidth)*0.7d);
        int minSize = Math.min(fitHeight, fitWidth);
        fit.getLayoutParams().height = minSize;
        fit.getLayoutParams().width = minSize;
    }

    public static void editTextClicked(Graphics.editText editText, AppCompatActivity activity) {
        timer = new Timer();
        TextView investment = activity.findViewById(R.id.investment);
        TextView percents = activity.findViewById(R.id.percents);
        TextView period = activity.findViewById(R.id.period);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tick) {
                            tick = false;
                            if (editText == Graphics.editText.Investment) {
                                investment.setText(investment.getText().toString()+"|");
                            } else if (editText == Graphics.editText.Percents){
                                percents.setText(percents.getText().toString()+"|");
                            } else if (editText == Graphics.editText.Period) {
                                period.setText(period.getText().toString()+"|");
                            }
                        } else {
                            tick = true;
                            if (editText == Graphics.editText.Investment && investment.getText().toString().endsWith("|"))
                                Graphics.deleteEditTextChar(investment);
                            else if (editText == Graphics.editText.Percents && percents.getText().toString().endsWith("|"))
                                Graphics.deleteEditTextChar(percents);
                            else if (editText == Graphics.editText.Period && period.getText().toString().endsWith("|"))
                                Graphics.deleteEditTextChar(period);
                        }
                    }
                });
            }
        }, 0, 600);
    }

}
