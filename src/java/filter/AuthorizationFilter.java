//package filter;
//
//import java.io.IOException;
//import java.util.List;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import model.Users;
//import dal.UserDAO;
//
//public class AuthorizationFilter implements Filter {
//    private static final String LOGIN_PAGE = "/login.jsp";
//    private static final String UNAUTHORIZED_PAGE = "/unauthorized.jsp";
//    private UserDAO userDAO;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        userDAO = new UserDAO(); // Khởi tạo đối tượng UserDAO
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        HttpSession session = httpRequest.getSession(false);
//
//        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
//        String loginURI = httpRequest.getContextPath() + LOGIN_PAGE;
//        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
//        boolean isLoginPage = httpRequest.getRequestURI().endsWith(LOGIN_PAGE);
//
//        if (isLoggedIn && (isLoginRequest || isLoginPage)) {
//            // User is already logged in and trying to access login page, redirect to home
//            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
//        } else if (!isLoggedIn && isProtectedResource(httpRequest)) {
//            // User not logged in and trying to access a protected page, redirect to login
//            httpResponse.sendRedirect(loginURI);
//        } else if (isLoggedIn) {
//            // User is logged in, check for role-based access
//            Users user = (Users) session.getAttribute("user");
//            List<String> roles = userDAO.getUserRoles(user.getUserID());
//            if (isAuthorized(httpRequest, roles)) {
//                chain.doFilter(request, response);
//            } else {
//                httpResponse.sendRedirect(httpRequest.getContextPath() + UNAUTHORIZED_PAGE);
//            }
//        } else {
//            // For other cases, continue the filter chain
//            chain.doFilter(request, response);
//        }
//    }
//
//    private boolean isProtectedResource(HttpServletRequest request) {
//        // Define logic to determine if a resource requires authentication
//        String path = request.getRequestURI().substring(request.getContextPath().length());
//        return !path.startsWith("/public/") && !path.equals(LOGIN_PAGE);
//    }
//
//    private boolean isAuthorized(HttpServletRequest request, List<String> roles) {
//        String path = request.getRequestURI().substring(request.getContextPath().length());
//        if (path.startsWith("/admin/")) {
//            return roles.contains("Administrator");
//        } else if (path.startsWith("/manager/")) {
//            return roles.contains("Manager");
//        } else if (path.startsWith("/staff/")) {
//            return roles.contains("Doctor") || roles.contains("Nurse");
//        }
//        return true; // Allow access to other resources
//    }
//
//    @Override
//    public void destroy() {
//    }
//}