package com.dw;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by F.C. on 28/03/2017.
 */
public class JsonUtils {

	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = null;
		try {

			//Convert object to JSON string
//			jsonInString = mapper.writeValueAsString(object);
//			System.out.println(jsonInString);

			//Convert object to JSON string and pretty print
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			System.out.println(jsonInString);


		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonInString;
	}
}
