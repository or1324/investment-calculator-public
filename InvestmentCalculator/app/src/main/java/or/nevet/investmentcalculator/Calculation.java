package or.nevet.investmentcalculator;

public class Calculation {
    public final double investment;
    public final double percents;
    public final double period;
    public final Graphics.PeriodType periodType;
    public Calculation(double investment, double percents, double period, Graphics.PeriodType periodType) {
        this.investment = investment;
        this.percents = percents;
        this.period = period;
        this.periodType = periodType;
    }

    public static String formatMoney(double money) {
        return String.format("%.5f", money);
    }

    public static String calculateAndFormatProfitPerInterval(double investment, double yearlyPercents) {
        if (Graphics.periodType == Graphics.PeriodType.Day) {
            return String.format("%.5f", (((double) investment)*(((double) yearlyPercents)/100d))/365d);
        } else if (Graphics.periodType == Graphics.PeriodType.Month) {
            return String.format("%.5f", (((double) investment)*(((double) yearlyPercents)/100d))/12d);
        } else {
            return String.format("%.5f", (((double) investment)*(((double) yearlyPercents)/100d)));
        }
    }

    public static String calculateAndFormatProfitTotal(double investment, double yearlyPercents, double periodLength) {
        return formatMoney(calculateProfitTotal(investment, yearlyPercents, periodLength));
    }

    public static double calculateProfitTotal(double investment, double yearlyPercents, double periodLength) {
        if (Graphics.periodType == Graphics.PeriodType.Day) {
            return (((double) investment)*(((double) yearlyPercents)/100d)) * (((double) (periodLength))/365d);
        } else if (Graphics.periodType == Graphics.PeriodType.Month) {
            return (((double) investment)*(((double) yearlyPercents)/100d)) * (((double) (periodLength))/12d);
        } else {
            return (((double) investment)*(((double) yearlyPercents)/100d)) * ((double) (periodLength));
        }
    }

}
