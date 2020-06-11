/*用户*/
drop table user;
create table user
(
	id int auto_increment,
	account varchar(50) not null primary key comment '用户名',
	password varchar(16) null comment '密码',
	token varchar(36) null comment '缓存数据',
	name varchar(50) null comment '昵称',
	avatar varchar(255) null comment '头像',
	bio varchar(255) null comment '简介',
	follow_count int default 0 null,
	fans_count int default 0 null,
	gmt_create bigint null,
	gmt_modify bigint null,
	like_count int default 0 comment '总计获赞数',
    chose_count int default 0 comment '总计优质回答数',
	constraint user_account_uindex
		unique (account),
	constraint user_id_uindex
		unique (id)
);


/*问题或文章*/
drop table content;
create table content
(
	id int auto_increment
		primary key,
	title varchar(50) null,
	description text null,
	tag varchar(256) null,
	creator int null,
	type int null default 0 comment '1：普通问题；2：文章',
	view_count int default 0 null,
	comment_count int default 0 null,
	like_count int default 0 null,
	coll_count int null comment '收藏',
	gmt_create bigint null,
	gmt_modify bigint null
);

/*评论*/
drop table comment;
create table comment
(
	id int auto_increment,
	parent_id int not null comment '被评论的问题or评论的id',
	type int not null comment '是一级评论or二级回复or精选回复',
	commentator int not null comment '撰写评论的人',
	comment_count int default 0 null comment '二级评论数',
	like_count int default 0 null comment '点赞数',
	content varchar(255) not null,
	gmt_create bigint null,
	gmt_modify bigint null,
	constraint comment_pk
		primary key (id)
);

/*通知*/
drop table notification;
create table notification
(
	id int auto_increment
		primary key,
	notifier int not null comment '通知者',
	notifier_name varchar(16) null comment '通知者昵称',
	outer_id int null,
	outer_title varchar(50) null comment '缓存相关问题的题目',
	receiver int not null comment '接收通知的人',
	type int null comment '1:回复内容;2:回复评论;3:关注;4:精选',
	status int default 0 null comment '状态1：已读or2：未读',
	gmt_create bigint null
);

/*收藏*/
create table collection
(
	id int auto_increment,
	user_id int null,
	question_id int null,
	gmt_create bigint null,
	constraint collection_pk
		primary key (id)
);

/*点赞*/
drop table mylike;
create table mylike
(
	id int auto_increment,
	user_id int null comment '点赞的人',
	type int null comment '被点赞的是1：问题or2：评论',
	content_id int null comment '被点赞的内容的id',
	gmt_create bigint null,
	constraint mylike_pk
		primary key (id)
);
/*关注*/
drop table follow;
create table follow
(
    id int auto_increment,
	user_id int null comment '操作用户',
	follower int null comment '被关注者',
	gmt_create bigint null,
	constraint follow_pk
		primary key (id)
);



/*文件*/
create table myfile
(
    id int auto_increment,
	url varchar(600) null comment '地址',
	name varchar(600) null,
	path varchar(600) null,
	constraint file_pk
		primary key (id)
);







