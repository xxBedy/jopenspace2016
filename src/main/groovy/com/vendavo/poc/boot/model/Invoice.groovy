package com.vendavo.poc.boot.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "invoice")
class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String company
    String ico
    String address
}
