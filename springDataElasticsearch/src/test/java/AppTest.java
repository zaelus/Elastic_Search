import com.javacodegeeks.spring.elasticsearch.SpringElasticsearchNodeClient;
import com.javacodegeeks.spring.elasticsearch.SpringElasticsearchTransportClient;
import junit.framework.TestCase;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.node.Node;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 * TODO : Not working as desired!
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:transport-client-spring-context.xml"})
public class AppTest
{

    private static final int CLIENT_NODES = 4;
    @Autowired
    private SpringElasticsearchTransportClient mainBean;

    private static List<SpringElasticsearchNodeClient> nodeClientList = new ArrayList<>();

    @BeforeClass
    public static void initTestCase(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "node-client-spring-context.xml");

        for(int i=0; i<CLIENT_NODES; i++){
            System.out.println("Load context [" + (i+1)+"]");
            SpringElasticsearchNodeClient nodeClient = (SpringElasticsearchNodeClient) ctx
                    .getBean("mainBean");
            nodeClientList.add(nodeClient);
        }

        System.out.println("Nodes loaded !");
    }

    @AfterClass
    public static void tearDownTestCase(){

        for (SpringElasticsearchNodeClient nodeClient : nodeClientList) {
            nodeClient.shutdownNodeClient();
        }

    }

    @Test
    public void testTransportClient_(){

        mainBean.addEmployees();
        mainBean.findAllEmployees();
    }
}
