package org.jugvale.sorteioapp;
/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the
 * template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.jugvale.sorteioapp.service.SorteioAppService;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
// TODO: Refatorar chamadas de métodos duplicadas...

/**
 *
 * @author william
 */
public class SorteaProjeto2 extends Application {


	// ARQUIVOS CARREGADOS
	final String AUDIO_URL = getClass().getResource("/audio/ssantos.mp3").toString();
	final ImageView IMG_PARAR = new ImageView(new Image("/icones/parar.png"));
	final ImageView IMG_INFO = new ImageView(new Image("/icones/info.png"));
	final ImageView IMG_TOCAR = new ImageView(new Image("/icones/tocar.png"));
	final ImageView IMG_LIMPAR = new ImageView(new Image("/icones/limpar.png"));
	final String IMG_FUNDO = getClass().getResource("/logo.jpg").toString();
	
	// CONFIGURAÇÁO APP
	final int ALTURA_APP = 600;
	final int LARGURA_APP = 800;
	final int FONTE_SORTEIO = 410;
	final int MAX_INICIAL = 150;
	final String INICIAL = "#";
	final String TITULO = "Sorteador";
	
	int maxSorteio = MAX_INICIAL;
	
	// pode ser modificado para sortear qq outra coisa que não seja número...
	List<Object> paraSortear = geraListaNumericaSorteio(maxSorteio);
	List<String> todasSorteadas = new ArrayList<>();
	Random geradorNumeros = new Random();
	SimpleStringProperty textoSorteado = new SimpleStringProperty(INICIAL);
	int i = 0;
	
	// "engine" de sorteio
	AnimationTimer timerPrincipal;
	
	// indica se está tocando a nossa "engine"
	SimpleBooleanProperty tocando = new SimpleBooleanProperty(false);
	SimpleBooleanProperty listaVazia = new SimpleBooleanProperty(false);
	
	// música do Sílvio Santos
	AudioClip clip = new AudioClip(AUDIO_URL);
	
	// elementos de interface
	final BorderPane raiz = new BorderPane();
	final GridPane pnlDetalhes = new GridPane();
	final Label txtSorteado = new Label();
	final Text txtSorteadas = new Text();
	final Text txtParaSortear = new Text(coletaParaSortear());
	final ToggleButton btnDetalhes = new ToggleButton("", IMG_INFO);
	final Button btnComecar = new Button("", IMG_TOCAR);
	final Button btnParar = new Button("", IMG_PARAR);
	final Button btnLimpar = new Button("", IMG_LIMPAR);
	final Button btnGerar = new Button("Gerar");
	final Button btnParticipantes = new Button("Participantes");
	final Slider sldFonte = new Slider();
	final HBox caixaBaixoEsquerda = new HBox(10);
	final HBox caixaBaixoDireita = new HBox(10);
	final HBox caixaCima = new HBox(10);
	final ImageView imgFundo = new ImageView(IMG_FUNDO);
	final ListView<Object> lstSorteados = new ListView<>();
	final ListView<Object> lstParaSortear = new ListView<>();
	private FadeTransition animacaoAposSorteio;
	private SorteioAppService appService;
	
	/**
	 * Dados vindo do SPI (serviço plugado)
	 */
	List<Object> dadosExternos;
	
	public static void main( final String[] args ) {
		launch(args);
	}

	@Override
	public void start( final Stage stage ) {
		appService = SorteioAppService.Factory.get();
		configuraLayout();
		configuraEngine();
		configuraListeners();

		// toca a música pra sempre
		clip.setCycleCount(-1);


		stage.setScene(new Scene(new StackPane(imgFundo, raiz)));
		stage.setTitle(TITULO);
		stage.setWidth(LARGURA_APP);
		stage.setHeight(ALTURA_APP);
		stage.show();
	}

	private void configuraLayout() {
		pnlDetalhes.visibleProperty().bind(btnDetalhes.selectedProperty());
		pnlDetalhes.add(new Label("Itens para sortear"), 0, 0);
		pnlDetalhes.add(lstParaSortear, 0, 1);
		pnlDetalhes.add(new Label("Itens sorteados"), 1, 0);
		pnlDetalhes.add(lstSorteados, 1, 1);		
		pnlDetalhes.setVgap(10);
		pnlDetalhes.setHgap(30);
		pnlDetalhes.setAlignment(Pos.CENTER);
		
		txtSorteado.setFont(new Font(FONTE_SORTEIO));
		txtSorteado.textProperty().bind(textoSorteado);
		txtSorteado.setWrapText(true);
		txtSorteado.setTextAlignment(TextAlignment.CENTER);
		txtSorteado.setEffect(new InnerShadow(100, Color.BLACK));
		txtSorteado.setTextFill(Color.YELLOW);
		caixaCima.getChildren().addAll(txtParaSortear, txtSorteadas);
		caixaBaixoEsquerda.getChildren().addAll(btnComecar, btnParar, btnDetalhes, btnLimpar);
		caixaBaixoDireita.getChildren().addAll(new Label("Fonte: "), sldFonte, new Label("Dados: "), btnGerar, btnParticipantes);
		caixaBaixoEsquerda.setAlignment(Pos.CENTER);
		caixaBaixoDireita.setAlignment(Pos.BOTTOM_LEFT);
		raiz.setTop(caixaCima);
		raiz.setCenter(new StackPane(txtSorteado, pnlDetalhes));
		raiz.setBottom(new HBox(180, caixaBaixoEsquerda, caixaBaixoDireita));
		
		// configura o slider da fonte
		sldFonte.setMin(30);
		sldFonte.setMaxWidth(80);
		sldFonte.setMax(FONTE_SORTEIO);
		sldFonte.setValue(FONTE_SORTEIO);

		// botões só estão habilitados de acordo com a timeline e com a lista
		btnComecar.disableProperty().bind(tocando.or(listaVazia));
		btnLimpar.disableProperty().bind(tocando);
		btnParar.disableProperty().bind(tocando.not());
		
		imgFundo.setFitHeight(ALTURA_APP);
		imgFundo.setFitWidth(LARGURA_APP);
		imgFundo.setOpacity(0.1);
		imgFundo.setEffect(new SepiaTone(0.2));
	}
	
	private void configuraEngine() {
		animacaoAposSorteio = new FadeTransition(new Duration(100));
		animacaoAposSorteio.setAutoReverse(true);
		animacaoAposSorteio.setCycleCount(10);
		animacaoAposSorteio.setFromValue(1);
		animacaoAposSorteio.setToValue(0);
		animacaoAposSorteio.setNode(txtSorteado);
	}

	private void configuraListeners() {
		sldFonte.valueProperty().addListener((o, v, n) -> {
			txtSorteado.setFont(Font.font(n.doubleValue()));
		});
		
		btnDetalhes.selectedProperty().addListener((o, v, n) -> {
			if(n) {
				atualizaListas();
			}
		});
		
		btnComecar.setOnAction(e -> {
			timerPrincipal.start();
			tocando.set(true);
			clip.play();
		});
		// muito código aqui... melhorar
		btnParar.setOnAction(e -> {
			timerPrincipal.stop();
			tocando.set(false);
			clip.stop();
			final String textoSorteadoAtual = txtSorteado.getText();
			// remove o valor já sorteado dos números para sortear
			for (int i = 0; i < paraSortear.size(); i++) {
				if(paraSortear.get(i).toString().equals(textoSorteadoAtual)) {
					paraSortear.remove(i);
					break;
				}
			}
		//	paraSortear.remove(textoSorteadoAtual);
			// atualiza a lista de textos sorteados
			todasSorteadas.add(textoSorteadoAtual);
			// atualiza os textos da tela com valores para sortear e já sorteados
			txtSorteadas.setText(coletaSorteados());
			txtParaSortear.setText(coletaParaSortear());
			// se a lista esvaziou, configura nosso booleano, aí desabilita o botão de start
			listaVazia.set(paraSortear.isEmpty());
			// toca animação pós efeito
			animacaoAposSorteio.play();
			atualizaListas();
		});
		
		EventHandler<ActionEvent> limpar = e -> {
			todasSorteadas.clear();
			txtSorteadas.setText("");
			textoSorteado.set(INICIAL);
			if(dadosExternos == null) {
				paraSortear = geraListaNumericaSorteio(maxSorteio);
			} else {
				paraSortear = new ArrayList<Object>();
				dadosExternos.forEach(paraSortear::add);
			}
			
			listaVazia.set(paraSortear.isEmpty());
			txtParaSortear.setText(coletaParaSortear());
			atualizaListas();
		};
		
		btnGerar.setOnAction(e -> {
			int novoMax  = pedeNovoMaximo();
			if(novoMax != maxSorteio) {
				dadosExternos = null;
				maxSorteio = novoMax;
				limpar.handle(e);
			}
		});
		
		btnParticipantes.setOnAction(e -> {
			if(confirmar("Buscar dados externos?")) {
				// chamada assíncrona?
				List<Object> resultado = appService.buscaValores();
				if(resultado.size() == 0) {
					alerta("Não foram retornados dados do acesso ao serviço!");
				} else {
					dadosExternos = resultado;
					limpar.handle(e);
				}
			}
		});
		
		btnLimpar.setOnAction(e -> {
			if(confirmar("Limpar dados sorteados?")) {
				limpar.handle(e);
			}
			
		});
		// Engine de tudo..
		timerPrincipal = new AnimationTimer() {

			@Override
			public void handle( final long l ) {
				// verifica se a lista está vazia. Se estiver, nada é sorteado...
				if (++i % 2 == 0) {
					final int n = geradorNumeros.nextInt(paraSortear.size());
					final String valor = String.valueOf(paraSortear.get(n));
					textoSorteado.setValue(valor);
				}
			}
		};
	}

	private void atualizaListas() {
		lstParaSortear.getItems().clear();
		paraSortear.forEach(lstParaSortear.getItems()::add);
		lstSorteados.getItems().clear();
		todasSorteadas.forEach(lstSorteados.getItems()::add);
	}

	private List<Object> geraListaNumericaSorteio( final long max ) {
		return LongStream.rangeClosed(1, max).mapToObj(String::valueOf).collect(Collectors.toList());
	}

	private String coletaSorteados() {
		// return todasSorteadas.stream().collect(Collectors.joining(",", "Sorteados: ", ""));
		return "Sorteios realizados: " + todasSorteadas.size();
	}

	private String coletaParaSortear() {
		// return paraSortear.stream().map(String::valueOf).collect(Collectors.joining(",", "Para Sortear: ", ""));
		return "Total para sortear: " + paraSortear.size();
	}
	
	
	private int pedeNovoMaximo() {
		TextInputDialog dialogoNome = new TextInputDialog();
		dialogoNome.setTitle("Gerar dados");
		dialogoNome.setHeaderText("Qual o número máximo para gerar?");
		dialogoNome.setContentText("Máximo: ");
		// se o usuário fornecer um valor, assignamos ao nome
		try {
			return dialogoNome.showAndWait().map(s ->  Integer.parseInt(s)).orElse(MAX_INICIAL);
		} catch (NumberFormatException e) {
			alerta("Valor inválido...");
			return MAX_INICIAL;
		}
	}
	private boolean confirmar(String msg) {
		Alert dialogoConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
		dialogoConfirmacao.setHeaderText(msg);
		dialogoConfirmacao.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
		return dialogoConfirmacao.showAndWait().get() == ButtonType.OK;
	}
	
	private boolean alerta(String msg) {
        Alert dialogoConfirmacao = new Alert(Alert.AlertType.WARNING);
        dialogoConfirmacao.setHeaderText(msg);
        dialogoConfirmacao.getButtonTypes().setAll(ButtonType.OK);
        return dialogoConfirmacao.showAndWait().get() == ButtonType.OK;
	}
}