insert into book_tb
select
	0 as book_id,
	book_name,
    author_id,
    publisher_id,
    category_id
from
	book_list_tb bl
    left outer join author_tb a on(a.author_name = bl.author_name)
    left outer join publisher_tb ps on(ps.publisher_name = bl.publisher_name)
    left outer join category_tb cg on(cg.category_name = bl.category_name)
group by
	book_id,
	book_name,
    author_id,
    publisher_id,
    category_id