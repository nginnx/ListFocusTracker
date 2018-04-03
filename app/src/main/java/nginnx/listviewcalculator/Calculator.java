package nginnx.listviewcalculator;

import android.view.View;

/**
 * Created by songyuqiang on 18/3/27.
 */
/*用来计算ListView或者RecyclerView中可见区域最大的item*/
public interface Calculator {
    int getSingleViewVisibility(View view);

    void calculateMostVisibleView(ItemPositionProvider provider);


    void onScrollStateIdle(ItemPositionProvider itemsPositionGetter);

    void onScroll(ItemPositionProvider itemsPositionGetter, int scrollState);
}
