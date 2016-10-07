package com.nateshoffner.seachemdoser.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.ui.activity.DrawerActivity;

public class DefaultFragment extends Fragment {

    private ImageView mImageView;

    public DefaultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_default, container, false);

        TextView tvVersion = (TextView) mRootView.findViewById(R.id.version);
        tvVersion.setText(String.format("Version v%s",
                getString(R.string.version_name)));

        TextView tvEula = (TextView) mRootView.findViewById(R.id.eula);
        tvEula.setText(Html.fromHtml(getString(R.string.about_eula)));
        tvEula.setMovementMethod(LinkMovementMethod.getInstance());

        mImageView = (ImageView) mRootView.findViewById(R.id.icon);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet anim = new AnimationSet(true);
                RotateAnimation rotate = new RotateAnimation(0f, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(800);
                rotate.setInterpolator(new DecelerateInterpolator());
                anim.addAnimation(rotate);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ((DrawerActivity) getActivity()).openDrawer();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mImageView.startAnimation(anim);
            }
        });

        return mRootView;
    }
}
