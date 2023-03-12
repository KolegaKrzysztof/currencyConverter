package com.kolega.currencyconverter.gui;

import com.kolega.currencyconverter.core.ExchangeRateGetter;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

@Route("gui")
public class GUI extends VerticalLayout {
    ExchangeRateGetter exchangeRateGetter = ExchangeRateGetter.getInstance();

    private final TextField textFieldPLN;
    private final TextField textFieldGBP;
    private final Label exchangeRateLabel;
    double exchangeRate;
    private boolean isGBPUpdating;
    private boolean isPLNUpdating;

    public GUI() {
        textFieldPLN = new TextField("PLN");
        textFieldGBP = new TextField("GBP");
        exchangeRateLabel = new Label("");

        textFieldPLN.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<String>>() {
            @Override
            public void valueChanged(ValueChangeEvent<String> event) {
                if (!isGBPUpdating) {
                    double pln = Double.parseDouble(event.getValue());
                    try {
                        isPLNUpdating = true;
                        exchangeRate =  exchangeRateGetter.getExchangeRate();
                        textFieldGBP.setValue((doubleFormatter(pln/exchangeRate)));
                        exchangeRateLabel.setText("1 GBP = " + doubleFormatter(exchangeRate) + " PLN");
                    } catch (NumberFormatException | URISyntaxException | IOException | InterruptedException e) {
                        textFieldGBP.setValue("Wrong input!");
                    } finally {
                        isPLNUpdating = false;
                    }
                }
            }
        });

        textFieldGBP.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<String>>() {
            @Override
            public void valueChanged(ValueChangeEvent<String> event) {
                if (!isPLNUpdating) {
                    double gbp = Double.parseDouble(event.getValue());
                    try {
                        isGBPUpdating = true;
                        exchangeRate =  exchangeRateGetter.getExchangeRate();
                        textFieldPLN.setValue(doubleFormatter(gbp * exchangeRate));
                        exchangeRateLabel.setText("1 GBP = " + doubleFormatter(exchangeRate) + " PLN");
                    } catch (NumberFormatException | URISyntaxException | IOException | InterruptedException e) {
                        textFieldPLN.setValue("Wrong input!");
                    } finally {
                        isGBPUpdating = false;
                    }
                }
            }
        });

        add(textFieldPLN, textFieldGBP, exchangeRateLabel);
    }

    private String doubleFormatter(Double val){
        return new DecimalFormat("0.00")
                .format(Math.floor(val * 100) / 100);
    }

}
