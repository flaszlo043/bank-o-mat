package org.muge.bankomat.company;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/company-list")
    public String getCompanyList(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "company-list";
    }

    @GetMapping("/add-company")
    public String addCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "add-company";
    }

    @PostMapping("/add-company")
    public String addCompanySubmit(@ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/company-list";
    }
}
