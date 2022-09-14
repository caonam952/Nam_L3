package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.domain.Ward;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.Feedback;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.WardRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ImportExportExcelUtil;
import com.globits.da.utils.NotifyMessage;
import com.globits.da.validate.ValidateEmployee;
import com.globits.da.validate.ValidateGlobal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, UUID> implements EmployeeService {

    @Autowired
    ModelMapper modelMapper;

    final private EmployeeRepository employeeRepository;

    final private ProvinceRepository provinceRepository;

    final private DistrictRepository districtRepository;

    final private WardRepository wardRepository;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository, ProvinceRepository theProvinceRepository, DistrictRepository theDistrictRepository, WardRepository theWardRepository) {
        employeeRepository = theEmployeeRepository;
        provinceRepository = theProvinceRepository;
        districtRepository = theDistrictRepository;
        wardRepository = theWardRepository;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Employee findById(UUID uuid) {
        Optional<Employee> result = employeeRepository.findById(uuid);

        if (!result.isPresent()) {
            throw new RuntimeException("Did not find employee id - " + uuid);
        }
        return result.get();

    }

    @Override
    public Feedback<EmployeeDto> saveEmployee(EmployeeDto theEmployee) {
        NotifyMessage success = NotifyMessage.SUCCESS;
        NotifyMessage notifyMessage = ValidateEmployee.validateEmployee(theEmployee);
        if (notifyMessage.equals(success)) {
            if (!employeeRepository.existsEmployeeByCode(theEmployee.getCode())) {
                Employee employee = new Employee();
                setEntity(employee, theEmployee);
                return new Feedback<>(
                        ValidateGlobal.resultStatusCode(notifyMessage),
                        notifyMessage.getMessage(),
                        modelMapper.map(employeeRepository.save(employee), EmployeeDto.class));
            }
            return new Feedback<>(
                    NotifyMessage.CODE_IS_EXIST.getCode(),
                    notifyMessage.getMessage(),
                    null);
        }
        return new Feedback<>(ValidateGlobal.resultStatusCode(notifyMessage), notifyMessage.getMessage(), null);
    }

    @Override
    public Feedback<EmployeeDto> update(EmployeeDto theEmployee, UUID uuid) {
        if (employeeRepository.existsEmployeeById(uuid)) {
            NotifyMessage success = NotifyMessage.SUCCESS;
            NotifyMessage notifyMessage = ValidateEmployee.validateEmployee(theEmployee);
            if (notifyMessage.equals(success)) {
                Employee employee = employeeRepository.getOne(uuid);
                setEntity(employee, theEmployee);
                return new Feedback<>(
                        ValidateGlobal.resultStatusCode(notifyMessage),
                        notifyMessage.getMessage(),
                        modelMapper.map(employeeRepository.save(employee), EmployeeDto.class));
            }
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    theEmployee);
        }
        return new Feedback<>(
                NotifyMessage.ID_IS_EXIST.getCode(),
                NotifyMessage.ID_IS_EXIST.getMessage(),
                theEmployee);
    }

    @Override
    public void deleteById(UUID uuid) {
        employeeRepository.deleteById(uuid);
    }

    @Override
    public List<String> importExcel(MultipartFile multipartFile) throws IOException {
        return ImportExportExcelUtil.importFileExcel(multipartFile);
    }

    @Override
    public List<EmployeeDto> exportExcel() throws IOException {
        return findAll();
    }

    public void setEntity(Employee employee, EmployeeDto employeeDto) {
        employee.setCode(employeeDto.getCode());
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        employee.setAge(employeeDto.getAge());
        employee.setProvince(modelMapper.map(employeeDto.getProvinceDto(), Province.class));
        employee.setDistrict(modelMapper.map(employeeDto.getDistrictDto(), District.class));
        employee.setWard(modelMapper.map(employeeDto.getWardDto(), Ward.class));
    }

}
