import java.awt.*;
import java.applet.Applet;
import java.io.*;
import java.awt.Graphics;
import TextPanel;

public class ChartPanelPoly extends Panel
{
   Dimension BorderLines;

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

   public Canvas_PolyChart Graph;
                            
   public ChartPanelPoly(String GraphTitle,
                       String RowTitle, String RowItems[],
                       String ColTitle, String ColItems[],
                       float Data[][])
   {
      setLayout( new BorderLayout() );

      Graph = new Canvas_PolyChart( GraphTitle,
                    		    RowTitle, RowItems,
                    		    ColTitle, ColItems,
                    		    Data );
      Graph.resize(600, 450);
      Graph.move( 0, 0 );
      add("Center", Graph);
     
      // 텍스트 패널을 생성한다.
      TextPanel tpRowItems = new TextPanel( this, RowItems.length );
      tpRowItems.SetTitle( RowTitle );
      for( int i = 0; i < RowItems.length; i ++ ){
        tpRowItems.Add( GraphColor[i], RowItems[i] );
      }
      tpRowItems.move( 470, 20 );
      add( tpRowItems );

      
      setBackground(Color.white);
                             
   }
   
   // 이미지 받는 생성자 
   public ChartPanelPoly(String GraphTitle,
                       String RowTitle, String RowItems[],
                       String ColTitle, String ColItems[],
                       float Data[][], Image img)
   {
      //setLayout( null );
      setLayout( new BorderLayout() );
      Graph = new Canvas_PolyChart( GraphTitle,
                    		    RowTitle, RowItems,
                    		    ColTitle, ColItems,
                    		    Data, img);
     
      // 텍스트 패널을 생성한다.
      TextPanel tpRowItems = new TextPanel( this, RowItems.length );
      tpRowItems.SetTitle( RowTitle );
      for( int i = 0; i < RowItems.length; i ++ ){
        tpRowItems.Add( GraphColor[i], RowItems[i] );
      }
      tpRowItems.move( 470, 20 );
      add( tpRowItems );
      
      add("Center", Graph);
      setBackground(Color.white);
   }
   
      
   public void paint(Graphics g)
   {
   }   
}


class Canvas_PolyChart extends Canvas
{
   
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
   int ParameterNumber;
   int GridInsetX, GridInsetY ;
   int DataStep = 10;
   static long STEP = 1;
   static float MaxGrid;
   String GraphTitle, ValueTitle, ValueTitle2;   
   
   // 파라메터 이름 배열(Tag에서는 ITEM이라는 이름으로 넘겨준다) 
   String TitleNames[];

   // 파라미터로 넘겨받은 데이터 값
   String XParaData [];
   
   // 파라미터의 값을 float 형으로 변환
   float ParaData[][];
   int py_data[][];

   // 몇가지 컴포넌트(축에 관련된 레이블 등등)
   Color CanvasColor;
   
   // 그래프 색상 배열(알아서 늘려주시길...)
   Color GraphsColor[] = {
      new Color(0, 135,  64),
      new Color(221, 127,  164),
      new Color(0,  99, 165),
      new Color(220,  96, 0),
      new Color(146, 192,  0),
      new Color(57,  24, 123),
      new Color(229,  50, 36),
      new Color(255, 236, 103)
   };
   
   Image img;
   
   // 표시할 레이블
   int OldMouseX, OldMouseY;
   String OldToolTip;
   int MouseX, MouseY;
   String ToolTip;
   
   // Move Event handler에서의 폭주 방지용 프래그
   boolean IsLoop;             
   
   public void signOfData(Graphics g)
   {
      int SizeNumber;
      SizeNumber = TitleNames.length;
      g.setColor( new Color(225, 233, 221) );
      g.fill3DRect(460, 40, 90, 18 * SizeNumber, true);      

      for(int i = 0; i < TitleNames.length; i++){
        g.setColor(GraphsColor[i]);
        g.fill3DRect(475 , 50 + 15*i, 10, 3, true);
        g.drawString(TitleNames[i], 490 , 57  + 15*i);
      }     
      
      // debuging
      System.out.println(SizeNumber);
   }   
  
   
   // XY 좌표축(그리드)을 설정하는 메소드
   public void DrawXYGrid(Graphics g, int BaseX, int BaseY,
                          int XGrid, int YGrid)
   {
      g.drawLine(BaseX, BaseY, XGrid, BaseY);
      g.drawLine(BaseX, BaseY, BaseX, YGrid);
      g.drawLine(XGrid, BaseY, XGrid, YGrid);
      // signOfData(g);   
   }
   
   // X축의 단위 설정및 표시
   public void setXInter(Graphics g)
   {
      
      int Xposit;
      int GraphCount;     
      Dimension CanvasSize;
      
      CanvasSize = size();

      GraphCount = TitleNames.length + 1;
      GridInsetX = (XGrid - BaseX)/XParaData.length;
      
      g.setColor(Color.black); 
      g.drawString(ValueTitle2, CanvasSize.width -100, CanvasSize.height- 10);	
      try{
        for (int i = 0; i < XParaData.length; i++ ){
	  /*
	  Xposit = BaseX + i * (GridInsetX) + (int)GridInsetX/2
                   + (int)GridInsetX/GraphCount;
          */
          Xposit = BaseX + i * (GridInsetX);
	  g.drawLine(Xposit, BaseY, Xposit, BaseY + 3);
	  g.drawString(XParaData [i], Xposit, BaseY + 20);
        }
      } catch( NullPointerException e){
        System.out.println(e);
      }  
   }

   // y축의 단위 설정및 표시
   public void setYInter(Graphics g)
   {
      
      GridInsetY = (BaseY - YGrid)/YGridStep;
      int Yposit;
      float XXX;
      
      g.setColor(Color.black); 
      g.drawString(ValueTitle, 10, 30);  
      for (int i = 0; i < YGridStep + 1; i++ ){
	Yposit = BaseY - (i * GridInsetY);
        XXX = (float)MaxGrid/10;
	g.drawLine(BaseX , Yposit, BaseX - 3, Yposit);
	g.drawString(Integer.toString(i * (int)XXX), BaseX - 30, Yposit);
      }

      for (int i = 0; i < YGridStep + 1; i++ ){
	Yposit = BaseY - (i * GridInsetY);
        XXX = (long)MaxGrid/10;
	g.drawLine(BaseX , Yposit, XGrid, Yposit);	
      } 
   }
   
   public float getMaxPara()
   {
      float MaxValue;
      int ValueIndex = 0, ValueItem = 0;      

      // 데이터 값중 최대 찾기(환상 지저분뽀록:일명 인해전술)
      // 버그있음.. 이것은 자체 해결바람...(앗 금방해결..) 
      MaxValue = ParaData[0][0];
      for(int Index = 0; Index < ParaData.length; Index++){ 
        for(int i = 0 ; i < ParaData[Index].length; i++){
          if (MaxValue < ParaData[Index][i]){
            MaxValue = ParaData[Index][i];
            ValueIndex = i;
            ValueItem = Index;
          }
        }
      }

      return MaxValue;
   }   
   
   // 좌표꼭지를 그려주는 메소드
   public void putPointBoxes(int x1, int y1, int x2, int y2, Graphics g)
   {
      int width     = 8;
      int height    = 8;
      int arcWidth  = width / 4;
      int arcHeight = height / 4;
      
      g.fillRoundRect(x1 - (width / 2), y1 - (height / 2), width, height, arcWidth, arcHeight);
      g.fillRoundRect(x2 - (width / 2), y2 - (height / 2), width, height, arcWidth, arcHeight);
   }
   
   public void putXYData(Graphics g, Color GraphColor)
   {
      // 캔버스 크기값 초기화
      BaseY = size().height - 50; 
      XGrid = size().width - 50;  
      
      int DataXSector;                              
      int FirstDataY, LastDataY;	     
      int FirstDataX, LastDataX;
      Color WhatColorIS;       

      // 위치 배열 초기화
      py_data = new int[ParaData.length][ParaData[0].length];
      
      g.setColor(GraphColor);
      try {
        for(int Index = 0; Index < ParaData.length; Index++ ){
          for(int i = 0; i < ParaData[Index].length - 1; i++){
	    FirstDataY = BaseY - (int)(ParaData[Index][i] * (GridInsetY/10) / (float)(MaxGrid/100) );
            LastDataY =  BaseY - (int)(ParaData[Index][i+1]* (GridInsetY/10) / (float)(MaxGrid/100));
          
            FirstDataX = BaseX + (GridInsetX * i);
            LastDataX =  BaseX + (GridInsetX * (i+1));     
            
            // 위치값 배열에 저장
            py_data[Index][i] = FirstDataY;
            py_data[Index][i+1] = LastDataY;
                
            // 그래프 그리기 
	    WhatColorIS = GraphsColor[Index];
            g.setColor(WhatColorIS);
            g.drawLine(FirstDataX , FirstDataY, LastDataX, 
                       LastDataY);
            putPointBoxes(FirstDataX , FirstDataY, LastDataX, 
                          LastDataY, g);           
	    g.drawString(Float.toString(ParaData[Index][i]), FirstDataX + 5, FirstDataY + 5);
            g.drawString(Float.toString(ParaData[Index][i+1]), LastDataX + 5, LastDataY + 5);
        
            /* Debuging
            System.out.println(Float.toString(ParaData[Index][i]) + ":" + (float)(MaxGrid/100)+ ":" +  
                           " : " + LastDataY + " : " + BaseY); */
          }
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println(e);      
      }    
   }
   
   
   public Canvas_PolyChart( String GraphTitle, 
                            String ValueTitle, String TitleNames[],
                            String ValueTitle2, String XParaData [],
   			                float ParaData [][])
   {   
       this.GraphTitle = GraphTitle ;
       this.ValueTitle  = ValueTitle;
       this.ValueTitle2 = ValueTitle2;      
       this.TitleNames = TitleNames;
       this.XParaData = XParaData;                      
       this.ParaData = ParaData;  
       
       // 최대값 얻기
       MaxGrid = getMaxPara();
       
       // debuging code
       for (int i = 0; i < XParaData.length; i++){
         System.out.println(XParaData[i]);
       }
       resize(600, 450);           
      
   }
   
   // 그림 넣은 생성자
   public Canvas_PolyChart( String GraphTitle, 
                            String ValueTitle, String TitleNames[],
                            String ValueTitle2, String XParaData [],
       	                    float ParaData [][], Image img)
   {   
       this.GraphTitle = GraphTitle ;
       this.ValueTitle  = ValueTitle;
       this.ValueTitle2 = ValueTitle2;      
       this.TitleNames = TitleNames;
       this.XParaData = XParaData;                      
       this.ParaData = ParaData;
       this.img = img;  
       
       // 최대값 얻기
       MaxGrid = getMaxPara();
       
       // debuging code
       for (int i = 0; i < XParaData.length; i++){
         System.out.println(XParaData[i]);
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
      
   // Paint Event override
   // Applet의 실제적인 Main 프로시저
   public void paint(Graphics g)
   {
      
      // Graph size 초기화
      setGraphsize();
   
      // 그래프 색상 설정및 좌표 그리기
      setBackground(Color.white);
      g.setColor(Color.black);
      
      // 배경 그려넣기(이미지가 있다면)
      drawBackimage(g, img);
            
      DrawXYGrid(g, BaseX, BaseY, XGrid, YGrid);
      setXInter(g);
      setYInter(g);
      
      // 데이터 넣기
      putXYData(g, Color.pink);
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
          System.out.println("ptx: " + x + " pty:" + y + " x:" + ( i * (GridInsetX) ) +
                             " y:" + py_data[j][i]); */
          if ( x >= ( i * (GridInsetX) - 4) && x <= ( i * (GridInsetX) + 4) 
               && y >=  (py_data[j][i] - 4) && y <= (py_data[j][i] + 4) ){
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
           
           ToolTip = GetToolTipText( x, y, ParaData);
         
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