package com.howabout.there;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.howabout.there.sign.service.SignInService;
import com.howabout.there.token.JWTFilter;



@SuppressWarnings("deprecation")//사용하지 말아야 할 메소드 관련 경고 억제
@EnableWebSecurity //이게 있어야 Security를 활성화 시킴
@Configuration //Bean등록을 할려면 class에는 Configuration을 해줘야함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { // 2

//  private final UserService userService; // 3
	@Autowired
	private SignInService userService;
	
	@Autowired   
	private JWTFilter jwtfilter;
	
	 @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userService);
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	       return new BCryptPasswordEncoder();
	    }
	    
	    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
	
	    @Override
	    public void configure(WebSecurity web) { // 4 권한 인가 여부 없이 그냥 인증 예외 
	    	web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
	    }

  @Override
  protected void configure(HttpSecurity http) throws Exception { // 5
	  
	  
	  http.csrf().disable().authorizeRequests().antMatchers(
			  "/","/login/**", "/findCourse/**", "/popularCourse/**", "/splash/**", "/mainPage" , "/test/kakao"
			  ,"/myCourse/myCourseSite","/myPage/withdrawal/webwithdrawal", "/myPage/myInfo/webData", "/myPage/myInfo/webDatasetting"
		  
			  ).permitAll() // 누구나 접근 허용
	  .antMatchers().hasRole("USER")
	  .anyRequest().authenticated()
      .and().exceptionHandling().and().sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	  http.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
}

 
}