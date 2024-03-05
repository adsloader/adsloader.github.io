/* 
   Poly3Chart ���ø� ver 0.2
   (1) �Ķ������ �ִ밪�� �ִ� Y������ ��������
       ��� ���ø� �׷��� 
   (2) x���� ���� y���� �ٸ� 3���� ���� �Ѿ�´�.
   (3) �ִ밪 ���ϱ�� ���� ���� ������ �з� 

                     1997. 9. 5 �ڼ���
   
   DrawXYGrid(): ��ǥ�� �׸���
   setXInter():  x ���� ���� �� ���
   setYInter():  y ���� ���� �� ���   
   
   getFromTag(): �Ķ���� �Է�
   getMaxPara(): �Ķ������ �ִ밪�� ��� �� ������ ���
   
   putXYData():  �Ķ���� �����͸� ǥ�� ���
   
   init(): �Ķ���� �Է¹� �ʱ�ȭ 
   paint(): �������� ���θ޼ҵ�
   
   D:/Program Files/Netscape/Communicator/Program/netscape.exe
   D:/Program Files/Internet Explorer/iexplore.exe
*/

import java.awt.*;
import java.applet.Applet;
import java.io.*;

/*
import ChartPanelArc;
import ChartPanelPoly;
import ChartPanel3D;
import graphWindow;
*/

public class Chart extends Applet
{
  // ��ǥ�� ũ�� ����(����)
  final static int BaseX = 30, BaseY = 350;
  final static int XGrid = 450, YGrid = 50;
  final static int YGridStep = 10;

  // �Ķ���ͷ� �Ѱܹ��� ������ ��
  String GraphTitle;
  String RowTitle, RowItems[];
  String ColTitle, ColItems[];
  float Data[][];

  // ��� ������Ʈ(�࿡ ���õ� ���̺� ���)
  Button cmdAction1, cmdAction2, cmdAction3, cmdAction4, cmdAction5, cmdAction6;
  Image titleGif;
  
  public Chart()
  {
  }

  // HTML�κ��� �Ķ���͸� �Է¹޴� �޼ҵ�
  public void getFromTag()
  {
      int i, j;

      // �׷����� �̸��� ��ǥ���� �о����
      GraphTitle = getParameter( "Chartname" );
      RowTitle = getParameter( "ValueName" );
      ColTitle = getParameter( "ValueName2" );

      //  COL �������� ������ ����
      int iColCount = 0;
      while( getParameter( "Para0X" + iColCount ) != null )
        iColCount ++;

      //  COL �������� ��Ʈ���� �д´�.
      ColItems = new String[ iColCount ];
      for( i = 0; i < iColCount; i ++ ){
        ColItems[i] = getParameter( "Para0X" + i );
      }

      //  ROW �������� ������ ����
      int iRowCount = 0;
      while( getParameter( "ITEM" + iRowCount ) != null )
        iRowCount ++;

      // ROW �������� ��Ʈ���� �д´�.
      RowItems = new String[ iRowCount ];
      for( i = 0; i < iRowCount; i ++ ){
        RowItems[i] = getParameter( "ITEM" + i );
      }

      // �Ķ���� ũ��� �迭�� �Ҵ�(Y ��:�Ķ���� ���� �±��� �̸��� ����)
      Data = new float[ iRowCount ][ iColCount ];
      for( i = 0; i < iRowCount; i ++ ){
        for( j = 0 ; j < iColCount; j ++ ){
          Float f = new Float( getParameter( "Para" + i + "Y" + j ) );
          Data[i][j] = f.floatValue();
        }
      }
  }

  // ȭ�� �ٽ� �׸���
  public void start()
  {
      repaint();
  }

  public void update()
  {
      // ���� ���̾ƿ��� ������(ȭ������)
      validate();
  }
  
  // �ʱ�ȭ �̺�Ʈ �ڵ鷯
  public void init()
  {
      // �Ķ����
      setLayout( new BorderLayout() );

      // Panel�� �׷��� ĵ���� ���̱�
      getFromTag();
      titleGif = getImage(getCodeBase(), "title.gif");
     
      Panel b_navigator = new Panel();
      b_navigator.setBackground(new Color(0, 74, 156));
      cmdAction1 = new Button("����׷���");
      cmdAction1.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction1);

      cmdAction2 = new Button("3D�׷���");
      cmdAction2.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction2);

      cmdAction3 = new Button("2D�׷���");
      cmdAction3.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction3);

      cmdAction4 = new Button("���׷���");
      cmdAction4.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction4);
      
      add("South", b_navigator);
     
      // Chart Type
      Panel GraphPanel;
      String Type = getParameter("GType");
      if ( Type  == null){
        Type = "0";
      }
      
      Integer III = new Integer(Type); 
      int GType = III.intValue();
      switch (GType){
        case 1 : GraphPanel = new ChartPanelBar (
                              GraphTitle,
                              RowTitle, RowItems,
                              ColTitle, ColItems,
                              Data, titleGif
                          );
             
                 GraphPanel.resize( 600, 500 );
                 add("Center", GraphPanel );
                 break;
      
        case 2 : GraphPanel = new ChartPanelPoly (
                              GraphTitle,
                              RowTitle, RowItems,
                              ColTitle, ColItems,
                              Data, titleGif
                          );
             
                 GraphPanel.resize( 600, 500 );
                 add("Center", GraphPanel );
                 break;
        
        case 3 : GraphPanel = new ChartPanel3D (
                              GraphTitle,
                              RowTitle, RowItems,
                              ColTitle, ColItems,
                              Data, titleGif
                          );
             
                 GraphPanel.resize( 600, 500 );
                 add("Center", GraphPanel );
                 break;         
        
        case 4 : GraphPanel = new ChartPanelArc (
                              GraphTitle,
                              RowTitle, RowItems,
                              ColTitle, ColItems,
                              Data, titleGif
                          );
             
                 GraphPanel.resize( 600, 500 );
                 add("Center", GraphPanel );
                 break;         
        
        default : GraphPanel = new ChartPanelBar (
                              GraphTitle,
                              RowTitle, RowItems,
                              ColTitle, ColItems,
                              Data, titleGif
                          );
             
                 GraphPanel.resize( 600, 500 );
                 add("Center", GraphPanel );
                 break;         
      
      }
      
      update();
  }

  public void paint(Graphics g)
  {
          
      setBackground(new Color(0, 74, 156));
      g.setColor(Color.black);
      /*
      int w = size().width;
      int h = size().height;
      int iw = titleGif.getWidth(this);
      int ih = titleGif.getHeight(this);
      
      int lw = w / iw;
      if( w % iw > 0 ) lw ++;
      int lh = h / ih;
      if( h % ih > 0 ) lh ++;
      
      int x, y;
      y = 0;
      for( int i = 0; i < lh; i ++ ){
        x = 0;
        for( int j = 0; j < lw; j ++ ){
          g.drawImage(titleGif, x, y, iw, ih, this);
          x += iw;
        }
        y += ih;
      }
      */         
  }

  // Button Click
  public boolean action( Event e, Object what )
  {
      if( e.target == cmdAction1 ){
                            
        ChartPanelPoly GraphPanel = new ChartPanelPoly(
                            GraphTitle,
                            RowTitle, RowItems,
                            ColTitle, ColItems,
                            Data, titleGif
                                       );
        graphWindow WinGraph = new graphWindow(GraphPanel, GraphTitle, this);
        WinGraph.show(); 
        
      } else if( e.target == cmdAction2 ){
        ChartPanel3D GraphPanel = new ChartPanel3D(
                            GraphTitle,
                            RowTitle, RowItems,
                            ColTitle, ColItems,
                            Data, titleGif
                                       );
        graphWindow WinGraph = new graphWindow(GraphPanel, GraphTitle, this);
        WinGraph.show(); 
        
      } else if( e.target == cmdAction3 ){
        ChartPanelBar GraphPanel = new ChartPanelBar(
                            GraphTitle,
                            RowTitle, RowItems,
                            ColTitle, ColItems,
                            Data, titleGif
                                       );
        graphWindow WinGraph = new graphWindow(GraphPanel, GraphTitle, this);
        WinGraph.show(); 
      } else if( e.target == cmdAction4 ){
        ChartPanelArc GraphPanel = new ChartPanelArc(
                            GraphTitle,
                            RowTitle, RowItems,
                            ColTitle, ColItems,
                            Data, titleGif
                                       );
        graphWindow WinGraph = new graphWindow(GraphPanel, GraphTitle, this);
        WinGraph.show(); 
      } 

      return( true );
  }
}

