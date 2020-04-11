package com.example.bank.application.web;

import com.example.bank.port.inbound.exception.InvalidAccountException;
import com.example.bank.port.inbound.exception.WrongPinException;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.math.BigDecimal;

@Route(BalanceInquiryView.ROUTE)
public class BalanceInquiryView extends Composite<VerticalLayout> {

    public static final String ROUTE = "balance-inquiry";

    private final OnlineBankAdapter onlineBanking;

    public BalanceInquiryView(OnlineBankAdapter onlineBanking) {
        this.onlineBanking = onlineBanking;

        TextField accountNumber = new TextField("Account number");
        PasswordField pin = new PasswordField("PIN");
        Button send = new Button("Send");

        getContent().setMaxWidth("800px");
        getContent().add(
                new H1("Balance inquiry"),
                new FormLayout(
                        accountNumber,
                        pin
                ),
                send,
                new RouterLink("Return to menu", MenuView.class)
        );

        send.addClickListener(event -> showBalance(accountNumber.getValue(), pin.getValue()));
    }

    private void showBalance(String accountNumber, String pin) {
        try {
            BigDecimal balance = onlineBanking.balanceInquiry(accountNumber, pin);
            Paragraph paragraph = new Paragraph(String.format("Balance: $%s", balance));
            new Dialog(paragraph).open();

        } catch (InvalidAccountException | WrongPinException e) {
            Notification.show(e.getMessage());
        }
    }

}
