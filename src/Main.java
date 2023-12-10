import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static Connection connect() {
        String url = "jdbc:sqlite:C:\\Users\\chaal\\labb3";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printMenu() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - Stäng av\n" +
                "1  - Visa alla länder\n" +
                "2  - Lägga till nytt land\n" +
                "3  - Uppdatera ett land\n" +
                "4  - Ta bort ett land\n" +
                "5  - Visa huvudstäder\n" +
                "6  - Visa en lista över alla val\n"
        );
    }

    public static void main(String[] args) {

        boolean quit = false;
        printMenu();
        while(!quit) {
            System.out.println("\nVälj 6. för att visa val:");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0 -> {
                    System.out.println("\nStänger ner.");
                    quit = true;
                }
                case 1 -> allCountries();
                case 2 -> addCountry("Spain", "Spanish", 47519628);
                case 3 -> updateCountry("France", "French", 65000000, 1);
                case 4 -> deleteCountry();
                case 5 -> innerJoin();
                case 6 -> printMenu();
            }


        }

    }


    private static void deleteCountry(){
        System.out.println("Skriv in id:t på landet som ska tas bort: ");
        int inputId = scanner.nextInt();
        delete(inputId);
        scanner.nextLine();
    }

    private static void allCountries(){
        String sql = "SELECT * FROM Countries";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("countriesId") +  "\t" +
                        rs.getString("countriesName") + "\t" +
                        rs.getString("language") + "\t" +
                        rs.getString("population"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addCountry(String lander, String sprak, int invanare) {
        String sql = "INSERT INTO Countries(countriesName, language, population) VALUES(?,?,?)";

        try{
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lander);
            pstmt.setString(2, sprak);
            pstmt.setInt(3, invanare);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till Spanien (Spain)!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void updateCountry(String lander, String sprak, int invanare, int id) {
        String sql = "UPDATE Countries SET countriesName = ? , "
                + "language = ? , "
                + "population = ? "
                + "WHERE countriesId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, lander);
            pstmt.setString(2, sprak);
            pstmt.setInt(3, invanare);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
            System.out.println("Du har uppdaterat Frankrike (France)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void delete(int id) {
        String sql = "DELETE FROM Countries WHERE countriesId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Du har tagit bort Spanien (Spain)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void innerJoin() {
        String sql = "SELECT Countries.countriesName, Countries.language, Countries.population, " +
                "Capitals.capitalsName, Capitals.landmark " +
                "FROM Countries " +
                "INNER JOIN Capitals ON Countries.countriesId = Capitals.capitalsCountriesId";


        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            System.out.printf("%-20s %-15s %-15s %-20s %-20s\n", "Countries", "Language", "Population", "Capital", "Landmark");
            System.out.println("---------------------------------------------------------------------------------------------");

            while  (rs.next()) {
                System.out.printf("%-20s %-15s %-15s %-20s %-20s\n",
                        rs.getString("countriesName"),
                        rs.getString("language"),
                        rs.getInt("population"),
                        rs.getString("capitalsName"),
                        rs.getString("landmark"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in innerJoin: " + e.getMessage());
        }
    }

}

