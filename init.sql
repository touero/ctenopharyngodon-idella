-- drop database AllSchoolAPI;
CREATE DATABASE IF NOT EXISTS AllSchoolAPI Character Set UTF8;
USE AllSchoolAPI;
CREATE TABLE IF NOT EXISTS info(
    school_id VARCHAR(255) NOT NULL COMMENT '' ,
    name VARCHAR(255) COMMENT '' ,
    belong VARCHAR(255) COMMENT '' ,
    province_id VARCHAR(255) COMMENT '' ,
    province_name VARCHAR(255) COMMENT '' ,
    site VARCHAR(255) COMMENT '' ,
    city_name VARCHAR(255) COMMENT '' ,
    level_name VARCHAR(255) COMMENT '' ,
    type_name VARCHAR(255) COMMENT '' ,
    school_type_name VARCHAR(255) COMMENT '' ,
    school_nature_name VARCHAR(255) COMMENT '' ,
    dual_class_name VARCHAR(255) COMMENT '' ,
    nature_name VARCHAR(255) COMMENT '' ,
    school_site VARCHAR(255) COMMENT '' ,
    address VARCHAR(255) COMMENT '' ,
    content VARCHAR(255) COMMENT '' ,
    PRIMARY KEY (school_id)
    )COMMENT = '';


CREATE TABLE IF NOT EXISTS score(
    school_id VARCHAR(255)    COMMENT '' ,
    province_id VARCHAR(255)    COMMENT '' ,
    type VARCHAR(255)    COMMENT '' ,
    min VARCHAR(255)    COMMENT '' ,
    year VARCHAR(255)    COMMENT ''
    )  COMMENT = '';

CREATE TABLE IF NOT EXISTS major(
    school_id VARCHAR(255)    COMMENT '' ,
    special_name VARCHAR(255)    COMMENT '' ,
    type_name VARCHAR(255)    COMMENT '' ,
    level3_name VARCHAR(255)    COMMENT '' ,
    level2_name VARCHAR(255)    COMMENT '' ,
    limit_year VARCHAR(255)    COMMENT ''
    )  COMMENT = '';








