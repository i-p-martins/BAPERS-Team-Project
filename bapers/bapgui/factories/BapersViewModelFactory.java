package bapers.bapgui.factories;

import bapers.bapgui.viewmodel.LoginViewModel;

public class BapersViewModelFactory {

    private BapersModelFactory bapersModelFactory;
    private LoginViewModel loginViewModel;

    /**
     * @param bmf
     */
    public BapersViewModelFactory(BapersModelFactory bmf) {
        // TODO - implement BapersViewModelFactory.BapersViewModelFactory
        throw new UnsupportedOperationException();
    }

    public LoginViewModel getLoginViewModel() {
        return this.loginViewModel;
    }

}