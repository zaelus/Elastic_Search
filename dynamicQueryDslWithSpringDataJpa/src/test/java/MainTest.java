import it.fcap.example.app.data.model.Customer;
import it.fcap.example.app.data.repo.CustomerRepository;
import it.fcap.example.app.data.search.CriteriaBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by F.C. on 04/04/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:jpa-context-remote-test.xml"})
@Transactional
@Rollback(true)
public class MainTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void testCreateCustomer(){
		Customer customer = createMockCustomer("Francesco", "Caporale");
		Customer savedOne = customerRepository.save(customer);
		assertNotNull(savedOne);
	}

	@Test
	public void testCreateSome_Customers(){

		List<Customer> mockCustomers = createMockCustomers();

		List<Customer> savedList = customerRepository.save(mockCustomers);

		assertEquals(mockCustomers.size(), savedList.size());
	}

	private List<Customer> createMockCustomers() {

		List<Customer> customers = new ArrayList<>();

		customers.add(new Customer("Angelina", "Jolie"));
		customers.add(new Customer("Brad", "Pitt"));
		customers.add(new Customer("Michael", "Jackson"));
		customers.add(new Customer("Michael", "Douglas"));
		customers.add(new Customer("Kirk", "Douglas"));

		return customers;
	}

	@Test
	public void testSearchWithCriteria(){

		customerRepository.save(createMockCustomers());

		CriteriaBean criteria = new CriteriaBean();
		criteria.setFirstName("cHAEl");
//		criteria.setLastName("ouGlas");

		Pageable pageable = new PageRequest(0, 1000, Sort.Direction.ASC, "id");
		Page<Customer> customers = customerRepository.searchAllCustomerWithSearchBean(criteria, pageable);

		logger.info(">> Filtering with criteria = " + criteria + " ...");

		if(customers != null && !CollectionUtils.isEmpty(customers.getContent())) {
			logger.info("<< Filtered by \"criteria\", Result size : ["+customers.getTotalElements()+"]: ");
			for (Customer customer : customers) {
				logger.info("\tcustomer = " + customer);
			}
		} else {
			logger.info("<< NO RESULTS");
		}

	}

	private Customer createMockCustomer(String fn, String ln) {

		return  new Customer(fn, ln);
	}
}
