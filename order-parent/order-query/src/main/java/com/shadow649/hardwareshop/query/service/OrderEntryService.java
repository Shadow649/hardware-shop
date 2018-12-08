package com.shadow649.hardwareshop.query.service;

import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.OrderNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Service used to manage {@link OrderEntry} projection
 * @author Emanuele Lombardi
 */
public interface OrderEntryService {

    /**
     *
     * @return all the persisted entries
     */
    List<OrderEntry> listAllOrders();

    /**
     *
     * @param orderID The Order id
     * @return the entry projection
     * @throws OrderNotFoundException
     */
    OrderEntry getById(String orderID);

    /**
     * Save and return the entry
     * @param orderEntry to be persisted
     * @return the persisted entry
     */
    OrderEntry save(OrderEntry orderEntry);

    /**
     *
     * @param confirmDateStart the start confirmation date
     * @param confirmDateEnd the end confirmation date
     * @return all the entries that matches the date filter
     */
    List<OrderEntry> filterByConfirmDate(Date confirmDateStart, Date confirmDateEnd);
}
