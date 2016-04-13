package demo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "USER_")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

	private static final long serialVersionUID = 4215364519105796087L;

	@Id
	@GeneratedValue
	@Getter
	@Setter
	@JsonView(DataTablesOutput.View.class)
	private Integer id;

	@Getter
	@Setter
	@JsonView(DataTablesOutput.View.class)
	private String name;

	/**
	 * This field will not be present in the response
	 */
	@Getter
	@Setter
	private String hiddenField;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	@JsonView(DataTablesOutput.View.class)
	private UserRole role;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	@JsonView(DataTablesOutput.View.class)
	private UserStatus status;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "id_address")
	@JsonView(DataTablesOutput.View.class)
	private Address address;

	@Getter
	@Setter
	@JsonView(DataTablesOutput.View.class)
	private Integer likes;

	public enum UserRole {
		ADMIN, AUTHOR, USER
	}

	public enum UserStatus {
		ACTIVE, BLOCKED
	}

}
