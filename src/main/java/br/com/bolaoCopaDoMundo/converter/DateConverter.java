package br.com.bolaoCopaDoMundo.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converter para valores do tipo <b>java.util.Date</b>.
 *
 */
public class DateConverter implements Converter {

	public static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


	/**
	 * Converte uma String no formato dd/MM/yyyy para um objeto Date.
	 */
	@Override
	public Object getAsObject(FacesContext fc, UIComponent ui, String value) {

		try {
			return (value==null||"".equals(value)||"  /  /    ".equals(value))?null:df.parse((String)value);
		} catch (Exception e) {
			throw new ConverterException("Erro na convers�o da data: " + value);
		}
	}


	/**
	 * Converte um  objeto Date para uma String no formato dd/MM/yyyy.
	 */
	@Override
	public String getAsString(FacesContext fc, UIComponent ui, Object value) {

		if(value == null || "".equals(value))
			return null;

		if(!(value instanceof Date )){
			throw new ConverterException(value + "deve ser do tipo java.util.Date" );
		}

		return df.format(value);
	}

}
