package com.example.yang.gout;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yang.gout.Util.DbUtils;
import com.example.yang.gout.step.adapter.CommonAdapter;
import com.example.yang.gout.step.adapter.CommonViewHolder;
import com.example.yang.gout.step.service.StepData;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.logging.Logger;

public class HistoryActivity extends AppCompatActivity {
    private LinearLayout layout_titlebar;
    private ImageView iv_left;
    private ImageView iv_right;
    private ListView lv;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void assignViews() {
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        lv = (ListView) findViewById(R.id.lv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_history);
        assignViews();
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        setEmptyView(lv);
        if (DbUtils.getLiteOrm() == null) {
            DbUtils.createDb(this, "jingzhi");
        }
        List<StepData> stepDatas = DbUtils.getQueryAll(StepData.class);
//        Logger.d("stepDatas=" + stepDatas);
        lv.setAdapter(new CommonAdapter<StepData>(this, stepDatas, R.layout.item) {
            @Override
            protected void convertView(View item, StepData stepData) {
                TextView tv_date = CommonViewHolder.get(item, R.id.tv_date);
                TextView tv_step = CommonViewHolder.get(item, R.id.tv_step);
                tv_date.setText(stepData.getToday());
                tv_step.setText(stepData.getStep() + "步");
            }
        });
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("History Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
