SELECT 
    count(*)
FROM 
	book_tb bt
    left outer join author_tb at on(at.author_id = bt.author_id)
    left outer join publisher_tb pt on(pt.publisher_id = bt.publisher_id)
    left outer join category_tb ct on(ct.category_id = bt.category_id)
where
	bt.book_name like '%기술%'