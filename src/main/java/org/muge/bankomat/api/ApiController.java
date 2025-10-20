package org.muge.bankomat.api;

import org.muge.bankomat.company.Company;
import org.muge.bankomat.company.CompanyRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Controller
public class ApiController {

    private final CompanyRepository companyRepository;
    private final TemplateEngine templateEngine;

    public ApiController(CompanyRepository companyRepository, TemplateEngine templateEngine) {
        this.companyRepository = companyRepository;
        this.templateEngine = templateEngine;
    }

    @GetMapping(value = "/api/companies/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getCompaniesAsXml(Model model) {
        List<Company> companies = companyRepository.findAll();
        Context context = new Context();
        context.setVariable("companies", companies);
        String xml = templateEngine.process("api/companies", context);
        return ResponseEntity.ok(xml);
    }
}
