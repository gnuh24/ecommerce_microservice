package com.ec.user.repository;


import com.ec.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String>  {
}
