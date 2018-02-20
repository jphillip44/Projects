note
	description: "Summary description for {ALIEN}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	ALIEN

inherit

	PERSON

create
	make_alien

feature {REGISTRY} -- init

	make_alien (l_id: INTEGER_64; l_name: STRING; l_dob: TUPLE [d, m, y: INTEGER_64]; l_country: STRING) --constructs a person with any citizenship
		do
			set_person (l_id, l_name, l_dob, l_country)
		end

end
