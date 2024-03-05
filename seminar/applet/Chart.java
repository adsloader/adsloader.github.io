/* 
   Poly3Chart 애플릿 ver 0.2
   (1) 파라메터의 최대값을 최대 Y축으로 동적으로
       잡는 애플릿 그래프 
   (2) x값은 같고 y값이 다른 3가지 값이 넘어온다.
   (3) 최대값 구하기와 색상에 따른 데이터 분류 

                     1997. 9. 5 박성완
   
   DrawXYGrid(): 좌표축 그리기
   setXInter():  x 눈금 설정 및 출력
   setYInter():  y 눈금 설정 및 출력   
   
   getFromTag(): 파라미터 입력
   getMaxPara(): 파라메터의 최대값을 얻고 그 단위를 계산
   
   putXYData():  파라미터 데이터를 표로 출력
   
   init(): 파라메터 입력및 초기화 
   paint(): 실질적인 메인메소드
   
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
  // 좌표축 크기 설정(고정)
  final static int BaseX = 30, BaseY = 350;
  final static int XGrid = 450, YGrid = 50;
  final static int YGridStep = 10;

  // 파라미터로 넘겨받은 데이터 값
  String GraphTitle;
  String RowTitle, RowItems[];
  String ColTitle, ColItems[];
  float Data[][];

  // 몇가지 컴포넌트(축에 관련된 레이블 등등)
  Button cmdAction1, cmdAction2, cmdAction3, cmdAction4, cmdAction5, cmdAction6;
  Image titleGif;
  
  public Chart()
  {
  }

  // HTML로부터 파라메터를 입력받는 메소드
  public void getFromTag()
  {
      int i, j;

      // 그래프의 이름과 좌표단위 읽어오기
      GraphTitle = getParameter( "Chartname" );
      RowTitle = getParameter( "ValueName" );
      ColTitle = getParameter( "ValueName2" );

      //  COL 아이템의 갯수를 측정
      int iColCount = 0;
      while( getParameter( "Para0X" + iColCount ) != null )
        iColCount ++;

      //  COL 아이템의 스트링을 읽는다.
      ColItems = new String[ iColCount ];
      for( i = 0; i < iColCount; i ++ ){
        ColItems[i] = getParameter( "Para0X" + i );
      }

      //  ROW 아이템의 갯수를 측정
      int iRowCount = 0;
      while( getParameter( "ITEM" + iRowCount ) != null )
        iRowCount ++;

      // ROW 아이템의 스트링을 읽는다.
      RowItems = new String[ iRowCount ];
      for( i = 0; i < iRowCount; i ++ ){
        RowItems[i] = getParameter( "ITEM" + i );
      }

      // 파라메터 크기로 배열들 할당(Y 값:파라메터 값과 태그의 이름을 주의)
      Data = new float[ iRowCount ][ iColCount ];
      for( i = 0; i < iRowCount; i ++ ){
        for( j = 0 ; j < iColCount; j ++ ){
          Float f = new Float( getParameter( "Para" + i + "Y" + j ) );
          Data[i][j] = f.floatValue();
        }
      }
  }

  // 화면 다시 그리기
  public void start()
  {
      repaint();
  }

  public void update()
  {
      // 폼의 레이아웃을 재정리(화면정리)
      validate();
  }
  
  // 초기화 이벤트 핸들러
  public void init()
  {
      // 파라메터
      setLayout( new BorderLayout() );

      // Panel에 그래프 캔버스 붙이기
      getFromTag();
      titleGif = getImage(getCodeBase(), "title.gif");
     
      Panel b_navigator = new Panel();
      b_navigator.setBackground(new Color(0, 74, 156));
      cmdAction1 = new Button("꺽쇠그래프");
      cmdAction1.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction1);

      cmdAction2 = new Button("3D그래프");
      cmdAction2.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction2);

      cmdAction3 = new Button("2D그래프");
      cmdAction3.setBackground(new Color(181, 184, 219));
      b_navigator.add(cmdAction3);

      cmdAction4 = new Button("원그래프");
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

