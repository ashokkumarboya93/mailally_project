package com.mailally.dashboard.entity;

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
 * Entity placeholder for Dashboard.
 */
@Entity
@Table(name = "dashboards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
