note
	description: ""
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	ETF_PUT

inherit

	ETF_PUT_INTERFACE
		redefine
			put
		end

create
	make

feature -- command

	put (id: INTEGER_64; name: STRING; dob: TUPLE [d: INTEGER_64; m: INTEGER_64; y: INTEGER_64])
		require else
			put_precond (id, name, dob)
		do
				-- perform some update on the model state
			if not (id > 0) then
				model.set_err_message (err_msg.err_id_nonpositive)
			elseif not (not model.valid_id (id)) then
				model.set_err_message (err_msg.err_id_taken)
			elseif not ((not name.is_empty) and then name [1].is_alpha) then
				model.set_err_message (err_msg.err_name_start)
			elseif not (model.is_valid_date (dob.d, dob.m, dob.y)) then
				model.set_err_message (err_msg.err_invalid_date)
			else
				model.set_err_message (err_msg.ok)
				model.put (id, name, dob)
			end
			etf_cmd_container.on_change.notify ([Current])
		end

end
