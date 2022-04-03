package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
    GraphicsContext gra ;

    static int[][] map = {
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9},
    }; 

    Image[] imgs = {new Image(new File("bin/img/kusa1.png").toURI().toString()),new Image(new File("bin/img/kusahana.png").toURI().toString())
    ,new Image(new File("bin/img/ki1.png").toURI().toString()),new Image(new File("bin/img/ki2.png").toURI().toString())
    ,new Image(new File("bin/img/mizu.png").toURI().toString()),new Image(new File("bin/img/mizuyokokusa1.png").toURI().toString())
    ,new Image(new File("bin/img/mizuyokokusa2.png").toURI().toString()) ,new Image(new File("bin/img/mizuyokokusa3.png").toURI().toString())
    ,new Image(new File("bin/img/mizuyokokusa4.png").toURI().toString()),new Image(new File("bin/img/eraser.png").toURI().toString())};

    static int nowSelect=9;

    @Override
    public void start(Stage stage){
        Group root = new Group();

        Canvas canvas = new Canvas(500,500);
        root.getChildren().add(canvas);

        this.gra = canvas.getGraphicsContext2D();

        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(80, 20);
        clearButton.setLayoutX(125);
        clearButton.setLayoutY(465);
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ( event ) -> this.mapClear());
        root.getChildren().add(clearButton);

        Button saveButton = new Button("Save");
        saveButton.setPrefSize(80, 20);
        saveButton.setLayoutX(225);
        saveButton.setLayoutY(465);
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ( event ) -> this.mapSave());
        root.getChildren().add(saveButton);

        Button loadButton = new Button("Load");
        loadButton.setPrefSize(80, 20);
        loadButton.setLayoutX(325);
        loadButton.setLayoutY(465);
        loadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ( event ) -> this.mapLoad());
        root.getChildren().add(loadButton);

        Scene scene = new Scene(root, 500, 500, Color.WHITE);
        scene.setOnMouseClicked(this::action);

        stage.setTitle("Mapmaker");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.show();
        draw();
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void draw(){

        gra.setFill(Color.WHITE);
        gra.fillRect(0, 0, 500, 500);

        // マップの描画
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                gra.drawImage(imgs[map[i][j]], 50*j+50, 50*i);
                gra.setFill(Color.WHITE);
                if(map[i][j]==9)gra.fillRect(50*j+50, 50*i, 50, 50);
            }
        }

        // インベントリの描画
        gra.setLineWidth(3);
        gra.setStroke(Color.RED);
        for(int i=0;i<imgs.length;i++){
            gra.setGlobalAlpha(i==nowSelect?1:0.5);
            gra.drawImage(imgs[i], 0,i*50 );
            gra.strokeRect(0, i*50, 50, 50);
        }

        // 網線の描画
        gra.setLineWidth(1);
        gra.setStroke(Color.BLACK);
        gra.setGlobalAlpha(1);
        for (int i = 0; i < 10; i++) {
            if(i==9){
                gra.setLineWidth(3);
                gra.strokeLine(50, i*50, 500, i*50);
        
            }else{
                gra.strokeLine(0, i*50, 500, i*50);
            }
        }
        gra.setLineWidth(1);
        for(int j=2;j < 10;j++){
            gra.strokeLine(j*50, 0, j*50, 450);
        }
    }
    private void action(MouseEvent e){
        int x = (int)e.getX();
        int y = (int)e.getY();

        if(x<50){
            // インベントリをクリックしている
            for(int i=1;i<11;i++){
                if(y < i*50){
                    nowSelect = i-1;
                    break;
                }
            }
        }else{
            // マップをクリックしている
            boolean isBreak = false;

            for (int i = 0; i < map.length; i++) {
                if(!(y < (i+1)*50))continue;
                for (int j = 0; j < map[0].length; j++) {
                    if(x < (j+2)*50){
                        map[i][j]=nowSelect;
                        isBreak = true;
                        break;
                    }
                }
                if(isBreak)break;
            }
        }
        draw();
    }

    private void mapClear(){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 9;
            }
        }
        draw();
    }

    private void mapSave(){
        File oldDate = new File("bin/date/date.txt");
        oldDate.delete();
        File newDate = new File("bin/date/date.txt");

        try {
		    newDate.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try (Writer saveWriter = new BufferedWriter(new FileWriter(newDate));) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    saveWriter.write(map[i][j]+"");
                }
            }
            saveWriter.flush();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        draw();
    }
    private void mapLoad(){
        try (FileReader loadReader = new FileReader("bin/date/date.txt")) {
            for(int i=0;i<map.length;i++){
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = Integer.parseInt(String.valueOf((char)loadReader.read()));
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
         draw();
    }
}
