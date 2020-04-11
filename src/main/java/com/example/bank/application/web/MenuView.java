package com.example.bank.application.web;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(MenuView.ROUTE)
public class MenuView extends Composite<VerticalLayout> {

    public static final String ROUTE = "";

    public MenuView() {

        getContent().add(
                new H1("Online banking"),
                new RouterLink("Balance inquiry", BalanceInquiryView.class),
                new RouterLink("Transfer", TransferView.class)
        );
    }

}
