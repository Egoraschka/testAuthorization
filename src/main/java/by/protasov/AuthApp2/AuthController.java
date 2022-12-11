package by.protasov.AuthApp2;
/**
 * @author Егор
 * @version 10.12.2022
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "AuthController", value = "/controller")
public class AuthController extends HttpServlet {
    public static final String URL = "jdbc:postgresql://localhost:5432/user_db";
    public static final String USER = "operator";
    public static final String PASS = "1111";
    public static final String OPTION_PARAMETER = "option";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String LOGIN_PARAMETER = "login";
    public static final String NAME_PARAMETER = "name";
    public static final String EMAIL_PARAMETER = "email";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getMethodByReflection(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getMethodByReflection(request, response);
    }

    private void authorization(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PreparedStatement preparedStatement = DriverManager.getConnection(URL, USER, PASS)
                    .prepareStatement("SELECT login, password, name FROM users WHERE login = ?");
            preparedStatement.setString(1, request.getParameter(LOGIN_PARAMETER));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(PASSWORD_PARAMETER).equals(request.getParameter(PASSWORD_PARAMETER))) {
                    response.getWriter().println("<html><h1>Hello, " + resultSet.getString(NAME_PARAMETER) + "</h1></html>");
                } else {
                    response.getWriter().println("wrong password");
                }
            } else {
                response.getWriter().println("user with this login does not exist");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            response.getWriter().println("error 5");
        }
    }

    private void registration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String validation = checkValidation(request, response);
        if (validation.equals("OK")) {
            try {
                PreparedStatement preparedStatement = DriverManager.getConnection(URL, USER, PASS)
                        .prepareStatement("INSERT INTO users (email, name, login, password) VALUES (?,?,?,?)");
                preparedStatement.setString(1, request.getParameter(EMAIL_PARAMETER));
                preparedStatement.setString(2, request.getParameter(NAME_PARAMETER));
                preparedStatement.setString(3, request.getParameter(LOGIN_PARAMETER));
                preparedStatement.setString(4, request.getParameter(PASSWORD_PARAMETER));
                preparedStatement.execute();
                preparedStatement.close();
                response.getWriter().println("registration completed successfully");
            } catch (SQLException e) {
                response.getWriter().println("error 2");
            }
        } else {
            response.getWriter().println(validation);
        }
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (String login : getLoginList(request, response)) {
            response.getWriter().println(login);
        }
    }

    private void hello(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<html><h1>Hello!</h1></html>");
    }

    private void goodbye(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<html><h1>Goodbye!</h1></html>");
    }

    private String checkValidation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (String[] input : request.getParameterMap().values()) {
            if (input[0].length() < 3 || input[0].length() > 30) {
                return "enter at least 3 and no more than 30 characters in each field";
            }
        }
        if (getLoginList(request, response).contains(request.getParameter(LOGIN_PARAMETER))) {
            return "this login already exist";
        }
        return "OK";
    }

    private List<String> getLoginList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> loginsList = new ArrayList<>();
        try (ResultSet resultSet = DriverManager.getConnection(URL, USER, PASS).createStatement()
                .executeQuery("SELECT login FROM users")
        ) {
            while (resultSet.next()) {
                loginsList.add(resultSet.getString(LOGIN_PARAMETER));
            }
        } catch (SQLException e) {
            response.getWriter().println("error 3");
        }
        return loginsList;
    }

    private void getMethodByReflection(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            getClass().getDeclaredMethod(request.getParameter(OPTION_PARAMETER), HttpServletRequest.class, HttpServletResponse.class)
                    .invoke(this, request, response);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            response.getWriter().println("error 1");
        }
    }
}
