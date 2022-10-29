package br.com.bolaoCopaDoMundo.util;

import java.io.IOException;
import java.io.Serializable;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class CharacterEncodingFilter implements Filter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest objServletRequest, ServletResponse objServletResponse, FilterChain objFilterChain) throws IOException, ServletException {
	    //todas as paginas voltam a ser UTF-8
		objServletResponse.setCharacterEncoding("UTF-8");
		objServletRequest.setCharacterEncoding("UTF-8");
		objFilterChain.doFilter(objServletRequest, objServletResponse);
	}

	@Override
	public void init(FilterConfig objFilterConfig) throws ServletException {
	}

}