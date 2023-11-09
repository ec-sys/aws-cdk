const config = {
  mysql: {
    host: 'localhost',
    port: '3306',
    user: 'admin',
    password: 'root12345',
    database: 'todo_db'
  },
  redis: {
    host: 'localhost',
    port: '3306'
  },
  listPerPage: 10,
};

module.exports = config;
