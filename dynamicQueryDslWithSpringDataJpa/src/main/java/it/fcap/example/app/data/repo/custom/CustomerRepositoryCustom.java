package it.fcap.example.app.data.repo.custom;

import it.fcap.example.app.data.model.Customer;
import it.fcap.example.app.data.search.CriteriaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by F.C. on 04/04/2017.
 */
public interface CustomerRepositoryCustom<T extends Customer> {

	Page<T> searchAllCustomerWithSearchBean(CriteriaBean criteria, Pageable pageable);

}