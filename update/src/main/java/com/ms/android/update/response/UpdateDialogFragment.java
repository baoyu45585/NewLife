package com.ms.android.update.response;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.android.update.InstallUtils;
import com.ms.android.update.R;
import com.ms.android.update.UpdateConstant;
import com.ms.android.update.dto.GetVersionDTO;

import static com.ms.android.update.R.id.tv_progress;


/**
 * 更新提示对话框
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/20
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String EXTRA_DTO = "extra_dto";
    private static final String EXTRA_FORCE = "extra_force";
    private TextView mTvVersion;
    private TextView mTvContent;
     ProgressBar mProgressBar;
    private View mDivider;
    private View mDivider2;
    private View mLayoutBottom;
    private TextView mTvUpdateNow;
    private TextView mTvUpdateNext;
    private TextView mTvProgress;

    public static UpdateDialogFragment newInstance(GetVersionDTO dto, boolean needForceUpdate) {
        UpdateDialogFragment dialog = new UpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DTO, dto);
        bundle.putBoolean(EXTRA_FORCE, needForceUpdate);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_white_co20);
            //设置对话框按屏幕宽度的75%
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().moveTaskToBack(true);
                        return false;
                    }
                    return false;
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = inflater.inflate(R.layout.dialog_fragment_update, container, false);
        mTvVersion = (TextView) inflate.findViewById(R.id.tv_version);
        mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
        mTvProgress = (TextView) inflate.findViewById(tv_progress);
        mProgressBar = (ProgressBar) inflate.findViewById(R.id.progress_bar_download);
        mDivider = inflate.findViewById(R.id.divider_bottom);
        mDivider2 = inflate.findViewById(R.id.divider_bottom2);
        mLayoutBottom = inflate.findViewById(R.id.layout_bottom);
        mTvUpdateNow = (TextView) inflate.findViewById(R.id.tv_update_now);
        mTvUpdateNext = (TextView) inflate.findViewById(R.id.tv_update_next);

        GetVersionDTO dto = getArguments().getParcelable(EXTRA_DTO);
        boolean force = getArguments().getBoolean(EXTRA_FORCE, false);
        setCancelable(!force);
        mTvVersion.setText(getString(R.string.update_latest_version, dto != null ? dto.getAppVersion() : "未知"));
        mTvContent.setText(dto.getDescribe());
        mTvUpdateNext.setVisibility(force ? View.GONE : View.VISIBLE);
        mDivider2.setVisibility(force ? View.GONE : View.VISIBLE);
        mTvUpdateNext.setOnClickListener(this);
        mTvUpdateNow.setOnClickListener(this);
        return inflate;
    }

    private void hideBottom() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTvProgress.setVisibility(View.VISIBLE);
        mDivider.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
    }

    /**
     * /sdcard/Android/data/data/包名/cache
     *
     * @return
     */
    private String getApkSavePath() {
        return getActivity().getExternalCacheDir().getAbsolutePath();
    }

    private String getApkName() {
        int appLabelRes = getContext().getApplicationInfo().labelRes;
        return getContext().getString(appLabelRes) + "_ms.apk";
    }

    @Override
    public void onClick(View v) {
        if (v == mTvUpdateNext) {
            //获取点击取消的时间
            UpdateConstant.isUpDate=false;
            dismiss();
        } else {
            if (v == mTvUpdateNow) {
                UpdateConstant.isUpDate = false;
                final GetVersionDTO dto = getArguments().getParcelable(EXTRA_DTO);
                if (!UpdateUtil.isWifi(getContext())){
                    showDialog(getContext(),getString(R.string.prompt),getString(R.string.wifi_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            update(dto);
                        }
                    });
                }else{
                    update(dto);
                }

            }
        }
    }

    private void  update( GetVersionDTO dto ){
        if (dto != null) {
            String appVersion = dto.getAppVersion();
            String url = dto.getUrl();
            boolean force = dto.getForce();
            //如果是强制必须升级的显示对话框下载
            if (force) {
                hideBottom();
//                    new DownloadTask().execute(dto.getUrl());
                update(url,"ms");

            } else {
                dismiss();
                Toast.makeText(getActivity(), getString(R.string.start_load), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DownloadIntentService.class);
                intent.putExtra(DownloadIntentService.INTENT_VERSION_NAME, appVersion);
                intent.putExtra(DownloadIntentService.INTENT_DOWNLOAD_URL, url);
                getActivity().startService(intent);

            }

        }
    }

    /**
     * 对话框
     */
    public void showDialog(Context context,String title, String message, DialogInterface.OnClickListener listener ){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton(getString(R.string.ok), listener);
        normalDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void update(String APK_URL,String APK_NAME) {

        new InstallUtils(getActivity(), APK_URL, APK_NAME, new InstallUtils.DownloadCallBack() {
            @Override
            public void onStart() {
                mProgressBar.setProgress(0);
                mTvProgress.setText(0+"%");
            }

            @Override
            public void onComplete(String path) {
                InstallUtils.installAPK(getActivity(), path, new InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), getString(R.string.installing_program), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(Exception e) {
                       Toast.makeText(getActivity(),getString(R.string.installing_fail),Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
                mProgressBar.setProgress(100);
                mTvProgress.setText(100+"%");
                Toast.makeText(getActivity(),getString(R.string.load_success),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading(long total, long current) {
                mTvProgress.setText((int) (current * 100 / total) + "%");
                mProgressBar.setProgress((int) (current * 100 / total));
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getActivity(),getString(R.string.load_fail),Toast.LENGTH_SHORT).show();
            }

        }).downloadAPK();

    }





}
