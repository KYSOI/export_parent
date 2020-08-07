package cn.itcast.web.controller.aspect;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Around(value = "execution(* cn.itcast.web.controller.*.*.*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        //    增强
        SysLog log = new SysLog();
        log.setIp(request.getRemoteAddr());  //ip
        log.setTime(new Date());
        Object obj = session.getAttribute("loginUser");
        if (obj != null) {
            User user = (User) obj;
            log.setUserName(user.getUserName());
            log.setCompanyId(user.getCompanyId());
            log.setCompanyName(user.getCompanyName());
        }
        //    通过反射获取
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
    //    操作的方法名
        log.setMethod(method.getName());
    //    操作的中文描述
        if (method.isAnnotationPresent(RequestMapping.class)) {
        //    有指定注解
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String name = annotation.name();
            log.setAction(name);
        }
        sysLogService.save(log);
        return pjp.proceed();
    }
}
