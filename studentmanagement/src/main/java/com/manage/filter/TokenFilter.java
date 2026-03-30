package com.manage.filter;


import com.manage.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.io.IOException;

/**
 * 令牌校验过滤器
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //1. 获取请求url。
        String url = request.getRequestURL().toString();

        //2. 判断请求url中是否包含login，如果包含，说明是登录操作，放行。
        if(url.contains("login")){ //登录请求
            log.info("登录请求 , 直接放行");
            chain.doFilter(request, response);
            return;
        }

        //2.1 判断请求url中是否包含register（注册接口），放行
        if(url.contains("register")){
            log.info("注册请求 , 直接放行");
            chain.doFilter(request, response);
            return;
        }

        //2.2 判断请求url中是否包含checkUsername（用户名检查接口），放行
        if(url.contains("checkUsername")){
            log.info("用户名检查请求 , 直接放行");
            chain.doFilter(request, response);
            return;
        }

        //3. 获取请求头中的令牌（token）。
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
        }

        //4. 判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if((token == null) || token.isEmpty()){ //jwt为空
            log.info("获取到jwt令牌为空, 返回错误结果");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //5. 解析token，如果解析失败，返回错误结果（未登录）。
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败, 返回401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //6. 放行。
        log.info("令牌合法, 放行");
        chain.doFilter(request , response);
    }

}