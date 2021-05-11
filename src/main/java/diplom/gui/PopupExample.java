package diplom.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopupExample extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Popup Example");
        final Popup popup = new Popup();
        popup.setX(300);
        popup.setY(200);
        popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));

        Button show = new Button("Show");
        show.setOnAction(event -> {
            final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();

            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>D</title>\n" +
                    "    <script src=\"https://d3js.org/d3.v6.js\"></script>\n" +
                    "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>\n" +
                    "\n" +
                    "</head>\n" +
                    "<style>\n" +
                    "\n" +
                    "    .node circle {\n" +
                    "        fill: #fff;\n" +
                    "        stroke: steelblue;\n" +
                    "        stroke-width: 1.5px;\n" +
                    "    }\n" +
                    "\n" +
                    "    .node {\n" +
                    "        font: 10px sans-serif;\n" +
                    "    }\n" +
                    "\n" +
                    "    .link {\n" +
                    "        fill: none;\n" +
                    "        stroke: #ccc;\n" +
                    "        stroke-width: 1.5px;\n" +
                    "    }\n" +
                    "\n" +
                    "</style>\n" +
                    "<body>\n" +
                    "\n" +
                    "<div id=\"elementSelector\">\n" +
                    "    <div id=\"plot\"></div>\n" +
                    "</div>\n" +
                    "\n" +
                    "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js\"></script>\n" +
                    "<script>\n" +
                    "\n" +
                    "    var clusters = {\n" +
                    "        \"name\": \"Документы\",\n" +
                    "        \"children\": [\n" +
                    "            {\n" +
                    "                \"name\": \"Группа №1\",\n" +
                    "                \"children\": [\n" +
                    "                    {\"name\": \"Документ 1\"},\n" +
                    "                    {\"name\": \"Документ 2\"},\n" +
                    "                    {\"name\": \"Документ 3\"},\n" +
                    "                    {\"name\": \"Документ 4\"}\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"name\": \"Группа №2\",\n" +
                    "                \"children\": [\n" +
                    "                    {\"name\": \"Документ 1\"},\n" +
                    "                    {\"name\": \"Документ 2\"},\n" +
                    "                    {\"name\": \"Документ 3\"},\n" +
                    "                    {\"name\": \"Документ 4\"}\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    };\n" +
                    "\n" +
                    "    var width = 400, height = 200;\n" +
                    "\n" +
                    "    var cluster = d3.layout.cluster()\n" +
                    "        .size([height, width - 160]);\n" +
                    "\n" +
                    "    var diagonal = d3.svg.diagonal()\n" +
                    "        .projection(function (d) {\n" +
                    "            return [d.y, d.x];\n" +
                    "        });\n" +
                    "\n" +
                    "    var svg = d3.select(\"#plot\").append(\"svg\")\n" +
                    "        .attr(\"width\", width)\n" +
                    "        .attr(\"height\", height)\n" +
                    "        .append(\"g\")\n" +
                    "        .attr(\"transform\", \"translate(40,0)\");\n" +
                    "\n" +
                    "    var nodes = cluster.nodes(clusters),\n" +
                    "        links = cluster.links(nodes);\n" +
                    "\n" +
                    "    var link = svg.selectAll(\".link\")\n" +
                    "        .data(links)\n" +
                    "        .enter().append(\"path\")\n" +
                    "        .attr(\"class\", \"link\")\n" +
                    "        .attr(\"d\", diagonal);\n" +
                    "\n" +
                    "    var node = svg.selectAll(\".node\")\n" +
                    "        .data(nodes)\n" +
                    "        .enter().append(\"g\")\n" +
                    "        .attr(\"class\", \"node\")\n" +
                    "        .attr(\"transform\", function (d) {\n" +
                    "            return \"translate(\" + d.y + \",\" + d.x + \")\";\n" +
                    "        });\n" +
                    "\n" +
                    "    node.append(\"circle\")\n" +
                    "        .attr(\"r\", 4.5);\n" +
                    "\n" +
                    "    node.append(\"text\")\n" +
                    "        .attr(\"dx\", function (d) {\n" +
                    "            return d.children ? -8 : 8;\n" +
                    "        })\n" +
                    "        .attr(\"dy\", 3)\n" +
                    "        .style(\"text-anchor\", function (d) {\n" +
                    "            return d.children ? \"end\" : \"start\";\n" +
                    "        })\n" +
                    "        .text(function (d) {\n" +
                    "            return d.name;\n" +
                    "        });\n" +
                    "\n" +
                    "    d3.select(self.frameElement).style(\"height\", height + \"px\");\n" +
                    "\n" +
                    "\n" +
                    "    var xml = new XMLSerializer().serializeToString(svg);\n" +
                    "    var svg64 = btoa(xml); //for utf8: btoa(unescape(encodeURIComponent(xml)))\n" +
                    "    var b64start = 'data:image/svg+xml;base64,';\n" +
                    "    var image64 = b64start + svg64;\n" +
                    "\n" +
                    "</script>\n" +
                    "</body>\n" +
                    "</html>";
            webEngine.loadContent(html);

            StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(browser);

            Scene secondScene = new Scene(secondaryLayout, 230, 100);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Second Stage");
            newWindow.setScene(secondScene);

            // Set position of second window, related to primary window.
            newWindow.setX(primaryStage.getX() + 200);
            newWindow.setY(primaryStage.getY() + 100);

            newWindow.show();
        });

        Button hide = new Button("Hide");
        hide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
            }
        });

        HBox layout = new HBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().addAll(show, hide);
        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }
}