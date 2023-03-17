package com.howabout.there.token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.howabout.there.sign.vo.UserVo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTUtil {
	private String secretkey = "howtheresecretkey";
	
	// 이건 뭐하는 것일까 ....
	public String extractUsername(String token) {
		//Claims에서 getSubject를 참조 해라 ::
		return extractClaim(token,Claims::getSubject);
	}
	// claim정보 가져오기  claim=권한
	public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	//token에서 body 정보를 가지고 옴
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
	}
	// 토큰 유효시간 추출
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	// 토큰 유효시간 확인
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	//유저 정보 작성 > jwt body에 들어가게됨
	public String generateToken(UserVo userVo, String user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("u_id", userVo.getU_id());
		claims.put("u_nick", userVo.getU_nick());
		claims.put("gender", userVo.getGender());
		claims.put("birth", userVo.getBirth());
		
		return createToken(claims, user);
	}
	//토큰 빌드
	private String createToken(Map<String, Object>claims, String subject) {
		return Jwts.builder()
				.setHeaderParam("jwt", "HS256")	//header 설정
				.setClaims(claims)				//body 값 넣기
				.setSubject(subject)			//토큰 용도	 > json.toString하여 넣어도됨	
												//토큰 발급시간
				.setIssuedAt(new Date(System.currentTimeMillis()))
												//토큰 만료 시간
				.setExpiration(new Date(System.currentTimeMillis()+1000 * 60 * 60 * 10))
												//해싱알고리즘 + 비밀키 설정
				.signWith(SignatureAlgorithm.HS256, secretkey)
				.compact();						//모든 설정이 끝나면 compact
	}
	
	//토큰 유저 정보 확인 > 이름이 일치하냐 && 토큰유효시간 확인
	public Boolean validateToken(String token, UserDetails userDetails) {
		System.out.println("토큰 유저 확인 && 유효시간 확인 ");
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	
	/** 
	 * Claim에서 각 항목 정보 가져오기
	**/
	// user ID 가지고 오기
	public String getUserIdFromToken(String token) {
		String userId = String.valueOf(extractAllClaims(token).get("u_id"));
		return userId;
	}
	// user NICK 가지고 오기
	public String getUserNickFromToken(String token) {
		String userNick = String.valueOf(extractAllClaims(token).get("u_nick"));
		return userNick;
	}
	// user GENDER 가지고오기
	public String getUserGenderFromToken(String token) {
		String userGender = String.valueOf(extractAllClaims(token).get("gender"));
		return userGender;
	}
	// user BIRTH 가지고 오기
	public String getUserBirthFromToken(String token) {
		String userBirth = String.valueOf(extractAllClaims(token).get("birth"));
		return userBirth;
	}
	
}
