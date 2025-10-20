package org.muge.bankomat.companyaffiliation;

import java.util.Optional;

import org.muge.bankomat.company.Company;
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
        Optional<Person> person = personRepository.findById(personId);
        person.ifPresent(prs -> {
            companyAffiliation.setPerson(prs);
            companyAffiliationRepository.save(companyAffiliation);
        });

        return "redirect:/person/" + personId + "/companyAffiliations";
    }

    @GetMapping("/person/{personId}/companyAffiliations/{affiliationId}")
    public String getCompanyAffiliationByPersonAndAffilationId(
        @PathVariable Long personId, @PathVariable Long affiliationId, Model model) {
        
        System.out.println("\n\naffiliationId: " + affiliationId + "\n\n");
        Optional<CompanyAffiliation> affiliation = companyAffiliationRepository.findById(affiliationId);
        affiliation
            .ifPresent( aff -> {
                model.addAttribute("companyAffiliation", aff);
                System.out.println("\n\n======\n companyAffiliation:" + aff + "\n==========\n\n");
                Company company = aff.getCompany();
                System.out.println("\n\n======\n company:" + company + "\n==========\n\n");
                
                Optional<Company> oCompany = companyRepository.findById(2L);
                System.out.println("\n\n======\nc ompanyAffiliation:" + aff + "\n==========\n\n");
                oCompany.ifPresent( cpny -> {
                    System.out.println("\n\n======\n oCompany:" + cpny + "\n==========\n\n");
                    aff.setCompany(cpny);
                });
                System.out.println("\n\n======\nc ompanyAffiliation:" + aff + "\n==========\n\n");
                
                var companies = companyRepository.findAll();
                System.out.println("\n\n======\n companies:" + companies + "\n==========\n\n");
                model.addAttribute("companies", companies);
                model.addAttribute("relationTypes", RelationType.values());

        } );
        return "company-affiliation/edit-company-affiliation";
    }


    @PostMapping("/person/{personId}/companyAffiliations/{affiliationId}")
    public String saveCompanyAffiliationByPersonAndAffilationId(@PathVariable Long personId, @PathVariable Long affiliationId, @ModelAttribute CompanyAffiliation companyAffiliation) {
        Optional<Person> person = personRepository.findById(personId);
        person.ifPresent(prs -> {
            companyAffiliation.setPerson(prs);
            companyAffiliationRepository.save(companyAffiliation);
        });

        return "redirect:/person/" + personId + "/companyAffiliations";
    }

}
