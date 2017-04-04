package it.fcap.example.app.data.repo;

import it.fcap.example.app.data.model.Customer;
import it.fcap.example.app.data.repo.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by F.C. on 04/04/2017.
 */

public interface CustomerRepository extends
		JpaRepository<Customer, Long>,
		QueryDslPredicateExecutor<Customer>,
		CustomerRepositoryCustom<Customer> {
	// NOTHING TO DO
}