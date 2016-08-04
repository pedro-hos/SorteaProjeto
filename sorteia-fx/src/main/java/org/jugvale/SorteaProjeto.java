package org.jugvale;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author william
 * @author pedro-hos
 */
public class SorteaProjeto extends Application {
	
	public static void main( final String[] args ) {
		launch(args);
	}
	
	private final String INICIAL = "#";
	
	// TODO: COlocar esse valor na view...
	private final int MAX = 3;
	
	// pode ser modificado para sortear qq outra coisa que não seja número...
	private List<Object> paraSortear = geraListaNumericaSorteio(MAX);
	private final List<String> todasSorteadas = new ArrayList<>();
	private final Random geradorNumeros = new Random();
	private final SimpleStringProperty textoSorteado = new SimpleStringProperty(INICIAL);
	private int i = 0;
	
	// "engine" de sorteio
	private AnimationTimer timerPrincipal;
	
	// indica se está tocando a nossa "engine"
	private final SimpleBooleanProperty tocando = new SimpleBooleanProperty(false);
	private final SimpleBooleanProperty listaVazia = new SimpleBooleanProperty(false);
	
	// música do Sílvio Santos
	private final String AUDIO_URL = getClass().getResource("/ssantos.mp3").toString();
	private final AudioClip clip = new AudioClip(AUDIO_URL);
	
	private final String IMG_FUNDO = getClass().getResource("/logo4.jpg").toString();
	private final int ALTURA_APP = 600;
	private final int LARGURA_APP = 800;
	
	private final BorderPane raiz = new BorderPane();
	private final Text txtSorteado = new Text();
	private final Text txtSorteadas = new Text();
	private final Text txtParaSortear = new Text(coletaParaSortear());
	private final Button btnComecar = new Button("VAI");
	private final Button btnParar = new Button("PARA");
	private final Button btnLimpar = new Button("LIMPA");
	private final HBox caixaBaixo = new HBox(20);
	private final VBox caixaCima = new VBox(10);
	private final ImageView imgFundo = new ImageView(IMG_FUNDO);
	private final FadeTransition animacaoAposSorteio = new FadeTransition(new Duration(100));
	
	@Override
	public void start( final Stage stage ) {
		
		montaTela();
		
		stage.setScene(new Scene(new StackPane(imgFundo, raiz)));
		stage.setTitle("Sorteador");
		stage.setWidth(LARGURA_APP);
		stage.setHeight(ALTURA_APP);
		stage.show();
		
		btnComecar.setOnAction(e -> comecarSorteio());
		btnParar.setOnAction(e -> pararSorteio());
		btnLimpar.setOnAction(e -> limparSorteio());
		
		// Engine de tudo..
		timerPrincipal = new AnimationTimer() {
			
			@Override
			public void handle( final long l ) {
				// verifica se a lista está vazia. Se estiver, nada é sorteado...
				if ((++i % 5) == 0) {
					final int n = geradorNumeros.nextInt(paraSortear.size());
					final String valor = String.valueOf(paraSortear.get(n));
					textoSorteado.setValue(valor);
				}
			}
		};
	}
	
	private void montaTela() {
		
		txtSorteado.setFont(new Font(350));
		txtSorteado.textProperty().bind(textoSorteado);
		txtSorteado.setEffect(new InnerShadow(100, Color.BLACK));
		txtSorteado.setFill(Color.RED);
		
		caixaCima.getChildren().addAll(txtParaSortear, txtSorteadas);
		caixaBaixo.getChildren().addAll(btnComecar, btnParar, btnLimpar);
		raiz.setTop(caixaCima);
		raiz.setCenter(txtSorteado);
		raiz.setBottom(caixaBaixo);
		caixaBaixo.setAlignment(Pos.CENTER);
		
		// botões só estão habilitados de acordo com a timeline e com a lista
		btnComecar.disableProperty().bind(tocando.or(listaVazia));
		btnLimpar.disableProperty().bind(tocando);
		btnParar.disableProperty().bind(tocando.not());
		
		// toca a música pra sempre
		clip.setCycleCount(-1);
		
		imgFundo.setFitHeight(ALTURA_APP);
		imgFundo.setFitWidth(LARGURA_APP);
		imgFundo.setOpacity(0.4);
		imgFundo.setEffect(new SepiaTone(0.5));
		
		animacaoAposSorteio.setAutoReverse(true);
		animacaoAposSorteio.setCycleCount(10);
		animacaoAposSorteio.setFromValue(1);
		animacaoAposSorteio.setToValue(0);
		animacaoAposSorteio.setNode(txtSorteado);
		
	}
	
	private void comecarSorteio() {
		timerPrincipal.start();
		tocando.set(true);
		clip.play();
	}
	
	private void limparSorteio() {
		todasSorteadas.clear();
		txtSorteadas.setText("");
		textoSorteado.set(INICIAL);
		paraSortear = geraListaNumericaSorteio(MAX);
		listaVazia.set(paraSortear.isEmpty());
		txtParaSortear.setText(coletaParaSortear());
	}
	
	private void pararSorteio() {
		timerPrincipal.stop();
		tocando.set(false);
		clip.stop();
		
		final String textoSorteadoAtual = txtSorteado.getText();
		
		// remove o valor já sorteado dos números para sortear
		paraSortear.remove(textoSorteadoAtual);
		
		// atualiza a lista de textos sorteados
		todasSorteadas.add(textoSorteadoAtual);
		
		// atualiza os textos da tela com valores para sortear e já sorteados
		txtSorteadas.setText(coletaSorteados());
		
		txtParaSortear.setText(coletaParaSortear());
		
		// se a lista esvaziou, configura nosso booleano, aí desabilita o botão de start
		listaVazia.set(paraSortear.isEmpty());
		
		// toca animação pós efeito
		animacaoAposSorteio.play();
	}
	
	private List<Object> geraListaNumericaSorteio( final long max ) {
		return LongStream.rangeClosed(1, max).mapToObj(String::valueOf).collect(Collectors.toList());
	}
	
	private String coletaSorteados() {
		return todasSorteadas.stream().collect(Collectors.joining(",", "Sorteados: ", ""));
	}
	
	private String coletaParaSortear() {
		return paraSortear.stream().map(String::valueOf).collect(Collectors.joining(",", "Para Sortear: ", ""));
	}
}