package cine.monteiro.screens.ouvintes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cine.monteiro.screens.administrador.WindowsPainelDeControle;
import cine.monteiro.screens.componentes.Windows;

public class OuvinteBtnVoltar implements ActionListener {
	private Windows janelaAtual;
	
	public OuvinteBtnVoltar(Windows janelaAtual) {
		this.janelaAtual = janelaAtual;
	}
	
	public void actionPerformed(ActionEvent e) {
		janelaAtual.dispose();
		new WindowsPainelDeControle();
	}
}
