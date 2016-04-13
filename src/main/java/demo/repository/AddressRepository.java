package demo.repository;

import org.springframework.data.repository.CrudRepository;

import demo.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {

}
