package com.example.yang.gout.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.gout.HistoryActivity;
import com.example.yang.gout.Login;
import com.example.yang.gout.R;
import com.example.yang.gout.SetPlanActivity;
import com.example.yang.gout.Util.SharedPreferencesUtils;
import com.example.yang.gout.step.service.StepService;
import com.example.yang.gout.step.service.UpdateUiCallBack;
import com.example.yang.gout.view.StepArcView;

public class StepFragment extends Fragment {
    private TextView tv_data;
    private StepArcView cc;
    private TextView tv_set;
    private TextView tv_isSupport;
    private SharedPreferencesUtils sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_step,null);
        tv_data = (TextView) view.findViewById(R.id.tv_data);
        cc = (StepArcView) view.findViewById(R.id.cc);
        tv_set = (TextView) view.findViewById(R.id.tv_set);
        tv_isSupport = (TextView) view.findViewById(R.id.tv_isSupport);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        addListener();
    }

    private void addListener() {
        tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "已经点设置锻炼计划", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(),SetPlanActivity.class);
                startActivity(intent);
            }
        });
        tv_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "已经点击历史步数", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(),HistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        sp = new SharedPreferencesUtils(this.getContext());
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("计步中...");
       setupService();
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(getActivity(), StepService.class);
        isBind = getActivity().getApplicationContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getActivity().getApplicationContext().startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.getActivity().getApplicationContext().unbindService(conn);
        }
    }



}
