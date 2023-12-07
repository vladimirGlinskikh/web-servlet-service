package kz.zhelezyaka.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.zhelezyaka.dto.PurchaseDTO;
import kz.zhelezyaka.service.PurchaseService;
import kz.zhelezyaka.service.PurchaseServiceImpl;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(
        name = "PurchaseServlet",
        value = {"/purchase/*"}
)
public class PurchaseServlet extends HttpServlet {
    private final PurchaseService service = new PurchaseServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");

        if (pathInfo == null || pathInfo.isEmpty()) {
            List<PurchaseDTO> allPurchases = service.getAllPurchases();
            String json = mapper.writeValueAsString(allPurchases);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);
            try {
                int purchaseId = Integer.parseInt(idString);
                PurchaseDTO purchase = service.getPurchaseById(purchaseId);
                if (purchase == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Purchase not found with id " + purchaseId);
                }
                String json = mapper.writeValueAsString(purchase);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
            } catch (NumberFormatException e) {
                response.setStatus(SC_BAD_REQUEST);
                response.getWriter().write("ID should be a number.");
            }
        }
    }
}
