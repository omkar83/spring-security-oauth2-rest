package org.ateam.common.controller;

import java.util.List;
import java.util.Map;

import org.ateam.common.model.AnnuityInfo;
import org.ateam.common.model.Person;
import org.ateam.common.repository.OauthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class JSONController {
	
	@Autowired
	private OauthRepository oauthRepository;

	@RequestMapping(value="{name}", method = RequestMethod.GET)
	public @ResponseBody Person getPersonInJSON(@PathVariable String name) {

		Person person = new Person();
		person.setName(name);
		
		return person;
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public @ResponseBody Person getAnnuityInfo() {

		Person person = new Person();
		person.setName("OmkAR");
		
		return person;
	}
	
	@RequestMapping(value="/info", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getAnnuityInfoDetails() {
		List<Map<String, Object>> info = oauthRepository.getByAnnuityInfo();
		
		return info;
	}
	
	
	
}
