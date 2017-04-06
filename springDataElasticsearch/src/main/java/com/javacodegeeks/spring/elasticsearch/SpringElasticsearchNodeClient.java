package com.javacodegeeks.spring.elasticsearch;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.javacodegeeks.spring.elasticsearch.data.model.Employee;
import com.javacodegeeks.spring.elasticsearch.data.model.Skill;
import com.javacodegeeks.spring.elasticsearch.data.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

@Configuration
public class SpringElasticsearchNodeClient {
	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ElasticsearchTemplate template;

	public static void main(String[] args) throws URISyntaxException, Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"node-client-spring-context.xml");
		try {
			System.out.println("Load context");
			SpringElasticsearchNodeClient s = (SpringElasticsearchNodeClient) ctx
					.getBean("mainBean");

			System.out.println("template = " + s.template);
//			s.addEmployees();
//			s.findAllEmployees();
//			s.findEmployee("Joe");
//			s.findEmployee("John");
//			s.findEmployeesByAge(32);

		} finally {
			ctx.close();
		}
	}

	public void addEmployees() {
		System.out.println("Add employees");

		Skill javaSkill = new Skill("Java", 10);
		Skill db = new Skill("Oracle", 5);
		Skill kotlin = new Skill("Kotlin", 3);

		Employee joe = new Employee("01", "Joe", 32);
		Employee johnS = new Employee("02", "John S", 32);
		Employee johnP = new Employee("03", "John P", 42);
		Employee sam = new Employee("04", "Sam", 30);
		joe.setSkills(Arrays.asList(javaSkill, db));
		johnP.setSkills(Arrays.asList(javaSkill, kotlin));

		template.deleteIndex(Employee.class);
		template.createIndex(Employee.class);
		template.putMapping(Employee.class);
		template.refresh(Employee.class, true);

		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(joe.getId());
		indexQuery.setObject(joe);
		template.index(indexQuery);

		IndexQuery indexQuery1 = new IndexQuery();
		indexQuery1.setId(johnP.getId());
		indexQuery1.setObject(johnP);
		template.index(indexQuery1);

		repository.save(johnS);
		repository.save(johnP);
		repository.save(sam);
	}

	public void findAllEmployees() {
		System.out.println("Find all employees");
		repository.findAll().forEach(System.out::println);
	}

	public void findEmployee(String name) {
		System.out.println("Find employee by name '"+name+"'");
		List<Employee> empList = repository.findEmployeesByName(name);
		System.out.println("Employee list: " + empList);
	}

	public void findEmployeesByAge(int age) {
		System.out.println("Find employees by age : "+ age);
		List<Employee> empList = repository.findEmployeesByAge(age);
		System.out.println("Employee list: " + empList);
	}

	public void shutdownNodeClient(){
		System.exit(0);
	}
}
