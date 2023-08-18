module com.example.demo8 {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
//  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
  requires javafx.media;

  opens com.example.demo8 to javafx.fxml;
  exports com.example.demo8;
}