package it.gamma.service.idp.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;

public class MockModel implements Model {
	
	private Map<String, Object> _attributesMap = new HashMap<String, Object>();

	@Override
	public Model addAllAttributes(Collection<?> attributeValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model addAllAttributes(Map<String, ?> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model addAttribute(Object attributeValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public Model addAttribute(String arg0, Object arg1) {
		_attributesMap.put(arg0, arg1);
		return this;
	}

	@Override
	public Map<String, Object> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsAttribute(String attributeName) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getAttribute(String arg0) {
		return _attributesMap.get(arg0);
	}

	@Override
	public Model mergeAttributes(Map<String, ?> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

}
