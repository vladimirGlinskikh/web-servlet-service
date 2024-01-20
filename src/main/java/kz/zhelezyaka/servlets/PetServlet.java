package kz.zhelezyaka.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.service.PetService;
import kz.zhelezyaka.service.PetServiceImpl;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(
        name = "PetServlet",
        value = {"/pets/*"}
)
public class PetServlet extends HttpServlet {
    private static final String MIME = "application/json";

    private final transient PetService service = new PetServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType(MIME);

        if (pathInfo == null || pathInfo.isEmpty()) {
            List<PetDTO> allPets = service.getAllPets();
            String json = mapper.writeValueAsString(allPets);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);
            try {
                int petId = Integer.parseInt(idString);
                PetDTO pet = service.getPetById(petId);
                if (pet == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Pet not found with id " + petId);
                }
                String json = mapper.writeValueAsString(pet);
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
            PetDTO convertedPet = mapper.readValue(request.getReader(), PetDTO.class);

            PetDTO petDTO = service.savePet(convertedPet);

            String json = mapper.writeValueAsString(petDTO);
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
        PetDTO convertedPet = mapper.readValue(request.getInputStream(), PetDTO.class);

        PetDTO petDTO = service.updatePet(convertedPet);

        String json = mapper.writeValueAsString(petDTO);
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
                int petId = Integer.parseInt(idString);
                boolean bool = service.removePet(petId);

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
