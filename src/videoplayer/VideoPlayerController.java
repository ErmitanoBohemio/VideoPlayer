/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

//Fig. 27.9: Using Media, MediaPlayer and MediaView to play a video
package videoplayer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
/**
 * FXML Controller class
 *
 * @author OscarFabianHP
 */
public class VideoPlayerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private MediaView mediaView;
    @FXML private Button playPauseButton;
    private MediaPlayer mediaPlayer;
    private boolean playing = false;

    @Override
    public void initialize(URL url2, ResourceBundle rb) {
        // TODO
        
        //create a Class object representing the videoPlayerController class, 
        //this is equivalent to calling inherited method getClass()
        //get URL of the video file
        URL url = VideoPlayerController.class.getResource("videoNASA.mp4");
        
        //create a Media object for the specified URL
        Media media = new Media(url.toExternalForm()); //el contructor Media lanza excepciones si no se encunetra el archivo, formato no soportado, etc
        
        //Para cargar video se asocia a un MediaPlayer, reproducir multiples videos requiere un MediaPlayer 
        //para cada objeto Media, sin embargo un objecto Media puede ser asociado con multiples MediaPlayer
        //create a MediaPlayer to control to control Media playback
        mediaPlayer = new MediaPlayer(media); //lanza excepciones tipo NullPointerException si el Media es null o MediaException si ocurre problema durante construcion del objecto MediaPlayer
        //a MediaPlayer transition through various states. some common MediaPlayer states include:
        //ready--The media is loaded and ready to play
        //playing--The media is currently playing
        //paused--The media was playing and is now paused
        //for these and other states, you can register Runnable's that execute as the MediaPlayer enters
        //the corresponding state
        
        //MediaPlayer no proporciona un visor donde mostrar video, por eso se requiere el MediaView
        //MediaPlayer es como culauier nodo y permite aplicarle estilos CSS, transform and animations
        //specified which MediaPlayer to display in the MediaView
        mediaView.setMediaPlayer(mediaPlayer);
        
        //set handler to be called when the video completes playing
        mediaPlayer.setOnEndOfMedia(() -> { //passing a lambda expression that represents a Runnable to execute when video playback complete
            playing=false; //se usa para indicar q el usuario puede clickear boton para reproducir de nuevo
            mediaPlayer.stop(); //para el video para poder darle play de nuevo
            playPauseButton.setText("Play");
        });
        
        //set handle that displays an ExceptionDialog if an error occurs
        mediaPlayer.setOnError(new Runnable() {
            @Override
            public void run() {
                /*
                //ExceptionDialog is one of many additional Java FX controls available through the open source project ControlFX at http://fxexperience.com/controlsfx
                //you can download the project JAR file from this web page, then add it to your project on project's Libraries node and Add JAR/FOLDE...
                ExceptionDialog dialog = new ExceptionDialog(mediaPlayer.getError());
                dialog.showAndWait();
                */
                Alert alert = new Alert(Alert.AlertType.ERROR);     
                alert.setTitle("Error Dialog");
                alert.setContentText(mediaPlayer.getError().getMessage());
                alert.showAndWait(); //muestra el mensaje en un dialogo q el usuario debe cerrar para continuar
            }
        });
        
        //bind the MediaView's width /height to the scene's width/height
        //so that the MediaView resize with app's windows
        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width")); //enlaza el ancho del mediaView con el ancho (width) de la Scene (Ventana).
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
    }

    @FXML
    private void playPauseButtonPressed(ActionEvent event){
        playing = !playing; //se cambia estado de playing de true->false o false->true
        
        if(playing){
            playPauseButton.setText("Pause");
            mediaPlayer.play(); //reproduce media
        }
        else{
            playPauseButton.setText("Play");
            mediaPlayer.pause(); //pausa media
        }
    }
    
}
