package org.commits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.sql.SQLException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application {
    public static void main(String[] args) throws SQLException {
//        DataBase db = new DataBase();
//        ArrayList<Commit> commits = db.getCommitsFromFile();
//        Model model = new Model();
//        model.changeSnapshot(commits);
//        try {
//            System.out.println(model.getModule("/2/0").getData());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("_");
        SpringApplication.run(Application.class, args);
    }
}