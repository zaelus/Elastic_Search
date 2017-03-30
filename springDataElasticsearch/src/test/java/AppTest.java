import junit.framework.TestCase;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertEquals;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AppTest
{

//	@Autowired
    private static Client client;

    @BeforeClass
    public static void initTestCase(){
		Node node = nodeBuilder()
				.clusterName("elasticsearch").client(true).node();
		client = node.client();
    }

    @Test
    public void testGivenJsonString_whenJavaObject_thenIndexDocument() {
        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
                +"\"fullName\":\"John Doe\"}";
        IndexResponse response = client.prepareIndex("people", "Doe")
                .setSource(jsonObject).get();

        String id = response.getId();
        String index = response.getIndex();
        String type = response.getType();
        long version = response.getVersion();

        assertTrue(response.isCreated());
        assertEquals(0, version);
        assertEquals("people", index);
        assertEquals("Doe", type);
    }
}
