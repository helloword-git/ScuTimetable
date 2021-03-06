package com.scu.timetable.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.felix.atoast.library.AToast;
import com.scu.timetable.R;
import com.scu.timetable.events.HideLoadingEvent;
import com.scu.timetable.events.ShowLoadingEvent;
import com.scu.timetable.events.StartFragmentEvent;
import com.scu.timetable.service.AlarmService;
import com.scu.timetable.ui.fragment.MainFragment;
import com.scu.timetable.ui.popup.UpdatePopup;
import com.scu.timetable.utils.TimetableHelper;
import com.scu.timetable.utils.UpdateUtil;
import com.zpj.fragmentation.SupportActivity;
import com.zpj.fragmentation.anim.DefaultHorizontalAnimator;
import com.zpj.fragmentation.anim.FragmentAnimator;
import com.zpj.popup.ZPopup;
import com.zpj.popup.impl.LoadingPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Z-P-J
 */
public final class MainActivity extends SupportActivity {

    private long firstTime = 0;

    private LoadingPopup loadingPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
//        EventBus.getDefault().register(this);

        AlarmService.start(this);

        MainFragment mainFragment = findFragment(MainFragment.class);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        loadRootFragment(R.id.content, mainFragment);

        UpdateUtil.with(this)
                .setOnErrorListener(throwable -> AToast.error("检查更新出错 errorMsg:" + throwable.getMessage()))
                .setOnSuccessListener(event -> {
                    if (event.getUpdateInfo() != null) {
                        AToast.normal("开始更新！");
                        new UpdatePopup(MainActivity.this, event.getUpdateInfo()).show();
//                        UpdateDialogFragment.newInstance(event.getUpdateInfo()).show(getSupportFragmentManager());
                    } else if (event.isLatestVersion()) {
                        AToast.normal("软件已是最新版");
                    }
                })
                .checkUpdate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
            return;
        }
        if (TimetableHelper.isVisitorMode()) {
            super.onBackPressedSupport();
            finish();
            return;
        }
        if (System.currentTimeMillis() - firstTime > 2000) {
            AToast.normal("再次点击退出！");
            firstTime = System.currentTimeMillis();
        } else {
            TimetableHelper.closeVisitorMode();
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }


    //    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onUpdateEvent(UpdateEvent event) {
//        if (event.getUpdateInfo() != null) {
//            AToast.normal("开始更新！");
////        http://tt.shouji.com.cn/wap/down/soft?id=1555815
//            UpdateDialogFragment.newInstance(event.getUpdateInfo()).show(getSupportFragmentManager());
//        } else if (event.isLatestVersion()) {
//            AToast.normal("软件已是最新版");
//        } else if (TextUtils.isEmpty(event.getErrorMsg())) {
//            AToast.error("检查更新出错 errorMsg:" + event.getErrorMsg());
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartFragmentEvent(StartFragmentEvent event) {
        start(event.getFragment());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowLoadingEvent(ShowLoadingEvent event) {
        if (loadingPopup != null && event.isUpdate()) {
            loadingPopup.setTitle(event.getText());
            return;
        }
        loadingPopup = null;
        loadingPopup = ZPopup.loading(MainActivity.this)
                .setTitle(event.getText())
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHideLoadingEvent(HideLoadingEvent event) {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
            loadingPopup = null;
        }
    }

}
