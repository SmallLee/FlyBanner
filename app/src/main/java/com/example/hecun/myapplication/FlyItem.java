package com.example.hecun.myapplication;

import com.fang.flybanner.BannerItem;

/**
 * Created by hecun on 2016/8/26.
 */
public class FlyItem extends BannerItem{
    //图片url地址
    public String imageUrl;
    //图片描述
    public String imageDesc;
    public FlyItem(String imageUrl, String imageDesc){
        super(imageUrl,imageDesc);
//        this.imageUrl = imageUrl;
//        this.imageDesc = imageDesc;
    }
}
