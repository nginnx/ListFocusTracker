package nginnx.listviewcalculator;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by songyuqiang on 18/3/27.
 */


/*用来计算ListView或者RecyclerView中可见区域最大的item,同时只有一个item处于活跃状态，类似焦点*/
/* 使用流程:
    1.在ListView或RecyclerView中代理对应的onScrollStateIdle方法
    2.实现callback,callback中将回调新的活跃的item和之前应该失效的item
*
*
* */
public final class SingleItemActiveCalculator implements Calculator {
    private CalculatorCallBack callBack;
    private Rect rectForCalculate = new Rect();
    private int currentActivePosition = -1;
    private View currentActiveView;

    public SingleItemActiveCalculator(CalculatorCallBack callBack) {
        this.callBack = callBack;
    }


    /**
     * @param view 获取单个view在屏幕上的可见比例
     * @return
     */
    @Override
    public int getSingleViewVisibility(View view) {

        int percents = 100;

        view.getLocalVisibleRect(rectForCalculate);

        int height = view.getHeight();

        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = (height - rectForCalculate.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = rectForCalculate.bottom * 100 / height;
        }

        return percents;
    }

    @Override
    public void calculateMostVisibleView(ItemPositionProvider provider) {
        int firstVisiblePosition = provider.getFirstVisiblePosition() - provider.getHeadersViewCount();
        int lastVisiblePosition = provider.getLastVisiblePosition();
        int endIndex = Math.max(lastVisiblePosition, firstVisiblePosition);
        int startIndex = Math.min(lastVisiblePosition, firstVisiblePosition);
        int visibility = 0;
        View mostVisibleView = null;
        int mostVisiblePosition = -1;

        for (int i = startIndex; i < endIndex && i >= 0; i++) {
            View itemView = getViewByPosition(i, provider);
            if (itemView == null) {
                mostVisibleView = null;
                mostVisiblePosition = i;
                break;
            }
            int visibilityPercents = getSingleViewVisibility(itemView);
            if (visibilityPercents > visibility) {
                mostVisibleView = itemView;
                mostVisiblePosition = i;
                visibility = visibilityPercents;
            }
        }
        if (firstVisiblePosition == 0) {
            if (lastVisiblePosition - 1 >= 0) {
                mostVisiblePosition = lastVisiblePosition - 1;
                mostVisibleView = getViewByPosition(mostVisiblePosition, provider);
            }
        }
        if (mostVisiblePosition != -1) {
            if (mostVisiblePosition != currentActivePosition) {
                if (currentActivePosition != -1) {
                    callBack.deactivateCurrentItem(currentActivePosition, currentActiveView);
                }
                currentActivePosition = mostVisiblePosition;
                currentActiveView = mostVisibleView;
                callBack.activateNewCurrentItem(mostVisiblePosition, mostVisibleView);
            }
        }

    }


    /**
     * @param provider idle状态时的position
     */
    @Override
    public void onScrollStateIdle(ItemPositionProvider provider) {
        calculateMostVisibleView(provider);
    }

    @Override
    public void onScroll(ItemPositionProvider itemsPositionGetter, int scrollState) {

    }


    private View getViewByPosition(int pos, ItemPositionProvider provider) {
        final int firstListItemPosition = provider.getFirstVisiblePosition() - provider.getHeadersViewCount();
        final int lastListItemPosition = firstListItemPosition + provider.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return null;
        } else {
            final int childIndex = pos - firstListItemPosition;
            return provider.getChildAt(childIndex);
        }
    }

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return rectForCalculate.bottom > 0 && rectForCalculate.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return rectForCalculate.top > 0;
    }


    /**
     * 在切屏切tab等场景清空状态，否则，再次resume的时候不能重新触发active
     */
    public void reset() {
        currentActivePosition = -1;
        currentActiveView = null;

    }
}
