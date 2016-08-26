# FlyBanner

一个自带指示器的图片轮播控件，可以指定指示器的样式和位置以及轮播图的其他一些属性。

###默认样式

![image](https://github.com/SmallLee/FlyBanner/blob/master/default.gif)

###修改后的样式

![image](https://github.com/SmallLee/FlyBanner/blob/master/GIF.gif)

##引入


##使用


在布局文件中添加
···
 <com.fang.flybanner.FlyBanner
       android:id="@+id/flyBanner"
       android:layout_width="match_parent"
       android:layout_height="160dp"
       app:delayTime="6"
       app:intervalTime="6"
       app:stopWhenTouch="true"
       app:leftScroll="true"
       />
···

其中app开头的是自定义样式，可以采用这种方式，也可以采用在代码中设置的方式,在Android Studio中自定义属性采用下面的命名空间，
当然app这个名字是可以修改的，大家看自己的喜好
···
xmlns:app="http://schemas.android.com/apk/res-auto"
···

##代码中使用

1.首先我们要写一个实体类继承BannerItem这个类

···
/**
 * 轮播图实体类
 */
public class FlyItem extends BannerItem{
    //图片url地址
    public String imageUrl;
    //图片描述
    public String imageDesc;
    public FlyItem(String imageUrl, String imageDesc){
        super(imageUrl,imageDesc);//这句必须写，否则无法轮播图正常工作
    }
}
···

2.将获得的数据添加到集合中

···
  private List<FlyItem> mList = new ArrayList<>();
//添加数据
   mList.add(new FlyItem(Constant.IMAGE_URL_ONE,"第一幅画"));
        mList.add(new FlyItem(Constant.IMAGE_URL_TWO,"第二幅画"));
        mList.add(new FlyItem(Constant.IMAGE_URL_THREE,"第三幅画"));
        mList.add(new FlyItem(Constant.IMAGE_URL_FOUR,"第四幅画"));
···

3.设置属性并开启轮播(注意：setSource这个方法必须放在最后一个属性)
···
  FlyBanner flyBanner = (FlyBanner) findViewById(R.id.flyBanner);
        flyBanner.setDelayTime(2)//延迟时间
                .setIntervalTime(3)//轮播图间隔
                .setLeftScroll(true)//起始时是否可以左滑
                .setStopWhenTouch(true).//触摸时是否停止轮播
                setMargins(100,0,50).//设置指示器的间距
                setIndicator(R.drawable.rb_selector,30, 0,FlyBanner.RIGTH)//设置指示器的样式和位置
                .setSource(mList)//图片的数据集合
                        .startScroll();//开启轮播
···






