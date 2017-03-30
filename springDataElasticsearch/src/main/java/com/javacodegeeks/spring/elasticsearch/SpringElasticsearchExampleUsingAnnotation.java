package com.javacodegeeks.spring.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration("mainBean")
//@ComponentScan(basePackages = { "com.javacodegeeks.*" })
//@EnableElasticsearchRepositories(basePackages = "com.javacodegeeks.spring.elasticsearch")
//@PropertySource(value = "classpath:config.properties")
public class SpringElasticsearchExampleUsingAnnotation {
	private final String CLUSTER_NAME = "elasticsearch";

//	@Value("${esearch.port}") int port;
//	@Value("${esearch.host}") String hostname;

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ElasticsearchTemplate template;
	
	
	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() {
//		return new ElasticsearchTemplate(getNodeClient());
		return new ElasticsearchTemplate(getClient());
//		return new ElasticsearchTemplate(getTransportClient());
	}

	public static void main(String[] args) throws URISyntaxException, Exception {
//		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ClassPathXmlApplicationContext ctx =new ClassPathXmlApplicationContext(
				"annotationApplicationContext.xml");
		try {
//			ctx.register(SpringElasticsearchExampleUsingAnnotation.class);
//			ctx.refresh();
			System.out.println("Load context");
			SpringElasticsearchExampleUsingAnnotation s = (SpringElasticsearchExampleUsingAnnotation) ctx
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
		} finally {
			ctx.close();
		}
	}

	public void addEmployees() {
		Employee joe = new Employee("01", "Joe", 32);
		Skill javaSkill = new Skill("Java", 10);
		Skill db = new Skill("Oracle", 5);
		joe.setSkills(Arrays.asList(javaSkill, db));
		Employee johnS = new Employee("02", "John S", 32);
		Employee johnP = new Employee("03", "John P", 42);
		Employee sam = new Employee("04", "Sam", 30);

		template.createIndex(Employee.class);
		template.putMapping(Employee.class);
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(joe.getId());
		indexQuery.setObject(joe);
		template.index(indexQuery);
		template.refresh(Employee.class, true);
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
	
	private static NodeClient getNodeClient() {
		return (NodeClient) nodeBuilder().clusterName(UUID.randomUUID().toString()).local(true).node()
				.client();
	}

	@Bean
	public Client getClient(){
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.node", "127.0.0.1:9300")
				.put("cluster.name", "localhost-elasticsearch-local")
				.put("client.transport.sniff", true).build();

		TransportClient client = new TransportClient();

		for(String host: client.settings().getAsArray("transport.client.initial_nodes")) {
			int port = 9300;

			// or parse it from the host string...
			String[] splitHost = host.split(":", 2);
			if(splitHost.length == 2) {
				host = splitHost[0];
				port = Integer.parseInt(splitHost[1]);
			}

			client.addTransportAddress(new InetSocketTransportAddress(host, port));
		}
//		TransportAddress address = new InetSocketTransportAddress("127.0.0.1", 9003);
//		client.addTransportAddress(address);

		ImmutableList<DiscoveryNode> discoveryNodes = client.connectedNodes();
		for (DiscoveryNode discoveryNode : discoveryNodes) {
			System.out.println("discoveryNode.getHostName() = " + discoveryNode.getHostName());
		}


		return client;
	}

	public Client getTransportClient(){
		System.out.println("Starting Elasticsearch Client");
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", CLUSTER_NAME).build();

		TransportClient esClient = new TransportClient(settings);
		for (String host : new String[]{"localhost"}) {
			esClient.addTransportAddress(
					new InetSocketTransportAddress(host, 9300));
			System.out.println(String.format("Added Elasticsearch Node : %s", host));
		}
		System.out.println("Started Elasticsearch Client");

		return esClient;
	}
}
