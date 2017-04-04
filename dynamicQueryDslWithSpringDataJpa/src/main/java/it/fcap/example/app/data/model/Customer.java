package it.fcap.example.app.data.model;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

/**
 * Created by F.C. on 04/04/2017.
 */
@Entity
@Table(name = "Customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = -1094412668809451436L;

	private Long id;
	private String firstName;
	private String lastName;

	public Customer() {
	}

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Id
	@GeneratedValue(strategy = AUTO, generator = "idCustomerGenerator")
	@SequenceGenerator(name = "idCustomerGenerator", sequenceName = "S_CUSTOMER")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setFirstName(String fn){
		firstName = fn;
	}

	public void setLastName(String ln) {
		lastName = ln;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Customer{");
		sb.append("id=").append(id);
		sb.append(", firstName='").append(firstName).append('\'');
		sb.append(", lastName='").append(lastName).append('\'');
		sb.append('}');
		return sb.toString();
	}
}