package nginnx.listviewcalculator;

import android.app.Application;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SingleItemActiveCalculator calculator;
    private ItemPositionProvider itemPositionProvider;
    private ListView listView;

    class Model {
        boolean active;
        String content = "xxxxxxxx";
        View attachView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv);

        final List<Model> models = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            models.add(new Model());
        }
        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return models.size();
            }

            @Override
            public Model getItem(int position) {
                return models.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(parent.getContext());
                Model model = getItem(position);
                model.attachView = tv;
                tv.setTextColor(model.active ? Color.BLACK : Color.WHITE);
                tv.setText(models.get(position).content + "");
                tv.setTextSize(getApplicationContext().getResources().getDimensionPixelSize(R.dimen.size));
                return tv;
            }
        };
        listView.setAdapter(adapter);
        CalculatorCallBack callBack = new CalculatorCallBack() {
            @Override
            public void activateNewCurrentItem(int activateAdapterPosition, View mostVisibleView) {
                models.get(activateAdapterPosition).active = true;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void deactivateCurrentItem(int previousAdapterPosition, View previousActiveView) {
                models.get(previousAdapterPosition).active = false;
            }
        };
        calculator = new SingleItemActiveCalculator(callBack) {
            @Override
            public View getConvertViewByPosition(int adapterPosition) {
                return models.get(adapterPosition).attachView;
            }
        };


        itemPositionProvider = new ListViewPositionProvider(listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    calculator.onScrollStateIdle(itemPositionProvider);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.post(new Runnable() {
            @Override
            public void run() {
                calculator.onScrollStateIdle(itemPositionProvider);
            }
        });
    }
}
