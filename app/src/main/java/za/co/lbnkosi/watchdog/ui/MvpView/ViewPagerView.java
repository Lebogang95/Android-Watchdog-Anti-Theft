package za.co.lbnkosi.watchdog.ui.MvpView;

public interface ViewPagerView {
    void transparentStatusBar();

    void slideAnimations();

    void fadeAnimations();

    void createViewPager(
            int viewpager, int layoutDots, int btn_skip, int btn_next,
            int layout1, int layout2, int layout3, int layout4
    );
}
