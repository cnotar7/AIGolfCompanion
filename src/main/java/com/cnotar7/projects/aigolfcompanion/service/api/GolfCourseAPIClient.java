package com.cnotar7.projects.aigolfcompanion.service.api;

import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourseResponse;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourseSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GolfCourseAPIClient {

    @Value("${golfcourseapi.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<ExternalCourse> searchGolfCourse(String query) {
        String url = baseUrl + "search?search_query=" + query;
        System.out.println("About to call api: " + url);
        ExternalCourseSearchResponse response = restTemplate.getForObject(url, ExternalCourseSearchResponse.class);
        System.out.println("API returned: " + response);
        return response != null ? response.getCourses() : new ArrayList<>();
    }

    public ExternalCourse getGolfCourseById(Long id) {
        String url = baseUrl + "courses/" + id;
        ExternalCourseResponse response = restTemplate.getForObject(url, ExternalCourseResponse.class);
        System.out.println("Golf Course get by ID: " + id + " returned: " + response);
        return response != null ? response.getCourse() : null;
    }
}
