import java.awt.*;
import java.io.*;

public class graphWindow extends Frame
{
   Panel Top, Down;
   Label title = new Label ("���⺸����� �׷��� ó��"); 
   Button SuperMan[]  = { 
                          new Button("�׷��� ����"), new Button("����"),
                          new Button("��"), new Button("����"),
                          new Button("3D") 
                        };
   
   Dimension winSize = size();
   
   // �ణ�� �Ƿϼ� �ڵ� -> ��Ʈ �ǳ��� �̹��� �׸��� ����...
   Image img;
   
   // ȭ�鿡 �������� ��Ʈ Ŭ������ Ŀ���̳ʿ� ��ϵǴ� ID
   private final int displayChart = 2;
   
   // ��û �Ƿϼ� �ʰ��� Ȳ�� ������ �Ǽ��� ���� �ڵ� 
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