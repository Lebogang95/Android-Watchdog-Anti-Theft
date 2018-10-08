package za.co.lbnkosi.watchdog.splashscreen;

public class SplashScreenPresenter implements SplashScreenContract.Presenter, SplashScreenContract.onPrefCheckListener {
    private SplashScreenContract.View mSplashScreenView;
    private SplashScreenInteractor mSplashScreenInteractor;

    SplashScreenPresenter(SplashScreenContract.View splashScreenView){
        this.mSplashScreenView = splashScreenView;
        mSplashScreenInteractor = new SplashScreenInteractor(this);
    }

    @Override
    public void onSuccess(String message) {
        mSplashScreenView.onPrefCheckSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mSplashScreenView.onPrefCheckFailure(message);
    }

    @Override
    public void prefCheck() {
        mSplashScreenInteractor.prefCheck();
    }
}

