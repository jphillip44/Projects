note
	description: ""
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	ETF_MARRY

inherit

	ETF_MARRY_INTERFACE
		redefine
			marry
		end

create
	make

feature -- command

	marry (id1: INTEGER_64; id2: INTEGER_64; date: TUPLE [d: INTEGER_64; m: INTEGER_64; y: INTEGER_64])
		require else
			marry_precond (id1, id2, date)
		do
				-- perform some update on the model state
			if not (id1 /= id2) then
				model.set_err_message (err_msg.err_id_same)
			elseif not (id1 > 0 and then id2 > 0) then
				model.set_err_message (err_msg.err_id_nonpositive)
			elseif not (model.is_valid_date (date.d, date.m, date.y)) then
				model.set_err_message (err_msg.err_invalid_date)
			elseif not (model.valid_id (id1) and then model.valid_id (id2)) then
				model.set_err_message (err_msg.err_id_unused)
			elseif not (not (model.get_person (id1).is_married or else model.get_person (id2).is_married)) then
				model.set_err_message (err_msg.err_marry)
			elseif not (model.get_person (id1).is_alive and then model.get_person (id2).is_alive) then
				model.set_err_message (err_msg.err_marry)
			elseif not ((model.get_person (id1).get_married_age (date) > 17) and then (model.get_person (id2).get_married_age (date) > 17)) then
				model.set_err_message (err_msg.err_marry)
			else
				model.set_err_message (err_msg.ok)
				model.marry (id1, id2, date)
			end
			etf_cmd_container.on_change.notify ([Current])
		end

end
