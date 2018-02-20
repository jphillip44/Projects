note
	description: "A default business model."
	author: "Jackie Wang"
	date: "$Date$"
	revision: "$Revision$"

class
	REGISTRY

inherit

	ANY
		redefine
			out
		end

create {REGISTRY_ACCESS}
	make

feature {NONE}

	persons: HASH_TABLE [PERSON, INTEGER_64]

	error_message: STRING

feature {NONE} -- Initialization

	make
		-- Initialization for `Current'.

		local
			err_msg: ERROR_MESSAGE
		do
			create persons.make (10)
			error_message := err_msg.ok
		end

feature -- model operations

	reset
		-- Reset model state.
		do
			make
		end

feature -- Model Commands

	put (id: INTEGER_64; name: STRING; dob: TUPLE [d, m, y: INTEGER_64]) --adds a citizen to the registry
		require
			err_id_nonpositive: id > 0
			err_id_taken: not valid_id (id)
			err_name_start: (not name.is_empty) and then name [1].is_alpha
			err_invalid_date: is_valid_date (dob.d, dob.m, dob.y)
		local
			new_person: CITIZEN
		do
			create new_person.make_citizen (id, name, dob)
			persons.extend (new_person, id)
		ensure
			correct_size: persons.count = (old persons.count + 1)
			new_key_inserted: persons.has_key (id) /= old persons.has (id)
		end

	put_alien (id: INTEGER_64; name: STRING; dob: TUPLE [d, m, y: INTEGER_64]; country: STRING) --adds a non-citizen to the registry
		require
			err_id_nonpositive: id > 0
			err_id_taken: not valid_id (id)
			err_name_start: (not name.is_empty) and then name [1].is_alpha
			err_invalid_date: is_valid_date (dob.d, dob.m, dob.y)
			err_country_start: (not country.is_empty) and then country [1].is_alpha
		local
			new_person: ALIEN
		do
			create new_person.make_alien (id, name, dob, country)
			persons.extend (new_person, id)
		ensure
			correct_size: persons.count = (old persons.count + 1)
			new_key_inserted: persons.has_key (id) /= old persons.has_key (id)
		end

	marry (id1, id2: INTEGER_64; date: TUPLE [d, m, y: INTEGER_64]) --marries two people in the registry
		require
			err_id_same: id1 /= id2
			err_id_nonpositive: id1 > 0 and then id2 > 0
			err_invalid_date: is_valid_date (date.d, date.m, date.y)
			err_id_unused: valid_id (id1) and then valid_id (id2)
			err_marry: not (get_person (id1).is_married or else get_person (id2).is_married)
			err_dead: get_person (id1).is_alive and then get_person (id2).is_alive
			err_under_18: (get_person (id1).get_married_age (date) > 17) and then (get_person (id2).get_married_age (date) > 17)
		do
			get_person (id1).set_married (get_person (id2), date)
			get_person (id2).set_married (get_person (id1), date)
		ensure
			spouse1_is_person2: get_person (id1).get_spouse = get_person (id2)
			spouse2_is_person1: get_person (id2).get_spouse = get_person (id1)
		end

	divorce (id1, id2: INTEGER_64) --divorces two people in the registry
		require
			err_id_same: id1 /= id2
			err_id_nonpositive: id1 > 0 and then id2 > 0
			err_id_unused: valid_id (id1) and then valid_id (id2)
			err_divorce: get_person (id1).get_spouse = get_person (id2) and then get_person (id2).get_spouse = get_person (id1)
		do
			get_person (id1).set_divorced
			get_person (id2).set_divorced
		ensure
			person1_is_single: get_person (id1).get_spouse = get_person (id1)
			person2_is_single: get_person (id2).get_spouse = get_person (id2)
		end

	die (id: INTEGER_64) --sets a person in the registry to deceased
		require
			err_id_nonpositive: id > 0
			err_id_unused: valid_id (id)
			err_dead_already: get_person (id).is_alive
		do
			if get_person (id).is_married then
				get_person (id).get_spouse.set_divorced
				get_person (id).set_divorced
			end
			get_person (id).set_death
		ensure
			not_alive: not get_person (id).is_alive
			spouse_not_married: not (old get_person(id)).get_spouse.is_married
		end

	set_err_message (s: STRING) --sets the error message
		do
			error_message := s.deep_twin
		end

feature -- Model Queries

	get_list: SORTED_TWO_WAY_LIST [PERSON] --returns the registry as a sorted list
		do
			create Result.make
			across
				persons as cursor
			loop
				Result.extend (cursor.item)
			end
		ensure
			sizes_match: persons.count = result.count
			registry_unchanged: persons ~ old persons
		end

	is_valid_date (date: TUPLE [d, m, y: INTEGER_64]): BOOLEAN --checks whether a date as a tuple is valid
		local
			d: DATE
		do
			create d.make_now
			Result := d.is_correct_date (date.y.to_integer_32, date.m.to_integer_32, date.d.to_integer_32)
		end

	get_person (id: INTEGER_64): PERSON --checks,given an id, that the person is attached and returns the person
		do
			check attached persons.item (id) as p then
				Result := p
			end
		ensure
			person_unchanged: persons.at (id) ~ old persons.at (id)
		end

	valid_id (id: INTEGER_64): BOOLEAN --checks if the id is in the registry
		do
			Result := persons.has_key (id)
		ensure
			person_unchanged: persons.at (id) ~ old persons.at (id)
		end

feature -- queries

	out: STRING --sets the output for ETF
		local
			list: SORTED_TWO_WAY_LIST [PERSON]
		do
			list := get_list
			create Result.make_empty
			Result.append ("  " + error_message)
			error_message := ""
			across
				list as cursor
			loop
				Result.append ("%N  ")
				Result.append (cursor.item.get_name + "; ")
				Result.append ("ID: " + cursor.item.get_id.out + "; ")
				Result.append ("Born: " + cursor.item.get_dob + "; ")
				Result.append ("Citizen: " + cursor.item.get_country + "; ")
				if cursor.item.is_married then
					Result.append ("Spouse: " + cursor.item.get_spouse.get_name)
					Result.append ("," + cursor.item.get_spouse.get_id.out)
					Result.append (",[" + cursor.item.get_married_since + "]")
				elseif cursor.item.is_alive then
					Result.append ("Single")
				else
					Result.append ("Deceased")
				end
			end
		end

invariant
	persons.count >= 0 --ensures the registry never has negative elements

end