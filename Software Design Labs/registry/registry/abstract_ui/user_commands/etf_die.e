note
	description: ""
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	ETF_DIE

inherit

	ETF_DIE_INTERFACE
		redefine
			die
		end

create
	make

feature -- command

	die (id: INTEGER_64)
		require else
			die_precond (id)
		do
				-- perform some update on the model state
			if not (id > 0) then
				model.set_err_message (err_msg.err_id_nonpositive)
			elseif not (model.valid_id (id)) then
				model.set_err_message (err_msg.err_id_unused)
			elseif not (model.get_person (id).is_alive) then
				model.set_err_message (err_msg.err_dead_already)
			else
				model.set_err_message (err_msg.ok)
				model.die (id)
			end
			etf_cmd_container.on_change.notify ([Current])
		end

end
