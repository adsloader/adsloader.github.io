import java.awt.*;
import java.applet.Applet;
import java.io.*;

public class ChartPanel3D extends Panel
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
      new Color(255, 236, 103),
      new Color(157,  124, 223),
      new Color(129,  150, 136),
      new Color(255, 236, 103),
      new Color(221,  250, 36),
      new Color(25, 36, 131),
      new Color(74,  01, 07),
      new Color(173,  03, 26),
      new Color(70, 03, 27)
  };

  public ChartPanel3D( String GraphTitle,
                       String RowTitle, String RowItems[],
                       String ColTitle, String ColItems[],
                       float Data[][] )
  {
      setLayout( new BorderLayout() );
      
      // 텍스트 패널을 생성한다.
      TextPanel tpRowItems = new TextPanel( this, RowItems.length );
      tpRowItems.SetTitle( RowTitle );
      for( int i = 0; i < RowItems.length; i ++ ){
        tpRowItems.Add( GraphColor[i], RowItems[i] );
      }
      tpRowItems.move( 470, 20 );
      add( tpRowItems );
      
      // 캔버스를 생성한다.
      Canvas_3DChart Graph = new Canvas_3DChart(
                    GraphTitle,
                    RowTitle, RowItems,
                    ColTitle, ColItems,
                    Data );

      add("Center", Graph);
      setBackground(Color.white);
  }
  
  // Image 포함 생성자
  public ChartPanel3D( String GraphTitle,
                       String RowTitle, String RowItems[],
                       String ColTitle, String ColItems[],
                       float Data[][], Image img )
  {
      //setLayout( null );
      setLayout( new BorderLayout() );
      
      // 텍스트 패널을 생성한다.
      TextPanel tpRowItems = new TextPanel( this, RowItems.length );
      tpRowItems.SetTitle( RowTitle );
      for( int i = 0; i < RowItems.length; i ++ ){
        tpRowItems.Add( GraphColor[i], RowItems[i] );
      }
      tpRowItems.move( 470, 20 );
      add( tpRowItems );
      
      // 캔버스를 생성한다.
      Canvas_3DChart Graph = new Canvas_3DChart(
                    GraphTitle,
                    RowTitle, RowItems,
                    ColTitle, ColItems,
                    Data, img);

      
      add("Center", Graph);
      setBackground(Color.white);
  }

  public void paint(Graphics g)
  {
  }
}

//
// 챠트 캔버스 클래스
//

class Canvas_3DChart extends Canvas
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
      new Color(255, 236, 103),
      new Color(157,  124, 223),
      new Color(129,  150, 136),
      new Color(255, 236, 103),
      new Color(221,  250, 36),
      new Color(25, 36, 131),
      new Color(74,  01, 07),
      new Color(173,  03, 26),
      new Color(70, 03, 27)
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
  static int ParameterNumber = 11;
  int GridInsetX, GridInsetY ;
  int DataStep = 10;
  static long STEP = 1;
  static float MaxGrid;

  // 파라미터로 넘겨받은 데이터 값
  String GraphTitle;
  String RowTitle, RowItems[];
  String ColTitle, ColItems[];
  float Data[][];

  // 캔버스 개체의 배경색을 설정하는 색상
  Color CanvasColor;
  Color TopColor[], SideColor[];
  
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
      g.fill3DRect( 520, 40, 150, 18 * SizeNumber, true );

      for( int i = 0; i < RowItems.length; i ++ ){
        g.setColor( GraphColor[i] );
        g.fill3DRect(532 , 50 + 18*i, 10, 3, true);
        g.drawString( RowItems[i], 548 , 57  + 15*i );
      }
   }

   public void DrawXYGrid(Graphics g, int BaseX, int BaseY,
                          int XGrid, int YGrid)
   {
       int GraphCount = RowItems.length + 1;

       // 실좌표축
       g.drawLine(BaseX, BaseY, XGrid, BaseY);

       // 좌표막기(더미)
       g.drawLine(XGrid + (int)GridInsetX/GraphCount,
                  BaseY - (int)GridInsetX/GraphCount,
                  XGrid + (int)GridInsetX/GraphCount,
                  YGrid - (int)GridInsetX/GraphCount);

       // 3차원 좌표축
       g.drawLine(BaseX + (int)GridInsetX/GraphCount, BaseY -
                  (int)GridInsetX/GraphCount,
                  XGrid + (int)GridInsetX/GraphCount,
                  BaseY - (int)GridInsetX/GraphCount);
       g.drawLine(BaseX + (int)GridInsetX/GraphCount, BaseY -
                  (int)GridInsetX/GraphCount,
                  BaseX + (int)GridInsetX/GraphCount,
                  YGrid - (int)GridInsetX/GraphCount);

       // signOfData(g);
  }

  public void setXInter(Graphics g)
  {
      Dimension CanvasSize = size();

      int GraphCount = RowItems.length + 1;
      GridInsetX = (XGrid - BaseX) / ColItems.length;

      g.setColor(Color.black);
      g.drawString(ColTitle, CanvasSize.width -100, CanvasSize.height- 10);

      int Xposit;
      for( int i = 0; i < ColItems.length; i ++ ){
        Xposit = BaseX + i * (GridInsetX) + (int)GridInsetX/2
               + (int)GridInsetX/GraphCount;

        g.drawLine(Xposit, BaseY, Xposit, BaseY + 3);
        g.drawString(ColItems [i], Xposit, BaseY + 20);
      }
  }

  public void setYInter(Graphics g)
  {
      int GraphCount = RowItems.length + 1;
      GridInsetY = (BaseY - YGrid)/YGridStep;

      g.setColor(Color.black);
      g.drawString(RowTitle, 30, 20);

      int Yposit, XXX;
      for( int i = 0; i <= YGridStep; i ++ ){
        Yposit = BaseY - (i * GridInsetY) - (int)GridInsetX/GraphCount;
        XXX = (int)MaxGrid / 10;
        g.drawLine(BaseX , Yposit, BaseX + 10, Yposit);
        g.drawString(Integer.toString(i*(int)XXX), BaseX - 28, Yposit);
      }

      // x로 좌표축 그어버리기
      for( int i = 0; i <= YGridStep; i ++ ){
        Yposit = BaseY - (i * GridInsetY) - (int)GridInsetX/GraphCount;
        XXX = (int)MaxGrid / 10;
        g.drawLine(BaseX + (int)GridInsetX/GraphCount , Yposit,
                   XGrid + (int)GridInsetX/GraphCount, Yposit);
      }
  }

  // 3D 효과를 위한 약간의 폴리곤들
  public void DimmentionEffect( Graphics g, int x, int y, int size,
                                int LENGTH, int GraphNumber )
  {
      int TopDimentionX[], TopDimentionY[];
      int SideDimentionX[], SideDimentionY[];

      TopDimentionX = new int[4];
      TopDimentionY = new int[4];
      SideDimentionX = new int[4];
      SideDimentionY = new int[4];

      // 머리 그리기
      TopDimentionX[0] = x;
      TopDimentionY[0] = y;
      TopDimentionX[1] = x + size;
      TopDimentionY[1] = y - size;
      TopDimentionX[2] = x + 2 * size;
      TopDimentionY[2] = y - size;
      TopDimentionX[3] = x + size;
      TopDimentionY[3] = y;

      // 옆 그리기
      SideDimentionX[0] = x + 2 * size;
      SideDimentionY[0] = y - size;
      SideDimentionX[1] = x + 2 * size;
      SideDimentionY[1] = y + LENGTH - size;
      SideDimentionX[2] = x + size;
      SideDimentionY[2] = BaseY;
      SideDimentionX[3] = x + size;
      SideDimentionY[3] = y;

      g.setColor( TopColor[GraphNumber] );
      g.fillPolygon(TopDimentionX, TopDimentionY, TopDimentionX.length );
      g.setColor( SideColor[GraphNumber] );
      g.fillPolygon( SideDimentionX, SideDimentionY, SideDimentionX.length );
  }

  // 프로시저 성격상 코드가 상당히 지저분 함
  public void putXYData(Graphics g, Color color)
  {
    int DataXSector, BarLength, BarLength2, BarLength3;
    int DataX, DataY, DataY2, DataY3;
    Color WhatColorIS;

    GridInsetY = (YGrid - BaseY)/YGridStep;
    int GraphCount = RowItems.length + 1;
    
    // canvas 좌표값 저장을 위한 초기화
    px_data = new int[ColItems.length];
    py_data = new int[RowItems.length][ColItems.length];

    g.setColor(color);
    for( int i = 0; i < RowItems.length; i ++ ){
      for( int j = 0; j < ColItems.length; j ++ ){
        DataXSector = j;

        // 각 막대 그래프의 크기
        BarLength = -1 * (int)( (Data[i][j] * (GridInsetY/10)) /
                                (float)(MaxGrid/100) );

        DataX = BaseX + (DataXSector * GridInsetX) + (int)GridInsetX/4;
        px_data[j] = DataX;
        
        // 각 데이터의 막대 좌표
        DataY =  BaseY + (int)( Data[i][j] * (GridInsetY/10) /
                               (MaxGrid/100) );
        py_data[i][j] = DataY;                      
        
        // 그래프와 값이 겹치지 않기 하기 위해서 지저분한 코드 작성
        // 그래프 그리기
        WhatColorIS = GraphColor[i];
        g.setColor(WhatColorIS);
        g.fillRect(DataX + (i * (int)GridInsetX/GraphCount),
                     DataY, (int)GridInsetX/GraphCount,
                     BarLength );
        DimmentionEffect( g, DataX + (i * (int)GridInsetX/GraphCount),
                         DataY, (int)GridInsetX/GraphCount,
                         BarLength, i );

      }
    }
  }

  public Canvas_3DChart( String GraphTitle,
                         String RowTitle, String RowItems[],
                         String ColTitle, String ColItems[],
                         float Data[][] )
  {
     this.GraphTitle = GraphTitle;
     this.RowTitle  = RowTitle;
     this.ColTitle = ColTitle;
     this.RowItems = RowItems;
     this.ColItems = ColItems;
     this.Data = Data;

     // 최대값 얻기
     MaxGrid = getMaxPara();

     TopColor = new Color[13];
     SideColor = new Color[13];
     for( int i = 0; i < 8; i ++ ){
       TopColor[i] = new Color(0);
       TopColor[i] = GraphColor[i].brighter();
       SideColor[i] = new Color(0);
       SideColor[i] = GraphColor[i].darker();
     }
     
     resize(600, 450);
  }
  
  public Canvas_3DChart( String GraphTitle,
                         String RowTitle, String RowItems[],
                         String ColTitle, String ColItems[],
                         float Data[][], Image img )
  {
     this.GraphTitle = GraphTitle;
     this.RowTitle  = RowTitle;
     this.ColTitle = ColTitle;
     this.RowItems = RowItems;
     this.ColItems = ColItems;
     this.Data = Data;
     this.img = img;

     // 최대값 얻기
     MaxGrid = getMaxPara();

     TopColor = new Color[13];
     SideColor = new Color[13];
     for( int i = 0; i < 8; i ++ ){
       TopColor[i] = new Color(0);
       TopColor[i] = GraphColor[i].brighter();
       SideColor[i] = new Color(0);
       SideColor[i] = GraphColor[i].darker();
     }
     
     resize(600, 450);
  }


  // BackImage drawing
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
    setGraphsize();
    drawBackimage(g, img);
    
    try{
      setXInter(g);
      setYInter(g);
      DrawXYGrid(g, BaseX, BaseY, XGrid, YGrid);
      putXYData(g, new Color(180, 160, 200));
      putTooltip(g);
    } catch(NullPointerException err){
      System.out.println("입력된 문자열"  + err);
    }
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
            return toolTip =  "->" + Data[j][i] + "  ";  
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
