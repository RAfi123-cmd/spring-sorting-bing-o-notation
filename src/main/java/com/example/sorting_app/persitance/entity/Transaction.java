package com.example.sorting_app.persitance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "transactions")
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Transaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "invoice_no", length = 20)
    private String invoiceNo;

    @Column (name = "stock_code", length = 20)
    private String stockCode;

    @Column (name = "description")
    private String description;

    @Column (name = "quantity")
    private Integer quantity;

    @Column (name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column (name = "unit_price")
    private Double unitPrice;

    @Column (name = "customer_id")
    private Long customerId;

    @Column (name = "country")
    private String country;

}
