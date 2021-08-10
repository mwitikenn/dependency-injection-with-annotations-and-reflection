package com.meliora.natujenge.resources;

import com.meliora.natujenge.annotations.Datasource;

@Datasource(id = "local", driverClassName = "com.mysql.cj.jdbc.Driver", jdbcUrl = "jdbc:mysql://localhost:3306/annotations_natujenge", username = "annot", password = "qwerty")
public class LocalMysqlDatabase {
}
