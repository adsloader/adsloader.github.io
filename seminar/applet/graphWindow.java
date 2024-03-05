import java.awt.*;
import java.io.*;

public class graphWindow extends Frame
{
   Panel Top, Down;
   Label title = new Label ("수출보험공사 그래프 처리"); 
   Button SuperMan[]  = { 
                          new Button("그래프 종료"), new Button("꺽쇠"),
                          new Button("원"), new Button("막대"),
                          new Button("3D") 
                        };
   
   Dimension winSize = size();
   
   // 약간의 뽀록성 코드 -> 차트 판넬의 이미지 그리기 때문...
   Image img;
   
   // 화면에 보여지는 차트 클래스가 커테이너에 등록되는 ID
   private final int displayChart = 2;
   
   // 엄청 뽀록성 초간단 황당 지저분 꽁수를 위한 코드 
   Chart parent;
   ChartPanelPoly graph_poly;
   ChartPanelArc graph_arc;
   ChartPanelBar graph_bar;
   ChartPanel3D graph_3D;
     
         
   public graphWindow(ChartPanelPoly graph, String GraphTitle, Chart parent)
   {
      super("");
      setLayout(new BorderLayout());
      Top  = new Panel();
      Down = new Panel();
      
      title.setForeground(Color.white);  
      Top.add(title);
      Top.setBackground(new Color(0, 74, 156));
      
      for (int i = 0; i < SuperMan.length; i++){
        SuperMan[i].setForeground(Color.white);
        SuperMan[i].setBackground(new Color(97, 169, 224));   
        SuperMan[i].resize(100, 100);
        Down.add(SuperMan[i]);
      }
      
      Down.setBackground(new Color(0, 74, 156));
      
      graph_poly = graph;
      
      add("North", Top);
      add("South", Down);
      add("Center", graph_poly);
      resize(600, 500);
      
      title.setText(GraphTitle);
      this.parent = parent;
   }
      
   public graphWindow(ChartPanelArc graph, String GraphTitle, Chart parent)
   {
      super("");
      setLayout(new BorderLayout());
      Top  = new Panel();
      Down = new Panel();
      
      title.setForeground(Color.white);  
      Top.add(title);
      Top.setBackground(new Color(0, 74, 156));
      
      for (int i = 0; i < SuperMan.length; i++){
        SuperMan[i].setForeground(Color.white);
        SuperMan[i].setBackground(new Color(97, 169, 224));   
        SuperMan[i].resize(100, 100);
        Down.add(SuperMan[i]);
      }
      
      Down.setBackground(new Color(0, 74, 156));
      
      graph_arc =graph;
      
      add("North", Top);
      add("South", Down);
      add("Center", graph_arc);
      resize(600, 500);
      
      title.setText(GraphTitle);
      this.parent = parent;
   }
   
   public graphWindow(ChartPanelBar graph, String GraphTitle, Chart parent)
   {
      super("");
      setLayout(new BorderLayout());
      Top  = new Panel();
      Down = new Panel();
      
      title.setForeground(Color.white);  
      Top.add(title);
      Top.setBackground(new Color(0, 74, 156));
      
      for (int i = 0; i < SuperMan.length; i++){
        SuperMan[i].setForeground(Color.white);
        SuperMan[i].setBackground(new Color(97, 169, 224));   
        SuperMan[i].resize(100, 100);
        Down.add(SuperMan[i]);
      }
      
      Down.setBackground(new Color(0, 74, 156));
      
      graph_bar = graph;
            
      add("North", Top);
      add("South", Down);
      add("Center", graph_bar);
      resize(600, 500);
      
      title.setText(GraphTitle);
      this.parent = parent;
   }
   
   public graphWindow(ChartPanel3D graph, String GraphTitle, Chart parent)
   {
      super("");
      setLayout(new BorderLayout());
      Top  = new Panel();
      Down = new Panel();
      
      title.setForeground(Color.white);  
      Top.add(title);
      Top.setBackground(new Color(0, 74, 156));
      
      for (int i = 0; i < SuperMan.length; i++){
        SuperMan[i].setForeground(Color.white);
        SuperMan[i].setBackground(new Color(97, 169, 224));   
        SuperMan[i].resize(100, 100);
        Down.add(SuperMan[i]);
      }
      
      Down.setBackground(new Color(0, 74, 156));
      
      graph_3D = graph;
            
      add("North", Top);
      add("South", Down);
      add("Center", graph_3D);
      resize(600, 500);
      
      title.setText(GraphTitle);
      this.parent = parent;
   }
   
   public boolean handleEvent(Event e)
   {
      if(e.id == Event.WINDOW_DESTROY){
        dispose();
      } else if( e.target == SuperMan[0] ){
         if( e.id == Event.ACTION_EVENT ){
           dispose();
         }
      } else if( e.target == SuperMan[1] ){
         if( e.id == Event.ACTION_EVENT ){
           
           graph_poly = new ChartPanelPoly(
                            parent.GraphTitle,
                            parent.RowTitle, parent.RowItems,
                            parent.ColTitle, parent.ColItems,
                            parent.Data, parent.titleGif
                                       );
           remove(getComponent(displayChart));
           add("Center", graph_poly);
           validate();
         }
      } else if( e.target == SuperMan[2] ){
         if( e.id == Event.ACTION_EVENT ){
           
           graph_arc = new ChartPanelArc(
                            parent.GraphTitle,
                            parent.RowTitle, parent.RowItems,
                            parent.ColTitle, parent.ColItems,
                            parent.Data, parent.titleGif
                                       );
           remove(getComponent(displayChart));
           add("Center", graph_arc);
           validate();
         }
      } else if( e.target == SuperMan[3] ){
         if( e.id == Event.ACTION_EVENT ){
           
           graph_bar = new ChartPanelBar(
                            parent.GraphTitle,
                            parent.RowTitle, parent.RowItems,
                            parent.ColTitle, parent.ColItems,
                            parent.Data, parent.titleGif
                                       );
           remove(getComponent(displayChart));
           add("Center", graph_bar);
           validate();
         }
      } else if( e.target == SuperMan[4] ){
         if( e.id == Event.ACTION_EVENT ){
           
           graph_3D = new ChartPanel3D(
                            parent.GraphTitle,
                            parent.RowTitle, parent.RowItems,
                            parent.ColTitle, parent.ColItems,
                            parent.Data, parent.titleGif
                                       );
           remove(getComponent(displayChart));
           add("Center", graph_3D);
           validate();
         }
      } 
      
      return true;   
   }
   
   public void paint(Graphics g)
   {
      invalidate();
   }
   
}