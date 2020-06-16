package com.crud.controlecondominio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.crud.controlecondominio.model.StatusTitulo;
import com.crud.controlecondominio.model.Titulo;
import com.crud.controlecondominio.repository.Titulos;
import com.crud.controlecondominio.repository.filter.TituloFilter;

@Service
public class CadastroTituloServices {

	@Autowired
	private Titulos titulos;

	public void salvar(Titulo titulo) {
		try {
			titulos.save(titulo);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}
	
	public void excluir(Long codigo) {
		titulos.deleteById(codigo);
	}

	public String receber(Long codigo) {
		
		Titulo titulo = titulos.findById(codigo).get();
		titulo.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(titulo);
		return StatusTitulo.RECEBIDO.getDescricao();
	}
	
	public Titulo editar(Long codigo) {
		return titulos.findById(codigo).get();
	}
	
	public List<Titulo> filtrar(TituloFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return titulos.findByDescricaoContaining(descricao);
	}
}
