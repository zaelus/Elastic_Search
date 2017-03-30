package com.javacodegeeks.spring.elasticsearch;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

@Configuration
public class SpringElasticsearchExample {
	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ElasticsearchTemplate template;

	public static void main(String[] args) throws URISyntaxException, Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		try {
			System.out.println("Load context");
			SpringElasticsearchExample s = (SpringElasticsearchExample) ctx
					.getBean("mainBean");
			System.out.println("Add employees");
			s.addEmployees();
			System.out.println("Find all employees");
			s.findAllEmployees();
			System.out.println("Find employee by name 'Joe'");
			s.findEmployee("Joe");
			System.out.println("Find employee by name 'John'");
			s.findEmployee("John");
			System.out.println("Find employees by age");
			s.findEmployeesByAge(32);

			System.out.println("Find employees by skills name list");
			s.findAllEmployeesBySkillsName();
		} finally {
			ctx.close();
		}
	}

	private void findAllEmployeesBySkillsName() {
		List<String> skillsToFind = new ArrayList<>();

		skillsToFind.add("Java");
		skillsToFind.add("Kotlin");

		List<Employee> empList = repository.findImpiegatiBySkillsIn(skillsToFind);
		System.out.println("Employee list by skills: " + empList);
	}

//	private void test(){
//		elasticsearchTemplate.deleteIndex(Article.class);
//		elasticsearchTemplate.createIndex(Article.class);
//		elasticsearchTemplate.putMapping(Article.class);
//		elasticsearchTemplate.refresh(Article.class, true);
//
//		IndexQuery article1 = new ArticleBuilder("1").title("article four").addAuthor(RIZWAN_IDREES).addAuthor(ARTUR_KONCZAK).addAuthor(MOHSIN_HUSEN).addAuthor(JONATHAN_YAN).score(10).buildIndex();
//		IndexQuery article2 = new ArticleBuilder("2").title("article three").addAuthor(RIZWAN_IDREES).addAuthor(ARTUR_KONCZAK).addAuthor(MOHSIN_HUSEN).addPublishedYear(YEAR_2000).score(20).buildIndex();
//		IndexQuery article3 = new ArticleBuilder("3").title("article two").addAuthor(RIZWAN_IDREES).addAuthor(ARTUR_KONCZAK).addPublishedYear(YEAR_2001).addPublishedYear(YEAR_2000).score(30).buildIndex();
//		IndexQuery article4 = new ArticleBuilder("4").title("article one").addAuthor(RIZWAN_IDREES).addPublishedYear(YEAR_2002).addPublishedYear(YEAR_2001).addPublishedYear(YEAR_2000).score(40).buildIndex();
//
//		elasticsearchTemplate.index(article1);
//		elasticsearchTemplate.index(article2);
//		elasticsearchTemplate.index(article3);
//		elasticsearchTemplate.index(article4);
//		elasticsearchTemplate.refresh(Article.class, true);
//	}

	public void addEmployees() {
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
		repository.findAll().forEach(System.out::println);
	}

	public void findEmployee(String name) {
		List<Employee> empList = repository.findEmployeesByName(name);
		System.out.println("Employee list: " + empList);
	}

	public void findEmployeesByAge(int age) {
		List<Employee> empList = repository.findEmployeesByAge(age);
		System.out.println("Employee list: " + empList);
	}
}
