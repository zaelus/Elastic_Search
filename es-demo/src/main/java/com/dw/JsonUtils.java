package com.dw;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;

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

	public static <T> T fromJson(String jsonInString, Class<T> myClass) {

		ObjectMapper mapper = new ObjectMapper();

		T object = null;
		try {
			object = mapper.readValue(jsonInString, myClass);
		}catch (UnrecognizedPropertyException upe){
			System.out.println("Error JSON -> to -> " + myClass.getName());
		} catch (IOException e ) {
			System.out.println(">>> Error : "  + e.getMessage());
		}
		System.out.println(object);

		return object;
	}
}
