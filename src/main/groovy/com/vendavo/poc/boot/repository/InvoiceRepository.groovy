package com.vendavo.poc.boot.repository

import com.vendavo.poc.boot.model.Invoice
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "invoices", path = "invoices")
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, QueryByExampleExecutor<Invoice> {
}
