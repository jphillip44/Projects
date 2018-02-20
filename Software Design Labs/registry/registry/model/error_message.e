note
	description: "Summary description for {ERROR_MESSAGE}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

expanded class
	ERROR_MESSAGE

feature -- Error Message Constants

	err_id_nonpositive: STRING = "id must be positive"

	err_id_unused: STRING = "id not identified with a person in database"

	err_id_same: STRING = "ids must be different"

	err_id_taken: STRING = "id already taken"

	err_name_start: STRING = "name must start with A-Z or a-z"

	err_country_start: STRING = "country must start with A-Z or a-z"

	err_invalid_date: STRING = "not a valid date in 1900..3000"

	err_marry: STRING = "proposed marriage invalid"

	err_divorce: STRING = "these are not married"

	err_dead_already: STRING = "person with that id already dead"

	ok: STRING = "ok"

end
