package org.muge.bankomat.companyaffiliation;

import java.util.Optional;

import org.muge.bankomat.account.Account;
import org.muge.bankomat.account.AccountRepository;
import org.muge.bankomat.company.CompanyRepository;
import org.muge.bankomat.person.Person;
import org.muge.bankomat.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompanyAffiliationController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyAffiliationRepository companyAffiliationRepository;

    @GetMapping("/person/{personId}/companyAffiliations")
    public String getCompanyAffiliationByPerson(@PathVariable Long personId, Model model) {
        System.out.println("\n\npersonId: " + personId + "\n\n");
        Optional<Person> person = personRepository.findById(personId);
        person
            .ifPresent( prs -> {
                System.out.println("\n\nperson.id: " + prs.getId()+ "\n\n");
                model.addAttribute("person", prs);
                var companyAffiliations = companyAffiliationRepository.findByPersonId(personId);
                model.addAttribute("companyAffiliations", companyAffiliations);
        } );
        return "company-affiliation/company-affiliation-list";
    }

    @GetMapping("/person/{personId}/companyAffiliations/new")
    public String showAddCompanyAffiliationByPersonForm(@PathVariable Long personId, Model model) {

        personRepository.findById(personId).ifPresent(person -> {
            model.addAttribute("person", person);
            var affiliation = new CompanyAffiliation();
            affiliation.setPerson(person);
            model.addAttribute("affiliation", affiliation);
            model.addAttribute("companies", companyRepository.findAll());
            model.addAttribute("relationTypes", RelationType.values());
        });
        return "company-affiliation/add-company-affiliation";
    }

    @PostMapping("/person/{personId}/companyAffiliations/new")
    public String addCompanyAffiliationToPerson(@PathVariable Long personId, @ModelAttribute CompanyAffiliation companyAffiliation) {
        companyAffiliationRepository.save(companyAffiliation);
        return "redirect:/person/" + personId + "/companyAffiliations";
    }
}
