package zp.com.zpfunctiondemo.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zp.com.zpfunctiondemo.R;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class ZpViewPagerTab extends HorizontalScrollView {

    public static final int DEF_VALUE_TAB_TEXT_ALPHA = 255; // 默认等于没有alpha通道

    private final PagerAdapterObserver mAdapterObserver = new PagerAdapterObserver();
    private final PageListener mPageListener = new PageListener();
    public ViewPager.OnPageChangeListener mDelegatePageListener;
    private LinearLayout mTabsContainer;
    private LinearLayout.LayoutParams mTabLayoutParams;
    private OnTabReselectedListener mTabReselectedListener = null;
    private OnTabClickListener tabClickListener;
    private ViewPager mPager;
    private int mTabCount;
    private int mCurrentPosition = 0;
    private float mCurrentPositionOffset = 0f;
    private Paint mRectPaint;
    private Paint mDividerPaint;
    private boolean isIndicatorWidthFixed = true;
    private int mIndicatorColor;
    private int mIndicatorWidth = 27;
    private int mIndicatorHeight = 3;
    private int mIndicatorPadding = 22;
    private RectF mIndicatorRectF = new RectF();
    private float mIndicatorRoundRadius = 1.3f;
    private int mUnderlineHeight = 0;
    private int mUnderlineColor;
    private int mDividerWidth = 0;
    private int mDividerPadding = 0;
    private int mDividerColor;
    private int mTabPadding = 20;
    private int mTabTextSize = 14;
    private ColorStateList mTabTextColor = null;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private boolean isExpandTabs = false;
    private boolean isCustomTabs;
    private boolean isPaddingMiddle = false;
    private boolean isTabTextAllCaps = true;
    private Typeface mTabTextTypeface = null;
    private int mTabTextTypefaceStyle = Typeface.BOLD;
    private int mScrollOffset;
    private int mLastScrollX = 0;
    private boolean mPagerAdapterNotified;
    private int mTabBackgroundResId = R.drawable.widget_psts_tab_background;

    public ZpViewPagerTab(Context context) {
        this(context, null);
    }

    public ZpViewPagerTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZpViewPagerTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
        setWillNotDraw(false);
        setBackgroundColor(ContextCompat.getColor(context, R.color.zp_fff_color));
        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabsContainer);

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset, dm);
        mIndicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorWidth, dm);
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
        mIndicatorPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorPadding, dm);
        mIndicatorRoundRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorRoundRadius, dm);
        mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight, dm);
        mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);
        mTabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabPadding, dm);
        mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, dm);
        mTabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTabTextSize, dm);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerWidth);

        // get default attrs for container
        int textPrimaryColor = ContextCompat.getColor(context, R.color.zp_555_color);
        mUnderlineColor = textPrimaryColor;
        mDividerColor = textPrimaryColor;
        mIndicatorColor = textPrimaryColor;

        String tabTextTypefaceName = "sans-serif";
        // Use Roboto Medium as the default typeface from API 21 onwards
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tabTextTypefaceName = "sans-serif-medium";
//            mTabTextTypefaceStyle = Typeface.NORMAL;
//        }

        // get custom attrs for tabs and container
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        mIndicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, mIndicatorColor);
        isIndicatorWidthFixed = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsIndicatorWidthFixed, isIndicatorWidthFixed);
        mIndicatorWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorWidth, mIndicatorWidth);
        mIndicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, mIndicatorHeight);
        mIndicatorPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorPaddingLeftRight, mIndicatorPadding);
        mIndicatorRoundRadius = a.getFloat(R.styleable.PagerSlidingTabStrip_pstsIndicatorRoundRadius, mIndicatorRoundRadius);
        mUnderlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, mUnderlineColor);
        mUnderlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, mUnderlineHeight);
        mDividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, mDividerColor);
        mDividerWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerWidth, mDividerWidth);
        mDividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, mDividerPadding);
        isExpandTabs = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, isExpandTabs);
        mScrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, mScrollOffset);
        isPaddingMiddle = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsPaddingMiddle, isPaddingMiddle);
        mTabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, mTabPadding);
        mTabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, mTabBackgroundResId);
        mTabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabTextSize, mTabTextSize);
        mTabTextColor = a.hasValue(R.styleable.PagerSlidingTabStrip_pstsTabTextColor) ? a.getColorStateList(R.styleable.PagerSlidingTabStrip_pstsTabTextColor) : null;
        mTabTextTypefaceStyle = a.getInt(R.styleable.PagerSlidingTabStrip_pstsTabTextStyle, mTabTextTypefaceStyle);
        isTabTextAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTabTextAllCaps, isTabTextAllCaps);
        int tabTextAlpha = a.getInt(R.styleable.PagerSlidingTabStrip_pstsTabTextAlpha, DEF_VALUE_TAB_TEXT_ALPHA);
        String fontFamily = a.getString(R.styleable.PagerSlidingTabStrip_pstsTabTextFontFamily);
        a.recycle();

        //Tab text color selector
        if (mTabTextColor == null) {
            mTabTextColor = createColorStateList(
                    textPrimaryColor,
                    textPrimaryColor,
                    Color.argb(tabTextAlpha,
                            Color.red(textPrimaryColor),
                            Color.green(textPrimaryColor),
                            Color.blue(textPrimaryColor)));
        }

        //Tab text typeface and style
        if (fontFamily != null) {
            tabTextTypefaceName = fontFamily;
        }
        mTabTextTypeface = Typeface.create(tabTextTypefaceName, mTabTextTypefaceStyle);

        //Bottom padding for the tabs container parent view to show indicator and underline
        setTabsContainerParentViewPaddings();

        //Configure tab's container LayoutParams for either equal divided space or just wrap tabs
        mTabLayoutParams = isExpandTabs ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    private void setTabsContainerParentViewPaddings() {
        int bottomMargin = mIndicatorHeight >= mUnderlineHeight ? mIndicatorHeight : mUnderlineHeight;
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), bottomMargin);
    }

    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        isCustomTabs = pager.getAdapter() instanceof CustomTabProvider;
        pager.addOnPageChangeListener(mPageListener);
        pager.getAdapter().registerDataSetObserver(mAdapterObserver);
        mAdapterObserver.setAttached(true);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        mTabCount = mPager.getAdapter().getCount();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            if (isCustomTabs) {
                tabView = ((CustomTabProvider) mPager.getAdapter()).getCustomTabView(this, i);
            } else {
                tabView = LayoutInflater.from(getContext()).inflate(R.layout.widget_sliding_tab_default, this, false);
            }

            CharSequence title = mPager.getAdapter().getPageTitle(i);
            addTab(i, title, tabView);
        }

        updateTabStyles();
    }

    private void addTab(final int position, CharSequence title, View tabView) {
        TextView textView = (TextView) tabView.findViewById(R.id.widget_sliding_tab_title);
        if (textView != null) {
            if (title != null) {
                textView.setText(title);
            }
        }

        tabView.setFocusable(true);
        tabView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() != position) {
                    View tab = mTabsContainer.getChildAt(mPager.getCurrentItem());
                    unSelect(tab);
                    mPager.setCurrentItem(position);
                } else if (mTabReselectedListener != null) {
                    mTabReselectedListener.onTabReselected(position);
                }
                if (tabClickListener != null) {
                    tabClickListener.clickTab(position);
                }
            }
        });

        mTabsContainer.addView(tabView, position, mTabLayoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            v.setBackgroundResource(mTabBackgroundResId);
            v.setPadding(mTabPadding, v.getPaddingTop(), mTabPadding, v.getPaddingBottom());
            TextView tabTitle = (TextView) v.findViewById(R.id.widget_sliding_tab_title);
            if (tabTitle != null) {
                tabTitle.setTextColor(mTabTextColor);
                tabTitle.setTypeface(mTabTextTypeface, mTabTextTypefaceStyle);
                tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (isTabTextAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tabTitle.setAllCaps(true);
                    } else {
                        tabTitle.setText(tabTitle.getText().toString().toUpperCase());
                    }
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount == 0) {
            return;
        }

        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            //Half screen offset.
            //- Either tabs start at the middle of the view scrolling straight away
            //- Or tabs start at the begging (no padding) scrolling when indicator gets
            //  to the middle of the view width
            newScrollX -= mScrollOffset;
            Pair<Float, Float> lines = getIndicatorCoordinates();
            newScrollX += ((lines.second - lines.first) / 2);
        }

        // 用于控制菜单数据变化时，布局位置的变化问题
        if (mPagerAdapterNotified) {
            newScrollX = mLastScrollX;
            mPagerAdapterNotified = false;
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    public Pair<Float, Float> getIndicatorCoordinates() {
        // default: line below current tab
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        float lineLeft;
        float lineRight;
        if (isIndicatorWidthFixed) {
            float offset = currentTab.getWidth() - mIndicatorWidth;
            lineLeft = currentTab.getLeft() + offset / 2;
            lineRight = currentTab.getRight() - offset / 2;
        } else {
            lineLeft = currentTab.getLeft() + mIndicatorPadding;
            lineRight = currentTab.getRight() - mIndicatorPadding;
        }

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (mCurrentPositionOffset > 0f && mCurrentPosition < mTabCount - 1) {
            View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
            float nextTabLeft;
            float nextTabRight;
            if (isIndicatorWidthFixed) {
                float offset = nextTab.getWidth() - mIndicatorWidth;
                nextTabLeft = nextTab.getLeft() + offset / 2;
                nextTabRight = nextTab.getRight() - offset / 2;
            } else {
                nextTabLeft = nextTab.getLeft() + mIndicatorPadding;
                nextTabRight = nextTab.getRight() - mIndicatorPadding;
            }
            lineLeft = (mCurrentPositionOffset * nextTabLeft + (1f - mCurrentPositionOffset) * lineLeft);
            lineRight = (mCurrentPositionOffset * nextTabRight + (1f - mCurrentPositionOffset) * lineRight);
        }

        return new Pair<>(lineLeft, lineRight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isPaddingMiddle && mTabsContainer.getChildCount() > 0) {
            View view = mTabsContainer.getChildAt(0);
            int halfWidthFirstTab = view.getMeasuredWidth() / 2;
            mPaddingLeft = mPaddingRight = getWidth() / 2 - halfWidthFirstTab;
        }

        if (isPaddingMiddle || mPaddingLeft > 0 || mPaddingRight > 0) {
            int width;
            if (isPaddingMiddle) {
                width = getWidth();
            } else {
                // Account for manually set padding for offsetting tab start and end positions.
                width = getWidth() - mPaddingLeft - mPaddingRight;
            }

            //Make sure tabContainer is bigger than the HorizontalScrollView to be able to scroll
            mTabsContainer.setMinimumWidth(width);
            //Clipping padding to false to see the tabs while we pass them swiping
            setClipToPadding(false);
        }

        setPadding(mPaddingLeft, getPaddingTop(), mPaddingRight, getPaddingBottom());
        if (mScrollOffset == 0) {
            mScrollOffset = getWidth() / 2 - mPaddingLeft;
        }

        if (mPager != null) {
            mCurrentPosition = mPager.getCurrentItem();
        }

        mCurrentPositionOffset = 0f;
        scrollToChild(mCurrentPosition, 0);
        updateSelection(mCurrentPosition);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount == 0) {
            return;
        }

        final int height = getHeight();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            canvas.drawRect(mPaddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + mPaddingRight, height, mRectPaint);
        }

        // draw indicator line
        if (mIndicatorHeight > 0) {
            mRectPaint.setColor(mIndicatorColor);
            Pair<Float, Float> lines = getIndicatorCoordinates();
            mIndicatorRectF.left = lines.first + mPaddingLeft;
            mIndicatorRectF.top = height - mIndicatorHeight;
            mIndicatorRectF.right = lines.second + mPaddingLeft;
            mIndicatorRectF.bottom = height;
            canvas.drawRoundRect(mIndicatorRectF, mIndicatorRoundRadius, mIndicatorRoundRadius, mRectPaint);
        }
    }

    public void setOnTabReselectedListener(OnTabReselectedListener tabReselectedListener) {
        this.mTabReselectedListener = tabReselectedListener;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }

    private void updateSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tv = mTabsContainer.getChildAt(i);
            final boolean selected = i == position;
            if (selected) {
                select(tv);
            } else {
                unSelect(tv);
            }
        }
    }

    private void unSelect(View tab) {
        if (tab != null) {
            TextView tabTitle = (TextView) tab.findViewById(R.id.widget_sliding_tab_title);
            if (tabTitle != null) {
                tabTitle.setSelected(false);
            }
            if (isCustomTabs) {
                ((CustomTabProvider) mPager.getAdapter()).tabUnselected(tab);
            }
        }
    }

    private void select(View tab) {
        if (tab != null) {
            TextView tabTitle = (TextView) tab.findViewById(R.id.widget_sliding_tab_title);
            if (tabTitle != null) {
                tabTitle.setSelected(true);
            }
            if (isCustomTabs) {
                ((CustomTabProvider) mPager.getAdapter()).tabSelected(tab);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPager != null) {
            if (!mAdapterObserver.isAttached()) {
                mPager.getAdapter().registerDataSetObserver(mAdapterObserver);
                mAdapterObserver.setAttached(true);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPager != null) {
            if (mAdapterObserver.isAttached()) {
                mPager.getAdapter().unregisterDataSetObserver(mAdapterObserver);
                mAdapterObserver.setAttached(false);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.currentPosition;
        if (mCurrentPosition != 0 && mTabsContainer.getChildCount() > 0) {
            unSelect(mTabsContainer.getChildAt(0));
            select(mTabsContainer.getChildAt(mCurrentPosition));
        }
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = mCurrentPosition;
        return savedState;
    }

    public int getIndicatorColor() {
        return this.mIndicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
    }

    public int getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.mIndicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public int getDividerWidth() {
        return mDividerWidth;
    }

    public void setDividerWidth(int dividerWidthPx) {
        this.mDividerWidth = dividerWidthPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.mUnderlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getDividerPadding() {
        return mDividerPadding;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.mDividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getScrollOffset() {
        return mScrollOffset;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.mScrollOffset = scrollOffsetPx;
        invalidate();
    }

    public boolean getShouldExpand() {
        return isExpandTabs;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.isExpandTabs = shouldExpand;
        if (mPager != null) {
            requestLayout();
        }
    }

    public int getTextSize() {
        return mTabTextSize;
    }

    public void setTextSize(int textSizePx) {
        this.mTabTextSize = textSizePx;
        updateTabStyles();
    }

    public boolean isTextAllCaps() {
        return isTabTextAllCaps;
    }

    public ColorStateList getTextColor() {
        return mTabTextColor;
    }

    void setTextColor(ColorStateList colorStateList) {
        this.mTabTextColor = colorStateList;
        updateTabStyles();
    }

    void setTextColor(int textColor) {
        setTextColor(createColorStateList(textColor));
    }

    int getTabBackground() {
        return mTabBackgroundResId;
    }

    public void setTabBackground(int resId) {
        this.mTabBackgroundResId = resId;
    }

    public int getTabPaddingLeftRight() {
        return mTabPadding;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.mTabPadding = paddingPx;
        updateTabStyles();
    }

    public LinearLayout getTabsContainer() {
        return mTabsContainer;
    }

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public float getCurrentPositionOffset() {
        return mCurrentPositionOffset;
    }

    public void setIndicatorColorResource(int resId) {
        this.mIndicatorColor = ContextCompat.getColor(getContext(), resId);
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.mUnderlineColor = ContextCompat.getColor(getContext(), resId);
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.mDividerColor = ContextCompat.getColor(getContext(), resId);
        invalidate();
    }

    public void setAllCaps(boolean textAllCaps) {
        this.isTabTextAllCaps = textAllCaps;
    }

    public void setTextColorResource(int resId) {
        setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    public void setTextColorStateListResource(int resId) {
        setTextColor(ContextCompat.getColorStateList(getContext(), resId));
    }

    private ColorStateList createColorStateList(int colorStateDefault) {
        return new ColorStateList(
                new int[][] {
                        new int[] {} //default
                },
                new int[] {
                        colorStateDefault //default
                }
        );
    }

    private ColorStateList createColorStateList(int colorStatePressed, int colorStateSelected, int colorStateDefault) {
        return new ColorStateList(
                new int[][] {
                        new int[] {android.R.attr.state_pressed}, //pressed
                        new int[] {android.R.attr.state_selected}, // enabled
                        new int[] {} //default
                },
                new int[] {
                        colorStatePressed,
                        colorStateSelected,
                        colorStateDefault
                }
        );
    }

    public void setTypeface(Typeface typeface, int style) {
        this.mTabTextTypeface = typeface;
        this.mTabTextTypefaceStyle = style;
        updateTabStyles();
    }

    public interface CustomTabProvider {

        View getCustomTabView(ViewGroup parent, int position);

        void tabSelected(View tab);

        void tabUnselected(View tab);
    }

    public interface OnTabReselectedListener {

        void onTabReselected(int position);
    }

    public void setOnTabClickListener(OnTabClickListener tabClickListener) {
        this.tabClickListener = tabClickListener;
    }

    /**
     * tab 点击监听
     */
    public interface OnTabClickListener{
        void clickTab(int position);
    }

    static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            int offset = mTabCount > 0 ? (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()) : 0;
            scrollToChild(position, offset);
            invalidate();
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mPager.getCurrentItem(), 0);
            }
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            updateSelection(position);

            //Select current item
            View currentTab = mTabsContainer.getChildAt(position);
            select(currentTab);
            //Unselect prev item
            if (position > 0) {
                View prevTab = mTabsContainer.getChildAt(position - 1);
                unSelect(prevTab);
            }
            //Unselect next item
            if (position < mPager.getAdapter().getCount() - 1) {
                View nextTab = mTabsContainer.getChildAt(position + 1);
                unSelect(nextTab);
            }

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
        }

    }

    private class PagerAdapterObserver extends DataSetObserver {

        private boolean attached = false;

        @Override
        public void onChanged() {
            mPagerAdapterNotified = true;
            notifyDataSetChanged();
        }

        boolean isAttached() {
            return attached;
        }

        void setAttached(boolean attached) {
            this.attached = attached;
        }
    }

}
