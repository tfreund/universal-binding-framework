

import java.util.Date;


public class Person {
	private String name;
	private String firstname;
	private boolean natPerson;
	private Date birthday;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public boolean isNatPerson() {
		return natPerson;
	}

	public void setNatPerson(boolean natPerson) {
		this.natPerson = natPerson;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}