CREATE DATABASE IF NOT EXISTS spot_teacher;
CREATE DATABASE IF NOT EXISTS spot_teacher_test;
grant all on spot_teacher.* to 'user';
grant all on spot_teacher_test.* to 'user';
FLUSH PRIVILEGES;
