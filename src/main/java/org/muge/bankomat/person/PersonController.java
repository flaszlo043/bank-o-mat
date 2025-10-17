package org.muge.bankomat.person;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/person-list")
    public String getPersonList(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "person/person-list";
    }

    @GetMapping("/add-person")
    public String addPersonForm(Model model) {
        model.addAttribute("person", new Person());
        return "person/add-person";
    }

    @PostMapping("/add-person")
    public String addPersonSubmit(@ModelAttribute Person person) {
        personRepository.save(person);
        return "redirect:/person-list";
    }

    @GetMapping("/edit-person/{id}")
    public String editPersonForm(@PathVariable Long id, Model model) {
        Person person = personRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + id));
        model.addAttribute("person", person);
        return "person/add-person";
    }
}
