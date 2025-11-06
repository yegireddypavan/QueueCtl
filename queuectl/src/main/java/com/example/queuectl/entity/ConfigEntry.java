package com.example.queuectl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "config")
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class ConfigEntry {
    @Id
    private String keyName;
    private String value;
}
