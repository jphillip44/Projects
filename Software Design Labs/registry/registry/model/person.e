note
	description: "Summary description for {PERSON}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

deferred class
	PERSON

inherit

	COMPARABLE

feature {PERSON}

	id: INTEGER_64

	name, country: IMMUTABLE_STRING_8

	dob: TUPLE [d, m, y: INTEGER_64]

	spouse: PERSON

	married_since: TUPLE [d, m, y: INTEGER_64]

	alive: BOOLEAN

	CANADIAN: STRING = "Canada"

	ZERO: TUPLE [INTEGER_64, INTEGER_64, INTEGER_64]
		local
			zero_64: INTEGER_64
		once
			zero_64 := 0
			Result := [zero_64, zero_64, zero_64]
		end

	set_person (l_id: INTEGER_64; l_name: STRING; l_dob: TUPLE [d, m, y: INTEGER_64]; l_country: STRING)
		do
			id := l_id
			name := l_name
			dob := l_dob
			alive := TRUE
			married_since := ZERO
			country := l_country
			spouse := current
		end

feature -- queries

	get_id: INTEGER_64 --returns the ID of the person
		do
			Result := id
		end

	get_name: STRING --returns the name of the person
		do
			Result := name
		end

	get_country: STRING --returns the nationality of the person
		do
			Result := country
		end

	date_to_string (date: TUPLE [d, m, y: INTEGER_64]): STRING --converts a date as a tuple to a string
		do
			create Result.make_empty
			Result.append (date.y.out + "-")
			if (date.m < 10) then
				Result.append ("0")
			end
			Result.append (date.m.out + "-")
			if (date.d < 10) then
				Result.append ("0")
			end
			Result.append (date.d.out)
		end

	get_dob: STRING -- returns the birthday of the perspn
		do
			Result := date_to_string (dob)
		end

	get_married_since: STRING --returns the wedding date of the person (returns 00-00-0000 for unmarried)
		do
			Result := date_to_string (married_since)
		end

	get_spouse: PERSON --returns the spouse of the person (returns self if unmarried)
		do
			Result := spouse
		end

	is_married: BOOLEAN --returns the marriage status of the person
		do
			Result := spouse /= current
		end

	is_alive: BOOLEAN --returns whether the person is alive
		do
			Result := alive
		end

	get_married_age (date: TUPLE [d, m, y: INTEGER_64]): INTEGER_64 --returns age at date of marriage(returns a negative value if unmarried)
		do
			if (date.m > dob.m) or else (date.m = dob.m and then date.d >= dob.d) then
				Result := date.y - dob.y
			else
				Result := date.y - dob.y - 1
			end
		end

	is_less alias "<" (other: like current): BOOLEAN --used to sort users, first by name, then by id
		do
			if current = other then
				Result := False
			elseif name < other.name then
				Result := True
			elseif current.name ~ other.name then
				Result := id < other.id
			end
		end

feature --commands

	set_married (p: PERSON; date: TUPLE [d, m, y: INTEGER_64]) --sets the persons spouse and date of marriage
		do
			spouse := p
			married_since := date
		end


	set_divorced --sets the person to single and sets marriage date to 0000-00-00
		do
			married_since := ZERO
			spouse := current
		end

	set_death --sets the person to deceased
		do
			alive := FALSE
		end

invariant
	spouse_is_alive: current.get_spouse.is_alive = current.is_alive
		--checks if spouse is alive, trivially true if the person is single
	married_person_is_alive: current.is_married = current.is_alive or else not current.is_married
		--checks that a married person is alive, does not if the person is single
	married_person_over_18: current.is_married = (current.get_married_age (married_since) >= 18)
		--checks if the person is married that they were over 18 when they were, trivially true if the person is single as the second condition will be false	
	positive_id: current.get_id > 0
		--check if the persons id is positive
	valid_name: not (current.get_name.is_empty) and then name [1].is_alpha
		--checks if the persons name is valid
	valid_country: not (current.get_country.is_empty) and then name [1].is_alpha
		--checks if the persons country is valid
end
