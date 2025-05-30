package com.shoplvm.productmanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(columnDefinition = "longtext")
  private String detail;

  @Column(name = "created_at")
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<ProductImage> images;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<ProductStatusLog> statusLogs;
}
