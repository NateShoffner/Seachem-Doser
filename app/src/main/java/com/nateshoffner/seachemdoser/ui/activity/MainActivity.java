package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.ui.fragment.PreferencesFragment;
import com.nateshoffner.seachemdoser.ui.fragment.ProductDetailFragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private class ProductTypeItem {

        private IDrawerItem mItem;
        private List<IDrawerItem> mChildren = new ArrayList<>();
        private boolean mExpanded;

        public ProductTypeItem(IDrawerItem item) {
            mItem = item;
        }

        public IDrawerItem getItem() {
            return mItem;
        }

        public void addChild(IDrawerItem item) {
            mChildren.add(item);
        }

        public void setOpen(boolean open) {
            mExpanded = open;
        }

        public boolean isExpanded() {
            return mExpanded;
        }

        public void collapse(Drawer drawer) {
            // convert to primitive types to pass as varargs
            long[] ids = new long[mChildren.size()];
            for (int i = 0, mChildrenIdsSize = mChildren.size(); i < mChildrenIdsSize; i++) {
                ids[i] = mChildren.get(i).getIdentifier();
            }

            drawer.removeItems(ids);

            mExpanded = false;
        }

        public void expand(Drawer drawer) {
            int position =  drawer.getPosition(getItem().getIdentifier());
            drawer.addItemsAtPosition(position + 1, mChildren.toArray(new IDrawerItem[mChildren.size()]));
            mExpanded = true;
        }
    }

    private Drawer mDrawer = null;
    private List<ProductTypeItem> mProductItems;
    private PreferencesFragment mPreferencesFragment = new PreferencesFragment();

    private final static long SETTINGS_ITEM_IDENTIFIER = 999;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        int identifierIncrementor = 0;

        List<SeachemProductType> productTypes = SeachemManager.GetProductTypes();

        mProductItems = new ArrayList<>();

        for (int i = 0; i < productTypes.size(); i++) {
            SeachemProductType type = productTypes.get(i);

            ProductTypeItem item = new ProductTypeItem(new PrimaryDrawerItem()
                    .withName(getProductTypeString(type))
                    .withIdentifier(identifierIncrementor++)
                    .withLevel(2)
                    .withSelectable(false));

            List<SeachemProduct> products = SeachemManager.GetProducts(type);

            for (SeachemProduct product : products) {
                item.addChild(new SecondaryDrawerItem()
                        .withName(product.getName())
                        .withIdentifier(identifierIncrementor++)
                        .withLevel(3));
            }

            mProductItems.add(item);
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.action_bar_gradient)
                .addProfiles(
                        new ProfileDrawerItem().withName(getString(R.string.app_name)).withIcon(R.mipmap.ic_launcher)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withDelayOnDrawerClose(250)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Products")

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        ProductTypeItem productTypeItem = getProductTypeItem(drawerItem.getIdentifier());
                        if (productTypeItem != null) {
                            if (productTypeItem.isExpanded()) {
                                productTypeItem.collapse(mDrawer);
                            } else {
                                productTypeItem.expand(mDrawer);
                            }
                        }

                        else {
                            String title = ((Nameable)drawerItem).getName().getText();

                            SeachemProduct product = SeachemManager.getProductByName(title);

                            if (product != null) {
                                showProduct(product);
                            }

                            else if (drawerItem.getIdentifier() == SETTINGS_ITEM_IDENTIFIER) {
                                setCurrentFragment(getString(R.string.action_settings), null,
                                        mPreferencesFragment);
                            }
                        }

                        return true;
                    }
                });



        for (ProductTypeItem item : mProductItems) {
            builder.addDrawerItems(item.getItem());
        }

        mDrawer = builder.build();

        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem()
                .withName(R.string.action_settings)
                .withIdentifier(SETTINGS_ITEM_IDENTIFIER);
        mDrawer.addStickyFooterItem(settingsItem);

        showDefaultProduct();
    }

    private void setCurrentFragment(String title, String subtitle, Fragment fragment) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();

        if (mDrawer.isDrawerOpen())
            mDrawer.closeDrawer();
    }

    private ProductTypeItem getProductTypeItem(long id) {
        for (ProductTypeItem item : mProductItems) {
            if (item.getItem().getIdentifier() == id) {
                return item;
            }
        }
        return null;
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
    }

    private void showDefaultProduct() {
        SeachemProduct defaultProduct = DoserApplication.getDoserPreferences().getDefaultProduct();
        if (defaultProduct != null) {
            SeachemProductType type = SeachemManager.getProductType(defaultProduct);
            String parentTitle = getProductTypeString(type);

            // first open parent item
            for (IDrawerItem item : mDrawer.getDrawerItems()) {
                String title = ((Nameable)item).getName().getText();
                if (title.equals(parentTitle)) {
                    mDrawer.setSelection(item, true);
                    break;
                }
            }

            // now select + click child item
            for (IDrawerItem item : mDrawer.getDrawerItems()) {
                String title = ((Nameable)item).getName().getText();
                if (title.equals(defaultProduct.getName())) {
                    mDrawer.setSelection(item, true);
                    break;
                }
            }
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