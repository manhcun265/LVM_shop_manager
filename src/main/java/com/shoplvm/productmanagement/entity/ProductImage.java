package com.shoplvm.productmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_images")
@Data
public class ProductImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
}
