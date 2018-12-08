package com.shadow649.hardwareshop.query.service;

import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.OrderNotFoundException;
import com.shadow649.hardwareshop.query.reposiroty.SpringOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Emanuele Lombardi
 */
@Service
public class BaseOrderEntryService implements OrderEntryService {

    private final SpringOrderRepository springOrderRepository;

    @Autowired
    public BaseOrderEntryService(SpringOrderRepository repository) {
        this.springOrderRepository = repository;
    }

    @Override
    public List<OrderEntry> listAllOrders() {
        return springOrderRepository.findAll();
    }

    @Override
    public OrderEntry getById(String orderID) {
        return springOrderRepository.findById(orderID).orElseThrow(OrderNotFoundException ::new);
    }

    @Override
    public OrderEntry save(OrderEntry orderEntry) {
        return springOrderRepository.save(orderEntry);
    }

    @Override
    public List<OrderEntry> filterByConfirmDate(Date confirmDateStart, Date confirmDateEnd) {
        return springOrderRepository.findAllByConfirmedDateBetween(confirmDateStart, confirmDateEnd);
    }
}
