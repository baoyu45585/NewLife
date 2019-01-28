package com.ms.android.base.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.ms.android.base.dialog.SharePlatformDialog;
import com.ms.android.base.entity.ShareDataEntity;
import com.ms.android.base.widget.CmToast;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import me.huha.sharesdk.onekeyshare.OnekeyShareParam;

/**
 * Created by Wenxc on 2017/3/2.
 * 分享activity
 * 使用showShareDialog（）分享方法之前必须先setShareParam，否则会抛出错误
 */
public abstract class ShareActivity extends BaseActivity implements SharePlatformDialog.PlatformSelectedListener, PlatformActionListener {
    public enum ShareType {
        /**
         * 评分成功后分享
         */
        SCORE_SHARE("scoreShare"),
        /**
         * 邀请好友帮忙评分
         */
        MARKE_SHARE("markeShare"),
        /**
         * 分享给未激活成员
         */
        ACTIVITY_SHARE("activityShare"),
        /**
         * 秘盘文件链接分享
         */
        DISC_SHARE("discShare"),
        /**
         * 邀请同事加入
         */
        WORKMATE_SHARE("workmateShare"),
        /**
         * 邀请员工加入
         */
        EMPLOYEE_SHARE("employeeShare"),
        /**
         * 职业征信报告
         */
        REPORT_SHARE("reportShare"),
        /**
         * 邀请HR激活企业
         */
        HR_SHARE("hrShare"),
        /**
         * 文章分享
         */
        ARTICLE_SHARE("articleShare"),

        /**
         * 群二维码分享
         */
        GROUPCODE_SHARE("groupCodeShare");
        String shareTag;

        ShareType(String shareTag) {
            this.shareTag = shareTag;
        }

        public String getShareTag() {
            return shareTag;
        }
    }


    protected interface ShareDataCallback {
        void shareDataCallback(ShareDataEntity entity);

        void onError(Throwable e);
    }

    private SharePlatformDialog dialog;
    private OnekeyShareParam param = null;

    public void setShareParam(OnekeyShareParam param) {
        this.param = param;
    }

    /**
     * 弹出分享对话框
     */
    public void showShareDialog() {
        if (param == null) {
            throw new IllegalArgumentException("before call share dialog,you need to init share params with setShareParam(ShareParams param)");
        }
        if (dialog == null) {
            dialog = new SharePlatformDialog();
            dialog.setSelectedListener(this);
        }
        dialog.show(getSupportFragmentManager());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    final boolean formateShareData(Platform plat) {
        HashMap<String, Object> shareParamsMap = param.getParams();
        String name = plat.getName();

        if ("SinaWeibo".equals(name) || "ShortMessage".equals(name)||"Email".equals(name)) {
            String content = (String) shareParamsMap.get("text");
            String titleUrl = (String) shareParamsMap.get("titleUrl");
            shareParamsMap.put("text", content + "\n" + titleUrl);
        }

        boolean isAlipay = "Alipay".equals(name) || "AlipayMoments".equals(name);
        if (isAlipay && !plat.isClientValid()) {
            toast("ssdk_alipay_client_inavailable");
            return false;
        }

        boolean isKakaoTalk = "KakaoTalk".equals(name);
        if (isKakaoTalk && !plat.isClientValid()) {
            toast("ssdk_kakaotalk_client_inavailable");
            return false;
        }

        boolean isKakaoStory = "KakaoStory".equals(name);
        if (isKakaoStory && !plat.isClientValid()) {
            toast("ssdk_kakaostory_client_inavailable");
            return false;
        }

        boolean isLine = "Line".equals(name);
        if (isLine && !plat.isClientValid()) {
            toast("ssdk_line_client_inavailable");
            return false;
        }

        boolean isWhatsApp = "WhatsApp".equals(name);
        if (isWhatsApp && !plat.isClientValid()) {
            toast("ssdk_whatsapp_client_inavailable");
            return false;
        }

        boolean isPinterest = "Pinterest".equals(name);
        if (isPinterest && !plat.isClientValid()) {
            toast("ssdk_pinterest_client_inavailable");
            return false;
        }

        if ("Instagram".equals(name) && !plat.isClientValid()) {
            toast("ssdk_instagram_client_inavailable");
            return false;
        }

        if ("QZone".equals(name) && !plat.isClientValid()) {
            toast("未安装QQ，不能分享~");
            return false;
        }

        boolean isLaiwang = "Laiwang".equals(name);
        boolean isLaiwangMoments = "LaiwangMoments".equals(name);
        if (isLaiwang || isLaiwangMoments) {
            if (!plat.isClientValid()) {
                toast("ssdk_laiwang_client_inavailable");
                return false;
            }
        }

        boolean isYixin = "YixinMoments".equals(name) || "Yixin".equals(name);
        if (isYixin && !plat.isClientValid()) {
            toast("ssdk_yixin_client_inavailable");
            return false;
        }

        boolean isWechat = "WechatFavorite".equals(name) || "Wechat".equals(name) || "WechatMoments".equals(name);
        if (isWechat && !plat.isClientValid()) {
            toast("未安装微信，不能分享~");
            return false;
        }

        if ("FacebookMessenger".equals(name) && !plat.isClientValid()) {
            toast("ssdk_facebookmessenger_client_inavailable");
            return false;
        }

        if (!shareParamsMap.containsKey("shareType")) {
            int shareType = Platform.SHARE_TEXT;
            String imagePath = String.valueOf(shareParamsMap.get("imagePath"));
            if (imagePath != null && (new File(imagePath)).exists()) {
                shareType = Platform.SHARE_IMAGE;
                if (imagePath.endsWith(".gif") && isWechat) {
                    shareType = Platform.SHARE_EMOJI;
                } else if (shareParamsMap.containsKey("url") && !TextUtils.isEmpty(shareParamsMap.get("url").toString())) {
                    shareType = Platform.SHARE_WEBPAGE;
                    if (shareParamsMap.containsKey("musicUrl") && !TextUtils.isEmpty(shareParamsMap.get("musicUrl").toString()) && isWechat) {
                        shareType = Platform.SHARE_MUSIC;
                    }
                }
            } else {
                Bitmap viewToShare = ResHelper.forceCast(shareParamsMap.get("viewToShare"));
                if (viewToShare != null && !viewToShare.isRecycled()) {
                    shareType = Platform.SHARE_IMAGE;
                    if (shareParamsMap.containsKey("url") && !TextUtils.isEmpty(shareParamsMap.get("url").toString())) {
                        shareType = Platform.SHARE_WEBPAGE;
                        if (shareParamsMap.containsKey("musicUrl") && !TextUtils.isEmpty(shareParamsMap.get("musicUrl").toString()) && isWechat) {
                            shareType = Platform.SHARE_MUSIC;
                        }
                    }
                } else {
                    Object imageUrl = shareParamsMap.get("imageUrl");
                    if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
                        shareType = Platform.SHARE_IMAGE;
                        if (String.valueOf(imageUrl).endsWith(".gif") && isWechat) {
                            shareType = Platform.SHARE_EMOJI;
                        } else if (shareParamsMap.containsKey("url") && !TextUtils.isEmpty(shareParamsMap.get("url").toString())) {
                            shareType = Platform.SHARE_WEBPAGE;
                            if (shareParamsMap.containsKey("musicUrl") && !TextUtils.isEmpty(shareParamsMap.get("musicUrl").toString()) && isWechat) {
                                shareType = Platform.SHARE_MUSIC;
                            }
                        }
                    }
                }
            }
            shareParamsMap.put("shareType", shareType);
        }

        return true;
    }

    private void toast(final String resOrName) {
        UIHandler.sendEmptyMessage(0, new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                int resId = ResHelper.getStringRes(ShareActivity.this, resOrName);
                if (resId > 0) {
                    CmToast.show(ShareActivity.this, resId);
                } else {
                    CmToast.show(ShareActivity.this, resOrName);
                }
                return false;
            }
        });
    }

    final Platform.ShareParams shareDataToShareParams(Platform plat) {
        HashMap<String, Object> shareParamsMap = param.getParams();
        if (plat == null || shareParamsMap == null) {
            toast("ssdk_oks_share_failed");
            return null;
        }

        try {
            String imagePath = ResHelper.forceCast(shareParamsMap.get("imagePath"));
            Bitmap viewToShare = ResHelper.forceCast(shareParamsMap.get("viewToShare"));
            if (TextUtils.isEmpty(imagePath) && viewToShare != null && !viewToShare.isRecycled()) {
                String path = ResHelper.getCachePath(plat.getContext(), "screenshot");
                File ss = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                FileOutputStream fos = new FileOutputStream(ss);
                viewToShare.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                shareParamsMap.put("imagePath", ss.getAbsolutePath());
            }
        } catch (Throwable t) {
            t.printStackTrace();
            toast("ssdk_oks_share_failed");
            return null;
        }

        return new Platform.ShareParams(shareParamsMap);
    }

    final void shareSilently(Platform platform) {
        if (formateShareData(platform)) {
            Platform.ShareParams sp = shareDataToShareParams(platform);
            if (sp != null) {
                platform.setPlatformActionListener(this);
                platform.share(sp);
            }
        }
    }

    @Override
    public void onPlatformSelected(SharePlatformDialog.Platform platform) {
        switch (platform) {
            case COPY: {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                //        cm.setText(mDiscsEntity.getFileUrl());
                cm.setPrimaryClip(ClipData.newPlainText(null, String.valueOf(param.getParams().get("titleUrl"))));
                CmToast.show(ShareActivity.this, "复制成功");
                break;
            }
            case EMAIL: {
                Platform actualPlatform = ShareSDK.getPlatform(this, Email.NAME);
                shareSilently(actualPlatform);
                break;
            }
            case QQ: {
                Platform actualPlatform = ShareSDK.getPlatform(this, QQ.NAME);
                shareSilently(actualPlatform);
                break;
            }
            case RENREN: {
                Platform actualPlatform = ShareSDK.getPlatform(this, Renren.NAME);
                shareSilently(actualPlatform);
                break;
            }
            case SINA_WEIBO: {
                Platform actualPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                shareSilently(actualPlatform);
                break;
            }
            case SMS: {
                Platform actualPlatform = ShareSDK.getPlatform(this, ShortMessage.NAME);
                shareSilently(actualPlatform);
                break;
            }

            case WEICHAT: {
                Platform actualPlatform = ShareSDK.getPlatform(this, Wechat.NAME);
                shareSilently(actualPlatform);
                break;
            }

            case WEIFRIEND: {
                Platform actualPlatform = ShareSDK.getPlatform(this, WechatMoments.NAME);
                shareSilently(actualPlatform);
                break;
            }

        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        CmToast.show(ShareActivity.this, "分享成功");
        Logger.d("share onComplete");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        CmToast.show(ShareActivity.this, throwable.getMessage());
        Logger.e("share onError" + platform.getName());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        CmToast.show(ShareActivity.this, "取消分享");
        Logger.i("share onCancel" + platform.getName());
    }

    public void doShareWithType() {
        requestShareDataWithType(new ShareDataCallback() {
            @Override
            public void shareDataCallback(ShareDataEntity entity) {
                OnekeyShareParam param = new OnekeyShareParam();
                param.setTitle(entity.getTitle());
                param.setUrl(entity.getUrl());
                param.setTitleUrl(entity.getUrl());
                param.setSiteUrl(entity.getUrl());//QZone分享参数
                param.setText(entity.getExplain());
                param.setImageUrl(entity.getPic());
                setShareParam(param);
                showShareDialog();
            }

            @Override
            public void onError(Throwable e) {
                CmToast.show(ShareActivity.this, e.getMessage());
            }
        });
    }

    public void requestShareDataWithType(final ShareDataCallback callback) {

        ShareDataEntity entity=new ShareDataEntity();
        entity.setUrl("https://www.baidu.com/");
        entity.setTitle("百度");
        entity.setPic("http://static.daily.huha.me/402881215bd2d5f8015bd2d5f82c0000/yARJNb.png");
        entity.setExplain("邮件分享");
        if (callback != null) {
            callback.shareDataCallback(entity);
        }
//        callback.onError(new Exception("share request content is null"));
    }
}
