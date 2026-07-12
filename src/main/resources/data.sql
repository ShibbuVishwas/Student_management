INSERT INTO users (username, password, branch)
SELECT 'cse.prof@college.edu', 'cse123', 'CSE'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'cse.prof@college.edu');

INSERT INTO users (username, password, branch)
SELECT 'it.prof@college.edu', 'it123', 'IT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'it.prof@college.edu');

INSERT INTO students (first_name, last_name, email, branch, year)
SELECT 'Arun', 'Kumar', 'arun.cse@student.edu', 'CSE', 2
WHERE NOT EXISTS (SELECT 1 FROM students WHERE email = 'arun.cse@student.edu');

INSERT INTO students (first_name, last_name, email, branch, year)
SELECT 'Priya', 'Sharma', 'priya.cse@student.edu', 'CSE', 3
WHERE NOT EXISTS (SELECT 1 FROM students WHERE email = 'priya.cse@student.edu');

INSERT INTO students (first_name, last_name, email, branch, year)
SELECT 'Rahul', 'Verma', 'rahul.it@student.edu', 'IT', 2
WHERE NOT EXISTS (SELECT 1 FROM students WHERE email = 'rahul.it@student.edu');

INSERT INTO students (first_name, last_name, email, branch, year)
SELECT 'Sneha', 'Patel', 'sneha.it@student.edu', 'IT', 4
WHERE NOT EXISTS (SELECT 1 FROM students WHERE email = 'sneha.it@student.edu');
