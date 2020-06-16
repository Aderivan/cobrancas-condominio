$('#modalExclusao').on('show.bs.modal', function(event) {
	
	let button = $(event.relatedTarget); //quem disparou o evento
	
	let codigoTitulo = button.data('codigo');
	let descricaoTitulo = button.data('descricao');
	
	let modal = $(this);
	let form = modal.find('form')
	let action = form.data('url-base');
	
	if(!action.endsWith('/')) {
		action += '/';
	}
	
	form.attr('action', action + codigoTitulo);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o título <strong>' + descricaoTitulo + '</strong> <br>'
			+ 'de código <strong>' + codigoTitulo + '</strong>');
})

$(function() {
	$('[rel="tooltip"]').tooltip();
	
	$('.js-currency').maskMoney({thousands:'.', decimal:',', allowZero:true});
	
	$('.js-atualizar-status').on('click', function(event) {
		event.preventDefault();
		
		let btnReceber = $(event.currentTarget);
		let urlReceber = btnReceber.attr('href');
		let codigoStatus = btnReceber.data('codigo');
		
		let response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		});
		
		response.done(function(e) {
			$('[data-role=' + codigoStatus + ']').html('<span class="badge badge-success">' + e + '</span>');
			btnReceber.hide();
		});
		
		response.fail(function(e) {
			
			alert('Erro ao receber a cobrança');
		});
	});
});

