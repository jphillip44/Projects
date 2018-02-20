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

	array: STRING

	list: STRING
	make
		do
			array:= "array"
			list:= "list"
			add_boolean_case (agent t1)
			add_boolean_case (agent t2)
			add_boolean_case (agent t3)
			add_violation_case_with_tag ("valid_expression", agent t10)
			add_violation_case_with_tag ("valid_expression", agent t11)
			add_violation_case_with_tag ("valid_expression", agent t12)
		end

feature

	t1: BOOLEAN
		local
			l_exp: STRING
			e, o: EVALUATOR
		do
			comment ("t1: Evaluate ( 10.3 + 13.1) and test other type for equivalence")
			l_exp := "( 10.1 + 13.3 )"
			create e.make (array)
			create o.make (list)
			e.evaluate (l_exp)
			o.evaluate (l_exp)
			Result := (e.value - 23.4).abs <= 0.0001
			check
				Result
			end
			Result := (e.value - o.value).abs <= 0.001
			check
				Result
			end
		end

	t2: BOOLEAN
		local
			l_exp1, l_exp2: STRING
			e: EVALUATOR
		do
			comment ("t2: Evaluate (4*5), show e1.evaluated(4*6) works and does not affect e1.value")
			l_exp1 := "(4*5)"
			l_exp2 := "(4*6)"
			create e.make (array)
			e.evaluate (l_exp1)
			Result := e.value = 20 and not e.error
			check
				Result
			end
			Result := e.evaluated (l_exp2) > e.value
			check
				Result
			end
		end

	t3: BOOLEAN
		local
			l_exp1, l_exp2: STRING
			e: EVALUATOR
		do
			comment ("t3: Same as t2 but with a list")
			l_exp1 := "(4*5)"
			l_exp2 := "(4*6)"
			create e.make (list)
			e.evaluate (l_exp1)
			Result := e.value = 20 and not e.error
			check
				Result
			end
			Result := e.evaluated (l_exp2) > e.value
			check
				Result
			end
		end

	t10
		local
			l_exp: STRING
			e: EVALUATOR
		do
			comment ("t10: Invalid input 13.1.0")
			l_exp := "( 10.3 + 13.1.0 )"
			create e.make (array)
			e.evaluate (l_exp)
		end

	t11
		local
			l_exp: STRING
			e: EVALUATOR
			test: REAL
		do
			comment ("t11: Invalid input p4")
            l_exp := "(7 + p4)"
			create e.make (list)
			test := e.evaluated (l_exp)
		end

	t12
		local
			l_exp: STRING
			e: EVALUATOR
		do
			comment ("t12: Invalid operator")
			l_exp := "(10 -+ 5)"
			create e.make(array)
			e.evaluate (l_exp)
		end

end
