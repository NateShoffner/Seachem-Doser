package com.nateshoffner.seachemdoser.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.LibsConfiguration;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.ui.dialog.DoserChangelog;
import com.nateshoffner.seachemdoser.ui.dialog.MaterialDialogChangeLog;
import com.nateshoffner.seachemdoser.utils.PlayStoreUtils;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DoserApplication.getDoserTheme().getResourceId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
            initializeAboutFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.about_menu, menu);

        MenuItem rateItem = menu.findItem(R.id.action_rate);
        rateItem.setIcon(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_rate_review).actionBar().color(Color.WHITE));

        MenuItem changelogItem = menu.findItem(R.id.action_changelog);
        changelogItem.setIcon(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_history).actionBar().color(Color.WHITE));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_changelog:
                MaterialDialogChangeLog cl = DoserChangelog.getInstance(AboutActivity.this);
                cl.getFullLogDialog().show();
                return true;
            case R.id.action_rate:
                PlayStoreUtils.GoToPlayStore(AboutActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeAboutFragment() {

        LibsConfiguration.LibsListener libsListener = new LibsConfiguration.LibsListener() {

            @Override
            public void onIconClicked(View v) {
                if (v instanceof AppCompatImageView) {
                    final AppCompatImageView imageView = (AppCompatImageView)v;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnimationSet anim = new AnimationSet(true);
                            RotateAnimation rotate = new RotateAnimation(0f, 360f,
                                    Animation.RELATIVE_TO_SELF, 0.5f,
                                    Animation.RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(800);
                            rotate.setInterpolator(new DecelerateInterpolator());
                            anim.addAnimation(rotate);
                            imageView.startAnimation(anim);
                        }
                    });
                }
            }

            @Override
            public boolean onLibraryAuthorClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryContentClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryBottomClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onExtraClicked(View v, Libs.SpecialButton specialButton) {
                return false;
            }

            @Override
            public boolean onIconLongClicked(View v) {
                return false;
            }

            @Override
            public boolean onLibraryAuthorLongClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryContentLongClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryBottomLongClicked(View v, Library library) {
                return false;
            }
        };

        final LibsBuilder libsBuilder = new LibsBuilder()
                .withAboutDescription(String.format("%s<br><br>%s<br><br>%s<br><br>%s",
                        getString(R.string.about_author),
                        getString(R.string.about_eula),
                        getString(R.string.about_disclaimer),
                        getString(R.string.about_libraries)))
                .withAboutAppName(getString(R.string.app_name))
                .withAutoDetect(true)
                .withLibraries("liberation_fonts")
                .withLicenseShown(true)
                .withLicenseDialog(true)
                .withAboutVersionString(String.format("v%s (rev. %s)",
                        getString(R.string.version_name_human),
                        getString(R.string.build_revision)))
                .withListener(libsListener);

        LibsSupportFragment fragment = libsBuilder.supportFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
    }
}
