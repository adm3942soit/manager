package com.adonis.car.manager;

import com.adonis.car.manager.login.SimpleLoginMainView;
import com.adonis.car.manager.login.SimpleLoginView;
import com.adonis.person.backend.PersonService;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Oxana on 12.02.2017.
 */
public class SimpleLoginUI extends UI {



    @Inject
    PersonService service;


    @PostConstruct
    void load() {
        service.loadDataFromDb();

    }



    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //
        // Create a new instance of the navigator. The navigator will attach
        // itself automatically to this view.
        //
        new Navigator(this, this);

        //
        // The initial log view where the user can login to the application
        //
        getNavigator().addView(SimpleLoginView.NAME, SimpleLoginView.class);//

        //
        // Add the main view of the application
        //
        getNavigator().addView(SimpleLoginMainView.NAME,
                SimpleLoginMainView.class);

        //
        // We use a view change handler to ensure the user is always redirected
        // to the login view if the user is not logged in.
        //
        getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof SimpleLoginView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(SimpleLoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {

            }
        });
    }

    @WebServlet(urlPatterns = "/*", name = "SimpleLoginUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = SimpleLoginUI.class, productionMode = false)
    public static class SimpleLoginUIServlet extends VaadinServlet {
    }
}
