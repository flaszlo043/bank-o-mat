package org.muge.bankomat.account;

import org.muge.bankomat.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/company/{companyId}/accounts")
    public String getAccountsByCompany(@PathVariable Long companyId, Model model) {
        model.addAttribute("accounts", accountRepository.findByCompanyId(companyId));
        companyRepository.findById(companyId).ifPresent(company -> model.addAttribute("company", company));
        return "account/account-list";
    }
}
