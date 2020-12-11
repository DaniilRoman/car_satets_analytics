package com.app.admin.controller;

import com.app.admin.api.CarApi;
import com.app.admin.api.model.AccountResponse;
import com.app.admin.api.model.CarListRequest;
import com.app.admin.api.model.CarParamResponse;
import com.app.admin.api.model.CarResponse;
import com.app.admin.data.Car;
import com.app.admin.data.CarParameter;
import com.app.admin.data.Parameter;
import com.app.admin.data.user.Account;
import com.app.admin.service.CarService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CarController implements CarApi {
    private final CarService carService;

    @Override
    public ResponseEntity<List<CarResponse>> getCars() {
        return ResponseEntity.ok().body(carService.findAll().stream()
                .map(car -> new CarResponse()
                        .id(car.getId())
                        .brand(car.getBrand())
                        .model(car.getModel())
                        .owner(composeAccountResponse(car.getOwner()))
                        .parameters(composeCarParamResponse(car)))
                .collect(Collectors.toList()));

    }

    @Override
    public ResponseEntity<List<CarResponse>> addCars(CarListRequest carListRequest) {
        try {
            List<CarResponse> carListResponse = carService.addCars(carListRequest).stream()
                    .map(car -> new CarResponse()
                            .id(car.getId())
                            .owner(composeAccountResponse(car.getOwner()))
                            .model(car.getModel())
                            .brand(car.getBrand()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(carListResponse);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteCarById(UUID carId) {
        carService.deleteCarById(carId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CarResponse> getCarById(UUID carId) {
        try {
            Car car = carService.getCarById(carId);
            return ResponseEntity.ok().body(new CarResponse()
                    .id(car.getId())
                    .brand(car.getBrand())
                    .model(car.getModel())
                    .owner(composeAccountResponse(car.getOwner()))
                    .parameters(composeCarParamResponse(car)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<CarResponse>> getCarsByOwnerId(UUID accountId) {
        return ResponseEntity.ok().body(carService.findCarsByOwnerId(accountId).stream()
                .map(car -> new CarResponse()
                        .id(car.getId())
                        .brand(car.getBrand())
                        .model(car.getModel())
                        .owner(composeAccountResponse(car.getOwner()))
                        .parameters(composeCarParamResponse(car)))
                .collect(Collectors.toList()));
    }

    private AccountResponse composeAccountResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setUsername(account.getUsername());
        return accountResponse;
    }

    private List<CarParamResponse> composeCarParamResponse(Car car) {
        List<CarParamResponse> resultSet = new ArrayList<>();
        for (CarParameter parameter: car.getParameters()) {
            Parameter parameterInfo = parameter.getParameter();
            resultSet.add(new CarParamResponse()
                    .paramName(parameterInfo.getName())
                    .paramId(parameterInfo.getId())
                    .paramType(parameterInfo.getType())
                    .value(parameter.getVal()));
        }
        return resultSet;
    }
}
