package com.shadow649.hardwareshop.service;

import com.shadow649.hardwareshop.domain.OrderRow;

import java.util.List;
import java.util.Set;

/**
 * Service to be used to validate and retrieve the order's products against the products catalog
 *
 * @author Emanuele Lombardi
 */
public interface ProductCatalogueService {

    /**
     * Checks if all the products exists in the catalog
     * @param productsId the product ID
     * @return true if ALL the products exists in the catalog
     */
    boolean productsExist(Set<String> productsId);

    /**
     * Extract the {@link OrderRow} from a productId
     * @param productIDs the IDs set
     * @return The OrderRows that belong to the IDs set
     */
    List<OrderRow> getOrderRows(Set<String> productIDs);

}
