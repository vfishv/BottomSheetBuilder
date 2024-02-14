package com.github.rubensousa.bottomsheetbuilder.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.util.BottomSheetBuilderUtils;


public class MainActivity extends AppCompatActivity implements BottomSheetItemClickListener {

    public static final String STATE_SIMPLE = "state_simple";
    public static final String STATE_HEADER = "state_header";
    public static final String STATE_GRID = "state_grid";
    public static final String STATE_LONG = "state_long";

    private BottomSheetMenuDialog mBottomSheetDialog;
    private BottomSheetBehavior mBehavior;

    FloatingActionButton fab;

    AppBarLayout appBarLayout;

    Toolbar toolbar;

    CoordinatorLayout coordinatorLayout;

    private boolean mShowingSimpleDialog;
    private boolean mShowingHeaderDialog;
    private boolean mShowingGridDialog;
    private boolean mShowingLongDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        appBarLayout = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        fab.setOnClickListener(v -> onFabClick());
        findViewById(R.id.showViewBtn).setOnClickListener(v -> onShowViewClick());
        findViewById(R.id.showDialogBtn).setOnClickListener(v -> onShowDialogClick());
        findViewById(R.id.showDialogHeadersBtn).setOnClickListener(v -> onShowDialogHeadersClick());
        findViewById(R.id.showDialogGridBtn).setOnClickListener(v -> onShowDialogGridClick());
        findViewById(R.id.showDialogLongBtn).setOnClickListener(v -> onShowLongDialogClick());

        View bottomSheet = new BottomSheetBuilder(this, coordinatorLayout)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setBackgroundColorResource(android.R.color.white)
                .setMenu(R.menu.menu_bottom_grid_sheet)
                .setItemClickListener(this)
                .createView();

        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BottomSheetBuilderUtils.saveState(outState, mBehavior);
        outState.putBoolean(STATE_SIMPLE, mShowingSimpleDialog);
        outState.putBoolean(STATE_GRID, mShowingGridDialog);
        outState.putBoolean(STATE_HEADER, mShowingHeaderDialog);
        outState.putBoolean(STATE_LONG, mShowingLongDialog);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BottomSheetBuilderUtils.restoreState(savedInstanceState, mBehavior);
        if (savedInstanceState.getBoolean(STATE_GRID)) onShowDialogGridClick();

        if (savedInstanceState.getBoolean(STATE_HEADER)) onShowDialogHeadersClick();

        if (savedInstanceState.getBoolean(STATE_SIMPLE)) onShowDialogClick();

        if (savedInstanceState.getBoolean(STATE_LONG)) onShowLongDialogClick();
    }

    @Override
    protected void onDestroy() {
        // Avoid leaked windows
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        super.onDestroy();
    }

    public void onFabClick() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        fab.hide();
    }

    public void onShowViewClick() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void onShowDialogClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }

        mShowingSimpleDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setAppBarLayout(appBarLayout)
                .addTitleItem("Custom title")
                .addItem(0, "Preview", R.drawable.ic_preview_24dp)
                .addItem(1, "Share", R.drawable.ic_share_24dp)
                .addDividerItem()
                .addItem(2, "Get link", R.drawable.ic_link_24dp)
                .addItem(3, "Make a copy", R.drawable.ic_content_copy_24dp)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingSimpleDialog = false;
                    }
                })
                .createDialog();
        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingSimpleDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }


    public void onShowDialogHeadersClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingHeaderDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog_Custom)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setAppBarLayout(appBarLayout)
                .setMenu(R.menu.menu_bottom_headers_sheet)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingHeaderDialog = false;
                    }
                })
                .createDialog();
        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingHeaderDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    public void onShowDialogGridClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingGridDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setAppBarLayout(appBarLayout)
                .setMenu(getResources().getBoolean(com.github.rubensousa.bottomsheetbuilder.R.bool.tablet_landscape)
                        ? R.menu.menu_bottom_grid_tablet_sheet : R.menu.menu_bottom_grid_sheet)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingGridDialog = false;
                    }
                })
                .createDialog();

        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingGridDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    public void onShowLongDialogClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingLongDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog_Custom)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setAppBarLayout(appBarLayout)
                .setMenu(R.menu.menu_bottom_list_sheet)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingLongDialog = false;
                    }
                })
                .createDialog();

        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingLongDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    @Override
    public void onBottomSheetItemClick(MenuItem item) {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}