-- author_tb 추가해주기--
insert into author_tb
select
	0,
    author_name
from
	book_list_tb
group by
	author_name;
    
-- category_tb 추가해주기--
insert into category_tb
select
	0,
    category_name
from
	book_list_tb
group by
	category_name;
    
-- publisher_tb 추가해주기--
insert into publisher_tb
select
	0,
    publisher_name
from
	book_list_tb
group by
	publisher_name;