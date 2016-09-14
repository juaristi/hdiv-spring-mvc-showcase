package org.hdiv.samples.mvc.controllers;

import eu.swept.wifc.Wifc;
import org.hdiv.session.ISession;
import org.hdiv.state.IState;
import org.hdiv.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by aja on 13/09/16.
 */
@RestController
public class HdivQueryController {
	@Autowired
	private ServletContext context;

	@RequestMapping(value="/hdiv-query")
	public HttpEntity<String> hdivQuery(HttpServletRequest request) {
		String body;

		ISession session = WebApplicationContextUtils.getRequiredWebApplicationContext(this.context)
			.getBean(ISession.class);

		String hdivState = null;
		Object hdivStateParamName = request.getSession().getAttribute(Constants.HDIV_PARAMETER);
		if (hdivStateParamName != null)
			hdivState = request.getParameter((String) hdivStateParamName);

		String xml = (hdivState == null ?
			null :
			generateXml(session, hdivState));

		HttpHeaders headers = new HttpHeaders();
		if (xml == null) {
			body = "null";
			headers.setContentType(MediaType.TEXT_PLAIN);
		} else {
			body = xml;
			headers.setContentType(MediaType.APPLICATION_XML);
		}

		return new HttpEntity<>(body, headers);
	}

	private String generateXml(ISession session, String hdivState) {
		Wifc wifc = new Wifc();
		return wifc.generateWifcResponse(session, hdivState);
	}
}
