package org.muge.bankomat.account;

import org.muge.bankomat.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/company/{companyId}/add-account")
    public String showAddAccountForm(@PathVariable Long companyId, Model model) {
        companyRepository.findById(companyId).ifPresent(company -> {
            model.addAttribute("company", company);
            Account account = new Account();
            account.setCompany(company);
            model.addAttribute("account", account);
        });
        return "account/add-account";
    }

    @PostMapping("/company/{companyId}/add-account")
    public String addAccount(@PathVariable Long companyId, @ModelAttribute Account account) {
        companyRepository.findById(companyId).ifPresent(account::setCompany);
        accountRepository.save(account);
        return "redirect:/company/" + companyId + "/accounts";
    }
}
