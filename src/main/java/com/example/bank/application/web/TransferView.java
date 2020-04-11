package com.example.bank.application.web;

import com.example.bank.port.inbound.exception.InsufficientFundsException;
import com.example.bank.port.inbound.exception.InvalidAccountException;
import com.example.bank.port.inbound.exception.InvalidAmountException;
import com.example.bank.port.inbound.exception.WrongPinException;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.math.BigDecimal;

@Route(TransferView.ROUTE)
public class TransferView extends Composite<VerticalLayout> {

    public static final String ROUTE = "transfer";

    private final OnlineBankAdapter onlineBanking;

    public TransferView(OnlineBankAdapter onlineBanking) {
        this.onlineBanking = onlineBanking;

        TextField fromAccountNumber = new TextField("From Account number");
        TextField toAccountNumber = new TextField("To Account number");
        NumberField amount = new NumberField("Amount");
        PasswordField pin = new PasswordField("PIN");
        Button send = new Button("Send");

        getContent().setMaxWidth("800px");
        getContent().add(
                new H1("Transfer"),
                new FormLayout(
                        fromAccountNumber,
                        toAccountNumber,
                        amount,
                        pin
                ),
                send,
                new RouterLink("Return to menu", MenuView.class)
        );

        send.addClickListener(event -> transfer(fromAccountNumber.getValue(), toAccountNumber.getValue(), amount.getValue(), pin.getValue()));
    }

    private void transfer(String fromAccountNumber, String toAccountNumber, Double amount, String pin) {
        try {
            String referenceNumber = onlineBanking.transfer(fromAccountNumber, toAccountNumber, new BigDecimal(amount), pin);
            Paragraph paragraph = new Paragraph(String.format("Transaction successful. Reference number: %s", referenceNumber));
            new Dialog(paragraph).open();

        } catch (WrongPinException | InsufficientFundsException | InvalidAccountException | InvalidAmountException e) {
            Notification.show(e.getMessage());
        }
    }

}
