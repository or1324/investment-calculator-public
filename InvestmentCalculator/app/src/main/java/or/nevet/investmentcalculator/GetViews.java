package or.nevet.investmentcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class GetViews {
    public static ArrayList<ArrayList<AppCompatButton>> getCalculatorButtons(ConstraintLayout calculatorLayout, AppCompatActivity activity) {
        ArrayList<ArrayList<AppCompatButton>> buttons = new ArrayList<>(Constants.rowNum);
        for (int i = 1; i <= Constants.rowNum; i++) {
            int rowId = calculatorLayout.getResources().getIdentifier("row"+i, "id", activity.getPackageName());
            ConstraintLayout row = calculatorLayout.findViewById(rowId);
            buttons.add(new ArrayList<>());
            for (int f = 1; f <= Constants.colNum; f++) {
                int colId = row.getResources().getIdentifier("col"+i+""+f, "id", activity.getPackageName());
                buttons.get(i-1).add(row.findViewById(colId));
            }
        }
        return buttons;
    }
}
