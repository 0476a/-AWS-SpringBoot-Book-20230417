update
	book_list_tb bl
    left outer join author_tb a on(a.author_name = bl.author_name)
    left outer join publisher_tb ps on(ps.publisher_name = bl.publisher_name)
    left outer join category_tb cg on(cg.category_name = bl.category_name)
    left outer join book_tb b on(
			b.book_name = bl.book_name
        and b.author_id = a.author_id
        and b.publisher_id = ps.publisher_id
        and b.category_id = cg.category_id)
set
	bl.book_id = b.book_id