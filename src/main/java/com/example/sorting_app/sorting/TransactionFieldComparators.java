package com.example.sorting_app.sorting;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.example.sorting_app.persitance.entity.Transaction;

public final class TransactionFieldComparators {

    private TransactionFieldComparators() {
    }
    
    private static final Map<String, Comparator<Transaction>> COMPARATORS = Map.of(
        "quantity", Comparator.comparing(Transaction::getQuantity, Comparator.nullsLast(Comparator.naturalOrder())),
            "unitprice", Comparator.comparing(Transaction::getUnitPrice, Comparator.nullsLast(Comparator.naturalOrder())),
            "customerid", Comparator.comparing(Transaction::getCustomerId, Comparator.nullsLast(Comparator.naturalOrder())),
            "invoicedate", Comparator.comparing(Transaction::getInvoiceDate, Comparator.nullsLast(Comparator.naturalOrder())),
            "description", Comparator.comparing(Transaction::getDescription, Comparator.nullsLast(Comparator.naturalOrder())),
            "stockcode", Comparator.comparing(Transaction::getStockCode, Comparator.nullsLast(Comparator.naturalOrder())),
            "invoiceno", Comparator.comparing(Transaction::getInvoiceNo, Comparator.nullsLast(Comparator.naturalOrder())),
            "country", Comparator.comparing(Transaction::getCountry, Comparator.nullsLast(Comparator.naturalOrder()))
    );

    public static  Comparator<Transaction> forField(String field, String order) {
        Comparator<Transaction> comparator = COMPARATORS.get(field.toLowerCase());
        if (comparator == null) {
            throw new NoSuchElementException(
                    "Unknown sort field '" + field + "'. Available: " + COMPARATORS.keySet());
        }
        return "desc".equalsIgnoreCase(order) ? comparator.reversed() : comparator;
    }
}
