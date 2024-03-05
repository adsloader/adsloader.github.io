/*
      ChartPanelBar.java
      : 이전의 그래프 중 3개의 꺽쇠와
        막대 그래프를 혼용한 것임
        그러므로 가독성을 위해서 주석을 
        생략함
                1997. 9.5 박 성완 

*/

import java.awt.*;
import java.applet.Applet;
import java.io.*;
import TextPanel;


class ChartPanelBar extends Panel
{
  // 그래프 색상 배열
  Color GraphColor[] = {
      new Color(0, 135,  64),
      new Color(221, 127,  164),
      new Color(0,  99, 165),
      new Color(220,  96, 0),
      new Color(146, 192,  0),
      new Color(57,  24, 123),
      new Color(229,  50, 36),
      new Color(255, 236, 103)
  };

  public ChartPanelBar( String GraphTitle,
                        String RowTitle, String RowItems[],
                        String ColTitle, String ColItems[],
                        float Data[][] )
  {
      setLayout( new BorderLayout() );

      // 캔버스를 생성한다.
      Canvas_BarChart Graph = new Canvas_BarChart(
                    GraphTitle,
                    RowTitle, RowItems,
                    ColTitle, ColItems,
                    Data );

      //Graph.resize(600, 450);
      add("Center",Graph );

    
      // 텍스트 패널을 생성한다.
      TextPanel tpRowItems = new TextPanel( this, RowItems.length );
      tpRowItems.SetTitle( ColTitle );
      for( int i = 0; i < RowItems.length; i ++ ){
        tpRowItems.Add( GraphColor[i], RowItems[i] );
      }
      tpRowItems.move( 470, 20 );
      add( tpRowItems );


      setBackground(Color.white);
  }
  // 이미지 추가된 생성자
  public ChartPanelBar( String GraphTitle,
                        String RowTitle, String RowItems[],
                        String ColTitle, String ColItems[],
                        float Data[][], Image img )
  {
      setLayout( new BorderLayout() );

      // 캔버스를 생성한다.
      Canvas_BarChart Graph = new Canvas_BarChart(
                    GraphTitle,
                    RowTitle, RowItems,
                    ColTitle, ColItems,
                    Data, img );

      
      // 텍스트 패널을 생성한다.
      TextPanel tpRowItems = new TextPanel( this, RowItems.length );
      tpRowItems.SetTitle( ColTitle );
      for( int i = 0; i < RowItems.length; i ++ ){
        tpRowItems.Add( GraphColor[i], RowItems[i] );
      }
      tpRowItems.move( 470, 20 );
      add( tpRowItems );
      
      add("Center",Graph );
      setBackground(Color.white);
  }
      
  public void paint(Graphics g)
  {
  }   
}


//
// 챠트 캔버스 클래스
//

class Canvas_BarChart extends Canvas
{
  // 그래프 색상 배열
  Color GraphColor[] = {
      new Color(0, 135,  64),
      new Color(221, 127,  164),
      new Color(0,  99, 165),
      new Color(220,  96, 0),
      new Color(146, 192,  0),
      new Color(57,  24, 123),
      new Color(229,  50, 36),
      new Color(255, 236, 103)
  };

  /* 좌표축 크기 설정(고정)
  final static int BaseX = 30, BaseY = 350;
  final static int XGrid = 450, YGrid = 50;
  final static int YGridStep = 10;
  */
  
  // 좌표축 크기 설정(동적)
  int BaseX = 30;
  int BaseY = size().height - 50;
  int XGrid = size().width - 50; 
  int YGrid = 50;
  int YGridStep = 10; 
   
  // 좌표단위 설정(동적) 
  int GridInsetX, GridInsetY ;
  //int DataStep = 10;
  //static long STEP = 1;
  static float MaxGrid;
   
  // 파라미터로 넘겨받은 데이터 값
  String GraphTitle;
  String RowTitle, RowItems[];
  String ColTitle, ColItems[];
  float Data[][];

  // 캔버스 개체의 배경색을 설정하는 색상
  Color CanvasColor;
  
  // 이미지
  Image img;
  
  // 표시할 레이블
  int OldMouseX, OldMouseY;
  int MouseX, MouseY;
  String OldToolTip;
  String ToolTip;
   
  // Move Event handler에서의 폭주 방지용 프래그
  boolean IsLoop;
  
  // 실제 좌표값
  int px_data[], py_data[][];             
   
   
  public float getMaxPara()
  {
      float MaxValue;
      int ValueIndex = 0, ValueItem = 0;
      MaxValue = Data[0][0];

      for( int i = 0; i < RowItems.length; i ++ ){
        for( int j = 0 ; j < ColItems.length; j ++ ){
          if( MaxValue < Data[i][j]){
            MaxValue = Data[i][j];
            ValueIndex = j;
            ValueItem = i;
          }
        }
      }

      return( MaxValue );
  }

  public void signOfData(Graphics g)
  {
      int SizeNumber = RowItems.length;
      g.setColor( new Color(225, 233, 221) );
      g.fill3DRect(570, 40, 130, 18 * SizeNumber, true);      

      for( int i = 0; i < RowItems.length; i ++ ){
        g.setColor(GraphColor[i]);
        g.fill3DRect(582 , 50 + 15*i, 10, 3, true);
        g.drawString(RowItems[i], 600 , 57  + 15*i);
      }     
   }   

   public void DrawXYGrid(Graphics g, int BaseX, int BaseY,
                          int XGrid, int YGrid)
   {
      g.drawLine(BaseX, BaseY, XGrid, BaseY);
      g.drawLine(BaseX, BaseY, BaseX, YGrid);
      g.drawLine(XGrid, BaseY, XGrid, YGrid);
      // signOfData(g);   
   }
   
   public void setXInter(Graphics g)
   {
      GridInsetX = (XGrid - BaseX) / ColItems.length;
      
      g.setColor(Color.black); 
      g.drawString(ColTitle, size().width - 50, size().height - 10);	

      int Xposit;
      for (int i = 0; i < ColItems.length; i++ ){
	    Xposit = BaseX + i * (GridInsetX);

        g.drawLine( Xposit, BaseY, Xposit, BaseY + 3);
        g.drawString( ColItems[i] , Xposit - (int)GridInsetY/2, BaseY + 20 );
      }
   }

   public void setYInter(Graphics g)
   {
      GridInsetY = (BaseY - YGrid)/YGridStep;
      
      g.setColor(Color.black); 
      g.drawString(RowTitle, 10, 30);  

      int Yposit, XXX;
      for( int i = 0; i < YGridStep + 1; i ++ ){
	    Yposit = BaseY - (i * GridInsetY);
        XXX = (int)MaxGrid / 10;
	    g.drawLine(BaseX , Yposit, BaseX - 3, Yposit);
	    // g.drawString(Integer.toString(i*(int)XXX), BaseX - 30, Yposit);
	    g.drawString(Integer.toString(i*(int)XXX), BaseX - 30, Yposit);	
      }

      for( int i = 0; i < YGridStep + 1; i ++ ){
	    Yposit = BaseY - (i * GridInsetY);
        XXX = (int)MaxGrid / 10;
	    g.drawLine(BaseX , Yposit, XGrid, Yposit);	
      }
   }
    
  // 프로시저 성격상 코드가 상당히 지저분 함 
  public void putXYData(Graphics g, Color color)
  {
      int GraphCount;     
      int DataXSector, BarLength, BarLength2, BarLength3;
      int DataX, DataY, DataY2, DataY3;
      Color WhatColorIS; 
      
      GridInsetY = (YGrid - BaseY)/YGridStep;
      GraphCount = RowItems.length + 1;

      // canvas 좌표값 저장을 위한 초기화
      px_data = new int[ColItems.length];
      py_data = new int[RowItems.length][ColItems.length];
      
      g.setColor(color);
      for( int i = 0; i < RowItems.length; i ++ ){
        for( int j = 0; j < ColItems.length; j ++ ){
	      DataXSector = j;
	  
          // 각 막대 그래프의 크기 
          BarLength = -1 * (int)( (Data[i][j] * (GridInsetY/10))/
                                  (float)(MaxGrid / 100)
                                );

	  DataX = BaseX + (DataXSector * GridInsetX) + (int)GridInsetX/4;
          px_data[j] = DataX;
   	  
   	  // 각 데이터의 막대 좌표 
   	  DataY =  BaseY + (int)(Data[i][j]* (GridInsetY/10) / 
                                (MaxGrid / 100) );
          py_data[i][j] = DataY;                      
          
          // 그래프와 값이 겹치지 않기 하기 위해서 지저분한 코드 작성 
          // 그래프 그리기 
	  WhatColorIS = GraphColor[i];
          g.setColor(WhatColorIS);
          g.fill3DRect(DataX + (i * (int)GridInsetX/GraphCount), 
                       DataY, (int)GridInsetX/GraphCount, 
                       BarLength, true);

        }
      }
   }
   
  public Canvas_BarChart( String GraphTitle,
                          String RowTitle, String RowItems[],
                          String ColTitle, String ColItems[],
                          float Data[][] )
  {
      this.GraphTitle = GraphTitle ;
      this.RowTitle  = RowTitle;
      this.ColTitle = ColTitle;      
      this.RowItems = RowItems;  
      this.ColItems = ColItems;                      
      this.Data = Data;  

     // 최대값 얻기
     MaxGrid = getMaxPara();

     resize(600, 450);
  }
  
  public Canvas_BarChart( String GraphTitle,
                          String RowTitle, String RowItems[],
                          String ColTitle, String ColItems[],
                          float Data[][], Image img )
  {
      this.GraphTitle = GraphTitle ;
      this.RowTitle  = RowTitle;
      this.ColTitle = ColTitle;      
      this.RowItems = RowItems;  
      this.ColItems = ColItems;                      
      this.Data = Data;  
      this.img = img;  

     // 최대값 얻기
     MaxGrid = getMaxPara();

     resize(600, 450);
  }
  
  // 배경 이미지 그려넣기
  public void drawBackimage(Graphics g, Image img)
  {
     if (img != null){
       int w = size().width;
       int h = size().height;
       int iw = img.getWidth(this);
       int ih = img.getHeight(this);
      
       int lw = w / iw;
       if( w % iw > 0 ) lw ++;
       int lh = h / ih;
       if( h % ih > 0 ) lh ++;
      
       int x, y;
       y = 0;

       for( int i = 0; i < lh; i ++ ){
         x = 0;
         for( int j = 0; j < lw; j ++ ){
           g.drawImage(img, x, y, iw, ih, this);
           x += iw;
         }
         y += ih;
       }
     }
  }
   
  public void setGraphsize()
  {
     // 캔버스 크기값 초기화
     BaseY = size().height - 50; 
     XGrid = size().width - 50;
      
     if (BaseY >= 0 && BaseY < 256 ){
       YGrid = BaseY - 100;
     } else if (BaseY >= 256 && BaseY < 350){
       YGrid = BaseY - 200;
     } else if (BaseY >= 350 && BaseY < 401){
       YGrid = BaseY - 300;
     } else {
       YGrid = BaseY - 400;
     }
  } 
  
  public void paint(Graphics g)
  {
      setBackground(CanvasColor);
      g.setColor(Color.black);
      
      setGraphsize();
      
      // 배경이미지 넣기
      drawBackimage(g, img);
    
      DrawXYGrid(g, BaseX, BaseY, XGrid, YGrid);
      setXInter(g);
      setYInter(g);
      
      putXYData(g, new Color(180, 160, 200));
      putTooltip(g);
   }
   
   // Tooltip handler
   public void putTooltip(Graphics g)
   {
        if( ToolTip != null && ToolTip != "" ){
          FontMetrics m = getFontMetrics(getFont());
		  int w = m.stringWidth( ToolTip );
		  int h = m.getHeight();
          int a = m.getAscent();

          int x = MouseX - 2;
          int y = MouseY - a - 2;
          g.setColor( new Color(181, 184, 219) );
          g.fill3DRect( x, y, w + 4, h + 4, true );
          g.setColor( Color.black );
          g.drawString( ToolTip, MouseX, MouseY );
        }
    }

   public void updateTooltip()
   {
      FontMetrics m = getFontMetrics(getFont());
      int h = m.getHeight();
      int a = m.getAscent();

      if( OldToolTip != null && OldToolTip != "" ){
         int w = m.stringWidth( OldToolTip );
         repaint( OldMouseX - 2, OldMouseY - a - 2, w + 4, h + 4 );
      }
      
      if( ToolTip != "" ){
        int w = m.stringWidth( ToolTip );
        repaint( MouseX - 2, MouseY - a - 2, w + 4, h + 4 );
      }

      OldMouseX = MouseX;
      OldMouseY = MouseY;
      OldToolTip = ToolTip;
  }
    
  public String GetToolTipText( int x, int y, float Data[][] )
  {
      
      int width     = 8;
      int height    = 8;
      String toolTip;
      
      // looping & Move Event defend
      IsLoop = true;
      
      for (int j = 0; j < py_data.length ; j++){
        for (int i = 0; i < py_data[j].length ; i ++){
          /* d b c
          System.out.println("ptx: " + x + " pty:" + y + " x:" + 
                              (px_data[i] + (j * (int)GridInsetX/(RowItems.length + 1)) - BaseX) +
                             " y:" + py_data[j][i] + " YGrid" + YGrid); */
          if ( x >= (px_data[i] + (j * (int)GridInsetX/(RowItems.length + 1)) - BaseX) && 
               x <= (px_data[i] + (j * (int)GridInsetX/(RowItems.length + 1)) - BaseX) + 
                    ( (int)GridInsetX/(RowItems.length + 1) )
               && y >= py_data[j][i] && y <= BaseY ){
            IsLoop = false; 
            return toolTip =  "->" + Data[j][i] + "  ";  ;  
          }  
        
        }
      }
      
      IsLoop = false; 
      return "";
  }

   
   public boolean mouseExit(Event e, int x, int y )
   {
       ToolTip = "";
       updateTooltip();

       return( true );
   }
   
   public boolean mouseMove(Event e, int x, int y )
   {
       
       if ( !IsLoop ){
         if( x >= BaseX && x < XGrid &&
             y >= YGrid && y < BaseY ){
           MouseX = x;
           MouseY = y;
      
           // y -= BaseY;
           x -= BaseX;
           
           ToolTip = GetToolTipText( x, y, Data);
         
           updateTooltip();
         
         } else {
           
            if( ToolTip != "" ){
              ToolTip = "";
              updateTooltip();
            }
         }  
    
       }  
            
       return( true );
   }
}
