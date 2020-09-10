package demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import demo.model.Address;
import demo.model.User;
import demo.model.User.UserRole;
import demo.model.User.UserStatus;
import demo.repository.AddressRepository;
import demo.repository.UserRepository;
import demo.specification.CustomSpecifications;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserRestController {

	private static final Pattern likesSearchPattern = Pattern
			.compile("(\\d*):(\\d*)");

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(value = "/data/users", method = RequestMethod.GET)
	public DataTablesOutput<User> getUsers(@Valid DataTablesInput input) {
		for (Column parameter : input.getColumns()) {
			if (!parameter.getData().equalsIgnoreCase("likes"))
				continue;
			// create a specification based on the search value
			val additionalSpecification = getLikesSpecification(parameter
					.getSearch().getValue());
			// remove the search value
			parameter.getSearch().setValue("");
			return userRepository.findAll(input, additionalSpecification);
		}
		return userRepository.findAll(input);
	}

	@PostConstruct
	public void insertSampleData() {
		val addresses = new ArrayList<Address>();
		addresses.add(Address.builder().town("LA").build());
		addresses.add(Address.builder().town("Paris").build());
		addresses.add(Address.builder().town("NYC").build());
		addresses.add(Address.builder().town("Las Vegas").build());
		addresses.add(Address.builder().town("Miami").build());
		addressRepository.saveAll(addresses);

		for (int i = 0; i < 42; i++) {
			// @formatter:off
			userRepository.save(User.builder()
					.name("John" + i)
					.role(UserRole.values()[i % 3])
					.status(UserStatus.values()[i % 2])
					.hiddenField("private")
					.address(i % 7 == 0 ? null : addresses.get(i % 5))
					.likes((int) (Math.random() * 10000))
					.build());
			// @formatter:on
		}
	}

	private Specification<User> getLikesSpecification(String searchValue) {
		Matcher matcher = likesSearchPattern.matcher(searchValue);
		if (!matcher.matches())
			return null;
		try {
			String lowerBound = matcher.group(1);
			String upperBound = matcher.group(2);
			return CustomSpecifications.hasLikesBetween(
					StringUtils.hasText(lowerBound) ? Integer
							.valueOf(lowerBound) : null,
					StringUtils.hasText(upperBound) ? Integer
							.valueOf(upperBound) : null);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
