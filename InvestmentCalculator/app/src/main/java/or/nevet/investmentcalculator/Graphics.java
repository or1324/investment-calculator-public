package or.nevet.investmentcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Graphics {

    AppCompatActivity activity;
    public enum editText {Investment, Percents, Period, None}
    editText clicked = editText.None;
    public static Toast toast;
    public enum PeriodType {Day, Month, Year}
    public static PeriodType periodType = PeriodType.Day;
//    final EditText invest;
//    final EditText percent;
//    final EditText period;
    final TextView invest;
    final TextView percent;
    final TextView period;
    final ConstraintLayout history;
    final LinearLayout historyList;
    final Spinner dropdown;
    public static boolean isHistory = false;
    final ConstraintLayout calculator;
    final TextView profit;
    final TextView pField;
    final TextView totalProfit;
    final TextView tpField;
    final TextView totalMoney;
    final TextView tmField;
    final TableLayout table;

    public Graphics (AppCompatActivity activity) {
        this.activity = activity;
        invest = activity.findViewById(R.id.investment);
        percent = activity.findViewById(R.id.percents);
        period = activity.findViewById(R.id.period);
        calculator = activity.findViewById(R.id.calc);
        dropdown = activity.findViewById(R.id.spinner);
        String[] items = new String[]{"Day", "Month", "Year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spinner_item, items);
        dropdown.setAdapter(adapter);
        history = activity.findViewById(R.id.history);
        historyList = activity.findViewById(R.id.history_list);
        profit = activity.findViewById(R.id.profit);
        pField = activity.findViewById(R.id.p_field);
        totalProfit = activity.findViewById(R.id.total_profit);
        tpField = activity.findViewById(R.id.tp_field);
        totalMoney = activity.findViewById(R.id.total_money);
        tmField = activity.findViewById(R.id.tm_field);
        table = activity.findViewById(R.id.table);
    }

    public void createCalculatorLayout(ConstraintLayout calculatorLayout) {
        ArrayList<ArrayList<AppCompatButton>> buttons = GetViews.getCalculatorButtons(calculatorLayout, activity);
        int colWidth = activity.getResources().getDisplayMetrics().widthPixels/Constants.colNum;
        int rowHeight = ((int)(((double)activity.getResources().getDisplayMetrics().heightPixels-activity.findViewById(R.id.adView).getHeight())*0.55d))/Constants.rowNum;
        int rowIndex = 0;
        calculator.setVisibility(View.GONE);
        ((TextView)(dropdown.getSelectedView())).setText(((TextView)(dropdown.getSelectedView())).getText().toString()+" ↓");
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    changePeriod(PeriodType.Day);
                else if (i == 1)
                    changePeriod(PeriodType.Month);
                else if (i == 2)
                    changePeriod(PeriodType.Year);
                TextView t = ((TextView)(view));
                if (!t.getText().toString().endsWith("↓"))
                    t.setText(t.getText()+" ↓");
                toggleKeyboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditTextClicked(editText.Investment);
            }
        });
        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditTextClicked(editText.Percents);
            }
        });

        period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditTextClicked(editText.Period);
            }
        });
        for (ArrayList<AppCompatButton> row : buttons) {
            int colIndex = 0;
            for (AppCompatButton button : row) {
                ViewHelper.fitInAnotherView(button, colWidth, rowHeight);
                if (colIndex < Constants.colNum-1 && rowIndex < Constants.rowNum-1 && rowIndex > 0) {
                    button.setText((3*(rowIndex-1)+colIndex+1)+"");
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendButtonText(button);
                        }
                    });
                } else if (colIndex == 1 && rowIndex == Constants.rowNum-1) {
                    button.setText("0");
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendButtonText(button);
                        }
                    });
                } else if (colIndex == 2 && rowIndex == Constants.rowNum-1) {
                    button.setText(".");
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendButtonText(button);
                        }
                    });
                } else if (colIndex == 1 && rowIndex == 0) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clicked == editText.Investment) {
                                if (!invest.getText().toString().isEmpty() && invest.getText().toString().endsWith("|"))
                                    deleteEditTextChar(invest);
                                if (!invest.getText().toString().isEmpty())
                                    deleteEditTextChar(invest);
                            } else if (clicked == editText.Percents) {
                                if (!percent.getText().toString().isEmpty() && percent.getText().toString().endsWith("|"))
                                    deleteEditTextChar(percent);
                                if (!percent.getText().toString().isEmpty())
                                    deleteEditTextChar(percent);
                            } else if (clicked == editText.Period) {
                                if (!period.getText().toString().isEmpty() && period.getText().toString().endsWith("|"))
                                    deleteEditTextChar(period);
                                if (!period.getText().toString().isEmpty())
                                    deleteEditTextChar(period);
                            }
                        }
                    });
                } else if (colIndex == 3 && rowIndex == Constants.rowNum-1) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showResult();
                        }
                    });
                } else if (colIndex == 0 && rowIndex == 0) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reset();
                        }
                    });
                } else if (colIndex == 0 && rowIndex == Constants.rowNum-1) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!isHistory)
                                showHistory();
                            else
                                hideHistory();
                        }
                    });
                }
                button.requestLayout();
                colIndex++;
            }
            rowIndex++;
        }
    }

    public void createMainLayout(ConstraintLayout mainLayout) {
        mainLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                //setupAutoresize(mainLayout.findViewById(R.id.invisibleTextViewInvestment), mainLayout.findViewById(R.id.investment));
                //setupAutoresize(mainLayout.findViewById(R.id.invisibleTextViewPercents), mainLayout.findViewById(R.id.percents));
                //setupAutoresize(mainLayout.findViewById(R.id.invisibleTextViewPeriod), mainLayout.findViewById(R.id.period));
                createCalculatorLayout(mainLayout.findViewById(R.id.calc));
                mainLayout.removeOnLayoutChangeListener(this);
            }
        });
    }

    private void setUpHint(TextView textView, boolean force) {
        if (force || textView.getText().toString().equals("")) {
            if (textView.getId() == invest.getId() && clicked != editText.Investment) {
                textView.setText("Investment");
                textView.setTextColor(Color.parseColor("#B3C8C8C8"));
            } else if (textView.getId() == percent.getId() && clicked != editText.Percents) {
                textView.setText("Percents");
                textView.setTextColor(Color.parseColor("#B3C8C8C8"));
            }
            if (textView.getId() == period.getId() && clicked != editText.Period) {
                textView.setText("Period");
                textView.setTextColor(Color.parseColor("#B3C8C8C8"));
            }
        }
    }

    public void setupAutoresize(TextView invisibleTextView, EditText resizableEditText) {
        invisibleTextView.setText("a", TextView.BufferType.EDITABLE); //calculate the right size for one character
        resizableEditText.setTextSize(autosizeText(invisibleTextView.getTextSize()));
        resizableEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String t = charSequence.toString();
                invisibleTextView.setText(t, TextView.BufferType.EDITABLE);
                if (charSequence.toString().startsWith("|") && charSequence.toString().length() > 1)
                    resizableEditText.setText(charSequence.toString().substring(1));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                resizableEditText.setTextSize(autosizeText(invisibleTextView.getTextSize()));
            }
        });
    }

    private float autosizeText(float size) {
        return size / (activity.getResources().getDisplayMetrics().density + 0.2f);
    }

    private void showResult() {
        try {
            hideKeyboard();
            String investText = invest.getText().toString();
            if (investText.endsWith("|"))
                investText = investText.substring(0, investText.length()-1);
            String percentText = percent.getText().toString();
            if (percentText.endsWith("|"))
                percentText = percentText.substring(0, percentText.length()-1);
            String periodText = period.getText().toString();
            if (periodText.endsWith("|"))
                periodText = periodText.substring(0, periodText.length()-1);
            if (percentText.length() > String.valueOf(Double.MAX_VALUE).length() || investText.length() > String.valueOf(Double.MAX_VALUE).length() || periodText.length() > String.valueOf(Double.MAX_VALUE).length())
                showToast("One of the numbers is too big...");
            double investNum = Double.parseDouble(investText);
            double percentNum = Double.parseDouble(percentText);
            double periodNum = Double.parseDouble(periodText);
            profit.setText("Profit per "+getPeriodName(periodType)+":");
            pField.setText(Calculation.calculateAndFormatProfitPerInterval(investNum, percentNum));
            totalProfit.setText("Total profit:");
            tpField.setText(Calculation.calculateAndFormatProfitTotal(investNum, percentNum, periodNum));
            totalMoney.setText("Total money:");
            tmField.setText(Calculation.formatMoney(Calculation.calculateProfitTotal(investNum, percentNum, periodNum)+investNum));
            //result.setText(getResultText(investNum, percentNum, periodNum, periodType));
            saveToHistory(investNum, percentNum, periodNum, periodType);
        } catch (Exception e) {
            showToast("Please make sure that you wrote a number in each textBox.");
        }
    }

    public static String getResultText(double investNum, double percentNum, double periodNum, PeriodType periodType) {
        return "Profit per "+getPeriodName(periodType)+": "+Calculation.calculateAndFormatProfitPerInterval(investNum, percentNum)+"\n"+"Total profit: "+Calculation.calculateAndFormatProfitTotal(investNum, percentNum, periodNum)+"\n"+"Total money: "+(Calculation.formatMoney(Calculation.calculateProfitTotal(investNum, percentNum, periodNum)+investNum));
    }

    public static String getPeriodName(PeriodType periodType) {
        if (periodType == PeriodType.Day) {
            return "Day";
        } else if (periodType == PeriodType.Month) {
            return "Month";
        }
        return "Year";
    }

    private void showToast(String message) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void deleteEditTextChar(TextView editText) {
        editText.setText(editText.getText().toString().substring(0, editText.getText().length()-1));
    }

    private void sendButtonText(AppCompatButton button) {
        if (clicked == editText.Investment) {
            if (invest.getText().toString().endsWith("|"))
                deleteEditTextChar(invest);
            invest.setText(invest.getText().toString()+button.getText());
        } else if (clicked == editText.Percents) {
            if (percent.getText().toString().endsWith("|"))
                deleteEditTextChar(percent);
            percent.setText(percent.getText().toString()+button.getText());
        } else if (clicked == editText.Period) {
            if (period.getText().toString().endsWith("|"))
                deleteEditTextChar(period);
            period.setText(period.getText().toString()+button.getText());
        }
    }

    private void onEditTextClicked(editText edit) {
        clicked = edit;
        toggleKeyboard();
        if (ViewHelper.timer != null) {
            ViewHelper.timer.cancel();
            ViewHelper.timer = null;
            if (edit != editText.Investment && invest.getText().toString().endsWith("|"))
                deleteEditTextChar(invest);
            if (edit != editText.Percents && percent.getText().toString().endsWith("|"))
                deleteEditTextChar(percent);
            if (edit != editText.Period && period.getText().toString().endsWith("|"))
                deleteEditTextChar(period);
        }
        if (edit == editText.Investment) {
            if (invest.getText().toString().equals("Investment")) {
                invest.setTextColor(Color.WHITE);
                invest.setText("");
            }
            setUpHint(percent, false);
            setUpHint(period, false);
        }
        else if (edit == editText.Percents) {
            if (percent.getText().toString().equals("Percents")) {
                percent.setTextColor(Color.WHITE);
                percent.setText("");
            }
            setUpHint(invest, false);
            setUpHint(period, false);
        }
        else if (edit == editText.Period) {
            if (period.getText().toString().equals("Period")) {
                period.setTextColor(Color.WHITE);
                period.setText("");
            }
            setUpHint(invest, false);
            setUpHint(percent, false);
        }
        ViewHelper.editTextClicked(edit, activity);
    }

    public void loadCalculation(Calculation calculation) {
        hideHistory();
        if (ViewHelper.timer != null) {
            ViewHelper.timer.cancel();
            ViewHelper.timer = null;
        }
        invest.setText(calculation.investment+"");
        period.setText(calculation.period+"");
        percent.setText(calculation.percents+"");
        hideKeyboard();
        changePeriod(calculation.periodType);
        switch (calculation.periodType) {
            case Day:
                ((TextView)(dropdown.getSelectedView())).setText("Day ↓");
                break;
            case Year:
                ((TextView)(dropdown.getSelectedView())).setText("Year ↓");
                break;
            case Month:
                ((TextView)(dropdown.getSelectedView())).setText("Month ↓");
                break;
        }
        //result.setText(getResultText(calculation.investment, calculation.percents, calculation.period, calculation.periodType));
        profit.setText("Profit per "+getPeriodName(periodType)+":");
        pField.setText(Calculation.calculateAndFormatProfitPerInterval(calculation.investment, calculation.percents));
        totalProfit.setText("Total profit:");
        tpField.setText(Calculation.calculateAndFormatProfitTotal(calculation.investment, calculation.percents, calculation.period));
        totalMoney.setText("Total money:");
        tmField.setText(Calculation.formatMoney(Calculation.calculateProfitTotal(calculation.investment, calculation.percents, calculation.period)+calculation.investment));
    }

    private void saveToHistory(double investment, double percents, double period, PeriodType periodType) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.sharedPreferencesName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.historyArrayNameSharedPrefs, null);
        ArrayList<Calculation> calculations;
        if (json != null) {
            Type type = new TypeToken<ArrayList<Calculation>>() {}.getType();
            calculations = gson.fromJson(sharedPreferences.getString(Constants.historyArrayNameSharedPrefs, "{}"), type);
        }
        else
            calculations = new ArrayList<>();
        calculations.add(new Calculation(investment, percents, period, periodType));
        if (calculations.size() > 50)
            calculations.remove(0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        json = gson.toJson(calculations);
        editor.putString(Constants.historyArrayNameSharedPrefs, json);
        editor.apply();
    }

    private void reset() {
        if (ViewHelper.timer != null) {
            ViewHelper.timer.cancel();
            ViewHelper.timer = null;
        }
        setUpHint(profit, true);
        setUpHint(percent,true);
        setUpHint(period, true);
        table.setVisibility(View.GONE);
    }

    private void showHistory() {
        isHistory = true;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.sharedPreferencesName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.historyArrayNameSharedPrefs, null);
        ArrayList<Calculation> calculations;
        if (json != null) {
            Type type = new TypeToken<ArrayList<Calculation>>() {}.getType();
            calculations = gson.fromJson(sharedPreferences.getString(Constants.historyArrayNameSharedPrefs, "{}"), type);
            for (Calculation calculation : calculations) {
                historyList.addView(new HistoryItem(calculation, this));
            }
        } else {
            historyList.addView(new HistoryItem(null, this));
        }
        history.setVisibility(View.VISIBLE);
    }

    private void hideHistory() {
        isHistory = false;
        historyList.removeAllViews();
        history.setVisibility(View.GONE);
    }

    private void changePeriod(PeriodType period) {
        periodType = period;
    }

    private void toggleKeyboard() {
        calculator.setVisibility(View.VISIBLE);
        table.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        calculator.setVisibility(View.GONE);
        table.setVisibility(View.VISIBLE);
    }

}