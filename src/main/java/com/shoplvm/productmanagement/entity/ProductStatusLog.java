package com.shoplvm.productmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Entity
@Table(name = "product_status_logs")
@Data
public class ProductStatusLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String status;

  @Column(name = "log_date")
  private Timestamp logDate;
}
