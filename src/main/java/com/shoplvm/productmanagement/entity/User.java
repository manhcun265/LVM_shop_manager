package com.shoplvm.productmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String email;

  @Column(name = "password_hash")
  private String passwordHash;

  private String role;

  @Column(name = "created_at")
  private Timestamp createdAt;
}
