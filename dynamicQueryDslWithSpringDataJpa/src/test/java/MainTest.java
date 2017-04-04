import it.fcap.example.app.data.model.Customer;
import it.fcap.example.app.data.repo.CustomerRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by F.C. on 04/04/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:jpa-context-remote-test.xml"})
@Transactional
@Rollback(false)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void testCreateCustomer(){
		Customer customer = createMockCustomer();
		Customer savedOne = customerRepository.save(customer);

		System.out.println("savedOne = " + savedOne);
	}

	private Customer createMockCustomer() {

		return  new Customer("Francesco", "Caporale");
	}
}
