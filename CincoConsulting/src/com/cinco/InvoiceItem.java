package com.cinco;

/**
 * Interface to represent a billable item that can be added to an invoice
 */
public interface InvoiceItem {
    double getCost();
    double getTax();
    double getTotal();
}