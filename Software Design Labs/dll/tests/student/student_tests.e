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

feature {NONE} -- Initialization

	make
			-- Initialization for `Current'.
		do
			add_boolean_case (agent t0)
			add_boolean_case (agent t1)
			add_boolean_case (agent t2)
			add_boolean_case (agent t3)
			add_boolean_case (agent t4)
			add_violation_case_with_tag ("valid_index_ra", agent t5)
			add_violation_case_with_tag ("valid_index", agent t6)
			add_violation_case_with_tag ("last_not_empty", agent t7)
			add_violation_case_with_tag ("node_exists", agent t8)
			add_violation_case_with_tag ("node_exists", agent t9)
			add_violation_case_with_tag ("valid_index", agent t10)
		end

feature

	t0: BOOLEAN
		local
			list1, list2: DOUBLY_LINKED_LIST[INTEGER]
		do
			comment ("t0: using replace to grab item from another list to make them match")
			create list1.make_from_array (<<0, 2>>)
			create list2.make_from_array (<<1, 2>>)
			list1.replace (list2.item (1), 1)
			Result := list1 ~ list2
			check
				Result
			end
		end

	t1: BOOLEAN
		local
			list1, list2: DOUBLY_LINKED_LIST [INTEGER]
		do
			comment ("t1: adding 1,2,3,4 out of sequence using add, add_before, add_after")
			create list1.make_from_array (<<3>>)
			list1.add_after (list1.node (1), 4)
			list1.add_before (list1.node (1), 1)
			list1.add (2, 2)
			create list2.make_from_array (<<1, 2, 3, 4>>)
			Result := list1 ~ list2
			check
				Result
			end
		end

	t2: BOOLEAN
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t2: add_last void to empty list, remove_first void")
			create list1.make_empty
			list1.add_last (Void)
			list1.remove_first
			Result := list1.is_empty
			check
				Result
			end
		end

	t3: BOOLEAN
		local
			list1, list2: DOUBLY_LINKED_LIST [INTEGER]
		do
			comment ("t3: add_first 2 numbers, remove the last number in the list")
			create list1.make_empty
			list1.add_first (1)
			list1.add_first (2)
			create list2.make_from_array (<<2>>)
			list1.remove_last
			Result := list1 ~ list2
			check
				Result
			end
		end

	t4: BOOLEAN
		local
			list1, list2: DOUBLY_LINKED_LIST [INTEGER]
		do
			comment ("t4: add_last 2 numbers, remove_at (2) in the list")
			create list1.make_empty
			list1.add_last (1)
			list1.add_last (2)
			create list2.make_from_array (<<1>>)
			list1.remove_at (2)
			Result := list1 ~ list2
			check
				Result
			end
		end

	t5
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t5: remove_at(1) and then remove from empty")
				--violates valid_index_ra of remove_at since remove_at (1) is not a valid index
			create list1.make_from_array (<<"test">>)
			list1.remove_at (1)
			list1.remove_at (1)
		end

	t6
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t6: remove (node(1)) and then remove from empty")
				--violates valid_index (i) of node, which is an argument for remove()
			create list1.make_from_array (<<"test">>)
			list1.remove (list1.node (1))
			list1.remove (list1.node (1))
		end

	t7
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t7: remove last element and then remove from empty")
				--violates last_not_empty because the list is empty
			create list1.make_from_array (<<"test">>)
			list1.remove_last
			list1.remove_last
		end

	t8
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t8: try to add_after header which is not a valid node")
				--violates next_node_exists
			create list1.make_empty
			list1.add_after (list1.header, "test")
		end

	t9
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t9: try to add_before trailer which is not a valid node")
				--violates prev_node_exists
			create list1.make_empty
			list1.add_before (list1.trailer, "test")
		end

	t10
		local
			list1: DOUBLY_LINKED_LIST [STRING]
		do
			comment ("t10: try to add non_existent index")
				--violates valid_index(i) of add()
			create list1.make_empty
			list1.add ("test", 1)
		end

end
