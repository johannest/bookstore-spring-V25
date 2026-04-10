package org.vaadin.example.bookstore;

import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import org.vaadin.example.bookstore.authentication.AccessControl;
import org.vaadin.example.bookstore.authentication.AccessControlFactory;
import org.vaadin.example.bookstore.ui.login.LoginScreen;


/**
 * This class is used to listen to BeforeEnter event of all UIs in order to
 * check whether a user is signed in or not before allowing entering any page.
 * Registered as a Spring bean so it is auto-detected by Vaadin Spring.
 */
@Component
public class BookstoreInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent initEvent) {
        final AccessControl accessControl = AccessControlFactory.getInstance()
                .createAccessControl();

        initEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                if (!accessControl.isUserSignedIn() && !LoginScreen.class
                        .equals(enterEvent.getNavigationTarget())) {
                    String redirectPath = enterEvent.getLocation().getPath();
                    QueryParameters queryParameters = QueryParameters.of(
                            LoginScreen.REDIRECT_PARAM, redirectPath);
                    enterEvent.rerouteTo(LoginScreen.class, queryParameters);
                }
            });
        });
    }
}
