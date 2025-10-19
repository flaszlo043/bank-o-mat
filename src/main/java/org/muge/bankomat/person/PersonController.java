package org.muge.bankomat.person;

import java.util.Optional;

import org.muge.bankomat.company.CompanyRepository;
import org.muge.bankomat.companyaffiliation.CompanyAffiliation;
import org.muge.bankomat.companyaffiliation.CompanyAffiliationRepository;
import org.muge.bankomat.companyaffiliation.RelationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyAffiliationRepository companyAffiliationRepository;

    @GetMapping("/person-list")
    public String getPersonList(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "person/person-list";
    }

    @GetMapping("/person/{id}")
    public String getPerson(@PathVariable Long id, Model model) {
        personRepository.findById(id).ifPresent(person -> {
            model.addAttribute("person", person);
        });
        return "person/person";
    }


    @PostMapping("/person/{id}")
    public String updatePerson(@PathVariable Long id, @ModelAttribute Person person) {
        personRepository.save(person);
        return "redirect:/person-list";
    }


    @GetMapping("/person/new")
    public String addPersonForm(Model model) {
        model.addAttribute("person", new Person());
        return "person/add-person";
    }

    @PostMapping("/person/new")
    public String addPersonSubmit(@ModelAttribute Person person) {
        personRepository.save(person);
        return "redirect:/person-list";
    }


    @GetMapping("/person/{personId}/add-affiliation")
    public String addAffiliationForm(@PathVariable Long personId, Model model) {
        personRepository.findById(personId).ifPresent(person -> {
            model.addAttribute("person", person);
            model.addAttribute("companies", companyRepository.findAll());
            model.addAttribute("affiliation", new CompanyAffiliation());
            model.addAttribute("relationTypes", RelationType.values());
        });
        return "person/add-affiliation";
    }



}
