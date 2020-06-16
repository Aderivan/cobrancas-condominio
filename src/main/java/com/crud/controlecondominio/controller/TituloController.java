package com.crud.controlecondominio.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crud.controlecondominio.model.StatusTitulo;
import com.crud.controlecondominio.model.Titulo;
import com.crud.controlecondominio.repository.filter.TituloFilter;
import com.crud.controlecondominio.service.CadastroTituloServices;

@Validated
@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	private static final String CADASTRO_VIEW = "CadastroTitulo";
	
	@Autowired
	private CadastroTituloServices cadastroTituloServices;
	
	@RequestMapping("/novo")
	public ModelAndView novo() {
		
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		return mv;
	}
	
	@RequestMapping()
	public ModelAndView pesquisa(@ModelAttribute("filtro") TituloFilter filtro) {
		
		List<Titulo> todosTitulos = cadastroTituloServices.filtrar(filtro);
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, BindingResult result, RedirectAttributes attributes) {				
		
		if(result.hasErrors()) {
			
			return CADASTRO_VIEW;
		}
		
		try {
			cadastroTituloServices.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Título cadastrado com sucesso!");
			
			return "redirect:/titulos/novo";
			
		} catch (IllegalArgumentException e) {
			result.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable Long codigo) {
		
		Titulo titulo = cadastroTituloServices.editar(codigo);
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
		
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		cadastroTituloServices.excluir(codigo);
		
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso");
		
		return "redirect:/titulos";

	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		
		return cadastroTituloServices.receber(codigo);
	}
	
	/*
	 * Deixando o status dinâmico no front com thymeleaf.
	 * Assim, caso seja cadastrado um novo satus, dinamicamente, independente da url que ele esteja sendo chamado
	 * irá aparecer.
	 * */
	@ModelAttribute("todosStatusTitulos")
	public List<StatusTitulo> todosStatusTitulos() {
		return Arrays.asList(StatusTitulo.values());
	}

}
