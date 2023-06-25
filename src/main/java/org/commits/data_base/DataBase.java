package org.commits.data_base;

import org.commits.entities.commits.ChangerCommit;
import org.commits.entities.commits.Commit;
import org.commits.entities.commits.CreationCommit;
import org.commits.entities.commits.DeletionCommit;

import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DataBase {

    private final String DB_USERNAME = "postgres";
    private final String DB_PASSWORD = "postgres";
    private final String DB_URL = "jdbc:postgresql://" + System.getenv("DB_CONTAINER") + ":5432/commits";//TODO поменять localhost на название контейнера с db если нужен докер
    private final String DB_NAME = "commits";

    private Integer k = 0;

    public DataBase() {
        //generateDataDB();
        generateDataFiles();
    }

    public ArrayList<Commit> getCommitsFromFile() {
        ArrayList<Commit> commits = new ArrayList<Commit>();
        String del = File.separator;
        final File folder = new File("src" + del + "main" + del + "resources" + del + "Database", "Commits");
        if (folder.exists() && folder.isDirectory()) {
            File[] files =  folder.listFiles();
            Arrays.sort(files);
            for (final File file : files) {
                if (file.isFile()) {
                    try {
                        FileReader reader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line = bufferedReader.readLine();
                        ArrayList<String> commitData = new ArrayList<String>();
                        while ((line = bufferedReader.readLine()) != null) {
                            commitData.add(line);
                        }
                        Commit com = null;
                        if (commitData.get(1).equals("Creation")) {
                            int dotPos = file.getName().indexOf('.');
                            if (dotPos != -1) {
                                com = new CreationCommit(Integer.parseInt(file.getName().substring(0, dotPos)), commitData.get(0), commitData.get(2));
                            }
                        } else if (commitData.get(1).equals("Changer")) {
                            int dotPos = file.getName().indexOf('.');
                            if (dotPos != -1) {
                                com = new ChangerCommit(Integer.parseInt(file.getName().substring(0, dotPos)), commitData.get(0), commitData.get(2));
                            }
                        } else if (commitData.get(1).equals("Deletion")) {
                            int dotPos = file.getName().indexOf('.');
                            if (dotPos != -1) {
                                com = new DeletionCommit(Integer.parseInt(file.getName().substring(0, dotPos)), commitData.get(0));
                            }
                        }
                        if (com != null) {
                            commits.add(com);
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("Problem with " + file.getName() + " - " + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Problem with " + file.getName() + " - " + e.getMessage());
                    }
                }
            }
        }
        return commits;
    }


    public ArrayList<Commit> getCommitsFromDB() {
        ArrayList<Commit> commits = new ArrayList<Commit>();
        String command = "SELECT * FROM " + DB_NAME;
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(command);
            while(result.next()){
                Commit com = null;
                if (result.getString("commitType").equals("Creation")) {
                    com = new CreationCommit(result.getInt("id"), result.getString("fullPath"), result.getString("data"));
                } else if (result.getString("commitType").equals("Changer")) {
                    com = new ChangerCommit(result.getInt("id"), result.getString("fullPath"), result.getString("data"));
                } else if (result.getString("commitType").equals("Deletion")) {
                    com = new DeletionCommit(result.getInt("id"), result.getString("fullPath"));
                }
                if (!Objects.isNull(com)) {
                    commits.add(com);
                }
            }
            return commits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createFileFromCommit(Commit commit) {
        String del = File.separator;
        try {
            File file = new File("src" + del + "main" + del + "resources" + del + "Database" + del + "Commits", idToString(k) + ".txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            commit.setId(k);
            String text = idToString(k) + "\n" + commit.getFullPath() + "\n";;
            if (commit instanceof CreationCommit) {
                text += "Creation\n" + ((CreationCommit)commit).getData();
            } else if (commit instanceof ChangerCommit) {
                text += "Changer\n" + ((ChangerCommit)commit).getData();
            } else if (commit instanceof DeletionCommit) {
                text += "Deletion\n";
            } else {
                throw new Exception("");
            }
            bufferedWriter.write(text);
            bufferedWriter.close();
            ++k;
        } catch (Exception e) {
            System.out.println("Error creating file during change snapshot after request");
            System.out.println(e.getMessage());
        }
    }

    private void createFile(String text) throws IOException {
        String del = File.separator;
        File file = new File("src" + del + "main" + del + "resources" + del + "Database" + del + "Commits", idToString(k) + ".txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file, false);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(text);
        bufferedWriter.close();
        ++k;
    }

    private String idToString(Integer k) {
        if (k < 10) {
            return "000" + k;
        }
        if (k < 100) {
            return "00" + k;
        }
        if (k < 1000) {
            return "0" + k;
        }
        return Integer.toString(k);
    }

    public void generateDataFiles() {
        String del = File.separator;
        try {
            File dir = new File("src" + del + "main" + del + "resources" + del + "Database", "Commits");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    String text = idToString(k) + "\n" + "_" + i + "\n" + "Creation" + "\n" + i;
                    createFile(text);
                    for (int j = 0; j < 3; ++j) {
                        text = idToString(k) + "\n" + "_" + i + "_" + j + "\n" + "Creation" + "\n" + i + "a" + j;
                        createFile(text);
                    }
                    text = idToString(k) + "\n" + "_" + i + "_" + 0 + "\n" + "Changer" + "\n" + "_" + i + "_" + 0 + " changed";
                    createFile(text);
                }
            }
            String text = idToString(k) + "\n" + "_" + 0 + "\n" + "Deletion" + "\n";
            createFile(text);
        } catch (IOException e) {
            System.out.println("generate data files error");
            System.out.println(e.getMessage());
        }
    }

    public void generateDataDB() {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement statement = connection.createStatement();
            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    String s = "INSERT INTO commits VALUES (1, %2F0, Creation, i);";
                    String SQL_INSERT_COMMIT = "INSERT INTO " + DB_NAME + " VALUES (" + Integer.toString(k) + ", _" + Integer.toString(i) + ", " + "Creation, " +
                            Integer.toString(i) + ");" ;
                    statement.executeUpdate(SQL_INSERT_COMMIT);
                    ++k;
                    for (int j = 0; j < 3; ++j) {
                        SQL_INSERT_COMMIT = "INSERT INTO " + DB_NAME + " VALUES (" + k + ", _" + Integer.toString(i) + "_" + Integer.toString(j)
                                + ", " + "Creation, " + Integer.toString(i) + "a" + Integer.toString(j) + ")" ;
                        statement.executeUpdate(SQL_INSERT_COMMIT);
                        ++k;
                    }
                    SQL_INSERT_COMMIT = "INSERT INTO " + DB_NAME + " VALUES (" + k + ", _" + Integer.toString(i) + "_"
                            + Integer.toString(0) + ", " + "Changer, " + "_" + Integer.toString(i) + "_" +
                            Integer.toString(0) + " changed" + ")" ;
                    statement.executeUpdate(SQL_INSERT_COMMIT);
                    ++k;
                }
            }
            String SQL_INSERT_COMMIT = "INSERT INTO " + DB_NAME + " VALUES (" + k + ", _" + Integer.toString(0) + ", " + "Delation" + ")" ;
            statement.executeUpdate(SQL_INSERT_COMMIT);
            connection.close();
            ++k;
        } catch (SQLException e) {
            System.out.println("Generate DB error:");
            System.out.println(e.getMessage());
        }
    }
}
