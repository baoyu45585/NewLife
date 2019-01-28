package com.ms.android.base.utils.asset;/**
 * Created by del on 17/4/6.
 */

import android.content.Context;
import android.text.TextUtils;

/**
 * created by lbw at 17/4/6
 *
 */
public class UpdateHtmlUtils {

    private Context context;
    private String htmlContent = null;


    public UpdateHtmlUtils(Context context) {
        this.context = context;

    }

    public String getHtmlContent() {
        return htmlContent;
    }

    /**
     * 读取assets文件夹assetPath的内容
     */
    public String readContent(String assetPath) {
         htmlContent = AssetsUtil.getInstance().getAssetFileContent(assetPath,context);
        if (TextUtils.isEmpty(htmlContent) || !htmlContent.contains("<head>")){
            htmlContent= AssetsUtil.getInstance().getAssetFileContent(assetPath,context);
        }
        return htmlContent;
    }


    /**
     *  内容写入手机路径
     * @param htmlPath  手机路径
     */
    public void writeContent(String htmlPath) {
        FileUtils.WriteStringToFile(htmlPath,htmlContent);
    }

    /**
     * 修改
     */
    public void updateContent(CharSequence target, CharSequence replacement) {
        //修改对应文字
        if (target!=null&&replacement!=null){
            htmlContent = htmlContent.replace(target,replacement);
        }
    }

}
