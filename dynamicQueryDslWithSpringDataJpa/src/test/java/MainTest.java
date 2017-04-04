import it.fcap.example.app.data.model.Customer;
import it.fcap.example.app.data.repo.CustomerRepository;
import it.fcap.example.app.data.search.CriteriaBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by F.C. on 04/04/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:jpa-context-remote-test.xml"})
@Transactional
@Rollback(true)
public class MainTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void testCreateCustomer(){
		Customer customer = createMockCustomer("Francesco", "Caporale");
		Customer savedOne = customerRepository.save(customer);

		System.out.println("savedOne = " + savedOne);
	}

	@Test
	public void testCreateSome_Customers(){

		customerRepository.save(new Customer("Giovanni", "Pallini"));
		customerRepository.save(new Customer("Luca", "Giovannelli"));
		customerRepository.save(new Customer("Giovanni Maria", "Teutezi"));
		customerRepository.save(new Customer("Maria", "Callo"));

	}

	@Test
	public void testSearchWithCriteria(){

		CriteriaBean criteria = new CriteriaBean();
		criteria.setFirstName("vanni");
		criteria.setLastName("utez");

		Pageable pageable = new PageRequest(0, 1000, Sort.Direction.ASC, "id");
		Page<Customer> customers = customerRepository.searchAllCustomerWithSearchBean(criteria, pageable);

		System.out.println("Filtered by \"criteria\" Result : ");
		for (Customer customer : customers) {
			System.out.println("customer = " + customer);
		}

	}

	private Customer createMockCustomer(String fn, String ln) {

		return  new Customer(fn, ln);
	}
}
