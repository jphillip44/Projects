note
	description: "Summary description for {CITIZEN}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	CITIZEN

inherit

	PERSON

create
	make_citizen

feature {REGISTRY}

	make_citizen (l_id: INTEGER_64; l_name: STRING; l_dob: TUPLE [d, m, y: INTEGER_64]) --constructs a person with Canadian citizenship
		do
			set_person (l_id, l_name, l_dob, CANADIAN)
		end

end
