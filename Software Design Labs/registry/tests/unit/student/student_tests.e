note
	description: "Summary description for {STUDENT_TESTS}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	STUDENT_TESTS

inherit

	ES_TEST

create
	make

feature {NONE}

	make
		do
			add_boolean_case (agent t1)
			add_boolean_case (agent t2)
			add_boolean_case (agent t3)
			add_boolean_case (agent t4)
			add_boolean_case (agent t5)
			add_violation_case_with_tag ("err_invalid_date", agent t10)
			add_violation_case_with_tag ("err_id_taken", agent t11)
			add_violation_case_with_tag ("err_id_nonpositive", agent t12)
			add_violation_case_with_tag ("err_name_start", agent t13)
			add_violation_case_with_tag ("err_country_start", agent t14)
			add_violation_case_with_tag ("err_id_same", agent t15)
			add_violation_case_with_tag ("err_id_unused", agent t16)
			add_violation_case_with_tag ("err_dead", agent t17)
			add_violation_case_with_tag ("err_under_18", agent t18)
			add_violation_case_with_tag ("err_marry", agent t19)
			add_violation_case_with_tag ("err_divorce", agent t20)
			add_violation_case_with_tag ("err_dead_already", agent t21)
		end

feature
	reg_acc: REGISTRY_ACCESS
	d, m, y: INTEGER_64
	reg: REGISTRY
	once
		Result := reg_acc.m
	end

feature

	t1: BOOLEAN
		do
			comment ("t1: Check creation of a citizen")
			d := 04
			m := 08
			y := 1987
			reg.put (1, "Steve", [d, m, y])
			Result := reg.get_person (1).get_name ~ "Steve"
			check
				Result
			end
			Result := reg.get_person (1).get_dob ~ "1987-08-04"
			check
				Result
			end
			Result := reg.get_person (1).get_country ~ "Canada"
			check
				Result
			end
		end

	t2: BOOLEAN
		do
			comment ("t2: Check creation of an alien")
			d := 02
			m := 06
			y := 1989
			reg.put_alien (2, "Sally", [d, m, y], "USA")
			Result := reg.get_person (2).get_name ~ "Sally"
			check
				Result
			end
			Result := reg.get_person (2).get_dob ~ "1989-06-02"
			check
				Result
			end
			Result := reg.get_person (2).get_country ~ "USA"
			check
				Result
			end
		end

	t3: BOOLEAN
		do
			comment ("t3: Check marry")
			d := 10
			m := 12
			y := 2010
			reg.marry (1, 2, [d, m, y])
			Result := reg.get_person (1).get_spouse = reg.get_person (2)
			check
				Result
			end
			Result := reg.get_person (2).get_spouse = reg.get_person (1)
			check
				Result
			end
			Result := reg.get_person (1).get_married_since ~ "2010-12-10"
			check
				Result
			end
			Result := reg.get_person (2).get_married_since ~ "2010-12-10"
			check
				Result
			end
		end

	t4: BOOLEAN
		do
			comment ("t4: Check divorce")
			reg.divorce (1, 2)
			Result := reg.get_person (1).get_spouse = reg.get_person (1)
			check
				Result
			end
			Result := reg.get_person (2).get_spouse = reg.get_person (2)
			check
				Result
			end
			Result := not reg.get_person (1).is_married
			check
				Result
			end
			Result := not reg.get_person (2).is_married
			check
				Result
			end
		end

	t5: BOOLEAN
		do
			comment ("t5: Check die")
			d := 10
			m := 12
			y := 2010
			reg.marry (1, 2, [d, m, y])
			reg.die (1)
			Result := not reg.get_person (1).is_married
			check
				Result
			end
			Result := not reg.get_person (2).is_married
			check
				Result
			end
			Result := not reg.get_person (1).is_alive
			check
				Result
			end
			Result := reg.get_person (2).is_alive
			check
				Result
			end
		end

	t10
	do
		comment("t10: Invalid birthday")
		d := 10
		m := 15
		y := 2010
		reg.put (3, "Jim", [d,m, y])
	end

	t11
	do
		comment("t11: Duplicate ID")
		m := 12
		reg.put (2, "Jim", [d, m, y])
	end

	t12
	do
		comment("t12: Non-positive ID")
		reg.put (0, "Jim", [d, m, y])
	end

	t13
	do
		comment("t13: Invalid name")
		reg.put (3, "4Jim", [d, m, y])
	end

	t14
	do
		comment("t14: Invalid country")
		reg.put_alien (3, "Jim", [d, m, y], "5USA")
	end

	t15
	do
		comment("t15: Self Marry")
		d := 10
		m := 12
		y := 2010
		reg.marry(2,2, [d, m, y])
	end

	t16
	do
		comment("t16: Marry non-existant person")
		reg.marry(2,3, [d, m, y])
	end

	t17
	do
		comment("t17: Marry non-living person")
		reg.marry(2,1, [d, m, y])
	end

	t18
	do
		comment("t18: Marry too young")
		d := 10
		m := 12
		y := 1993
		reg.put (3, "Jim", [d, m, y])
		d := 10
		m := 12
		y := 2010
		reg.marry(2,3, [d, m, y])
	end

	t19
	do
		comment("t19: Already married")
		y := 2011
		reg.marry(2,3, [d, m, y])
		d := 5
		m := 6
		y := 1991
		reg.put (4, "Cindy", [d, m, y])
		d := 8
		m := 1
		y := 2012
		reg.marry (2, 4, [d, m ,y])
	end

	t20
	do
		comment("t20: Not married")
		reg.divorce (2, 4)
	end

	t21
	do
		comment("t21: Already dead")
		reg.die (1)
	end

end
