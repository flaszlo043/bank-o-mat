package org.muge.bankomat.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.muge.bankomat.account.Account;
import org.muge.bankomat.account.AccountRepository;
import org.muge.bankomat.company.Company;
import org.muge.bankomat.company.CompanyRepository;
import org.muge.bankomat.companyaffiliation.CompanyAffiliation;
import org.muge.bankomat.companyaffiliation.CompanyAffiliationRepository;
import org.muge.bankomat.person.Person;
import org.muge.bankomat.person.PersonRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExportController {

    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final CompanyAffiliationRepository companyAffiliationRepository;
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    public ExportController(AccountRepository accountRepository,
                            CompanyRepository companyRepository,
                            CompanyAffiliationRepository companyAffiliationRepository,
                            PersonRepository personRepository,
                            ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.companyRepository = companyRepository;
        this.companyAffiliationRepository = companyAffiliationRepository;
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/export")
    public Map<String, Object> exportAllData() {
        Map<String, Object> data = new HashMap<>();
        data.put("accounts", accountRepository.findAll());
        data.put("companies", companyRepository.findAll());
        data.put("companyAffiliations", companyAffiliationRepository.findAll());
        data.put("persons", personRepository.findAll());
        return data;
    }

    @PostMapping("/import")
    public void importAllData(@RequestBody Map<String, Object> data, @RequestParam(name = "delete", required = false, defaultValue = "false") boolean delete) {
        if (delete) {
            deleteAllData();
        }

        List<Account> accounts = objectMapper.convertValue(data.get("accounts"), objectMapper.getTypeFactory().constructCollectionType(List.class, Account.class));
        accountRepository.saveAll(accounts);

        List<Company> companies = objectMapper.convertValue(data.get("companies"), objectMapper.getTypeFactory().constructCollectionType(List.class, Company.class));
        companyRepository.saveAll(companies);

        List<CompanyAffiliation> companyAffiliations = objectMapper.convertValue(data.get("companyAffiliations"), objectMapper.getTypeFactory().constructCollectionType(List.class, CompanyAffiliation.class));
        companyAffiliationRepository.saveAll(companyAffiliations);

        List<Person> persons = objectMapper.convertValue(data.get("persons"), objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
        personRepository.saveAll(persons);
    }

    @PostMapping("/delete-all")
    public void deleteAllData() {
        accountRepository.deleteAll();
        companyAffiliationRepository.deleteAll();
        companyRepository.deleteAll();
        personRepository.deleteAll();
    }

    @GetMapping("/export/accounts/csv")
    public ResponseEntity<String> exportAccountsToCsv() {
        List<Account> accounts = accountRepository.findAll();
        String csv = CsvUtils.toCsv(accounts);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"accounts.csv\"")
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @GetMapping("/export/companies/csv")
    public ResponseEntity<String> exportCompaniesToCsv() {
        List<Company> companies = companyRepository.findAll();
        String csv = CsvUtils.toCsv(companies);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"companies.csv\"")
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @GetMapping("/export/company-affiliations/csv")
    public ResponseEntity<String> exportCompanyAffiliationsToCsv() {
        List<CompanyAffiliation> affiliations = companyAffiliationRepository.findAll();
        String csv = CsvUtils.toCsv(affiliations);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"company-affiliations.csv\"")
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @GetMapping("/export/persons/csv")
    public ResponseEntity<String> exportPersonsToCsv() {
        List<Person> persons = personRepository.findAll();
        String csv = CsvUtils.toCsv(persons);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"persons.csv\"")
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(csv);
    }

/*
    @PostMapping("/import/accounts/csv")
    public void importAccountsFromCsv(@RequestBody String csv, @RequestParam(name = "delete", required = false, defaultValue = "false") boolean delete) throws IOException {
        if (delete) {
            accountRepository.deleteAll();
        }
        List<Account> accounts = CsvUtils.fromCsvAccounts(csv, personRepository);
        accountRepository.saveAll(accounts);
    }
*/

    @PostMapping("/import/companies/csv")
    public void importCompaniesFromCsv(@RequestBody String csv, @RequestParam(name = "delete", required = false, defaultValue = "false") boolean delete) throws IOException {
        if (delete) {
            companyRepository.deleteAll();
        }
        List<Company> companies = CsvUtils.fromCsvCompanies(csv);
        companyRepository.saveAll(companies);
    }

    @PostMapping("/import/company-affiliations/csv")
    public void importCompanyAffiliationsFromCsv(@RequestBody String csv, @RequestParam(name = "delete", required = false, defaultValue = "false") boolean delete) throws IOException {
        if (delete) {
            companyAffiliationRepository.deleteAll();
        }
        List<CompanyAffiliation> affiliations = CsvUtils.fromCsvCompanyAffiliations(csv, personRepository, companyRepository);
        companyAffiliationRepository.saveAll(affiliations);
    }

    @PostMapping("/import/persons/csv")
    public void importPersonsFromCsv(@RequestBody String csv, @RequestParam(name = "delete", required = false, defaultValue = "false") boolean delete) throws IOException {
        if (delete) {
            personRepository.deleteAll();
        }
        List<Person> persons = CsvUtils.fromCsvPersons(csv);
        personRepository.saveAll(persons);
    }
}