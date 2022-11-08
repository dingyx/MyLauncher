### 写个简单的 Launcher App

> Launcher 是 Android 系统中的桌面启动器

首先看一下这个简易 launcher 的效果如下:

![Android_Simple_Launcher](https://dingyx.oss-cn-shenzhen.aliyuncs.com/picgo/Android_Simple_Launcher.gif)

> 按下 home 键会提示进入我们的 launcher app，launcher 中有一个 GridView 显示所有的应用程序，点击可以进入相应的 app。(如果没有设置默认或者有多个 launcher 按下home键会提示选择进入哪个 launcher)



### 实现步骤

* 配置 manifest 如下 :
```xml
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
                <category android:name="android.intent.category.HOME"/>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </activity>
    </application> 
```
> `action android:name="android.intent.action.MAIN" `
>
> 表示所在的 Activity 为应用程序入口，启动 application 就启动这个 Activity

> `category android:name="android.intent.category.LAUNCHER"` 
>
> 表示 app 会出现在 launcher 中 

> `category android:name="android.intent.category.HOME" `   
>
> 设置 app 可按下 home 键进入 ( launcher 形式 )

> `category android:name="android.intent.category.DEFAULT" `
>
> 设置 app 可以通过隐式意图打开




* 修改 activity_main 布局，使用一个 GridView 填充
```xml
<GridView
     android:id="@+id/gv_item"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:numColumns="auto_fit"
     android:columnWidth="96dp"
     android:verticalSpacing="12dp"
     android:horizontalSpacing="12dp"
     android:stretchMode="columnWidth"
     android:gravity="center">
</GridView>
```

* 使用 GridView 实现 ImageView+TextView  ( 上面显示app图标 / 下面显示app名字 -- 如上图效果 )  

  *  在 layout 中写一个 grid_item.xml 布局来显示 ImageView+TextView 

    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <ImageView
            android:id="@+id/iv_icon"
            android:layout_gravity="center"
            android:layout_width="90dp"
            android:layout_height="90dp" />

        <TextView
            android:id="@+id/tv_title"
            android:layout_gravity="center"
            android:gravity="center"
            android:singleLine="true"
            android:layout_width="90dp"
            android:layout_height="wrap_content" />

    </LinearLayout>
    ```

  * 写一个 javabean 来存放要显示的数据 GridInfo.java

    ```java
    public class GridInfo {

        private Drawable appIcon;		// 图标
        private String title;			// app 名字
        private String packageName; 	// app 包名 (用来实现根据包名打开app)

        public GridInfo(Drawable appIcon,String title,String packageName) {
            this.appIcon = appIcon;
            this.title = title;
            this.packageName = packageName;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public String getTitle() {
            return title;
        }
    	
    	public String getPackageName() {
            return packageName;
        }

    }
    ```

  * 自定义 GridItemAdapter 继承 BaseAdapter

    ```java
    public class GridItemAdapter extends BaseAdapter{

        private List<GridInfo> mGridInfoList;    //定义数据类
        private LayoutInflater mInflater;       //定义Inflater

        public GridItemAdapter(Context context, List<GridInfo> gridInfoList){
            mGridInfoList = gridInfoList;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return mGridInfoList.size();
        }

        public Object getItem(int position) {
            return mGridInfoList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.grid_item,null);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //设置holder
            GridInfo mGridInfo =  mGridInfoList.get(position);
            viewHolder.iv.setImageDrawable(mGridInfo.getAppIcon());
            viewHolder.tv.setText(mGridInfo.getTitle());
            return convertView;
        }

        private class ViewHolder{
            ImageView iv;
            TextView tv;
        }

    }
    ```

  

* MainActivity.java 实现点击图标进入相应程序


```java
public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    //应用的图标，名称，包名
    GridInfo mGridInfo;
    List<GridInfo> mGridInfoList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//隐藏ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
		// 初始化数据
        init();
        // 初始化View
        initView();
    }

    private void init() {
		// 获取所有可显示的应用信息
        Intent mainInent = new Intent(Intent.ACTION_MAIN,null);
        mainInent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(mainInent, 0);
      	// 新建集合保存显示时需要的 app 信息
        mGridInfoList = new ArrayList();
      	// 遍历 resolveInfos 获取 app 名字,图标,包名 保存在 mGridInfoList
        for(ResolveInfo resolveInfo:resolveInfos){
            Drawable icon = resolveInfo.activityInfo.loadIcon(getPackageManager());
            String title = resolveInfo.activityInfo.loadLabel(getPackageManager()).toString();
            String packageName = resolveInfo.activityInfo.packageName;

            mGridInfo = new GridInfo(icon, title, packageName);
            mGridInfoList.add(mGridInfo);
        }
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gv_item);
        GridItemAdapter mAdapter = new GridItemAdapter(this,mGridInfoList);
        mGridView.setAdapter(mAdapter);
		
		//点击图标打开应用
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    String packageName = mGridInfoList.get(position).getPackageName();
                    Intent intent = MainActivity.this.getPackageManager().getLaunchIntentForPackage(packageName);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"该应用无法直接打开",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
```
只需如上几步即可实现上图演示的 Launcher app 效果

