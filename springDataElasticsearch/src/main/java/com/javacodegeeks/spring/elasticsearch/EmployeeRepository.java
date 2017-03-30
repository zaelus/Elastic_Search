package com.javacodegeeks.spring.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EmployeeRepository extends ElasticsearchRepository<Employee,String> {
    List<Employee> findEmployeesByAge(int age);  
    List<Employee> findEmployeesByName(String name);
//    @Query("{" +
//            "    \"bool\": {" +
//            "      \"must\": [" +
//            "        {" +
//            "          \"nested\": {" +
//            "            \"path\": \"skills\", " +
//            "            \"query\": {" +
//            "              \"bool\": {" +
//            "                \"must\": [ " +
//            "                  {" +
//            "                    \"match\": {" +
//            "                      \"skills.name\": \"?\"" +
//            "                    }" +
//            "                  }" +
//            "                ]" +
//            "              }" +
//            "            }" +
//            "          }" +
//            "        }" +
//            "      ]" +
//            "}}")
    @Query("{" +
            "    \"bool\": {" +
            "      \"must\": [" +
            "        {" +
            "          \"nested\": {" +
            "            \"path\": \"skills\"," +
            "            \"score_mode\" : \"avg\","+
            "            \"query\": {" +
            "              \"bool\": {" +
            "                \"should\": [" +
            "                  {" +
            "                    \"match\": {" +
            "                      \"skills.name\": \"?\"" +
            "                    }" +
            "                  }" +
            "                ]" +
            "              }" +
            "            }" +
            "          }" +
            "        }" +
            "      ]" +
            "    }" +
            "}")
    List<Employee> findImpiegatiBySkillsIn(List<String> skills);
}
