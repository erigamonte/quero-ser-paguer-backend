package br.com.pague.desafio.config.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.pague.desafio.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${desafio.jwt.expiration}")
	private String expiration;
	
	@Value("${desafio.jwt.secret}")
	private String secret;
	
	private static final String AUTHORITIES_KEY = "auth";

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		
		String authorities = authentication.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.joining(","));
		
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API do Desafio Pag!")
				.setSubject(logado.getId().toString())
				.claim(AUTHORITIES_KEY, authorities)
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(String token) {
		Claims claims = Jwts.parser()
	            .setSigningKey(this.secret)
	            .parseClaimsJws(token)
	            .getBody();
		
		Collection<? extends GrantedAuthority> authorities =
	            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
	                .map(SimpleGrantedAuthority::new)
	                .collect(Collectors.toList());
		
		return authorities;
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
