package com.mailally.analytics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity placeholder for Analytics.
 */
@Entity
@Table(name = "analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
