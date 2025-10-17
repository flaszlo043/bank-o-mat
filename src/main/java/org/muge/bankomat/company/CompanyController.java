package org.muge.bankomat.company;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/company-list")
    public String getCompanyList(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "company/company-list";
    }

    @GetMapping("/add-company")
    public String addCompanyForm(Model model) {
        var company = new Company();
        companyRepository.save(company);
        model.addAttribute("company", company);
        return "company/add-company";
    }

    @PostMapping("/add-company")
    public String addCompanySubmit(@ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/company-list";
    }

    @GetMapping("/edit-company/{id}")
    public String editCompany(@PathVariable Long id, Model model) {
        companyRepository.findById(id).ifPresent(company -> model.addAttribute("company", company));
        return "company/edit-company";
    }

    @PostMapping("/edit-company/{id}")
    public String updateCompany(@PathVariable Long id, @ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/company-list";
    }
}
