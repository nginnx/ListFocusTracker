package nginnx.listviewcalculator;

import android.view.View;
import android.widget.ListView;

public class ListViewPositionProvider implements ItemPositionProvider {
    private ListView listView;

    public ListViewPositionProvider(ListView listView) {
        this.listView = listView;
    }

    @Override
    public View getChildAt(int position) {
        return listView.getChildAt(position);
    }

    @Override
    public int indexOfChild(View view) {
        return listView.indexOfChild(view);
    }

    @Override
    public int getChildCount() {
        return listView.getChildCount();
    }

    @Override
    public int getLastVisiblePosition() {
        return listView.getLastVisiblePosition();
    }

    @Override
    public int getFirstVisiblePosition() {
        return listView.getFirstVisiblePosition();
    }

    @Override
    public int getHeadersViewCount() {
        return listView.getHeaderViewsCount();
    }
}