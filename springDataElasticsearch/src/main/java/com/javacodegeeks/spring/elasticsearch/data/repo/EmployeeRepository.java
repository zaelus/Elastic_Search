package com.javacodegeeks.spring.elasticsearch.data.repo;

import java.util.List;

import com.javacodegeeks.spring.elasticsearch.data.model.Employee;
import com.javacodegeeks.spring.elasticsearch.data.model.Skill;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EmployeeRepository extends ElasticsearchRepository<Employee,String> {
    List<Employee> findEmployeesByAge(int age);  
    List<Employee> findEmployeesByName(String name);

    @Deprecated
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

    @Query("{\"bool\":{\"must\":[{\"nested\":{\"path\":\"skills\",\"query\":{\"bool\":{\"must\":[{\"match\":{\"skills.name\":\"?0\"}}]}}}}]}}")
    List<Employee> findBySkills_NameIn(List<String> skills);

    @Deprecated
    List<Employee> findAllBySkills_Name(String skillName);

    @Deprecated
    @Query("{" +
            "    \"bool\": {" +
            "      \"must\": [" +
            "        {" +
            "          \"nested\": {" +
            "            \"path\": \"skills\"," +
            "            \"query\": {" +
            "              \"bool\": {" +
            "                \"should\": [" +
            "                  {" +
            "                    \"match\": {" +
            "                      \"skills\": \"?0\"" +
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
    List<Employee> findBySkills(Skill skill);
}
