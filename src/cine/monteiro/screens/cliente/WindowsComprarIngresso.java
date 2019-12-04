package cine.monteiro.screens.cliente;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import cine.monteiro.dados.CentralDeInformacoes;
import cine.monteiro.dados.Persistencia;
import cine.monteiro.email.Boleto;
import cine.monteiro.gerenciamento.Ingresso;
import cine.monteiro.gerenciamento.Sala;
import cine.monteiro.gerenciamento.Sessao;
import cine.monteiro.imagens.Imagens;
import cine.monteiro.screens.componentes.*;
import cine.monteiro.usuarios.Cliente;
import cine.monteiro.usuarios.Usuario;

public class WindowsComprarIngresso extends Windows {
	// Atributos
	private Usuario cliente;
	private Sala local;
	private Sessao sessao;
	private float total;
	private JLabel lblTotal;
	private JTextField tfQntdIngressos;
	
	// Instâncias	
	private ArrayList<String> assentosReservados = new ArrayList<String>();
	private ArrayList<JButton> assentos = new ArrayList<JButton>();
	
	// Construtor
	public WindowsComprarIngresso(Usuario cliente, Sala local, Sessao sessao) {
		super("Carrinho de Compra", 840, 530);
		this.local = local;
		this.sessao = sessao;
		this.cliente = cliente;
		total = Float.parseFloat(local.getPrecoDoIngresso().split(" ")[1]);
		adicionarImagem();
		adicionarMenuBar();
		adicionarSeparador();
		adicionarLabels();
		adicionarInput();
		adicionarButtons();
		configurarButtons();
		setVisible(true);
	}
	
	private void adicionarImagem() {
		JLabel lblIconeIngresso = new JLabel(Imagens.INGRESSO);
		lblIconeIngresso.setBounds(80, 20, 100, 100);
		add(lblIconeIngresso);
	}
	
	private void adicionarMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("OPÇÕES");
		menuBar.add(menu);
		
		JMenuItem opSair = new JMenuItem("SAIR");
		menu.add(opSair);
	}
	
	private void adicionarSeparador() {
		JSeparator separador = new Separador(250, 5, 2, 455);
		add(separador);
		
		JSeparator separador2 = new JSeparator(SwingConstants.HORIZONTAL);
		separador2.setBounds(5, 330, 240, 2);
		add(separador2);
	}
	
	private void adicionarLabels() {
		JLabel lblEscolhaOAssento = new RotuloTitulo("ESCOLHA SEU ASSENTO", 250, 15, 590, 20);
		add(lblEscolhaOAssento);
		
		JLabel lblTela = new JLabel("TELA");
		Border bordar = BorderFactory.createLineBorder(Color.GRAY, 1);
		lblTela.setOpaque(true);
		lblTela.setBackground(Color.WHITE);
		lblTela.setBorder(bordar);
		lblTela.setHorizontalAlignment(JLabel.CENTER);
		lblTela.setBounds(270, 55, 535, 35);
		add(lblTela);
		
		JLabel lblIngresso = new RotuloTitulo("INGRESSO", 0, 115, 250, 20);
		add(lblIngresso);
		
		JLabel lblNomeDoFilme = new RotuloTitulo(sessao.getFilme().getNomeDoFilme(), 0, 160, 250, 30);
		add(lblNomeDoFilme);
		
		JLabel lblLocal = new RotuloDetalhar("Local: " + local.getNomeDaSala(), 20, 200, 150, 20);
		add(lblLocal);
		
		JLabel lblHoraDeInicio = new RotuloDetalhar("Horário: " + sessao.getHoraDeInicio(), 20, 225, 150, 20);
		add(lblHoraDeInicio);
		
		JLabel lblClassificacaoEtaria = new RotuloDetalhar("Classificação Etária: " + sessao.getFilme().getClassificacaoEtaria(), 20, 250, 250, 20);
		add(lblClassificacaoEtaria);
		
		JLabel lblQntdIngressos = new RotuloDetalhar("Qntd. Ingressos:", 20, 282, 150, 20);
		add(lblQntdIngressos);
		
		JLabel lblPrecoDoIngresso = new RotuloDetalhar("Preço do Ingresso: " + local.getPrecoDoIngresso(), 20, 350, 200, 20);
		add(lblPrecoDoIngresso);
		
		lblTotal = new RotuloDetalhar("Total: " + NumberFormat.getCurrencyInstance().format(total), 20, 380, 200, 20);
		lblTotal.setForeground(new Color(0, 184, 148));
		add(lblTotal);
	}
	
	private void adicionarInput() {
		tfQntdIngressos = new Input(135, 280, 30, 30);
		tfQntdIngressos.setText("1");
		tfQntdIngressos.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				char caractere = e.getKeyChar();
				if(!Character.isDigit(caractere)) {
					e.consume();
				}
			}
			
			public void keyReleased(KeyEvent e) {
			
			}
			
			public void keyPressed(KeyEvent e) {
				
			}
		});
		tfQntdIngressos.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
				if(Integer.parseInt(tfQntdIngressos.getText()) > sessao.getVagasDisponiveis()) {
					JOptionPane.showMessageDialog(null, "QUANTIDADE DE INGRESSOS SUPERIOR A QUANTIDADE DE VAGAS DISPONÍVEIS!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
					tfQntdIngressos.setText("1");
				} else {
					tfQntdIngressos.setEditable(false);
					total = Float.parseFloat(local.getPrecoDoIngresso().split(" ")[1]) * Integer.parseInt(tfQntdIngressos.getText());
					lblTotal.setText("Total: " + NumberFormat.getCurrencyInstance().format(total));
					repaint();
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
		add(tfQntdIngressos);
	}
	
	private void adicionarButtons() {
		JButton btnComprar = new ButtonPersonalizado("COMPRAR", 20, 415, 210, 35);
		btnComprar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Persistencia bancoDeInformacoes = new Persistencia();
				CentralDeInformacoes cpd = bancoDeInformacoes.recuperarCentralDeInformacoes();
				
				Ingresso ingresso = new Ingresso(cliente, local, sessao, total, assentosReservados);
				Boleto boleto = new Boleto(ingresso);
				
				try {
					boleto.enviarBoleto();
					JOptionPane.showMessageDialog(null, "INGRESSOS COMPRADO COM SUCESSO. VERIFIQUE SEU E-MAIL!", "ATENÇÃO!", JOptionPane.PLAIN_MESSAGE, Imagens.CONFIRMAR_25x25);
					sessao.setVagasDisponiveis(sessao.getVagasDisponiveis() - assentosReservados.size());
					bancoDeInformacoes.salvarCentralDeInformacoes(cpd);
					dispose();
					new WindowsHomeCliente(cliente);
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "ERRO AO ENVIAR BOLETO!", "ATENÇÃO!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(btnComprar);
		
		JPanel painelAssentos = new JPanel(new GridLayout(5, 8, 10, 10));
		
		int contador = 1;
		for (int l = 0; l < 5; l++) {
			for(int j = 0; j < 8; j++) {
				JButton btnAssento = new ButtonAssento(contador + "");
				btnAssento.addActionListener(new OuvinteBtnAssento(btnAssento));
				assentos.add(btnAssento);
				painelAssentos.add(btnAssento);
				contador++;
			}
		}
		
		JScrollPane container = new JScrollPane(painelAssentos);
		container.setBounds(270, 100, 535, 350);
		container.setBorder(null);
		add(container);
	}
	
	private void configurarButtons() {
		for(int i = 0; i < local.getQuantidadeDeAssentos(); i++) {
			assentos.get(i).setEnabled(true);
		}
	}
	
	// Ouvintes
	private class OuvinteBtnAssento implements ActionListener {
		private JButton btn;
		
		public OuvinteBtnAssento(JButton btn) {
			this.btn = btn;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(assentosReservados.size() < Integer.parseInt(tfQntdIngressos.getText())) {
				for(String assento : assentosReservados) {
					if(assento.equals(btn.getText())) {
						JOptionPane.showMessageDialog(null, "ASSENTO JÁ FOI SELECIONADO!", "ATENÇÃO", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				assentosReservados.add(btn.getText());
				btn.setBackground(new Color(78, 238, 148));
				repaint();
				
				if(assentosReservados.size() == Integer.parseInt(tfQntdIngressos.getText())) {
					for(int i = 0; i < local.getQuantidadeDeAssentos(); i++) {
						for(String assento : assentosReservados) {
							if(!(assento.equals(assentos.get(i).getText()))) {
								assentos.get(i).setEnabled(false);
							}
						}
						
					}
				}
			} 
		}
	}
}
