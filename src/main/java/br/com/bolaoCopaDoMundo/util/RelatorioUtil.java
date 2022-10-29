package br.com.bolaoCopaDoMundo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class RelatorioUtil {
	
	static Logger logger = Logger.getLogger(RelatorioUtil.class);
	

	public String relatorio(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo) throws Exception {  

		// pegando o servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		// pegando o ds do spring
		String springVar = "org.springframework.web.context.WebApplicationContext.ROOT";
		WebApplicationContext wctx = (WebApplicationContext) servletContext.getAttribute(springVar);
		DataSource dataSource = (DataSource) wctx.getBean("dataSource");
		System.out.println(springVar);
		System.out.println(dataSource.getConnection());
		String logo = servletContext.getRealPath("//img/" + "logotst.png");
		String logo_tce = servletContext.getRealPath("//img/" + "logotst.png");
		String back = servletContext.getRealPath("//img/" + "bg-topo.png");
		String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/" + arquivoRelatorio);  

		//parametros  
		parametros.put("LOGO", logo);
		parametros.put("LOGO_TCE", logo_tce);
		parametros.put("BACK", back);

		// Fill the report using an empty data source
		JasperPrint print;

		print = JasperFillManager.fillReport(pathRel, parametros, dataSource.getConnection());
		byte[] bytes = JasperExportManager.exportReportToPdf(print);
		
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);
		System.out.println(nomeArquivo);
		
		FileOutputStream fileOutputStream = new FileOutputStream(new File(servletContext.getRealPath("//WEB-INF/relatorios/classificacao.pdf")));  
        fileOutputStream.write(bytes);  
        fileOutputStream.flush();             // &lt;&lt;--- ATENÇÃO AQUI 
        fileOutputStream.close(); 
		return null;
	}
	
	protected void writeBytesAsAttachedTextFile(byte[] bytes, String fileName) throws Exception {  

		if (bytes == null)  
			throw new Exception("Array de bytes nulo.");  

		if (fileName == null)  
			throw new Exception("Nome do arquivo � nulo.");  

		FacesContext facesContext = FacesContext.getCurrentInstance();  
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();  
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();

	}  
	
	public String getRelatorio() {
		// pegando o servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
				
		return servletContext.getRealPath("//WEB-INF/relatorios/classificacao.pdf");
	}

}