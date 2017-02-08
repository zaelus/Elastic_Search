package com.dw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args )
    {

        logger.info( "Hello World!" );
        Client client =null;
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        } catch (UnknownHostException e) {
            logger.error("Qualcosa è andato storto nella creazione del " + client.getClass().getName(), e);
        }

		/**
		 * INDEX TODO : (PUT/UPDATE) ??
		 */
		IndexResponse indexResponse = null;
        try {
            indexResponse = client.prepareIndex("twitter", "tweet", "2")
                    .setSource(XContentFactory.jsonBuilder()
                            .startObject()
                            .field("user", "francesco")
                            .field("postDate", new Date())
                            .field("message", "Giochiamo un pò con elastic search")
                            .endObject()
                    )
                    .execute()
                    .actionGet();

			System.out.println("indexResponse = " + indexResponse);
		} catch (Exception e) {
            logger.error("Problemi nell'indexing ... ", e);
        }

		/**
		 * GET
		 */
        GetResponse getResponse = (GetResponse) client.prepareGet("twitter", "tweet", "1")
                .get();

        System.out.println("getResponse = " + getResponse.getSourceAsString());


		/**
		 * MULTI_GET
		 */
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "tweet", "1", "2")
                .add("twitter", "songs", "2")
                .add("music", "lyrics", "2")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
				System.out.print("MultiGetItemResponse - ");
				String json = response.getSourceAsString();

                System.out.println("json = " + json);
            }
        }


		/**
		 * Chiusura client
		 */
		client.close();

        System.out.println(" >>> EXIT from Main");
        System.exit(0);
    }
}
