package at.ac.tuwien.big.we16.ue3.controller;

import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.AuthService;
import at.ac.tuwien.big.we16.ue3.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.Exchanger;
import java.util.regex.Pattern;

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void getRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("/");
            return;
        }
        request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
    }

    // TODO validation of user data
    public void postRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /* Create new user from request and verify input data */
        User user = new User();
        /* Set User Balance to 1500 */
        user.setBalance(150000);
        /* Verify Salutation */
        user.setSalutation(request.getParameter("salutation"));
        if(!user.getSalutation().equals("mr") && !user.getSalutation().equals("ms")){
            //response.sendError(1, "Salutation must be mr or ms!\n");
            response.sendRedirect("#salutation-input");
        }
        /* Verify Firstname */
        user.setFirstname(request.getParameter("firstname"));
        if(user.getFirstname().isEmpty()){
            //response.sendError(1, "Firstname cannot be empty!\n");
            response.sendRedirect("#firstname-input");
        }

        /* Verify Lastname */
        user.setLastname(request.getParameter("lastname"));
        if(user.getLastname().isEmpty()){
            //response.sendError(1, "Lastname cannot be empty!\n");
            response.sendRedirect("#lastname-input");
        }

        /* Verify Email */
        user.setEmail(request.getParameter("email"));
        Pattern emailPattern = Pattern.compile("^\\S+@\\S+\\.\\S+$");
        if(!emailPattern.matcher(user.getEmail()).matches()){
            //response.sendError(1, "Invalid email address!\nEmail address format is: email@email.com");
            response.sendRedirect("#email-input");
        }

        /* Verify Password */
        user.setPassword(request.getParameter("password"));
        if(user.getPassword().length() < 4 || user.getPassword().length() > 8){
            //response.sendError(1, "Password length must be between 4 and 8!");
            response.sendRedirect("#password-input");
        }

        /* Verify Date */
        Date selDate = new Date();
        try {
            selDate = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(request.getParameter("dateofbirth"));

            Calendar tempCal = new GregorianCalendar();
            tempCal.set(Calendar.YEAR, tempCal.get(Calendar.YEAR) - 18);

            Date maxDate = new Date().from(tempCal.toInstant());

            if (maxDate.before(selDate)) {
                //response.sendError(1, "You need to be at least 18 years old in order to register!");
                response.sendRedirect("#dateofbirth-input");
            }
        }catch (ParseException e){
            //response.sendError(1, "Invalid Date Format!\n(Please use dd.MM.yyyy)");
            response.sendRedirect("#dateofbirth-input");
        }
        user.setDate(selDate);

        /* User is valid, create and login */
        this.userService.createUser(user);
        this.authService.login(request.getSession(), user);
        response.sendRedirect("/");
    }

}
