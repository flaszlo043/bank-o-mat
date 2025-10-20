package org.muge.bankomat.export;

import org.muge.bankomat.account.Account;
import org.muge.bankomat.company.Company;
import org.muge.bankomat.companyaffiliation.CompanyAffiliation;
import org.muge.bankomat.person.Person;
import org.muge.bankomat.person.PersonRepository;
import org.muge.bankomat.company.CompanyRepository;
import org.muge.bankomat.companyaffiliation.RelationType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtils {

    public static String toCsv(List<?> objects) {
        if (objects.isEmpty()) {
            return "";
        }
        Object first = objects.get(0);
        if (first instanceof Account) {
            return toCsvAccounts((List<Account>) objects);
        } else if (first instanceof Company) {
            return toCsvCompanies((List<Company>) objects);
        } else if (first instanceof CompanyAffiliation) {
            return toCsvCompanyAffiliations((List<CompanyAffiliation>) objects);
        } else if (first instanceof Person) {
            return toCsvPersons((List<Person>) objects);
        }
        return "";
    }

    private static String toCsvAccounts(List<Account> accounts) {
        StringBuilder csv = new StringBuilder();
        csv.append("id,name,type,number,availableBalance,currentBalance,company_id\n");
        for (Account account : accounts) {
            csv.append(account.getId()).append(",")
               .append(account.getName()).append(",")
               .append(account.getType()).append(",")
               .append(account.getNumber()).append(",")
               .append(account.getAvailableBalance()).append(",")
               .append(account.getCurrentBalance()).append(",")
               .append(account.getCompany() != null ? account.getCompany().getId() : "").append("\n");
        }
        return csv.toString();
    }

    private static String toCsvCompanies(List<Company> companies) {
        StringBuilder csv = new StringBuilder();
        csv.append("id,name\n");
        for (Company company : companies) {
            csv.append(company.getId()).append(",")
               .append(company.getName()).append("\n");
        }
        return csv.toString();
    }

    private static String toCsvCompanyAffiliations(List<CompanyAffiliation> affiliations) {
        StringBuilder csv = new StringBuilder();
        csv.append("id,person_id,company_id,relationType\n");
        for (CompanyAffiliation affiliation : affiliations) {
            csv.append(affiliation.getId()).append(",")
               .append(affiliation.getPerson() != null ? affiliation.getPerson().getId() : "").append(",")
               .append(affiliation.getCompany() != null ? affiliation.getCompany().getId() : "").append(",")
               .append(affiliation.getRelationType()).append("\n");
        }
        return csv.toString();
    }

    private static String toCsvPersons(List<Person> persons) {
        StringBuilder csv = new StringBuilder();
        csv.append("id,firstName,lastName,middleName\n");
        for (Person person : persons) {
            csv.append(person.getId()).append(",")
               .append(person.getFirstName()).append(",")
               .append(person.getLastName()).append(",")
               .append(person.getMiddleName()).append("\n");
        }
        return csv.toString();
    }

    public static List<Account> fromCsvAccounts(String csv, CompanyRepository companyRepository) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(csv))) {
            return reader.lines().skip(1).map(line -> {
                String[] fields = line.split(",");
                Account account = new Account();
                account.setId(Long.parseLong(fields[0]));
                account.setName(fields[1]);
                account.setType(fields[2]);
                account.setNumber(fields[3]);
                account.setAvailableBalance(Double.parseDouble(fields[4]));
                account.setCurrentBalance(Double.parseDouble(fields[5]));
                if (fields.length > 6 && !fields[6].isEmpty()) {
                    companyRepository.findById(Long.parseLong(fields[6])).ifPresent(account::setCompany);
                }
                return account;
            }).collect(Collectors.toList());
        }
    }

    public static List<Company> fromCsvCompanies(String csv) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(csv))) {
            return reader.lines().skip(1).map(line -> {
                String[] fields = line.split(",");
                Company company = new Company();
                company.setId(Long.parseLong(fields[0]));
                company.setName(fields[1]);
                return company;
            }).collect(Collectors.toList());
        }
    }

    public static List<CompanyAffiliation> fromCsvCompanyAffiliations(String csv, PersonRepository personRepository, CompanyRepository companyRepository) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(csv))) {
            return reader.lines().skip(1).map(line -> {
                String[] fields = line.split(",");
                CompanyAffiliation affiliation = new CompanyAffiliation();
                affiliation.setId(Long.parseLong(fields[0]));
                if (fields.length > 1 && !fields[1].isEmpty()) {
                    personRepository.findById(Long.parseLong(fields[1])).ifPresent(affiliation::setPerson);
                }
                if (fields.length > 2 && !fields[2].isEmpty()) {
                    companyRepository.findById(Long.parseLong(fields[2])).ifPresent(affiliation::setCompany);
                }
                if (fields.length > 3 && !fields[3].isEmpty()) {
                    affiliation.setRelationType(RelationType.valueOf(fields[3]));
                }
                return affiliation;
            }).collect(Collectors.toList());
        }
    }

    public static List<Person> fromCsvPersons(String csv) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(csv))) {
            return reader.lines().skip(1).map(line -> {
                String[] fields = line.split(",");
                Person person = new Person();
                person.setId(Long.parseLong(fields[0]));
                person.setFirstName(fields[1]);
                person.setLastName(fields[2]);
                person.setMiddleName(fields[3]);
                return person;
            }).collect(Collectors.toList());
        }
    }
}
