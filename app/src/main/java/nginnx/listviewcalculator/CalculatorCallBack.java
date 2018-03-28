package nginnx.listviewcalculator;

import android.view.View;

/**
 * Created by songyuqiang on 18/3/27.
 */

public interface CalculatorCallBack {
    void activateNewCurrentItem(int activateAdapterPosition, View mostVisibleView);

    void deactivateCurrentItem(int previousAdapterPosition, View previousActiveView);
}
