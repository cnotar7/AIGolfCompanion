package com.cnotar7.projects.aigolfcompanion.service.api;

import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourse;
import com.cnotar7.projects.aigolfcompanion.dto.external.ExternalCourseSearchResponse;
import com.cnotar7.projects.aigolfcompanion.model.Course;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
        String url = this.baseUrl + "?search_query=" + query;
        ExternalCourseSearchResponse response = this.restTemplate.getForObject(url, ExternalCourseSearchResponse.class);
        return response != null ? response.getCourses() : new ArrayList<>();
    }
}
