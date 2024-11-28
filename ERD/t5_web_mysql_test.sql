SELECT TABLE_NAME FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'db2409'
  AND TABLE_NAME LIKE 't5_%'
;


SELECT * FROM t5_authority;
SELECT * FROM t5_user ORDER BY id DESC;
SELECT * FROM t5_user_authorities;
SELECT * FROM t5_post ORDER BY id DESC;
SELECT * FROM t5_comment ORDER BY id DESC;
SELECT * FROM t5_attachment ORDER BY id DESC;


-- 특정 id 의 사용자 조회
SELECT
    id "id"
     , username "username"
     , password "password"
     , email "email"
     , name "name"
     , regdate "regdate"
FROM t5_user
WHERE 1 = 1
  AND id = 1
;

-- 특정 name 의 authority 조회
SELECT
    id "id"
     , name "name"
FROM t5_authority
WHERE name = 'ROLE_ADMIN'
;

-- 특정 사용자의 authority 조회
SELECT a.id "id", a.name "name"
FROM t5_authority a, t5_user_authorities u
WHERE a.id = u.authority_id  AND  u.user_id = 3
;

-- 페이징 테스트용 다량의 데이터
insert into t5_post (user_id, subject, content)
select user_id, subject, content from t5_post;

select count(*) from t5_post;

select * from t5_post order by id desc limit 5;
select * from t5_post order by id desc limit 5, 5;

delete from t5_post where id > 4;

# -------------------------------------------------------
# 댓글


# 특정글 의 (댓글 + 사용자) 정보
SELECT c.id "id", c.content "content", c.regdate "regdate",
       u.id "user_id", u.username "user_username", u.password "user_password", u.name "user_name", u.email "user_email", u.regdate "user_regdate"
FROM t5_comment c, t5_user u
WHERE c.user_id = u.id AND c.post_id = 1
ORDER BY c.id DESC
;



