package nginnx.listviewcalculator;

import android.view.View;

/**
 * Created by songyuqiang on 18/3/27.
 */
/*提供来自recyclerView和listView的position*/
public interface ItemPositionProvider {
    View getChildAt(int position);

    int indexOfChild(View view);

    int getChildCount();

    int getLastVisiblePosition();

    int getFirstVisiblePosition();

    int getHeadersViewCount();
}
