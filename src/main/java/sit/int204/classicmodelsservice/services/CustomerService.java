package sit.int204.classicmodelsservice.services;

import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.classicmodelsservice.dtos.NewCustomerDto;
import sit.int204.classicmodelsservice.entities.Customer;
import sit.int204.classicmodelsservice.entities.Employee;
import sit.int204.classicmodelsservice.entities.Order;
import sit.int204.classicmodelsservice.exceptions.ItemNotFoundException;
import sit.int204.classicmodelsservice.repositories.CustomerRepository;
import sit.int204.classicmodelsservice.repositories.EmployeeRepository;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    public Customer findByID(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer number '" + id + "' does not exist !!!!"));
//        return repository.findById(id).orElseThrow(
//                ()->new ItemNotFoundException("Customer number '"+ id + "' does not exist !!!!"));
    }

    public Page<Customer> getCustomers(int page, int size) {
        String x = null;
        x.toLowerCase();
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Customer> getCustomers() {
        return repository.findAll();
    }

    @Transactional
    public NewCustomerDto createCustomer(NewCustomerDto newCustomer) {
        if (repository.existsById(newCustomer.getCustomerNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate customer for id "
                    + newCustomer.getCustomerNumber());
        }
        Customer customer = mapper.map(newCustomer, Customer.class);
//        Employee e = employeeRepository.findById(newCustomer.getSales().getEmployeeNumber()).orElseThrow(
//                ()->new ItemNotFoundException("Employee number "+ newCustomer.getSales().getEmployeeNumber()+
//                        " does not exist !!!")
//        );
//        customer.setSales(e);
        System.out.println(customer);
        return mapper.map(repository.save(customer), NewCustomerDto.class);
    }

    public List<NewCustomerDto> getAllCustomers() {
        Customer c = new Customer();
        c.setCountry("U");
        c.setCity("pa");
        //import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("country", ignoreCase().contains())
                .withMatcher("city", ignoreCase().contains());
        List<Customer> customers = repository.findAll(Example.of(c, matcher));
        return listMapper.mapList(customers, NewCustomerDto.class, mapper);
        //return listMapper.mapList(repository.findAll(), NewCustomerDto.class, mapper);
    }
}
