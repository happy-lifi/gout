package com.example.yang.gout.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yang.gout.Adapter.MyFragmentAdapter;
import com.example.yang.gout.Login;
import com.example.yang.gout.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_center extends Fragment {

    CircleImageView circleImageView;
    ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_center,null);
        circleImageView= (CircleImageView) view.findViewById(R.id.head_image);
        viewPager= (ViewPager)view.findViewById(R.id.id_viewpager);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "已经点击", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(),Login.class);
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {
        List<Fragment> list=new ArrayList<>();
//        Bundle bundle1=new Bundle();
//        bundle1.putString("Title","第一个Fragment");
//        bundle1.putInt("pager_num",1);
//        fragment_run fg1= fragment_run.newInstance(bundle1);
//            StepFragment fg1=new StepFragment();
//        Bundle bundle2=new Bundle();
//        bundle2.putString("Title","第二个Fragment");
//        bundle2.putInt("pager_num",2);
//        fragment_run fg2= fragment_run.newInstance(bundle2);
//
//        Bundle bundle3=new Bundle();
//        bundle3.putString("Title","第三个Fragment");
//        bundle3.putInt("pager_num",3);
//        fragment_run fg3= fragment_run.newInstance(bundle3);
//
//        Bundle bundle4=new Bundle();
//        bundle4.putString("Title","第四个Fragment");
//        bundle4.putInt("pager_num",4);
//        fragment_run fg4= fragment_run.newInstance(bundle4);

        list.add(new StepFragment());
        list.add(new fragment_run());
        list.add(new NearFragment());
        list.add(new DiscoveryFragment());

        viewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager(),list));
    }


}
