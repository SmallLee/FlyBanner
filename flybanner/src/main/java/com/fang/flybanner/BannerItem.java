package com.fang.flybanner;

import java.io.Serializable;

/**
 * 图片实体类
 */
public class BannerItem implements Serializable{
    //图片url地址
    public String imageUrl;
    //图片描述
    public String imageDesc;
    public BannerItem(String imageUrl, String imageDesc){
        this.imageUrl = imageUrl;
        this.imageDesc = imageDesc;
    }
}
