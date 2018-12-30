package com.wizardev.autoscrollrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.wizardev.autoscrollrecyclerview.view.AdAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private List<String> adLists = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearSmoothScroller mSmoothScroller;
    private Disposable mAutoTask;
    private int mCurrentPosition ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initData();

        mRecyclerView = findViewById(R.id.autoScrollRecyclerView);
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        AdAdapter adAdapter = new AdAdapter(adLists, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSmoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 3f / (displayMetrics.density);
            }
        };
        mRecyclerView.setAdapter(adAdapter);
    }

    private void initData() {
        adLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546151445706&di=3ef56dc686f920af5e07b40388048075&imgtype=0&src=http%3A%2F%2Fimg3.xiazaizhijia.com%2Fwalls%2F20170116%2F1440x900_d9e56d4eb65eda6.jpg");
        adLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546151446524&di=0b5634579935ff98fe4986d884bafac6&imgtype=0&src=http%3A%2F%2Fs2.sinaimg.cn%2Fmw690%2F002LmZW0zy6KEO1ckc9b1%26690");
        adLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546151448438&di=879393ca5c7f687eb6c86b38b79724d0&imgtype=0&src=http%3A%2F%2Fs14.sinaimg.cn%2Fmw690%2F001R5mFczy77zmp12q15d%26690");
    }



    private void startAuto() {

        if (mAutoTask != null && !mAutoTask.isDisposed()) {
            mAutoTask.dispose();
        }

        mAutoTask = Observable.interval(1, 2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {

            @Override
            public void accept(Long aLong) {
                Log.e("wizardev", "accept: "+ aLong.intValue());
                if (mCurrentPosition == 0) {
                    mCurrentPosition = aLong.intValue();
                } else {
                    mCurrentPosition++;
                }
                mSmoothScroller.setTargetPosition(mCurrentPosition);
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager!=null)
                layoutManager.startSmoothScroll(mSmoothScroller);
            }
        });
    }

    private void stopAuto() {
        if (mAutoTask != null && !mAutoTask.isDisposed()) {
            mAutoTask.dispose();
            mAutoTask = null;
        }
    }

    @Override
    protected void onPause() {
        stopAuto();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startAuto();
        super.onResume();
    }
}
