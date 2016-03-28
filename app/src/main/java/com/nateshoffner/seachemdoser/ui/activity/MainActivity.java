package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.fragment.DefaultFragment;
import com.nateshoffner.seachemdoser.ui.fragment.PreferencesFragment;
import com.nateshoffner.seachemdoser.ui.fragment.ProductDetailFragment;
import com.nateshoffner.seachemdoser.utils.UnitLocale;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private Drawer mDrawer = null;
    private PreferencesFragment mPreferencesFragment = new PreferencesFragment();
    private List<ExpandableDrawerItem> mProductTypeItems = new ArrayList<>();

    private final static long SETTINGS_ITEM_IDENTIFIER = 999;

    // manually track selected item between item expand/collapse
    private IDrawerItem mSelectedProductItem;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(DoserApplication.getThemeId());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.action_bar_gradient)
                .addProfiles(new ProfileDrawerItem()
                                .withName(getString(R.string.app_name))
                                .withIcon(R.mipmap.ic_launcher)
                                .withEmail(String.format("v%s", getString(R.string.version_name))))
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesClickable(false)
                .build();

        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult, true)
                .withToolbar(toolbar)
                .withDelayOnDrawerClose(250)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(-1)
                .withTranslucentStatusBar(false)
                .withTranslucentNavigationBar(false)
                .withFullscreen(false)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == SETTINGS_ITEM_IDENTIFIER) {
                            setCurrentFragment(getString(R.string.action_settings), null,
                                    mPreferencesFragment);
                        } else {
                            String title = ((Nameable) drawerItem).getName().getText();
                            SeachemProduct product = SeachemManager.getProductByName(title);

                            if (product != null) {
                                mSelectedProductItem = drawerItem;
                                showProduct(product);
                            }
                        }

                        return true;
                    }
                });

        // populate product items
        int identifierIncrementor = 0;
        List<SeachemProductType> productTypes = SeachemManager.GetProductTypes();

        for (SeachemProductType type : productTypes) {
            ExpandableDrawerItem ex =  new ExpandableDrawerItem()
                    .withName(getProductTypeString(type))
                    .withIdentifier(identifierIncrementor++)
                    .withSelectable(false)
                    .withIconColorRes(R.color.product_list_text_color);

            ex.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                    if (mSelectedProductItem != null) {
                        // re-select product item
                        mDrawer.setSelection(mSelectedProductItem, false);
                    }

                    // only allow one item to be expanded at a time
                    if(drawerItem instanceof ExpandableDrawerItem) {
                        boolean expanded = ((ExpandableDrawerItem) drawerItem).isExpanded();

                        if(expanded) {
                            for(int expandedPosition : mDrawer.getAdapter().getExpandedItems()) {
                                if(expandedPosition != mDrawer.getPosition(drawerItem)) {
                                    mDrawer.getAdapter().collapse(expandedPosition);
                                    mDrawer.getAdapter().notifyItemChanged(expandedPosition);
                                }
                            }
                        }
                    }

                    return false;
                }
            });

            List<SeachemProduct> products = SeachemManager.GetProducts(type);

            for (SeachemProduct product : products) {
                ex.withSubItems(new SecondaryDrawerItem()
                        .withName(product.getName())
                        .withIdentifier(identifierIncrementor++)
                        .withLevel(2)
                        .withTextColorRes(R.color.product_list_text_color));
            }

            mProductTypeItems.add(ex);
            builder.addDrawerItems(ex);
        }

        mDrawer = builder.build();

        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem()
                .withName(R.string.action_settings)
                .withIdentifier(SETTINGS_ITEM_IDENTIFIER)
                .withIcon(GoogleMaterial.Icon.gmd_settings);
        mDrawer.addStickyFooterItem(settingsItem);

        showDefaultProduct();

        if (!DoserApplication.getDoserPreferences().isUnitMeasurementSet()) {
            showUnitMeasurementPrompt();
        }
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
                .title("Unit Measurement")
                .items(items)
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

    private void setCurrentFragment(String title, String subtitle, Fragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subtitle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();

        if (mDrawer.isDrawerOpen())
            mDrawer.closeDrawer();
    }

    private String getProductTypeString(SeachemProductType type) {
        switch(type) {
            case Gravel:
                return getString(R.string.product_type_gravel);
            case Planted:
                return getString(R.string.product_type_planted);
            case Reef:
                return getString(R.string.product_type_reef);
        }

        return null;
    }

    public void showProduct(SeachemProduct product) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ProductDetailFragment.EXTRA_PRODUCT, product);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(arguments);
        setCurrentFragment(getString(R.string.app_name), product.getName(), fragment);

        DoserApplication.getDoserPreferences().setLastProduct(product);
    }

    private void showDefaultProduct() {
        SeachemProduct defaultProduct = DoserApplication.getDoserPreferences().getDefaultProduct();
        if (defaultProduct != null) {
            SeachemProductType type = SeachemManager.getProductType(defaultProduct);
            String parentTitle = getProductTypeString(type);

            // first open parent item
            for (ExpandableDrawerItem item : mProductTypeItems) {
                String title = item.getName().getText();
                if (title != null && title.equals(parentTitle)) {
                    item.withIsExpanded(true);
                    mDrawer.updateItem(item);
                    break;
                }
            }

            // now select + click child item
            for (IDrawerItem item : mDrawer.getDrawerItems()) {
                String title = ((Nameable)item).getName().getText();
                if (title != null && title.equals(defaultProduct.getName())) {
                    mDrawer.setSelection(item, true);
                    break;
                }
            }
        }

        else {
            setCurrentFragment(getString(R.string.welcome), null, new DefaultFragment());
        }
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}