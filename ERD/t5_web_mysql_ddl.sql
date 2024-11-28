SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS t5_attachment;
DROP TABLE IF EXISTS t5_user_authorities;
DROP TABLE IF EXISTS t5_authority;
DROP TABLE IF EXISTS t5_comment;
DROP TABLE IF EXISTS t5_post;
DROP TABLE IF EXISTS t5_user;


CREATE TABLE t5_attachment
(
    id         INT          NOT NULL AUTO_INCREMENT,
    post_id    INT          NOT NULL,
    sourcename VARCHAR(100) NOT NULL COMMENT '원본이름',
    filename   VARCHAR(100) NOT NULL COMMENT '저장된 이름',
    PRIMARY KEY (id)
) COMMENT '첨부파일';

CREATE TABLE t5_authority
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL COMMENT '권한명',
    PRIMARY KEY (id)
) COMMENT '권한';

ALTER TABLE t5_authority
    ADD CONSTRAINT UQ_name UNIQUE (name);

CREATE TABLE t5_comment
(
    id      INT      NOT NULL AUTO_INCREMENT,
    user_id INT      NOT NULL,
    post_id INT      NOT NULL,
    content TEXT     NOT NULL,
    regdate DATETIME NULL     DEFAULT now(),
    PRIMARY KEY (id)
) COMMENT '댓글';

CREATE TABLE t5_post
(
    id      INT          NOT NULL AUTO_INCREMENT,
    user_id INT          NOT NULL,
    subject VARCHAR(200) NOT NULL,
    content LONGTEXT     NULL    ,
    viewcnt INT          NULL     DEFAULT 0,
    regdate DATETIME     NULL     DEFAULT now(),
    PRIMARY KEY (id)
) COMMENT '게시글';

CREATE TABLE t5_user
(
    id       INT          NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name     VARCHAR(80)  NOT NULL,
    email    VARCHAR(80)  NULL    ,
    regdate  DATETIME     NULL     DEFAULT now(),
    PRIMARY KEY (id)
) COMMENT '회원';

ALTER TABLE t5_user
    ADD CONSTRAINT UQ_username UNIQUE (username);

CREATE TABLE t5_user_authorities
(
    user_id      INT NOT NULL,
    authority_id INT NOT NULL,
    PRIMARY KEY (user_id, authority_id)
);

ALTER TABLE t5_post
    ADD CONSTRAINT FK_t5_user_TO_t5_post
        FOREIGN KEY (user_id)
            REFERENCES t5_user (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE
;


ALTER TABLE t5_comment
    ADD CONSTRAINT FK_t5_user_TO_t5_comment
        FOREIGN KEY (user_id)
            REFERENCES t5_user (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE
;

ALTER TABLE t5_comment
    ADD CONSTRAINT FK_t5_post_TO_t5_comment
        FOREIGN KEY (post_id)
            REFERENCES t5_post (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE
;

ALTER TABLE t5_attachment
    ADD CONSTRAINT FK_t5_post_TO_t5_attachment
        FOREIGN KEY (post_id)
            REFERENCES t5_post (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE
;

ALTER TABLE t5_user_authorities
    ADD CONSTRAINT FK_t5_user_TO_t5_user_authorities
        FOREIGN KEY (user_id)
            REFERENCES t5_user (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE
;

ALTER TABLE t5_user_authorities
    ADD CONSTRAINT FK_t5_authority_TO_t5_user_authorities
        FOREIGN KEY (authority_id)
            REFERENCES t5_authority (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE
;
