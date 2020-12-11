package com.app.admin.service;

import com.app.admin.api.model.ParamRequest;
import com.app.admin.data.Parameter;
import com.app.admin.repository.ParameterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParameterService {

    private final ParameterRepository parameterRepository;

    public List<Parameter> findAll() {
        return parameterRepository.findAll();
    }

    public Parameter addNewParameter(ParamRequest paramRequest) {
        Parameter newParameter = new Parameter();
        newParameter.setType(paramRequest.getType());
        newParameter.setName(paramRequest.getName());
        newParameter = parameterRepository.saveAndFlush(newParameter);
        return newParameter;
    }

    public void deleteParameterById(UUID id) {
        parameterRepository.deleteById(id);
    }
}
