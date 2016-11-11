package com.z_tech.mylauncher;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.z_tech.mylauncher.adapter.GridItemAdapter;
import com.z_tech.mylauncher.bean.GridInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    GridInfo mGridInfo;
    List<GridInfo> mGridInfoList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main);

        init();
        initView();

    }

    //初始化数据
    private void init() {

        Intent mainInent = new Intent(Intent.ACTION_MAIN,null);
        mainInent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(mainInent, 0);

        mGridInfoList = new ArrayList<>();

        for(ResolveInfo resolveInfo:resolveInfos){

            Drawable icon = resolveInfo.activityInfo.loadIcon(getPackageManager());
            String title = resolveInfo.activityInfo.loadLabel(getPackageManager()).toString();
            String packageName = resolveInfo.activityInfo.packageName;

            mGridInfo = new GridInfo(icon, title, packageName);
            mGridInfoList.add(mGridInfo);

        }

    }

    //显示数据
    private void initView() {

        mGridView = (GridView) findViewById(R.id.gv_item);
        GridItemAdapter mAdapter = new GridItemAdapter(this,mGridInfoList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    String packageName = mGridInfoList.get(position).getPackageName();
                    Intent intent = MainActivity.this.getPackageManager().getLaunchIntentForPackage(packageName);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"应用打开时出错",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
