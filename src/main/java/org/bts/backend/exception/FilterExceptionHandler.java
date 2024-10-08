package org.bts.backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bts.backend.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class FilterExceptionHandler extends OncePerRequestFilter {
    private final ObjectMapper ObjectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException{
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            log.error("FilterExceptionHandler: " + e.getResponse());
            setErrorResponse(response, e.getResponse());
        }catch (Exception e) {
            log.error("FilterExceptionHandler: " + "필터 예외가 발생했습니다.");
            ApiResponse<?> err = ApiResponse.fail("필터 내부의 예외가 발생했습니다.");
            setErrorResponse(response, err);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ApiResponse<?> err) throws IOException {
        response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(ObjectMapper.writeValueAsString(err));
    }
}
