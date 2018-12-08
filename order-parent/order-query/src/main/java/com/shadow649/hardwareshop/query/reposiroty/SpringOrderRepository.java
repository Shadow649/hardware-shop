package com.shadow649.hardwareshop.query.reposiroty;

import com.shadow649.hardwareshop.query.domain.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Emanuele Lombardi
 */
public interface SpringOrderRepository extends JpaRepository<OrderEntry, String> {
    List<OrderEntry> findAllByConfirmedDateBetween(Date confirmedDateStart, Date confirmedDateEnd);
}
