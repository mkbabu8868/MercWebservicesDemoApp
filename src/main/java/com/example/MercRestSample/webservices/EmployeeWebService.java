package com.example.MercRestSample.webservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MercRestSample.exception.ResourceNotFoundException;
import com.example.MercRestSample.repository.EmployeeWebRepository;
import com.example.MercRestSample.webmodel.EmployeeWebModel;

@RestController
@RequestMapping("/api/v1")
public class EmployeeWebService {

	@Autowired
	private EmployeeWebRepository employeeRespository;

	@GetMapping("/employees")
	public List<EmployeeWebModel> getAllEmployee() {
		return employeeRespository.findAll();
	}

	@PostMapping("/employees")
	public EmployeeWebModel createEmployee(@Valid @RequestBody EmployeeWebModel employee) {
		return employeeRespository.save(employee);

	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<EmployeeWebModel> getEmployeeById(@PathVariable(value = "id") Long empId)
			throws ResourceNotFoundException {
		EmployeeWebModel emp = employeeRespository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		return ResponseEntity.ok().body(emp);

	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<EmployeeWebModel> updateEmployee(@PathVariable(value = "id") Long empId,
			@RequestBody EmployeeWebModel employee) {
		EmployeeWebModel emp = employeeRespository.findById(empId).get();

		emp.setFirstName(employee.getFirstName());
		emp.setLastname(employee.getLastname());
		emp.setEmailId(employee.getEmailId());
		final EmployeeWebModel updatedEmp = employeeRespository.save(emp);
		return ResponseEntity.ok(updatedEmp);
	}

	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long empId) throws ResourceNotFoundException {
		Map<String, Boolean> response = new HashMap<>();
		EmployeeWebModel emp = employeeRespository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		
		employeeRespository.delete(emp);
		response.put("Deleted", Boolean.TRUE);
		return response;

	}
}
