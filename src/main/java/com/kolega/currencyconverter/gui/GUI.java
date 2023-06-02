package com.kolega.currencyconverter.gui;

import com.kolega.currencyconverter.core.ExchangeRateGetter;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

@Route("converter")
public class GUI extends VerticalLayout {
    ExchangeRateGetter exchangeRateGetter = ExchangeRateGetter.getInstance();

    private final TextField textFieldPLN;
    private final TextField textFieldGBP;
    private final Label exchangeRateLabel;
    double exchangeRate;
    private boolean isUpdating;

    public GUI() {
        this.textFieldPLN = new TextField("");
        Div plnSuffix = new Div();
        plnSuffix.setText("PLN");
        this.textFieldPLN.setSuffixComponent(plnSuffix);

        this.textFieldGBP = new TextField("");
        Div gbpSuffix = new Div();
        gbpSuffix.setText("GBP");
        this.textFieldGBP.setSuffixComponent(gbpSuffix);
        this.exchangeRateLabel = new Label("");

        this.textFieldPLN.addValueChangeListener((ValueChangeListener<ValueChangeEvent<String>>) event -> {
            if (!this.isUpdating) {
                double pln = Double.parseDouble(event.getValue());
                if (pln < 0) {
                    this.textFieldGBP.setValue("Negative value!");
                } else {
                    try {
                        this.isUpdating = true;
                        this.exchangeRate = this.exchangeRateGetter.getExchangeRate("bid");
                        this.textFieldGBP.setValue((doubleFormatter(pln / this.exchangeRate)));
                        this.exchangeRateLabel.setText("1 GBP = " + doubleFormatter(this.exchangeRate) + " PLN");
                        this.textFieldPLN.setLabel("Sent");
                        this.textFieldGBP.setLabel("Received");
                    } catch (NumberFormatException | URISyntaxException | IOException | InterruptedException e) {
                        this.textFieldGBP.setValue("Wrong input!");
                    } finally {
                        this.isUpdating = false;
                    }
                }
            }
        });

        this.textFieldGBP.addValueChangeListener((ValueChangeListener<ValueChangeEvent<String>>) event -> {
            if (!this.isUpdating) {
                double gbp = Double.parseDouble(event.getValue());
                if (gbp < 0) {
                    this.textFieldPLN.setValue("Negative value!");
                } else {
                    try {
                        this.isUpdating = true;
                        this.exchangeRate = this.exchangeRateGetter.getExchangeRate("ask");
                        this.textFieldPLN.setValue(doubleFormatter(gbp * this.exchangeRate));
                        this.exchangeRateLabel.setText("1 GBP = " + doubleFormatter(this.exchangeRate) + " PLN");
                        this.textFieldGBP.setLabel("Sent");
                        this.textFieldPLN.setLabel("Received");
                    } catch (NumberFormatException | URISyntaxException | IOException | InterruptedException e) {
                        this.textFieldPLN.setValue("Wrong input!");
                    } finally {
                        this.isUpdating = false;
                    }
                }
            }
        });
        add(this.textFieldPLN, this.textFieldGBP, this.exchangeRateLabel);
    }

    private String doubleFormatter(Double val){
        return new DecimalFormat("0.00")
                .format(Math.floor(val * 100) / 100);
    }

}
