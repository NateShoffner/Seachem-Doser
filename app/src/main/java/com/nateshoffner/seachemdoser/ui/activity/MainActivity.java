package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.dialog.DoserChangelog;
import com.nateshoffner.seachemdoser.ui.dialog.MaterialDialogChangeLog;
import com.nateshoffner.seachemdoser.ui.fragment.DefaultFragment;
import com.nateshoffner.seachemdoser.ui.fragment.ProductFragment;
import com.nateshoffner.seachemdoser.utils.PlayStoreUtils;
import com.nateshoffner.seachemdoser.utils.UnitLocale;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, DrawerActivity {

    private static final String TAG = "MainActivity";

    private Toolbar mToolbar = null;
    private Drawer mDrawer = null;
    private List<ExpandableDrawerItem> mProductTypeItems = new ArrayList<>();

    private final static long PINNED_ITEM_IDENTIFIER = 997;
    private final static long SUPPORT_ITEM_IDENTIFIER = 998;
    private final static long SETTINGS_ITEM_IDENTIFIER = 999;

    private final static String EXTRA_PRODUCT = "PRODUCT";
    private final static String EXTRA_PRODUCT_FRAGMENT = "PRODUCT_FRAGMENT";

    private ExpandableDrawerItem mPinnedItem;

    private long mIdentifierIncrementor;

    // manually track selected item between item expand/collapse
    private IDrawerItem mSelectedProductItem;

    private SeachemProduct mProduct;
    private ProductFragment mProductFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(DoserApplication.getDoserTheme().getResourceId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initializeDrawer(savedInstanceState);
        populatePinnedProducts();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_PRODUCT))
                mProduct = (SeachemProduct)savedInstanceState.getSerializable(EXTRA_PRODUCT);
            if (savedInstanceState.containsKey(EXTRA_PRODUCT_FRAGMENT))
                mProductFragment = (ProductFragment)getSupportFragmentManager().
                        getFragment(savedInstanceState, EXTRA_PRODUCT_FRAGMENT);

            if (mProduct != null && mProductFragment != null)
                showProduct(mProduct, mProductFragment);
        } else {
            showDefaultProduct();

            final MaterialDialogChangeLog cl = DoserChangelog.getInstance(this);
            if (cl.isFirstRun()) {
                final MaterialDialog dialog = cl.getLogDialog();
                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        showInitialPrompts();
                    }
                });
                dialog.show();
            }

            else {
                showInitialPrompts();
            }
        }

        DoserApplication.getDoserPreferences().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInitialPrompts() {
        if (!DoserApplication.getDoserPreferences().isUnitMeasurementSet()) {
            showUnitMeasurementPrompt();
        }

        // prompt for rating after 5 calculations
        if (!DoserApplication.getDoserPreferences().getHasBeenPromptedForRating() &&
                DoserApplication.getDoserPreferences().getTotalCalculations() == 5) {
            showRatingPrompt();
        }
    }

    private void initializeDrawer(Bundle savedInstanceState) {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(new ProfileDrawerItem()
                        .withName(getString(R.string.app_name))
                        .withIcon(R.drawable.ic_launcher)
                        .withEmail(String.format("v%s", getString(R.string.version_name))))
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesClickable(false)
                .build();

        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult, false)
                .withToolbar(mToolbar)
                .withDelayOnDrawerClose(250)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(-1)
                .withTranslucentStatusBar(false)
                .withTranslucentNavigationBar(false)
                .withFullscreen(false)
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == SETTINGS_ITEM_IDENTIFIER) {
                            startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                            if (mSelectedProductItem != null)
                                mDrawer.setSelection(mSelectedProductItem);
                        } else if (drawerItem.getIdentifier() == SUPPORT_ITEM_IDENTIFIER) {
                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                            if (mSelectedProductItem != null)
                                mDrawer.setSelection(mSelectedProductItem);
                        } else {
                            String title = ((Nameable) drawerItem).getName().getText();
                            SeachemProduct product = SeachemManager.getProductByName(title);

                            if (product != null) {
                                mSelectedProductItem = drawerItem;
                                showProduct(product, null);
                            }
                        }

                        return true;
                    }
                }).withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);
                    }
                });

        Drawer.OnDrawerItemClickListener expandableListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (mProduct != null) {
                    for(IDrawerItem item : mDrawer.getDrawerItems()) {
                        if (item instanceof SecondaryDrawerItem) {
                            if (((SecondaryDrawerItem) item).getName().toString() == mProduct.getName()) {
                                item.withSetSelected(true);
                            }
                        }
                    }
                }

                // only allow one item to be expanded at a time
                if (drawerItem instanceof ExpandableDrawerItem) {
                    boolean expanded = ((ExpandableDrawerItem) drawerItem).isExpanded();
                    if (expanded) {
                        for (int expandedPosition : mDrawer.getAdapter().getExpandedItems()) {
                            if (expandedPosition != position) {
                                mDrawer.getAdapter().collapse(expandedPosition);
                                mDrawer.getAdapter().notifyItemChanged(expandedPosition);
                            }
                        }
                    }
                }

                return false;
            }
        };

        mPinnedItem = new ExpandableDrawerItem()
                .withName(R.string.pinned)
                .withSelectable(false)
                .withIcon(FontAwesome.Icon.faw_thumb_tack)
                .withIdentifier(PINNED_ITEM_IDENTIFIER)
                .withIconColorRes(R.color.product_list_text_color)
                .withOnDrawerItemClickListener(expandableListener);

        // populate product items
        List<SeachemProductType> productTypes = SeachemManager.GetProductTypes();

        for (SeachemProductType type : productTypes) {
            ExpandableDrawerItem ex = new ExpandableDrawerItem()
                    .withName(getProductTypeString(type))
                    .withIcon(GoogleMaterial.Icon.gmd_format_color_fill)
                    .withIdentifier(mIdentifierIncrementor++)
                    .withSelectable(false)
                    .withIconColorRes(getProductTypeColorRes(type))
                    .withArrowColorRes(R.color.product_list_text_color)
                    .withOnDrawerItemClickListener(expandableListener);

            List<SeachemProduct> products = SeachemManager.GetProducts(type);

            for (SeachemProduct product : products) {
                ex.withSubItems(new SecondaryDrawerItem()
                        .withName(product.getName())
                        .withIdentifier(mIdentifierIncrementor++)
                        .withLevel(2)
                        .withIcon(GoogleMaterial.Icon.gmd_radio_button_unchecked)
                        .withTextColorRes(R.color.text_color)
                        .withSelectedTextColorRes(R.color.product_list_text_color)
                        .withSelectedIconColorRes(R.color.product_list_text_color));
            }

            mProductTypeItems.add(ex);
            builder.addDrawerItems(ex);
        }

        mDrawer = builder.build();

        mDrawer.addStickyFooterItem(new DividerDrawerItem());
        PrimaryDrawerItem supportItem = new PrimaryDrawerItem()
                .withName(R.string.about_support)
                .withIdentifier(SUPPORT_ITEM_IDENTIFIER)
                .withIcon(GoogleMaterial.Icon.gmd_info_outline)
                .withIconColorRes(R.color.product_list_text_color)
                .withSelectable(false);
        mDrawer.addStickyFooterItem(supportItem);
        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem()
                .withName(R.string.action_settings)
                .withIdentifier(SETTINGS_ITEM_IDENTIFIER)
                .withIcon(GoogleMaterial.Icon.gmd_settings)
                .withIconColorRes(R.color.product_list_text_color)
                .withSelectable(false);
        mDrawer.addStickyFooterItem(settingsItem);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private int getProductTypeColorRes(SeachemProductType productType) {
        switch(productType) {
            case Gravel:
                return R.color.gravel;
            case Planted:
                return R.color.planted;
            case Reef:
                return R.color.reef;
        }

        return 0;
    }

    private void populatePinnedProducts() {
        List<SeachemProduct> pinnedProducts =
                DoserApplication.getDoserPreferences().getPinnedProducts();

        if (pinnedProducts.size() == 0) {
            if (mDrawer.getDrawerItem(PINNED_ITEM_IDENTIFIER) != null) {
                mDrawer.getAdapter().collapse(mDrawer.getPosition(mPinnedItem)); // collapse item first
                mDrawer.removeItem(PINNED_ITEM_IDENTIFIER);
                mDrawer.removeItemByPosition(1); // divider
                return;
            }
        } else {
            mDrawer.addItemAtPosition(mPinnedItem, 0);
            mDrawer.addItemAtPosition(new DividerDrawerItem(), 1);
            mPinnedItem = (ExpandableDrawerItem) mDrawer.getDrawerItem(PINNED_ITEM_IDENTIFIER);
        }

        int parentPosition = mDrawer.getPosition(mPinnedItem);

        boolean isExpanded = mPinnedItem.isExpanded();

        if (mPinnedItem.getSubItems() != null) {
            // collapse the item before clearing to avoid item being locked
            mDrawer.getAdapter().collapse(parentPosition);
            mPinnedItem.getSubItems().clear();
        }

        for (SeachemProduct product : pinnedProducts) {
            mPinnedItem.withSubItems(new SecondaryDrawerItem()
                    .withName(product.getName())
                    .withIdentifier(mIdentifierIncrementor++)
                    .withLevel(2)
                    .withIcon(GoogleMaterial.Icon.gmd_radio_button_unchecked)
                    .withTextColorRes(R.color.text_color)
                    .withSelectedTextColorRes(R.color.product_list_text_color));
        }

        // restore expansion if children exist
        if (isExpanded && mPinnedItem.getSubItems() != null)
            mDrawer.getAdapter().expand(parentPosition);

        mDrawer.getAdapter().notifyItemChanged(parentPosition);
    }

    private void showRatingPrompt() {
        new MaterialDialog.Builder(this)
                .title(String.format("%s %s", getString(R.string.rate), getString(R.string.app_name)))
                .content("Now that you've used the app for a little while, how about leaving a rating/some feedback?")
                .positiveText("Rate Now")
                .negativeText("No Thanks")
                .neutralText("Later")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        PlayStoreUtils.GoToPlayStore(MainActivity.this);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        DoserApplication.getDoserPreferences().setHasBeenPromptedForRating(true);
                    }
                }).show();
    }

    private void showUnitMeasurementPrompt() {
        UnitMeasurement detected = UnitLocale.getLocaleMeasurmentUnit();

        final CharSequence[] items = new CharSequence[UnitMeasurement.values().length];
        final UnitMeasurement[] unitMeasurements = UnitMeasurement.values();

        int checkedIndex = -1;
        String[] stringArray = getResources().getStringArray(R.array.unit_measurements);
        for (int i = 0; i < stringArray.length; i++) {
            UnitMeasurement unitMeasurement = unitMeasurements[i];
            items[i] = stringArray[i];

            if (unitMeasurement == detected)
                checkedIndex = i;
        }

        new MaterialDialog.Builder(this)
                .title(R.string.unit_measurement_dialog_title)
                .items(items)
                .cancelable(false)
                .itemsCallbackSingleChoice(checkedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        UnitMeasurement unitMeasurement = unitMeasurements[i];
                        DoserApplication.getDoserPreferences().setUnitMeasurement(unitMeasurement);
                        return true;
                    }
                })
                .show();
    }

    private void setCurrentFragment(String title, Fragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();

        if (mDrawer.isDrawerOpen())
            mDrawer.closeDrawer();

        invalidateOptionsMenu();
    }

    private String getProductTypeString(SeachemProductType type) {
        switch (type) {
            case Gravel:
                return getString(R.string.product_type_gravel);
            case Planted:
                return getString(R.string.product_type_planted);
            case Reef:
                return getString(R.string.product_type_reef);
        }

        return null;
    }

    public void showProduct(SeachemProduct product, ProductFragment existingFragment) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ProductFragment.EXTRA_PRODUCT, product);

        ProductFragment fragment;
        if (existingFragment != null)
            fragment = existingFragment;
        else {
            fragment = new ProductFragment();
            fragment.setArguments(arguments);
        }

        setCurrentFragment(getString(R.string.app_name), fragment);
        mProduct = product;
        mProductFragment = fragment;
        DoserApplication.getDoserPreferences().setLastProduct(product);
    }

    private void showDefaultProduct() {
        SeachemProduct defaultProduct = DoserApplication.getDoserPreferences().getDefaultProduct();
        if (defaultProduct != null) {
            SeachemProductType type = SeachemManager.getProductType(defaultProduct);
            String parentTitle = getProductTypeString(type);

            ExpandableDrawerItem parent = null;

            // first open parent item
            for (ExpandableDrawerItem item : mProductTypeItems) {
                String title = item.getName().getText();
                if (title != null && title.equals(parentTitle)) {
                    parent = item;
                    item.withIsExpanded(true);
                    mDrawer.updateItem(item);
                    break;
                }
            }

            // now select + click child item
            for (IDrawerItem item : parent.getSubItems()) {
                String title = ((Nameable) item).getName().getText();
                if (title != null && title.equals(defaultProduct.getName())) {
                    mDrawer.setSelection(item, true);
                    break;
                }
            }
        } else {
            setCurrentFragment(getString(R.string.app_name), new DefaultFragment());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mDrawer.saveInstanceState(outState);
        if (mProductFragment != null && mProductFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, EXTRA_PRODUCT_FRAGMENT, mProductFragment);
        }
        outState.putSerializable(EXTRA_PRODUCT, mProduct);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.pref_pinned_products))) {
            populatePinnedProducts();
        }
    }

    @Override
    public void openDrawer() {
        mDrawer.openDrawer();
    }

    @Override
    public void closeDrawer() {
        mDrawer.closeDrawer();
    }
}