package edu.supavenir.spanimals.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.supavenir.spanimals.models.Animal;
import edu.supavenir.spanimals.repositories.AnimalRepository;

@Controller
public class TestController {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AnimalRepository aRepo;

	@GetMapping("/form")
	public String formAction() {
		return "form";
	}

	@PostMapping("/submit")
	public String submitAction(@RequestParam Map<String, String> allRequestParams, Model model) {
		List<String> sqlParts = new ArrayList<String>();

		for (Entry<String, String> entry : allRequestParams.entrySet()) {
			if (!entry.getValue().equals("")) {
				sqlParts.add("a." + entry.getKey() + "= :" + entry.getKey());
			}
		}
		String sql = "from ANIMAL a left join ESPECE e on e.id=a.idEspece where " + String.join(" AND ", sqlParts);
		System.out.println(sql);
		Query query = entityManager.createQuery(sql);

		for (Entry<String, String> entry : allRequestParams.entrySet()) {
			if (!entry.getValue().equals("")) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		List<Animal> animals = query.getResultList();
		model.addAttribute("animals", animals);
		return "submit";
	}

	@GetMapping("/add")
	public String addAction() {
		return "add";
	}

	@PostMapping("/add")
	public @ResponseBody String submitAddAction(Animal animal) {
		aRepo.save(animal);
		return animal.toString();
	}
}
