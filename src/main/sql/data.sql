/*mimir 가데이터*/


-- 1단계: staff_permission 은 staff, permission 참조
TRUNCATE TABLE staff_permission;

-- 2단계: staff 는 position, department, log_record 참조
TRUNCATE TABLE staff;

-- 3단계: permission 은 참조되지 않음 (독립적)
TRUNCATE TABLE permission;

-- 4단계: log_record, department, position 은 staff에게 참조됨
TRUNCATE TABLE log_record;
TRUNCATE TABLE department;
TRUNCATE TABLE position;


/*department - 부서*/
INSERT INTO department VALUES ('room', '객실관리');
INSERT INTO department VALUES ('dinning', '다이닝 관리');
INSERT INTO department VALUES ('inquiry', '문의관리');
INSERT INTO department VALUES ('person', '인사관리');
INSERT INTO department VALUES ('member', '회원관리');
INSERT INTO department VALUES ('manage', '경영지원실');


/*position - 직책*/
INSERT INTO position VALUES ('E', '사원', 4);
INSERT INTO position VALUES ('D', '대리', 3);
INSERT INTO position VALUES ('C', '과장', 2);
INSERT INTO position VALUES ('B', '팀장', 1);
INSERT INTO position VALUES ('A', '대표', 0);



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

INSERT INTO LOG_RECORD VALUES ('LOG011', 'logfile11.log');
INSERT INTO LOG_RECORD VALUES ('LOG012', 'logfile12.log');
INSERT INTO LOG_RECORD VALUES ('LOG013', 'logfile13.log');
INSERT INTO LOG_RECORD VALUES ('LOG014', 'logfile14.log');
INSERT INTO LOG_RECORD VALUES ('LOG015', 'logfile15.log');
INSERT INTO LOG_RECORD VALUES ('LOG016', 'logfile16.log');
INSERT INTO LOG_RECORD VALUES ('LOG017', 'logfile17.log');
INSERT INTO LOG_RECORD VALUES ('LOG018', 'logfile18.log');
INSERT INTO LOG_RECORD VALUES ('LOG019', 'logfile19.log');
INSERT INTO LOG_RECORD VALUES ('LOG020', 'logfile20.log');

/*staff - 직원*/
INSERT INTO STAFF VALUES ('mimir_438219', 'E', 'room', 'LOG001', 'pass1', '홍길동', 'ACTIVE', 'hong@hotel.com', '2020-01-01', NULL);
INSERT INTO STAFF VALUES ('mimir_927364', 'D', 'dinning', 'LOG002', 'pass2', '김철수', 'ACTIVE', 'kim@hotel.com', '2021-02-15', NULL);
INSERT INTO STAFF VALUES ('mimir_318046', 'C', 'person', 'LOG003', 'pass3', '이영희', 'ACTIVE', 'lee@hotel.com', '2022-03-10', NULL);
INSERT INTO STAFF VALUES ('mimir_784120', 'B', 'person', 'LOG004', 'pass4', '박민수', 'ACTIVE', 'park@hotel.com', '2020-06-01', NULL);
INSERT INTO STAFF VALUES ('mimir_659432', 'C', 'person', 'LOG005', 'pass5', '최지우', 'ACTIVE', 'choi@hotel.com', '2019-08-01', NULL);
INSERT INTO STAFF VALUES ('mimir_210895', 'E', 'member', 'LOG006', 'pass6', '정우성', 'ACTIVE', 'jung@hotel.com', '2021-04-01', NULL);
INSERT INTO STAFF VALUES ('mimir_874360', 'D', 'room', 'LOG007', 'pass7', '한지민', 'ACTIVE', 'han@hotel.com', '2020-09-01', NULL);
INSERT INTO STAFF VALUES ('mimir_605417', 'C', 'dinning', 'LOG008', 'pass8', '신하균', 'ACTIVE', 'shin@hotel.com', '2022-05-01', NULL);
INSERT INTO STAFF VALUES ('mimir_132784', 'B', 'room', 'LOG009', 'pass9', '김하늘', 'ACTIVE', 'haneul@hotel.com', '2023-01-01', NULL);
INSERT INTO STAFF VALUES ('mimir_501276', 'A', 'manage', 'LOG010', 'pass10', '유재석', 'ACTIVE', 'yoo@hotel.com', '2024-03-01', NULL);

INSERT INTO STAFF VALUES ('mimir_712345', 'B', 'room',     'LOG011', 'pass11', '오세훈', 'ACTIVE', 'oh@hotel.com',   '2020-07-15', NULL);
INSERT INTO STAFF VALUES ('mimir_623451', 'C', 'member',   'LOG012', 'pass12', '장나라', 'ACTIVE', 'jang@hotel.com', '2021-10-20', NULL);
INSERT INTO STAFF VALUES ('mimir_534126', 'D', 'person',   'LOG013', 'pass13', '윤도현', 'ACTIVE', 'yoon@hotel.com', '2019-11-01', NULL);
INSERT INTO STAFF VALUES ('mimir_445623', 'E', 'dinning',  'LOG014', 'pass14', '하정우', 'ACTIVE', 'ha@hotel.com',   '2023-05-01', NULL);
INSERT INTO STAFF VALUES ('mimir_356712', 'C', 'member',  'LOG015', 'pass15', '이하늬', 'ACTIVE', 'leeh@hotel.com', '2022-02-14', NULL);
INSERT INTO STAFF VALUES ('mimir_267801', 'D', 'room',     'LOG016', 'pass16', '김수현', 'ACTIVE', 'soo@hotel.com',  '2020-12-31', NULL);
INSERT INTO STAFF VALUES ('mimir_178902', 'E', 'member',   'LOG017', 'pass17', '임수정', 'ACTIVE', 'lim@hotel.com',  '2021-06-30', NULL);
INSERT INTO STAFF VALUES ('mimir_089123', 'C', 'dinning',  'LOG018', 'pass18', '정해인', 'ACTIVE', 'jung@hotel.com', '2024-01-10', NULL);
INSERT INTO STAFF VALUES ('mimir_954321', 'B', 'inquiry',  'LOG019', 'pass19', '김유정', 'ACTIVE', 'kimy@hotel.com', '2022-08-05', NULL);
INSERT INTO STAFF VALUES ('mimir_843210', 'D', 'person',   'LOG020', 'pass20', '조인성', 'ACTIVE', 'cho@hotel.com',  '2023-09-15', NULL);






/*permission - 권한*/
INSERT INTO permission VALUES ('room', '객실');
INSERT INTO permission VALUES ('dinning', '다이닝');
INSERT INTO permission VALUES ('inquiry', '문의');
INSERT INTO permission VALUES ('member', '회원');
INSERT INTO permission VALUES ('employee', '직원');
INSERT INTO permission VALUES ('admin', '관리자');
INSERT INTO permission VALUES ('common', '공통');


/*staff_permission - 직원 권한*/
-- room 부서 → room 권한
INSERT INTO staff_permission VALUES ('room', 'mimir_438219');
INSERT INTO staff_permission VALUES ('room', 'mimir_874360');
INSERT INTO staff_permission VALUES ('room', 'mimir_132784');
INSERT INTO staff_permission VALUES ('room', 'mimir_712345');
INSERT INTO staff_permission VALUES ('room', 'mimir_267801');

-- dinning 부서 → dinning 권한
INSERT INTO staff_permission VALUES ('dinning', 'mimir_927364');
INSERT INTO staff_permission VALUES ('dinning', 'mimir_605417');
INSERT INTO staff_permission VALUES ('dinning', 'mimir_445623');
INSERT INTO staff_permission VALUES ('dinning', 'mimir_089123');

-- person 부서 → employee 권한
INSERT INTO staff_permission VALUES ('employee', 'mimir_318046');
INSERT INTO staff_permission VALUES ('employee', 'mimir_784120');
INSERT INTO staff_permission VALUES ('employee', 'mimir_659432');
INSERT INTO staff_permission VALUES ('employee', 'mimir_534126');
INSERT INTO staff_permission VALUES ('employee', 'mimir_843210');

-- member 부서 → member 권한
INSERT INTO staff_permission VALUES ('member', 'mimir_210895');
INSERT INTO staff_permission VALUES ('member', 'mimir_623451');
INSERT INTO staff_permission VALUES ('member', 'mimir_356712');
INSERT INTO staff_permission VALUES ('member', 'mimir_178902');

-- manage 부서 → admin 권한
INSERT INTO staff_permission VALUES ('admin', 'mimir_501276');

-- inquiry 부서 → 제외 (이번 조건에 명시되지 않음)
-- → 예: 'mimir_954321' 등은 권한 미할당
INSERT INTO staff_permission VALUES ('inquiry', 'mimir_501276');




select * from permission;
select * from staff;
select * from log_record;
select * from department;
select * from position;
select * from staff_permission;


