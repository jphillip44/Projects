note
	description: ""
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	ETF_DIVORCE

inherit

	ETF_DIVORCE_INTERFACE
		redefine
			divorce
		end

create
	make

feature -- command

	divorce (a_id1: INTEGER_64; a_id2: INTEGER_64)
		require else
			divorce_precond (a_id1, a_id2)
		do
				-- perform some update on the model state
			if not (a_id1 /= a_id2) then
				model.set_err_message (err_msg.err_id_same)
			elseif not (a_id1 > 0 and then a_id2 > 0) then
				model.set_err_message (err_msg.err_id_nonpositive)
			elseif not (model.valid_id (a_id1) and then model.valid_id (a_id2)) then
				model.set_err_message (err_msg.err_id_unused)
			elseif not (model.get_person (a_id1).get_spouse = model.get_person (a_id2) and then model.get_person (a_id2).get_spouse = model.get_person (a_id1)) then
				model.set_err_message (err_msg.err_divorce)
			else
				model.set_err_message (err_msg.ok)
				model.divorce (a_id1, a_id2)
			end
			etf_cmd_container.on_change.notify ([Current])
		end

end
