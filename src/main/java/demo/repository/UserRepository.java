package demo.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import demo.model.User;

public interface UserRepository extends DataTablesRepository<User, Integer> {

}
