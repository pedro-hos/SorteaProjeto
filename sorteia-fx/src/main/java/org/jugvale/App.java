/**
 *
 */
package org.jugvale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.jugvale.model.impl.Inscricao;
import org.jugvale.service.ParticipanteService;

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
 * @author pedro-hos
 *
 */
public class App extends Application {

	public List<Object> paraSortear = coletaParticipantes();
	public List<String> todasSorteadas = new ArrayList<>();
	public int MAX = 3;

	private final String INICIAL = "#";
	private final SimpleStringProperty textoSorteado = new SimpleStringProperty(INICIAL);

	// indica se está tocando a nossa "engine"
	private final SimpleBooleanProperty tocando = new SimpleBooleanProperty(false);
	private final SimpleBooleanProperty listaVazia = new SimpleBooleanProperty(false);

	private final String AUDIO_URL = getClass().getResource("/ssantos.mp3").toString();
	private final String IMG_FUNDO = getClass().getResource("/logo4.jpg").toString();
	private final int ALTURA_APP = 600;
	private final int LARGURA_APP = 800;

	private BorderPane raiz = null;
	private Text txtSorteado = null;
	private Text txtSorteadas = null;
	private Button btnComecar = null;
	private Button btnParar = null;
	private Button btnLimpar = null;
	private HBox caixaBaixo = null;
	private VBox caixaCima = null;
	private ImageView imgFundo = null;
	private AudioClip clip = null;
	private FadeTransition animacaoAposSorteio = null;

	private final Random geradorNumeros = new Random();
	private int i = 0;

	// "engine" de sorteio
	private AnimationTimer timerPrincipal;

	@Override
	public void start(final Stage stage) throws Exception {

		iniciaView();
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
			public void handle(final long l) {
				sorteia();
			}

			private void sorteia() {
				if (++i % 5 == 0) {
					final int n = geradorNumeros.nextInt(paraSortear.size());
					final String valor = String.valueOf(paraSortear.get(n));
					textoSorteado.setValue(valor);
				}
			}
		};

	}

	private void limparSorteio() {
		todasSorteadas.clear();
		txtSorteadas.setText("");
		textoSorteado.set(INICIAL);

		paraSortear = coletaParticipantes();

		listaVazia.set(paraSortear.isEmpty());

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

		// atualiza os textos da tela com valores para sortear e já
		// sorteados
		txtSorteadas.setText(coletaSorteados());

		// se a lista esvaziou, configura nosso booleano, aí desabilita o
		// botão de start
		listaVazia.set(paraSortear.isEmpty());

		// toca animação pós efeito
		animacaoAposSorteio.play();
	}

	private void comecarSorteio() {
		timerPrincipal.start();
		tocando.set(true);
		clip.play();
	}

	private void iniciaView() {

		raiz = new BorderPane();
		txtSorteado = new Text();
		txtSorteadas = new Text();

		btnComecar = new Button("VAI");
		btnParar = new Button("PARA");
		btnLimpar = new Button("LIMPA");
		caixaBaixo = new HBox(20);
		caixaCima = new VBox(10);
		imgFundo = new ImageView(IMG_FUNDO);
		clip = new AudioClip(AUDIO_URL);
		animacaoAposSorteio = new FadeTransition(new Duration(100));
	}

	private void montaTela() {

		txtSorteado.setFont(new Font(350));
		txtSorteado.textProperty().bind(textoSorteado);
		txtSorteado.setEffect(new InnerShadow(100, Color.BLACK));
		txtSorteado.setFill(Color.RED);

		caixaCima.getChildren().addAll(txtSorteadas);

		caixaBaixo.getChildren().addAll(btnComecar, btnParar, btnLimpar);
		caixaBaixo.setAlignment(Pos.CENTER);

		raiz.setTop(caixaCima);
		raiz.setCenter(txtSorteado);
		raiz.setBottom(caixaBaixo);

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

	/*private static List<Object> geraListaNumericaSorteio(final long max) {
		return LongStream.rangeClosed(1, max).mapToObj(String::valueOf).collect(Collectors.toList());
	}*/

	public String coletaSorteados() {
		return todasSorteadas.stream().collect(Collectors.joining(",", "Sorteados: ", ""));
	}

	private static List<Object> coletaParticipantes() {
		final Collection<Inscricao> inscritos = ParticipanteService.getInstance().pegaInscritosPresentesNoEvento("312");
		return inscritos.stream().map(i -> i.getParticipante().getNome()).collect(Collectors.toList());
	}

	public static void main(final String[] args) {
		launch(args);
	}

}
