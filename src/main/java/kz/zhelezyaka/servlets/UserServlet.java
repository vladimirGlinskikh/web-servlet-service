package kz.zhelezyaka.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.zhelezyaka.dto.UserDTO;

import kz.zhelezyaka.service.UserService;
import kz.zhelezyaka.service.UserServiceImpl;


import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(
        name = "UserServlet",
        value = {"/users/*"}
)
public class UserServlet extends HttpServlet {
    private static final String MIME = "application/json";
    private final transient UserService service = new UserServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType(MIME);

        if (pathInfo == null || pathInfo.isEmpty()) {
            List<UserDTO> allUsers = service.getAllUsers();
            String json = mapper.writeValueAsString(allUsers);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);
            try {
                int userId = Integer.parseInt(idString);
                UserDTO user = service.getUserById(userId);
                if (user == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("User not found with id " + userId);
                }
                String json = mapper.writeValueAsString(user);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
            } catch (NumberFormatException e) {
                response.setStatus(SC_BAD_REQUEST);
                response.getWriter().write("ID should be a number.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            response.getWriter().write("Invalid Path");
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        try {
            UserDTO convertedUser = mapper.readValue(request.getReader(), UserDTO.class);

            UserDTO userDTO = service.saveUser(convertedUser);

            String json = mapper.writeValueAsString(userDTO);
            response.setContentType(MIME);
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.getWriter().write("Internal Server Error. Check the server logs for details.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            response.getWriter().write("Invalid Path");
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        UserDTO convertedUser = mapper.readValue(request.getInputStream(), UserDTO.class);

        UserDTO userDTO = service.updateUser(convertedUser);

        String json = mapper.writeValueAsString(userDTO);
        response.setContentType(MIME);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            String idString = pathInfo.substring(1);

            try {
                int userId = Integer.parseInt(idString);
                boolean bool = service.removeUser(userId);

                response.setContentType(MIME);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Successfully removed: " + bool);
            } catch (NumberFormatException e) {
                response.setStatus(SC_BAD_REQUEST);
                response.getWriter().write("Path should contain only numbers.");
            }
        }
    }
}
