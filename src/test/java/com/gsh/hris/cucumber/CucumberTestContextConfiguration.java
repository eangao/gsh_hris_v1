package com.gsh.hris.cucumber;

import com.gsh.hris.HrisApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = HrisApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
