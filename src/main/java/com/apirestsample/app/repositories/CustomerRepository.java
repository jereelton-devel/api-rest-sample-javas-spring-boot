package com.apirestsample.app.repositories;

import com.apirestsample.app.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    @Query(value = "SELECT * FROM customers cs WHERE cs.name = ?1", nativeQuery = true)
    CustomerEntity findByUsername(String name);
}
