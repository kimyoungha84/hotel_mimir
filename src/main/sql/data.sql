/*mimir 가데이터*/


/*department - 부서*/
INSERT INTO department VALUES ('room', '객실관리');
INSERT INTO department VALUES ('dinning', '다이닝 관리');
INSERT INTO department VALUES ('inquiry', '문의관리');
INSERT INTO department VALUES ('person', '인사관리');
INSERT INTO department VALUES ('membership', '회원관리');
INSERT INTO department VALUES ('manage', '경영지원실');


/*position - 직책*/
INSERT INTO position VALUES ('E', '사원', 4);
INSERT INTO position VALUES ('D', '대리', 3);
INSERT INTO position VALUES ('C', '과장', 2);
INSERT INTO position VALUES ('B', '팀장', 1);
INSERT INTO position VALUES ('A', '사장', 0);



/*log_record - 로그 기록*/
INSERT INTO LOG_RECORD VALUES ('LOG001', 'logfile1.log');
INSERT INTO LOG_RECORD VALUES ('LOG002', 'logfile2.log');
INSERT INTO LOG_RECORD VALUES ('LOG003', 'logfile3.log');
INSERT INTO LOG_RECORD VALUES ('LOG004', 'logfile4.log');
INSERT INTO LOG_RECORD VALUES ('LOG005', 'logfile5.log');
INSERT INTO LOG_RECORD VALUES ('LOG006', 'logfile6.log');
INSERT INTO LOG_RECORD VALUES ('LOG007', 'logfile7.log');
INSERT INTO LOG_RECORD VALUES ('LOG008', 'logfile8.log');
INSERT INTO LOG_RECORD VALUES ('LOG009', 'logfile9.log');
INSERT INTO LOG_RECORD VALUES ('LOG010', 'logfile10.log');


/*staff - 직원*/
INSERT INTO STAFF VALUES ('mimir_438219', 'E', 'room', 'LOG001', 'pass1', '홍길동', 'ACTIVE', 'hong@hotel.com', '2020-01-01', NULL);
INSERT INTO STAFF VALUES ('mimir_927364', 'D', 'dinning', 'LOG002', 'pass2', '김철수', 'ACTIVE', 'kim@hotel.com', '2021-02-15', NULL);
INSERT INTO STAFF VALUES ('mimir_318046', 'C', 'inquiry', 'LOG003', 'pass3', '이영희', 'ACTIVE', 'lee@hotel.com', '2022-03-10', NULL);
INSERT INTO STAFF VALUES ('mimir_784120', 'B', 'person', 'LOG004', 'pass4', '박민수', 'ACTIVE', 'park@hotel.com', '2020-06-01', NULL);
INSERT INTO STAFF VALUES ('mimir_659432', 'A', 'manage', 'LOG005', 'pass5', '최지우', 'ACTIVE', 'choi@hotel.com', '2019-08-01', NULL);
INSERT INTO STAFF VALUES ('mimir_210895', 'E', 'member', 'LOG006', 'pass6', '정우성', 'ACTIVE', 'jung@hotel.com', '2021-04-01', NULL);
INSERT INTO STAFF VALUES ('mimir_874360', 'D', 'room', 'LOG007', 'pass7', '한지민', 'ACTIVE', 'han@hotel.com', '2020-09-01', NULL);
INSERT INTO STAFF VALUES ('mimir_605417', 'C', 'dinning', 'LOG008', 'pass8', '신하균', 'ACTIVE', 'shin@hotel.com', '2022-05-01', NULL);
INSERT INTO STAFF VALUES ('mimir_132784', 'B', 'inquiry', 'LOG009', 'pass9', '김하늘', 'ACTIVE', 'haneul@hotel.com', '2023-01-01', NULL);
INSERT INTO STAFF VALUES ('mimir_501276', 'A', 'person', 'LOG010', 'pass10', '유재석', 'ACTIVE', 'yoo@hotel.com', '2024-03-01', NULL);





/*permission - 권한*/
INSERT INTO permission VALUES ('room', '객실');
INSERT INTO permission VALUES ('dinning', '다이닝');
INSERT INTO permission VALUES ('inquiry', '문의');
INSERT INTO permission VALUES ('member', '회원');
INSERT INTO permission VALUES ('employee', '직원');
INSERT INTO permission VALUES ('admin', '관리자');
INSERT INTO permission VALUES ('common', '공통');


/*staff_permission - 직원 권한*/
-- mimir_438219 (room)
INSERT INTO staff_permission VALUES ('room', 'mimir_438219');
INSERT INTO staff_permission VALUES ('common', 'mimir_438219');

-- mimir_927364 (dinning)
INSERT INTO staff_permission VALUES ('dinning', 'mimir_927364');
INSERT INTO staff_permission VALUES ('common', 'mimir_927364');

-- mimir_318046 (inquiry)
INSERT INTO staff_permission VALUES ('inquiry', 'mimir_318046');
INSERT INTO staff_permission VALUES ('common', 'mimir_318046');

-- mimir_784120 (person)
INSERT INTO staff_permission VALUES ('employee', 'mimir_784120');
INSERT INTO staff_permission VALUES ('common', 'mimir_784120');

-- mimir_659432 (manage)
INSERT INTO staff_permission VALUES ('admin', 'mimir_659432');
INSERT INTO staff_permission VALUES ('common', 'mimir_659432');

-- mimir_210895 (membership)
INSERT INTO staff_permission VALUES ('member', 'mimir_210895');
INSERT INTO staff_permission VALUES ('common', 'mimir_210895');

-- mimir_874360 (room)
INSERT INTO staff_permission VALUES ('room', 'mimir_874360');
INSERT INTO staff_permission VALUES ('common', 'mimir_874360');

-- mimir_605417 (dinning)
INSERT INTO staff_permission VALUES ('dinning', 'mimir_605417');
INSERT INTO staff_permission VALUES ('common', 'mimir_605417');

-- mimir_132784 (inquiry)
INSERT INTO staff_permission VALUES ('inquiry', 'mimir_132784');
INSERT INTO staff_permission VALUES ('common', 'mimir_132784');

-- mimir_501276 (person)
INSERT INTO staff_permission VALUES ('employee', 'mimir_501276');
INSERT INTO staff_permission VALUES ('common', 'mimir_501276');

